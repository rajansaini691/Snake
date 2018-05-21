package server;

import java.util.ArrayList;
import java.util.Arrays;

public class ServerSnake {
	private ArrayList<Integer> moveQueue = new ArrayList<Integer>();
	private ArrayList<int[]> coords = new ArrayList<int[]>();
	private int startingLength = 10;
	private int[] headCoords = new int[2];
	private final int DIR_RIGHT = 0, DIR_LEFT = 1, DIR_UP = 2, DIR_DOWN = 3;
	private int direction = DIR_RIGHT;
	private boolean growing = false;
	private int growNumber = 0;
	private boolean canShoot = true;
	private int coolDown = 10;
	private static final int coolDownMax = 50;
	private String message;
	boolean dead = false;
	
	public ServerSnake() {
		for(int i = 0; i < startingLength; i++) {
			coords.add(new int[] {15, 15 + i});
		}
		headCoords = coords.get(0);
	}
	
	public void addMove(int move) {
		int firstInLine = (moveQueue.size() > 0)? moveQueue.get(0) : direction;
		if(move % 2 == 1 && firstInLine != move - 1) {
			moveQueue.add(move);
		} else if(move % 2 == 0 && firstInLine != move + 1) {
			moveQueue.add(move);
		}
	}

	private String toString(ArrayList<int[]> input) {
		String output = "";
		message = (message != null)? message : "";
		
		for(int i = 0; i < input.size(); i++) {
			output += input.get(i)[0] + ",";
			output += input.get(i)[1] + " ";
		}
		message += output.substring(0, output.length() - 1);
		
		return message;
	}
	
	public void update(ClientThread[] threads, ClientThread thread) {
		headCoords = coords.get(0).clone();
		
		if(moveQueue.size() > 0) {
			direction = moveQueue.get(0);
			moveQueue.remove(0);
		}
		
		switch(direction) {
		case DIR_LEFT:
			headCoords[0]--;
			break;
		case DIR_RIGHT:
			headCoords[0]++;
			break;
		case DIR_UP:
			headCoords[1]--;
			break;
		case DIR_DOWN:
			headCoords[1]++;
		}
		
		coords.add(0, headCoords);
		if(!growing) {
			coords.remove(coords.size() - 1);
		} else {
			growNumber--;
			growing = growNumber != 0;
		}
		checkDeath(threads, thread);
		
	}
	
	public void grow(int amount) {
		growing = true;
		growNumber = amount;
	}
	
	public void shootLaser(ClientThread[] threads) {
		if(canShoot) {
			int coord = (direction > 1)? headCoords[0] : headCoords[1];
			int otherCoord = (direction > 1)? headCoords[1] : headCoords[0];
			
			for(int i = 0; i < threads.length; i++) {			
				if(threads[i] != null && threads[i].snake != null) {
					threads[i].snake.cut(direction, coord, otherCoord);
				}
			}
			
			message = "l" + coord + "," + direction + " " + message;
			
			canShoot = false;
			coolDown = coolDownMax;
		}
	}
	
	public void cut(int direction, int coord, int otherCoord) {
		boolean cutting = false;
		if(direction > 1) {	
			for(int i = 0; i < coords.size(); i++) {
				if(cutting) {
					coords.remove(i);
					i--;
				} else if(coord == coords.get(i)[0] && ((direction == 3 && coords.get(i)[1] > otherCoord + 2) || (direction == 2 && coords.get(i)[1] < otherCoord - 2))) {
					cutting = true;
				}
			}
		} else {
			for(int i = 0; i < coords.size(); i++) {
				if(cutting) {
					coords.remove(i);
					i--;
				} else if(coord == coords.get(i)[1] && ((direction == 0 && coords.get(i)[0] > otherCoord + 2) || (direction == 1 && coords.get(i)[0] < otherCoord - 2))) {
					cutting = true;
				}
			}
		}
	}
	
	public void die(ClientThread thread) {
		coords = null;
		dead = true;
		thread.kill();
	}
	
	public boolean checkDeath(ClientThread[] threads, ClientThread myThread) {
		
		for(ClientThread thread : threads) {
			if(thread != null && thread.snake != null) {
				ArrayList<int[]> tempCoords = thread.snake.coords;
				for(int i = 1; (tempCoords != null && i < tempCoords.size()); i++) {
					if(Arrays.equals(tempCoords.get(i), headCoords)) {
						thread.score += 5;
						die(myThread);
						return true;
					}
				}
			}
		}
		
		if(coords.get(0)[0] < 0 || coords.get(0)[0] > Server.fieldWidth || coords.get(0)[1] < 0 || coords.get(0)[1] > Server.fieldHeight) {
			//System.out.println("Out of bounds");
			return true;
		}
		return false;
	}
	
	public void checkEat(ServerFood food, ClientThread[] threads, ClientThread thread) {
		if(coords.get(0)[0] == food.getI() && coords.get(0)[1] == food.getJ()) {
			grow(5);
			food.teleport(threads);
			thread.score += 1;
		}
	}
	
	public String broadcast() {
		toString(coords);
		String temp = message;
		message = "";
		
		if(!canShoot) coolDown--; if(coolDown == 0) canShoot = true;
		if(canShoot) temp = "c" + temp;
		
		return temp;
	}
	
	public int getDir() {
		return direction;
	}
	
	public int[] getHead() {
		return headCoords;
	}
	
	public ArrayList<int[]> getCoords() {
		return coords;
	}
}
