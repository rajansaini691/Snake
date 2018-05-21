package objects;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;

public class Block extends Rectangle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected int width, i, j;
	Color color;
	
	public Block(int i, int j) {
		super(blockHeight * i + (i + 1)*padding, blockHeight * j + (j + 1) * padding, blockHeight, blockHeight);
		this.width = blockHeight;
		this.i = i;
		this.j = j;
	}
	
	//Main loop
	public void draw(Graphics2D win) {
		win.setColor((color == null)? blockColor : color);
		win.fillRect(blockHeight * i + (i + 1)*padding, blockHeight * j + (j + 1) * padding, blockHeight, blockHeight);
	}
	
	//Class variables specific to the block itself
	public static int padding = 10;
	public static int blockHeight = 20;
	private static Color blockColor = Color.WHITE;

	//Getter for the i coordinate
	public int getI() {
		return i;
	}
	
	//Getter for the j coordinate
	public int getJ() {
		return j;
	}
	
}
