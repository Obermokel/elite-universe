package borg.ed.galaxy.eddn;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.channels.ClosedByInterruptException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.zeromq.ZMQ;
import org.zeromq.ZMQ.Context;
import org.zeromq.ZMQ.Socket;
import org.zeromq.ZMQException;

import zmq.ZError;

public class EddnReaderThread extends Thread {

	private static final Logger logger = LoggerFactory.getLogger(EddnReaderThread.class);

	private static final String RELAY = "tcp://eddn.edcd.io:9500";

	private Context context = null;
	private Socket socket = null;

	@Autowired
	private EddnBufferThread eddnBufferThread = null;

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
					this.eddnBufferThread.bufferJsonString(jsonMessage);
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

}
