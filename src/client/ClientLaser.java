package client;

import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;

import objects.Block;

public class ClientLaser {
	private Color color;	
	private int coord, direction, width = 3, height = width;
	
	public ClientLaser(Color color) {
		this.color = color;
	}
	
	public void setCoords(int coord, int direction) {
		this.coord = coord;
		this.direction = direction;
	}
	
	//
	public void draw(Graphics2D win, int[] headCoord) {
		int x, y;
		win.setColor(color);
		if(direction > 1) {
			//TODO Draw vertical
			x = coord * (Block.blockHeight + 10) + 1;
			y = headCoord[1] * (Block.blockHeight + 10) + ((direction == 2)? -20 : 20);
			
			win.setPaint(new GradientPaint(x, 0, Color.YELLOW, x + width, 0, Color.BLACK));
			win.fillRect(x, (direction == 2)? 0 : y, width, (direction == 2)? y : 600);
			win.setPaint(new GradientPaint(x - width, 0, Color.BLACK, x, 0, Color.YELLOW));
			win.fillRect(x - width, (direction == 2)? 0 : y, width, (direction == 2)? y : 600);
			
			win.setColor(color);
			win.drawLine(x, y, x, (direction == 2)? 0 : 600);
		} else {
			//TODO Draw horizontal
			x = headCoord[0] * (Block.blockHeight + 10) + ((direction == 1)? -20 : 20);
			y = coord * (Block.blockHeight + 10) + Block.blockHeight/2;
			
			win.setPaint(new GradientPaint(0, y, Color.YELLOW, 0, y + height, Color.BLACK)); 
			win.fillRect((direction == 1)? 0 : x, y, (direction == 1)? x : 600, height);
			win.setPaint(new GradientPaint(0, y - height, Color.BLACK, 0, y, Color.YELLOW));
			win.fillRect((direction == 1)? 0 : x, y - height, (direction == 1)? x : 600, height);
			
			win.drawLine((direction == 1)? 0 : 800, y, x, y);
		}
		
	}
	
}
