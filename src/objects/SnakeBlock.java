package objects;

import java.awt.Color;

public class SnakeBlock extends Block {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	public SnakeBlock(int i, int j) {
		super(i, j);
		color = Color.red;
	}
	
	/****************Move Methods*******************/
	public void moveLeft(SnakeBlock sb) {
		this.i = sb.getI() - 1;
		this.j = sb.getJ();
	}
	
	public void moveRight(SnakeBlock sb) {
		this.i = sb.getI() + 1;
		this.j = sb.getJ();
	}
	
	public void moveUp(SnakeBlock sb) {
		this.i = sb.getI();
		this.j = sb.getJ() - 1;
	}
	
	public void moveDown(SnakeBlock sb) {
		this.i = sb.getI();
		this.j = sb.getJ() + 1;
	}
	
}
