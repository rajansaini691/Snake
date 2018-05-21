package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Random;

import source.Comp;

public class Food extends Block {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private static Random random = new Random();
	private boolean empty = true;
	
	public Food() {
		super(random.nextInt(Comp.getFieldWidth()), random.nextInt(Comp.getFieldHeight()));
		color = Color.BLUE;
	}
	
	public void moveAndDraw(Graphics2D win, PlayerSnake snake) {
		super.draw(win);
		
		if(snake.intersects(this)) {
			empty = false;
			while(!empty) {
				i = random.nextInt(Comp.getFieldWidth());
				j = random.nextInt(Comp.getFieldHeight());
				for(int i = 0; i < snake.snake.size(); i++) {
					if(i == snake.snake.get(0).getI() && j == snake.snake.get(0).getJ()) {
						empty = true;
						break;
					}
				}
				empty = false;
			}
			
			snake.grow(3);
		}
		
	}

}
