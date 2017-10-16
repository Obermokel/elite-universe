package borg.ed.universe.journal;

import borg.ed.universe.journal.events.AbstractJournalEvent;
import borg.ed.universe.journal.events.FSDJumpEvent;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

/**
 * JournalReaderThread
 *
 * @author <a href="mailto:b.guenther@xsite.de">Boris Guenther</a>
 */
public class JournalReaderThread extends Thread {

    static final Logger logger = LoggerFactory.getLogger(JournalReaderThread.class);

    private final Path journalDir;

    @Autowired
    private JournalEventReader journalEventReader = null;

    private String currentFilename = null;
    private int lastProcessedLineNumber = 0;
    private ZonedDateTime lastProcessedTimestamp = ZonedDateTime.ofInstant(Instant.EPOCH, ZoneId.of("Z"));
    private final List<JournalUpdateListener> listeners = new ArrayList<>();

    public JournalReaderThread() {
        this.setName("JournalWatcherThread");
        this.setDaemon(false);

        journalDir = lookupJournalDir();
    }

    private Path lookupJournalDir() {
        Path homeDir = Paths.get(System.getProperty("user.home"));
        Path journalDir = homeDir.resolve("Saved Games\\Frontier Developments\\Elite Dangerous");

        if (Files.exists(journalDir)) {
            return journalDir;
        } else {
            journalDir = homeDir.resolve("Google Drive\\Elite Dangerous\\Journal");

            if (Files.exists(journalDir)) {
                return journalDir;
            } else {
                throw new RuntimeException("Journal directory not found");
            }
        }
    }

    @Override
    public void run() {
        logger.info(this.getName() + " started");

        try (WatchService watcher = journalDir.getFileSystem().newWatchService()) {
            journalDir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);

            while (!Thread.currentThread().isInterrupted()) {
                try {
                    final WatchKey key = watcher.take(); // Wait for the next event

                    for (WatchEvent<?> event : key.pollEvents()) {
                        final Kind<?> kind = event.kind();

                        if (kind == StandardWatchEventKinds.OVERFLOW) {
                            logger.warn("WatchService overflow for " + journalDir); // Print warning and continue
                        } else if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                            this.updateJournal(event.context().toString());
                        }
                    }

                    if (!key.reset()) {
                        break; // Quit
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(this.getName() + " crashed", e);
        }

        logger.info(this.getName() + " terminated");
    }

    private void updateJournal(String filename) {
        if (StringUtils.isNotEmpty(filename) && filename.startsWith("Journal.") && filename.endsWith(".log")) {
            try {
                int lineNumberBackup = this.lastProcessedLineNumber;
                String filenameBackup = this.currentFilename;

                if (!filename.equals(this.currentFilename)) {
                    this.lastProcessedLineNumber = 0; // New file
                }
                this.currentFilename = filename;

                boolean eventAdded = false;
                List<String> lines = Files.readAllLines(this.journalDir.resolve(filename), StandardCharsets.UTF_8);
                for (int lineNumber = 1; lineNumber <= lines.size(); lineNumber++) {
                    if (lineNumber > this.lastProcessedLineNumber) {
                        this.lastProcessedLineNumber = lineNumber;

                        String line = lines.get(lineNumber - 1);
                        AbstractJournalEvent event = null;
                        try {
                            event = this.journalEventReader.readLine(line);
                        } catch (Exception e) {
                            logger.error("Failed to read line " + lineNumber + " of " + filename + "\n\t" + line, e);
                        }

                        if (event != null && event.getTimestamp().compareTo(this.lastProcessedTimestamp) >= 0) {
                            if (event instanceof FSDJumpEvent) {
                                logger.info(event.getTimestamp().format(DateTimeFormatter.ISO_INSTANT) + ": JUMP TO " + ((FSDJumpEvent) event).getStarSystem());
                            }

                            this.lastProcessedTimestamp = event.getTimestamp();

                            eventAdded = true;

                            for (JournalUpdateListener listener : this.listeners) {
                                try {
                                    listener.onNewJournalEntry(event);
                                } catch (Exception e) {
                                    logger.warn(listener + " failed: " + e);
                                }
                            }
                        }
                    }
                }

                if (!eventAdded) {
                    this.currentFilename = filenameBackup;
                    this.lastProcessedLineNumber = lineNumberBackup;
                }
            } catch (IOException | RuntimeException e) {
                logger.error("Failed to read journal file " + this.currentFilename, e);
            }
        }
    }

    public boolean addListener(JournalUpdateListener listener) {
        if (listener == null || this.listeners.contains(listener)) {
            return false;
        } else {
            return this.listeners.add(listener);
        }
    }

    public boolean removeListener(JournalUpdateListener listener) {
        if (listener == null) {
            return false;
        } else {
            return this.listeners.remove(listener);
        }
    }

}
