package client;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;
import java.util.ArrayList;

import javax.sound.midi.Sequencer;

import objects.Block;
import source.GameDriverV3;

public class Loop extends GameDriverV3 implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	//Render given string with syntax: x1,y1 x2,y2 x3,y3;
	//Semicolon stops the string from populating; coords are separated by commas
	ArrayList<int[]> coords = new ArrayList<int[]>();
	
	ArrayList<ClientSnake> snakes = new ArrayList<ClientSnake>();
	String direction = "0";
	boolean space = false;
	
	Scoreboard board;
	
	Sequencer sequencer;
	InputStream is;
	
	String data;
	String scores;
	
	public Loop() {
		this.addKeyListener(this);
		board = new Scoreboard(snakes);
	}
	
	// Takes incoming data from server and splits it between each snake, separated by ';'
	public void addData(String data) {
		int snakeNumber = 0;
		int lastIndex = 0;
		this.data = data;
		
		for(int i = 0; i < data.length(); i++) {
			if(data.charAt(i) == ';') {
				try {
					if(data.charAt(lastIndex) == 'D') {
						/*if(Integer.parseInt(data.substring(lastIndex + 1, i) == ID)) {
							
						}*/
					} else if(data.length() > 0) {
						snakes.get(snakeNumber).fillArray(data.substring(lastIndex, i));
					}
				} catch(IndexOutOfBoundsException e) {
					e.printStackTrace();
					System.out.println(data);
				}
				lastIndex = i + 1;
				snakeNumber++;
			}
			if(data.charAt(i) == ':') {
				scores = data.substring(i+1);
				board.setScores(scores);
			}
		}
		
	}
	
	/*
	 * Called when a client enters the room
	 * @param identifier	a string that is assigned uniquely to each snake
	 */
	public void addSnake(String identifier) {
		snakes.add(new ClientSnake(Color.RED, identifier));
	}
	
	
	/*
	 * Called when a client leaves the room
	 * @param identifier	the ID of the snake to be removed
	 */
	public void removeSnake(String identifier) {
		for(int i = 0; i < snakes.size(); i++) {
			if(snakes.get(i).getID().equals(identifier)) {
				snakes.remove(i);
				return;
			}
		}
	}

	
	@Override
	public void draw(Graphics2D win) {
		synchronized(this) {
			win.setColor(Color.BLACK);
			win.fillRect(0, 0, 800, 600);
			
			win.setColor(Color.RED);
			
			Block.blockHeight = 4;
			
			for(ClientSnake snake : snakes) {
				snake.moveAndDraw(win);
			}
			
			board.draw(win);
			
		}
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		//Controls main snake movement
		if(e.getKeyCode() == KeyEvent.VK_RIGHT && !direction.equals("1")) {
			RenderClient.sendMessage("0");
			direction = "0";
		}
		if(e.getKeyCode() == KeyEvent.VK_LEFT && !direction.equals("0")) {
			RenderClient.sendMessage("1");
			direction = "1";
		}
		if(e.getKeyCode() == KeyEvent.VK_UP && !direction.equals("3")) {
			RenderClient.sendMessage("2");
			direction = "2";
		}
		if(e.getKeyCode() == KeyEvent.VK_DOWN) {
			RenderClient.sendMessage("3");
			direction = "3";
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE && !space) {
			RenderClient.sendMessage(" ");
			space = true;
		}
		
		if(e.getKeyCode() == KeyEvent.VK_R) {
			RenderClient.sendMessage("r");
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			space = false;
		}
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
