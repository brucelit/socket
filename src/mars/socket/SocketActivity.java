package mars.socket;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class SocketActivity extends Activity {
    /** Called when the activity is first created. */
	private Button startButton1,startButton2;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        startButton1 = (Button)findViewById(R.id.startListener1);
        startButton1.setOnClickListener(new StartSocketListener());
        startButton2 = (Button)findViewById(R.id.startListener2);
        startButton2.setOnClickListener(new StartPacketListener());
    }
    
   public class StartSocketListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			new ServerThread().start();
		}
    	
    }
    
  public  class StartPacketListener implements OnClickListener{

	@Override
	public void onClick(View v) {
		try {
			//首先创建一个DatagramSocket对象
			DatagramSocket socket = new DatagramSocket(4567);
			//创建一个InetAddree
			InetAddress serverAddress = InetAddress.getByName("192.168.43.164");
			String str = "hello";
			byte data [] = str.getBytes();
			//创建一个DatagramPacket对象，并指定要讲这个数据包发送到网络当中的哪个地址，以及端口号
			DatagramPacket packet = new DatagramPacket(data,data.length,serverAddress,4567);
			//调用socket对象的send方法，发送数据
			socket.send(packet);
			socket.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	    		
		}
    
    
    
    
   public class ServerThread extends Thread{
    	public void run(){
    		try {
				DatagramSocket socket = new DatagramSocket(4567);
				byte data [] = new byte[1024];
				DatagramPacket packet = new DatagramPacket(data,data.length);
				while(true)
				{
				socket.receive(packet);
				String result = new String(packet.getData(),packet.getOffset(),packet.getLength());
				System.out.println("result--->" + result);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
    	}
    }


}
    
   
    
