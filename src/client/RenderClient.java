package client;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.SocketException;
import java.net.UnknownHostException;

import javax.swing.JFrame;
	
public class RenderClient {
	
	static boolean sent = false;
	
	private static Socket clientSocket;

	private static DataOutputStream dout;
	public static DataInputStream din;
	public static BufferedReader input;
	
	public static void sendMessage(String message) {
		try {
			dout.writeUTF(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		//Client-side networking variables
		int port = 2222;
		String host = "localhost";//"10.30.133.161";
		String name = ""; //Stores the client's name
		
		// Sets up a GUI for clients to connect to the server
		LaunchFrame start = new LaunchFrame();		
		
		//Creates the socket & initializes I/O variables
		while(true) {
			try {
				host = start.start();
				name = host.substring(host.indexOf(';') + 1);
				host = host.substring(0, host.indexOf(';'));
				clientSocket = new Socket(host, port);
				System.out.println("Tried");
			} catch(SocketException e) {
				System.out.println("Socket Exception");
				e.printStackTrace();
				continue;
			} catch (UnknownHostException e) {
				System.out.println("Could not connect");
				continue;
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			break;
		}
		System.out.println("success");
		start.close();
		
		//Main frame class
		JFrame frame = new JFrame();
		Loop loop = new Loop();
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.add(loop);
		
		try {
			din = new DataInputStream(clientSocket.getInputStream());
			dout = new DataOutputStream(clientSocket.getOutputStream());
			//input = new BufferedReader(new InputStreamReader(System.in));
			
			dout.writeUTF(name);
		} catch (IOException e) {
			e.printStackTrace();
		}
			
		//Sets frame visible only after client is ready to send info - don't want to send to a null socket
		frame.setVisible(true);
		
		//Tells server to close the thread when the player exits
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent we) {
				try {
					dout.writeUTF("exit");
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
		
		//Main output loop: even though it's empty, it ensures that the client is constantly running
		InputThread thread = new InputThread(loop);
		thread.start();
	}
	
}

// Handles input coming from server
class InputThread extends Thread {
	
	String message = "";
	Loop loop;
	
	public InputThread(Loop loop) {
		this.loop = loop;
	}
	
	@Override
	public void run() {
		try {
			// Initializes from server
			message = RenderClient.din.readUTF();
			int lastIndex = 3;
			for(int i = 0; i < message.length(); i++) {
				if(message.charAt(i) == ',') {
					loop.addSnake(message.substring(lastIndex, i));
					System.out.println(message.substring(lastIndex, i));
					lastIndex = i + 1;
				}
			}
			while(!(message = RenderClient.din.readUTF()).equals("exit")) {
				if(message.length() > 1 && message.charAt(0) == ':' && message.charAt(1) == ':') {
					if(message.charAt(2) == '+') {
						loop.addSnake(message.substring(4, 5));
					} else if(message.charAt(2) == '-') {
						System.out.println("Removing Snake");
						loop.removeSnake(message.substring(4, 5));
					} else {
						System.out.println("Expected + or -, got " + message.charAt(2));
					}
				} else {
					loop.addData(message);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
}
