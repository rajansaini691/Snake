package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.util.ArrayList;

public class Scoreboard {
	
	int numOfSnakes;
	Color textColor = new Color(73, 73, 73);
	ArrayList<ClientSnake> snakes;
	
	String scores;
	//Format of "name1,score1 name2,score2"
	
	//Stores names in array
	ArrayList<String[]> names = new ArrayList<String[]>();
	
	public Scoreboard(ArrayList<ClientSnake> snakes) {
		this.snakes = snakes;
	}
	
	public void draw(Graphics2D win) {
		win.setFont(new Font("Century Gothic", Font.PLAIN, 40));
		win.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING,  RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		win.setColor(textColor);
		win.drawString("Number of snakes: " + snakes.size(), 200, 500);
		
		win.setFont(new Font("Century Gothic", Font.PLAIN, 30));
		for(int i = 0; i < names.size(); i++) {
			try {
				win.drawString(names.get(i)[0], 750 - names.get(i)[0].length() * 30, 400 + 80 * i);
				win.drawString(names.get(i)[1], 750, 400 + 80 * i);
			} catch (IndexOutOfBoundsException e) {
				
			}
		}
	}
	
	public void setScores(String scores) {
		int charIndex = 0;
		int nameLength = 0;
		String[] tempScores = new String[2];
		names.clear();
		
		//System.out.println(scores);
		
		while(charIndex < scores.length() && scores.length() > 0) {
			
			while(charIndex < scores.length() && scores.charAt(charIndex) != ',') {
				nameLength++;
				charIndex++;
			}
			tempScores[0] = scores.substring(charIndex - nameLength, charIndex);
			nameLength = 0;
			charIndex++;
			
			while(charIndex < scores.length() && scores.charAt(charIndex) != ' ') {
				charIndex++;
				nameLength++;
			}
			
			tempScores[1] = scores.substring(charIndex - nameLength, charIndex);
			
			names.add(tempScores.clone());
			
			charIndex++;
		}
		this.scores = scores;
	}
}
