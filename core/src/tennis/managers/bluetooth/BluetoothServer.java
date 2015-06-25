package tennis.managers.bluetooth;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.ObjectInputStream;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import tennis.managers.bluetooth.*;

public class BluetoothServer implements Runnable {
	// Default Device UUID. Don't change.
	static final String deviceUUID = "0000110100001000800000805F9B34FB";
	public static float accelerometerX, accelerometerY, accelerometerZ;
	static StreamConnectionNotifier server;

	public void run() {
		try {
			LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);

			String url = "btspp://localhost:" + deviceUUID
					+ ";name=BlueToothServer";

			server = (StreamConnectionNotifier) Connector.open(url);
			// This separated method allows to reconnect wo breaking the app.
			startServer();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("FATAL ERROR: Error while creating the server. " + e.getMessage());
		}
	}

	public void startServer() throws IOException {

		// Wait until client connects
		StreamConnection connection = server.acceptAndOpen();
		ObjectInputStream dis = new ObjectInputStream(connection.openInputStream());

		while (true) {
			try {
				try {
					Beacon beacon = (Beacon) dis.readObject();
					if (beacon.getBeaconType().equals(BeaconType.ACCELEROMETER)){
						accelerometerX = beacon.getCoordinates()[0];
						accelerometerY = beacon.getCoordinates()[1];
						accelerometerZ = beacon.getCoordinates()[2];
					}
				} catch (ClassNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				
			} catch (EOFException e) {
				break;
			}
		}

		connection.close();
		startServer();

	}
}
