package server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.Timer;
import java.util.TimerTask;

public class Server {
	
	static int fieldWidth = 25;
	static int fieldHeight = 18;
	static int delay = 100;
	
	public static void main(String[] args) {
		int port = 2222;
		ServerSocket ss;
		
		ClientThread[] threads = new ClientThread[20];
		
		(new BroadcastThread(threads)).start();

		try {
			ss = new ServerSocket(port);

			for (int i = 0; i < threads.length; i++) {
				Socket client = ss.accept();
				(threads[i] = new ClientThread(client, threads, i)).start();
				System.out.println("A client has connected");
				String message = ":: "; //+ i + ",";
				for(int j = 0; j < threads.length; j++) {
					if(threads[j] != null /*&& j != i*/) {
						message += j + ",";
					}
				}
				threads[i].dout.writeUTF(message);
				for(int j = 0; j < threads.length; j++) {
					if(threads[j] != null && i != j) {
						threads[j].dout.writeUTF("::+ " + i + " connected");
					}
				}
			}

		} catch(BindException e) {
			System.exit(0);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}

// Broadcasts and processes
class BroadcastThread extends Thread {
	boolean gameAlive = true;
	String message = "message";
	Timer timer = new Timer();
	ClientThread[] threads;

	public BroadcastThread(ClientThread[] threads) {
		this.threads = threads;
	}

	@Override
	public void run() {
		timer.scheduleAtFixedRate(new TimerTask() {

			@Override
			public void run() {
				String message = "";
				for(int i = 0; i < threads.length; i++) {
					if(threads[i] != null && !threads[i].helper) {
						message += threads[i].broadcast();
					}
				}
				
				message += ":";
				for(int i = 0; i < threads.length; i++) {
					if(threads[i] != null && !threads[i].snake.dead) {
						message += threads[i].name + "," + threads[i].score + " ";
					}
				}
				
				for (int i = 0; i < threads.length; i++) {
					if (threads[i] != null && (threads[i].snake != null || threads[i].helper)) {
						try {
							if(!threads[i].snake.dead) {
								threads[i].update();
							}
							threads[i].dout.writeUTF(message);
							
						} catch (SocketException e) {
							
						} catch (IOException e) {
							e.printStackTrace();
						} 
					}
				}
			}
		}, 10, Server.delay);
	}
}
class ClientThread extends Thread {

	Socket client;
	boolean disconnect = false;
	DataInputStream din;
	DataOutputStream dout;
	ClientThread[] threads;
	ServerSnake snake;
	ServerFood food;
	ServerLaser laser;
	String name;
	int score = 0;
	int index;
	boolean helper = false;

	public ClientThread(Socket client, ClientThread[] threads, int index) {
		this.client = client;
		this.threads = threads;
		this.index = index;
		
		if(!helper) {
			snake = new ServerSnake();
			food = new ServerFood();
		}
		
		try {
			din = new DataInputStream(client.getInputStream());
			dout = new DataOutputStream(client.getOutputStream());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void kill() {
		for(ClientThread thread : threads) {
			if(thread != null) {
				try {
					thread.dout.writeUTF("::- " + index + " disconnected");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void revive() {
		for(ClientThread thread : threads) {
			if(thread != null) {
				try {
					thread.dout.writeUTF("::+ " + index + " connected");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void update() {
		snake.checkEat(food, threads, this);
		snake.update(threads, this);
	}
	public void run() {
		try {
			name = din.readUTF();
			System.out.println(name);
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		
		while (!disconnect) {
			try {
				String input = din.readUTF();
				if (!input.equals("exit")) {
					if(!helper && !snake.dead) {
						//Shoot laser instead if " "
						if(input.equals(" ")) {
							// Cut every other snake
							snake.shootLaser(threads); 
						} else if(!input.equals("r")){
							snake.addMove(Integer.parseInt(input));
						}
					} else if(input.equals("r")) {
						snake = new ServerSnake();
					}
				} else {
					dout.writeUTF("exit");
					
					snake = null;
					
					for(int i = 0; i < threads.length; i++) {
						if(threads[i] != this && threads[i] != null) {
							threads[i].dout.writeUTF("::- " + index + "disconnected");
						}
					}
					
					System.out.println("A client has left");
					disconnect = true;
					din.close();
					dout.close();
					client.close();
					threads[index] = null;
					return;
				}
			} catch(SocketException e) {
				//System.out.println(index);
				//e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public String broadcast() {
		return (!snake.dead)? snake.broadcast() + " " + food.broadcast() + ";" : "";
	}

}