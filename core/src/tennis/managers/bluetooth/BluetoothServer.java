package tennis.managers.bluetooth;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BluetoothServer implements Runnable {
	static final String deviceUUID = "0000110100001000800000805F9B34FB";
	public static float accelerometerX, accelerometerY, accelerometerZ;
	static StreamConnectionNotifier server;

	public void run() {
		try {
			accelerometerX = accelerometerY = accelerometerZ = 0;
			LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);

			String url = "btspp://localhost:" + deviceUUID
					+ ";name=BlueToothServer";

			server = (StreamConnectionNotifier) Connector.open(url);
			// Able to reconnect
			startServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// start server
	public void startServer() throws IOException {

		// Wait until client connects
		StreamConnection connection = server.acceptAndOpen();
		DataInputStream dis = connection.openDataInputStream();

		while (true) {
			try {
				accelerometerX = dis.readFloat();
				accelerometerY = dis.readFloat();
				accelerometerZ = dis.readFloat();
				if (dis.readFloat() == Float.MAX_VALUE)
					break;
			} catch (EOFException e) {
				break;
			}
		}

		connection.close();
		startServer();

	}
}
