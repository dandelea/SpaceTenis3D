package tennis.managers.bluetooth;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import tennis.screens.scenes3d.GameScreen;

import com.badlogic.gdx.math.WindowedMean;

public class BluetoothServer implements Runnable {
	static final String deviceUUID = "0000110100001000800000805F9B34FB";
	static StreamConnectionNotifier server;
	public static boolean connected = false;
	public static boolean paused = false;

	public static WindowedMean movementX;
	public static WindowedMean movementY;
	public static WindowedMean movementZ;
	private final static int WINDOW_SIZE = 10;

	public final static float MESSAGE_END = Float.POSITIVE_INFINITY;
	public final static float MESSAGE_PAUSE = Float.MAX_VALUE;

	@Override
	public void run() {
		try {
			movementX = new WindowedMean(WINDOW_SIZE);
			movementY = new WindowedMean(WINDOW_SIZE);
			movementZ = new WindowedMean(WINDOW_SIZE);

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

		connected = true;
		float x;
		float y;
		float z;

		while (true) {
			try {

				x = dis.readFloat();

				if (x == MESSAGE_END) {

					connected = false;
					System.out.println("Received disconnection");

				} else if (x == MESSAGE_PAUSE
						&& GameScreen.state == GameScreen.GAME_RUNNING) {

					paused = true;
					System.out.println("Paused game remotely");

				} else {

					y = dis.readFloat();
					z = dis.readFloat();
					movementX.addValue(x);
					movementY.addValue(y);
					movementZ.addValue(z);

				}

			} catch (EOFException e) {
				connected = false;
				break;
			}

			if (!connected) {
				connection.close();
				break;
			}
		}

		startServer();
	}
}