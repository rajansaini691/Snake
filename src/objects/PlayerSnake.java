package objects;

import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;

import source.Comp;

public class PlayerSnake implements KeyListener {
	// Defines the snake as an ArrayList of SnakeBlocks
	ArrayList<SnakeBlock> snake;

	// In case the user presses buttons too quickly, the directions get put
	// into a queue so that the snake can respond accurately.
	ArrayList<Integer> moveQueue;

	// Stores direction of snake as an integer:
	// 0 - right, 1 - left, 2 - up, 3 - down; defaults to up
	private int direction = 2;
	private final int DIR_RIGHT = 0, DIR_LEFT = 1, DIR_UP = 2, DIR_DOWN = 3;

	// Stores whether the snake is growing or not
	boolean growing = true;
	int growNumber = 2;

	// Handles delay between moves
	private int moveTimer = 0;
	private static final int MOVE_DELAY = 5;

	// Main constructor
	public PlayerSnake() {
		// Initializes and populates snake
		snake = new ArrayList<SnakeBlock>();
		for (int i = 0; i < 5; i++) {
			snake.add(new SnakeBlock(20, 10 + i));
		}

		// Initializes moveQueue
		moveQueue = new ArrayList<Integer>();
	}

	// Main game loop
	public void moveAndDraw(Graphics2D win) {
		for (int i = 0; i < snake.size(); i++) {
			snake.get(i).draw(win);
		}
		
		move();
	}
	
	public void grow(int amount) {
		growing = true;
		growNumber = amount;
	}

	// Main method to move the snake, creating a delay between movements and calling
	// SnakeBlock's respective move method, depending on the direction from
	// moveQueue
	public void move() {
		if (canMove()) {
			// Basically, if there is nothing in the queue, that means that the snake will
			// continue in the same direction. If there is something (size > 0), then move
			// the direction of the next-in-line element in the queue.
			direction = (moveQueue.size() == 0) ? direction : moveQueue.get(0);
			if (growing) {
				expand();
				if(--growNumber == 1) {
					growing = false;
				}
			} else {
				// Moves the last block in the snake to the beginning
				snake.add(0, snake.get(snake.size() - 1));
				snake.remove(snake.size() - 1);
				slither();
			}

			// Once the value from the queue is used, delete it so that you can use the next
			// value.
			if (moveQueue.size() > 0) {
				moveQueue.remove(0);
			}

			checkDeath();
		}
	}

	// Moves by taking from the back and putting up front; normal locomotion
	private void slither() {
		switch (direction) {
		case DIR_LEFT:
			snake.get(0).moveLeft(snake.get(1));
			break;
		case DIR_RIGHT:
			snake.get(0).moveRight(snake.get(1));
			break;
		case DIR_DOWN:
			snake.get(0).moveDown(snake.get(1));
			break;
		case DIR_UP:
			snake.get(0).moveUp(snake.get(1));
			break;
		default:
			System.out.println("Unknown direction, stored as: " + direction);
		}
	}

	// Basically slither, except it adds a new block to the front of the snake
	private void expand() {
		switch (direction) {
		case DIR_UP:
			snake.add(0, new SnakeBlock(snake.get(0).getI(), snake.get(0).getJ() - 1));
			break;
		case DIR_DOWN:
			snake.add(0, new SnakeBlock(snake.get(0).getI(), snake.get(0).getJ() + 1));
			break;
		case DIR_LEFT:
			snake.add(0, new SnakeBlock(snake.get(0).getI() - 1, snake.get(0).getJ()));
			break;
		case DIR_RIGHT:
			snake.add(0, new SnakeBlock(snake.get(0).getI() + 1, snake.get(0).getJ()));
			break;
		}
	}

	// Loops through snake array and checks to see if the snake collides with itself
	// or goes out of bounds
	private void checkDeath() {
		for (int i = 2; i < snake.size(); i++) {
			if ((snake.get(i).getI() == snake.get(0).getI()) && (snake.get(i).getJ() == snake.get(0).getJ())) {
				System.out.println("DEEEEEEAAAAAAAAAAAAAAAADDDDDDDDDDDDD");
				break;
			}
		}
		if (snake.get(0).getI() < 0 || snake.get(0).getJ() < 0 || snake.get(0).getI() > Comp.getFieldWidth()
				|| snake.get(0).getJ() >= Comp.getFieldHeight()) {
			System.out.println("out of bounds");
		}
	}

	// Checks if enough frames have gone by that the snake can move again - adds
	// delay between movements
	private boolean canMove() {
		moveTimer++;
		moveTimer %= MOVE_DELAY;
		return moveTimer == 0;
	}

	public boolean intersects(Block block) {
		return (snake.get(0).getI() == block.getI() && snake.get(0).getJ() == block.getJ());
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_RIGHT && direction != DIR_LEFT) {
			direction = DIR_RIGHT;
			moveQueue.add(DIR_RIGHT);
		} else if (e.getKeyCode() == KeyEvent.VK_LEFT && direction != DIR_RIGHT) {
			direction = DIR_LEFT;
			moveQueue.add(DIR_LEFT);
		}

		if (e.getKeyCode() == KeyEvent.VK_UP && direction != DIR_DOWN) {
			direction = DIR_UP;
			moveQueue.add(DIR_UP);
		} else if (e.getKeyCode() == KeyEvent.VK_DOWN && direction != DIR_UP) {
			direction = DIR_DOWN;
			moveQueue.add(DIR_DOWN);
		}
		
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			//growing = true;
			//growNumber = 5;
			System.out.println("Growing");
			grow(4);
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}
}
