package tennis.managers.bluetooth;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

import tennis.managers.Log;
import tennis.screens.scenes3d.GameScreen;

import com.badlogic.gdx.math.WindowedMean;

/**
 * Bluetooth Server implementation with BlueCove library. Runs in a different
 * thread from the main game.
 * 
 * @see <a href="https://code.google.com/p/bluecove/">Bluecove library</a>
 * @author Daniel de los Reyes Leal
 * @version 1
 */
public class BluetoothServer implements Runnable {
	private static final String UUID = "0000110100001000800000805F9B34FB";
	private static StreamConnectionNotifier server;
	public static boolean connected = false;
	public static boolean paused = false;

	public static WindowedMean movementX;
	public static WindowedMean movementY;
	public static WindowedMean movementZ;

	/**
	 * Window size to store a swing movement.
	 */
	private final static int WINDOW_SIZE = 10;

	private final static float MESSAGE_END = Float.POSITIVE_INFINITY;
	private final static float MESSAGE_PAUSE = Float.MAX_VALUE;

	@Override
	/**
	 * Runs the bluetooth server.
	 */
	public void run() {
		try {
			movementX = new WindowedMean(WINDOW_SIZE);
			movementY = new WindowedMean(WINDOW_SIZE);
			movementZ = new WindowedMean(WINDOW_SIZE);

			// SET DISCOVERABLE
			LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);

			String url = "btspp://localhost:" + UUID + ";name=BlueToothServer";
			server = (StreamConnectionNotifier) Connector.open(url);

			// ABLE TO RECONNECT
			listen();
		} catch (IOException e) {
			Log.error(e.getMessage());
		}
	}

	/**
	 * Bluetooth server listener. Called from {@link #run()}
	 * 
	 * @throws IOException
	 */
	private void listen() throws IOException {

		// Wait until client connects
		StreamConnection connection = server.acceptAndOpen();
		DataInputStream dis = connection.openDataInputStream();

		connected = true;
		float x, y, z;

		while (true) {
			try {

				x = dis.readFloat();

				if (x == MESSAGE_END) {
					// DETECTED END OF CONNECTION
					connected = false;
					Log.info("BT: Connection aborted");
				} else if (x == MESSAGE_PAUSE
						&& GameScreen.state == GameScreen.GAME_RUNNING) {
					// DETECTED PAUSE
					paused = true;
					Log.info("BT: Game paused remotely");
				} else {
					// NORMAL BEHAVIOUR. SWING
					y = dis.readFloat();
					z = dis.readFloat();
					movementX.addValue(x);
					movementY.addValue(y);
					movementZ.addValue(z);

				}

			} catch (EOFException e) {
				connected = false;
				Log.error("Error listening. " + e.getMessage());
				break;
			}

			if (!connected) {
				connection.close();
				break;
			}
		}

		listen();
	}
}