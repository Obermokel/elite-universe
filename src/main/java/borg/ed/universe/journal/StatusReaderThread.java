package borg.ed.universe.journal;

import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import borg.ed.universe.data.Coord;
import borg.ed.universe.util.GsonCoord;
import borg.ed.universe.util.GsonZonedDateTime;

public class StatusReaderThread extends Thread {

    static final Logger logger = LoggerFactory.getLogger(StatusReaderThread.class);

    public volatile boolean shutdown = false;

    private final Path statusFile;

    private final Gson gson = new GsonBuilder().registerTypeAdapter(ZonedDateTime.class, new GsonZonedDateTime()).registerTypeAdapter(Coord.class, new GsonCoord()).create();

    private final List<StatusUpdateListener> listeners = new ArrayList<>();

    public StatusReaderThread() {
        this.setName("StatusReaderThread");
        this.setDaemon(true);

        statusFile = lookupStatusFile();
    }

    private Path lookupStatusFile() {
        Path homeDir = Paths.get(System.getProperty("user.home"));
        Path journalDir = homeDir.resolve("Saved Games\\Frontier Developments\\Elite Dangerous\\Status.json");

        if (Files.exists(journalDir)) {
            return journalDir;
        } else {
            journalDir = homeDir.resolve("Google Drive\\Elite Dangerous\\Journal\\\\Status.json");

            if (Files.exists(journalDir)) {
                return journalDir;
            } else {
                throw new RuntimeException("Status file not found");
            }
        }
    }

    @Override
    public void run() {
        logger.info(this.getName() + " started");

        this.watchUsingThreadSleep();

        logger.info(this.getName() + " terminated");
    }

    void watchUsingThreadSleep() {
        byte[] lastData = null;

        while (!Thread.currentThread().isInterrupted() && !this.shutdown) {
            try {
                byte[] currentData = Files.readAllBytes(statusFile);

                if (areArraysDifferent(lastData, currentData)) {
                    try {
                        this.updateStatus(new String(currentData, "UTF-8"));
                    } finally {
                        lastData = currentData;
                    }
                }

                Thread.sleep(50);
            } catch (InterruptedException | ClosedByInterruptException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                logger.error("IOException in " + this.getName(), e);
            }
        }
    }

    private static boolean areArraysDifferent(byte[] a, byte[] b) {
        if (a == null && b == null) {
            return false;
        } else if (a == null && b != null) {
            return true;
        } else if (a != null && b == null) {
            return true;
        } else if (a.length != b.length) {
            return true;
        } else {
            for (int i = 0; i < a.length; i++) {
                if (a[i] != b[i]) {
                    return true;
                }
            }
            return false;
        }
    }

    private void updateStatus(String jsonString) {
        try {
            Status status = this.gson.fromJson(jsonString, Status.class);

            if (status != null) {
                for (StatusUpdateListener listener : this.listeners) {
                    try {
                        listener.onNewStatus(status);
                    } catch (Exception e) {
                        logger.warn(listener + " failed", e);
                    }
                }
            }
        } catch (RuntimeException e) {
            logger.error("Failed to read status file " + this.statusFile, e);
        }
    }

    public boolean addListener(StatusUpdateListener listener) {
        if (listener == null || this.listeners.contains(listener)) {
            return false;
        } else {
            return this.listeners.add(listener);
        }
    }

    public boolean removeListener(StatusUpdateListener listener) {
        if (listener == null) {
            return false;
        } else {
            return this.listeners.remove(listener);
        }
    }

}
