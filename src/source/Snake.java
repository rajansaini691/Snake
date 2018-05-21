package source;

import javax.swing.JFrame;

public class Snake {
	
	public static int frameHeight, frameWidth;

	public static void main(String[] args) {
		JFrame frame = new JFrame();
		
		frame.setTitle("Breakout!");
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);		
		
		frame.add(new Comp(frame));
		frame.setVisible(true);
		
		frameHeight = frame.getContentPane().getHeight();
		frameWidth = frame.getContentPane().getWidth();
		
		Comp.init(frame);
		
		
	}

}
