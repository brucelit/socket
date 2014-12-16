package mars.socket;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SocketActivity extends Activity {
	/** Called when the activity is first created. */
	private Button startButton1, startButton2;
	private EditText et1;
	TextView et2;
	private TextView tv1, tv2, tv3;
	public String result = null;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);
		et1 = (EditText) findViewById(R.id.et1);
		et2 = (TextView) findViewById(R.id.et2);
		tv1 = (TextView) findViewById(R.id.tv1);
		tv2 = (TextView) findViewById(R.id.tv2);
		tv3 = (TextView) findViewById(R.id.tv3);
		startButton1 = (Button) findViewById(R.id.startListener1);
		startButton1.setOnClickListener(new StartSocketListener());
		startButton2 = (Button) findViewById(R.id.startListener2);
		startButton2.setOnClickListener(new StartPacketListener());
	}

	public class StartSocketListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			new ServerThread().start();
		}

	}

	public class StartPacketListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			try {
				DatagramSocket socket = new DatagramSocket(5678);
				InetAddress serverAddress = InetAddress
						.getByName("192.168.43.224");
				String str = et1.getText().toString();
				byte data[] = str.getBytes();
				DatagramPacket packet = new DatagramPacket(data, data.length,
						serverAddress, 4567);
				socket.send(packet);
				socket.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public class ServerThread extends Thread {
		public void run() {
			try {
				DatagramSocket socket = new DatagramSocket(4567);
				byte data[] = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data, data.length);
				while (true) {
					socket.receive(packet);
					result = new String(packet.getData(), packet.getOffset(),
							packet.getLength());
					// System.out.println("result--->" + result);
					Message msg = new Message();
					Bundle bundle = new Bundle();
					bundle.putString("result", result);
					msg.setData(bundle);
					handler.sendMessage(msg);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			Log.e("result_handler", result);
			et2.setText(result);
		}

	};

}
