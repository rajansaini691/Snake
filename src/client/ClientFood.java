package client;

import java.awt.Graphics2D;

import objects.Block;

public class ClientFood {
	
	private int i, j;
	
	public void fillArray(int i, int j) {
		this.i = i;
		this.j = j;
	}
	
	public void draw(Graphics2D win) {
		win.fillRect(i*(Block.blockHeight + 10), j*(Block.blockHeight + 10), Block.blockHeight, Block.blockHeight);
	}
}
