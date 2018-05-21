package client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.ArrayList;

import objects.Block;

public class ClientSnake {
	Color color;
																																																Color mainColor;
	Color shootColor;
	ArrayList<int[]> coords = new ArrayList<int[]>();
	String ID;
	ClientFood food;
	ClientLaser laser;
	int laserFrameCount = 0;
	boolean shooting = false;
	
	public ClientSnake(Color color, String ID) {
		this.color = color;
		this.ID = ID;
		this.food = new ClientFood();
		
		switch(this.ID) {
		case "0":
			this.mainColor = Color.RED;
			this.shootColor = new Color(232, 116, 0);
			break;
		case "1":
			this.mainColor = Color.BLUE;
			this.shootColor = new Color(0, 157, 255);
			break;
		case "2":
			this.mainColor = new Color(7, 132, 47);
			this.shootColor = new Color(0, 255, 42);
			break;
		}
		
		color = mainColor;
	}
	
	public void moveAndDraw(Graphics2D win) {
		synchronized(this) {
			win.setColor(color);
			food.draw(win);
			
			if(shooting) {
				drawLaser(win);
			}
			
			for(int i = 0; i < coords.size(); i++) {
				win.fillRect(coords.get(i)[0]*(Block.blockHeight + 10), coords.get(i)[1]*(Block.blockHeight + 10), Block.blockHeight, Block.blockHeight);
			}
			
		}
	}
	
	public void drawLaser(Graphics2D win) {
		if (laserFrameCount < 10) {
			if(coords.size() > 0) laser.draw(win, coords.get(0));
			laserFrameCount++;
		} else {
			laser = null;
			laserFrameCount = 0;
			shooting = false;
		}
	}
	
	public void initLaser(int coord, int dir) {
		laser = new ClientLaser(Color.yellow);
		laser.setCoords(coord, dir);
		shooting = true;
	}
	
	public synchronized void fillArray(String data) {
		int charIndex = 0;
		int numLength = 0;
		int[] tempCoords = new int[2];
		boolean laser = false;
		
		if(data.charAt(charIndex) == 'c') {
			//Ready to shoot
			charIndex++;
			color = shootColor;
		} else {
			color = mainColor;
		}
		
		if(data.charAt(charIndex) == 'l') {
			laser = true;
			charIndex++;
		}
		
		coords.clear();
		
		while(charIndex < data.length()) {
			//Raise count until you reach a comma
			numLength = 0;
			while(data.charAt(charIndex) != ',') {
				charIndex++;
				numLength++;
			}
			
			//Populate x coordinate
			try {	
				tempCoords[0] = Integer.valueOf(data.substring(charIndex - numLength, charIndex));	
			} catch (NumberFormatException e) {
				tempCoords[0] = Integer.valueOf(data.substring(1, 3));
			}
			
			numLength = 0;
			charIndex++;
			
			//Populate y coordinate
			while(charIndex != data.length() && data.charAt(charIndex) != ' ' && data.charAt(charIndex) != ';') {
				charIndex++;
				numLength++;
			}
			
			tempCoords[1] = Integer.valueOf(data.substring(charIndex - numLength, charIndex));
			
			//Populate ArrayList - lc,d x,y x,y x,y i,j; where l flags laser, c is the coordinate & d is the direction
			if(laser) {
				//Draw laser based on coords
				laser = false;
				initLaser(tempCoords[0], tempCoords[1]);
			} else if(charIndex == data.length() - 1) {
				food.fillArray(tempCoords[0], tempCoords[1]);
			} else {
				coords.add(tempCoords.clone());
			}
			
			charIndex++;
		
		}
	}
	
	public String getID() {
		return ID;
	}
	
	
}
