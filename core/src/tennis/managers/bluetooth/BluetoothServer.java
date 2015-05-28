package tennis.managers.bluetooth;

import java.io.DataInputStream;
import java.io.IOException;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;
import javax.microedition.io.Connector;
import javax.microedition.io.StreamConnection;
import javax.microedition.io.StreamConnectionNotifier;

public class BluetoothServer extends Thread{
	static final String deviceUUID = "0000110100001000800000805F9B34FB";

	// start server
	public void startServer() throws IOException {
		LocalDevice.getLocalDevice().setDiscoverable(DiscoveryAgent.GIAC);
		
		String url = "btspp://localhost:" + deviceUUID + ";name=BlueToothServer";
		
		StreamConnectionNotifier server = (StreamConnectionNotifier) Connector.open(url);
		
		// Wait until client connects
		StreamConnection connection = server.acceptAndOpen();
		DataInputStream dis = connection.openDataInputStream();
		
		float c;
		while(true){
			c = dis.readFloat();
			System.out.println(c);
		    if (c == 'x')
		        break;
		}
		
		connection.close();

	}

}
