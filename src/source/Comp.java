package source;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;

import objects.Block;
import objects.Food;
import objects.PlayerSnake;
import objects.SnakeBlock;

public class Comp extends GameDriverV3 implements KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	Color backgroundColor;
	static Block[][] field;
	private static int fieldWidth;
	private static int fieldHeight;
	PlayerSnake snake;
	static Food food;
	
	public Comp(JFrame frame) {
		backgroundColor = new Color(0, 0, 0);
		
		snake = new PlayerSnake();
		
		//adds KeyListeners
		this.addKeyListener(this);
		this.addKeyListener(snake);
	}
	
	//Used for initializing any window-dependent items
	public static void init(JFrame frame) {
		//Declares the field, whose size is dependent on the frame's size
		setFieldWidth((int) (((double) Snake.frameWidth - Block.padding)/(Block.blockHeight + Block.padding)));
		setFieldHeight((Snake.frameHeight - Block.padding)/(Block.blockHeight + Block.padding));
		field = new Block[getFieldWidth()][getFieldHeight()];
		
		//Fills in the field with Block objects
		for(int i = 0; i < getFieldWidth(); i++) {
			for(int j = 0; j < getFieldHeight(); j++) {
				field[i][j] = new Block(i, j);
			}
		}
		
		field[10][10] = new SnakeBlock(10, 10);

		food = new Food();
	}
	
	//Game loop
	@Override
	public void draw(Graphics2D win) {
		//Draws the background
		win.setColor(backgroundColor);
		win.fillRect(0, 0, 800, 600);
		
		//Draws the field
		/*for(int i = 0; i < fieldWidth; i++) {
			for(int j = 0; j < fieldHeight; j++) {
				field[i][j].draw(win);
			}
		}*/
		
		food.moveAndDraw(win, snake);
		
		//Draws the snake
		snake.moveAndDraw(win);
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub
		
	}

	public static int getFieldWidth() {
		return fieldWidth;
	}

	public static void setFieldWidth(int fieldWidth) {
		Comp.fieldWidth = fieldWidth;
	}

	public static int getFieldHeight() {
		return fieldHeight;
	}

	public static void setFieldHeight(int fieldHeight) {
		Comp.fieldHeight = fieldHeight;
	}

}
