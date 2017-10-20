package borg.ed.universe.eddn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zeromq.ZMQ;
import org.zeromq.ZMQException;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import zmq.ZError;
import com.google.gson.Gson;

import borg.ed.universe.exceptions.NonUniqueResultException;
import borg.ed.universe.exceptions.SuspiciousDataException;
import borg.ed.universe.journal.JournalEventReader;
import borg.ed.universe.journal.events.AbstractJournalEvent;

public class EddnReaderThread extends Thread {

    private static final Logger logger = LoggerFactory.getLogger(EddnReaderThread.class);

    private static final String RELAY = "tcp://eddn.edcd.io:9500";

    private static final String SCHEMA_JOURNAL_v1 = "https://eddn.edcd.io/schemas/journal/1";
    private static final String SCHEMA_JOURNAL_v1_TEST = "https://eddn.edcd.io/schemas/journal/1/test";
    private static final String SCHEMA_COMMODITY_v3 = "https://eddn.edcd.io/schemas/commodity/3";
    private static final String SCHEMA_BLACKMARKET_v1 = "https://eddn.edcd.io/schemas/blackmarket/1";
    private static final String SCHEMA_SHIPYARD_v2 = "https://eddn.edcd.io/schemas/shipyard/2";
    private static final String SCHEMA_OUTFITTING_v2 = "https://eddn.edcd.io/schemas/outfitting/2";

    private final Gson gson = new Gson();

    private Context context = null;
    private Socket socket = null;

    private final List<EddnUpdateListener> listeners = new ArrayList<>();

    @Autowired
    private JournalEventReader journalEventReader = null;

    public EddnReaderThread() {
        this.setName("EddnReaderThread");
        this.setDaemon(true);
    }

    @Override
    public void run() {
        logger.info(this.getName() + " started");

        this.connect();

        while (!Thread.currentThread().isInterrupted()) {
            try {
                byte[] compressed = socket.recv(0);
                byte[] decompressed = this.decompress(compressed);
                String jsonMessage = decompressed == null ? null : new String(decompressed, "UTF-8");

                if (jsonMessage == null) {
                    logger.debug("Seems like we lost the ZMQ connection, will try to reconnect");
                    this.disconnect();
                    this.connect();
                } else {
                    try {
                        this.handleMessage(jsonMessage);
                    } catch (NonUniqueResultException e) {
                        logger.error("NonUniqueResultException: " + e.getMessage() + "\n\t" + e.getOthers() + "\n\t" + jsonMessage);
                    } catch (SuspiciousDataException e) {
                        //logger.warn("SuspiciousDataException: " + e.getMessage() + "\n\t" + jsonMessage);
                    }
                }
            } catch (ClosedByInterruptException e) {
                this.disconnect();
                Thread.currentThread().interrupt();
            } catch (ZMQException e) {
                if (ZError.EINTR == e.getErrorCode()) {
                    this.disconnect();
                    Thread.currentThread().interrupt();
                } else {
                    logger.error("Exception in " + this.getName(), e);
                }
            } catch (Exception e) {
                logger.error("Exception in " + this.getName(), e);
            }
        }

        this.disconnect();

        logger.info(this.getName() + " terminated");
    }

    @SuppressWarnings("unchecked")
    private void handleMessage(String jsonMessage) throws NonUniqueResultException, SuspiciousDataException {
        //System.out.println(jsonMessage);

        LinkedHashMap<String, Object> data = this.gson.fromJson(jsonMessage, LinkedHashMap.class);
        String schemaRef = (String) data.get("$schemaRef");

        if (SCHEMA_JOURNAL_v1.equals(schemaRef)) {
            Map<String, Object> header = (Map<String, Object>) data.get("header");
            ZonedDateTime gatewayTimestamp = ZonedDateTime.parse((String) header.get("gatewayTimestamp"));
            String uploaderID = (String) header.get("uploaderID");
            Map<String, Object> message = (Map<String, Object>) data.get("message");

            AbstractJournalEvent journalEvent = this.journalEventReader.readLine(this.gson.toJson(message));

            for (EddnUpdateListener listener : this.listeners) {
                try {
                    listener.onNewJournalMessage(gatewayTimestamp, uploaderID, journalEvent);
                } catch (Exception e) {
                    logger.warn(listener + " failed: " + e);
                }
            }
        } else if (SCHEMA_JOURNAL_v1_TEST.equals(schemaRef)) {
            // NOOP
        } else if (SCHEMA_COMMODITY_v3.equals(schemaRef)) {
            // NOOP
        } else if (SCHEMA_BLACKMARKET_v1.equals(schemaRef)) {
            // NOOP
        } else if (SCHEMA_SHIPYARD_v2.equals(schemaRef)) {
            // NOOP
        } else if (SCHEMA_OUTFITTING_v2.equals(schemaRef)) {
            // NOOP
        } else {
            logger.warn("Unknown schemaRef: " + schemaRef);
        }
    }

    private void connect() {
        logger.debug("Connecting to " + RELAY);
        context = ZMQ.context(1);
        socket = context.socket(ZMQ.SUB);
        socket.subscribe(new byte[0]);
        socket.setReceiveTimeOut(60000);
        socket.connect(RELAY);
        logger.debug("Connected");
    }

    private void disconnect() {
        if (socket != null) {
            logger.debug("Disconnecting");
            socket.close();
            socket = null;
            logger.debug("Disconnected");
        }
        if (context != null) {
            context.term();
            context = null;
        }
    }

    private byte[] decompress(byte[] compressed) throws IOException, DataFormatException {
        if (compressed == null) {
            return null;
        } else {
            Inflater inflater = new Inflater();
            inflater.setInput(compressed);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(compressed.length);
            byte[] buffer = new byte[1024];
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
            return outputStream.toByteArray();
        }
    }

    public boolean addListener(EddnUpdateListener listener) {
        if (listener == null || this.listeners.contains(listener)) {
            return false;
        } else {
            return this.listeners.add(listener);
        }
    }

    public boolean removeListener(EddnUpdateListener listener) {
        if (listener == null) {
            return false;
        } else {
            return this.listeners.remove(listener);
        }
    }

}
