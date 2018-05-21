package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.awt.image.RasterFormatException;
import java.util.ArrayList;

import source.GameDriverV3;

public class TitleScreen extends GameDriverV3 implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = -4139379155487395867L;
	int blinkCounter = 0;
	final int blinkMax = 60;
	int snakeCounter = 0;
	final int snakeMax = 3;
	String dir = "right";
	int gameState = 0;
	boolean entered = false;
	
	BufferedImage[] banner = new BufferedImage[32];
	BufferedImage spriteSheet;
	int frameWidth;
	int frameHeight;
	int frame = 0;
	int counter = 0;
	final int counterMax = 10;

	ArrayList<int[]> demoSnake = new ArrayList<int[]>();

	
	public TitleScreen() {
		for(int i = 0; i < 10; i++) {
			demoSnake.add(new int[2]);
			demoSnake.get(i)[0] = 4 + i;
			demoSnake.get(i)[1] = 30;
		}
		
		spriteSheet = this.addImage("banner.png");
		
		int frameWidth = spriteSheet.getWidth() / 3;
		int frameHeight = spriteSheet.getHeight() / 11;
		
		for(int i = 0; i < 3; i++) {
			for(int j = 0; j < 11; j++) {
				if(j + i * 11 < 32) {
					banner[j + i * 11] = spriteSheet.getSubimage(i * frameWidth, j * frameHeight, frameWidth, frameHeight);
				} else {
					banner[31] = banner[30];
				}
			}
		}
		
		this.addKeyListener(this);
	}
	
	public void drawMainScreen(Graphics2D win) {
		win.setFont(new Font("Century Gothic", Font.BOLD, 100));
		win.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		win.setColor(Color.WHITE);
		win.fillRect(0, 0, 800, 600);
		
		win.setColor(Color.BLACK);
		win.drawString("Snake", 230, 150);
		
		win.setFont(new Font("Century Gothic", Font.PLAIN, 30));
		win.drawString("Enter in your IP address and name.", 130, 270);
		win.drawString("Then click START to play", 200, 320);
		
		if(blinkCounter < 30) {
			win.setColor(Color.WHITE);
		}
		win.drawString("Press ENTER for rules", 230, 500);
		
		blinkCounter %= 60;
		blinkCounter++;
		
		win.setColor(Color.BLACK);
		
		//TODO Draw image
		counter++;
		if(counter == counterMax) {
			counter = 0;
			frame++;
			frame %= banner.length;
		}
		
		win.drawImage(banner[frame], 280, 160, null);
		
		
		for(int[] snake : demoSnake) {
			win.fillRect(snake[0] * 14, snake[1] * 14, 4, 4);
		}
		
		snakeCounter++;
		snakeCounter %= snakeMax;
		if(snakeCounter == 0) {
			if(dir == "right") {
				demoSnake.get(0)[0] = demoSnake.get(demoSnake.size() - 1)[0] + 1;
				demoSnake.get(0)[1] = demoSnake.get(demoSnake.size() - 1)[1];
			} else if(dir == "up") {
				demoSnake.get(0)[0] = demoSnake.get(demoSnake.size() - 1)[0];
				demoSnake.get(0)[1] = demoSnake.get(demoSnake.size() - 1)[1] - 1;
			} else if(dir == "left") {
				//TODO implement this
				demoSnake.get(0)[0] = demoSnake.get(demoSnake.size() - 1)[0] - 1;
				demoSnake.get(0)[1] = demoSnake.get(demoSnake.size() - 1)[1];
			} else if(dir == "down") {
				demoSnake.get(0)[0] = demoSnake.get(demoSnake.size() - 1)[0];
				demoSnake.get(0)[1] = demoSnake.get(demoSnake.size() - 1)[1] + 1;
			}
			
			demoSnake.add(demoSnake.get(0).clone());
			demoSnake.remove(0);
			
			if(demoSnake.get(demoSnake.size() - 1)[0] == 50) {
				dir = "up";
			}
			if(demoSnake.get(demoSnake.size() - 1)[1] == 4) {
				dir = "left";
			}
			if(demoSnake.get(demoSnake.size() - 1)[0] == 4) {
				dir = "down";
			}
			if(demoSnake.get(demoSnake.size() - 1)[1] == 30 && demoSnake.get(demoSnake.size() - 1)[0] < 15) {
				dir = "right";
			}
		}
	}
	
	public void drawRules(Graphics2D win) {
		win.setColor(Color.WHITE);
		win.fillRect(0, 0, 800, 600);
		
		win.setColor(Color.BLACK);
		win.setFont(new Font("Century Gothic", Font.BOLD, 75));
		win.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		
		win.drawString("Rules", 290, 100);
		
		win.setFont(new Font("Century Gothic", Font.PLAIN, 30));
		win.drawString("1) Use the arrow keys to move your snake", 50, 175);
		win.drawString("2) Keep your name short (<7 characters)", 50, 225);
		win.drawString("3) When your snake changes color, press", 50, 275);
		win.drawString("SPACEBAR to fire a laser", 90, 320);
		win.drawString("4) You get a point every time you eat", 50, 370);
		win.drawString("5) If you crash into anyone, you die!", 50, 420);
		
		win.drawString("Press ENTER to go back", 200, 500);
	}
	
	@Override
	public void draw(Graphics2D win) {
		if(gameState == 0) {
			drawMainScreen(win);
		} else if(gameState == 1) {
			drawRules(win);
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_ENTER && !entered) {
			gameState += 1;
			gameState %= 2;
			entered = true;
		}
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			entered = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
