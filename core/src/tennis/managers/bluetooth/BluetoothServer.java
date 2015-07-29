package tennis.managers.bluetooth;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import com.badlogic.gdx.math.Vector3;

public class BluetoothServer implements Runnable {
	static final String deviceUUID = "0000110100001000800000805F9B34FB";
	public static Vector3 accelerometer;
	static StreamConnectionNotifier server;

	@Override
	public void run() {
		try {
			accelerometer = new Vector3();
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
				accelerometer.x = dis.readFloat();
				accelerometer.y = dis.readFloat();
				accelerometer.z = dis.readFloat();
				
				System.out.println(accelerometer);
			} catch (EOFException e) {
				break;
			}
		}

		connection.close();
		startServer();

	}
}