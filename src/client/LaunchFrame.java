package client;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class LaunchFrame implements ActionListener {
	private JFrame frame;
	private JButton startButton;
	private JTextField field;
	private JTextField nameField;
	private JLabel instructions;
	private JLabel name;
	private TitleScreen screen;
	private boolean start = false;
	
	public LaunchFrame() {
		//Creates frame & sets properties
		frame = new JFrame();
		frame.setSize(800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null);
		frame.getContentPane().setLayout(new FlowLayout());
		
		//Creates and positions "Enter the IP address of the host computer"
		instructions = new JLabel("IP: ");
		name = new JLabel("Name: ");
		
		//Creates and positions the start button
		startButton = new JButton("Start");
		startButton.setActionCommand("start");
		startButton.setText("Start");
		startButton.addActionListener(this);
		
		//Creates and positions the text field asking what server to connect to
		field = new JTextField(20);
		nameField = new JTextField(20);
		
		//Adds the main canvas
		screen = new TitleScreen();
		screen.setPreferredSize(new Dimension(780, 520));
		
		//Adds components to frame
		frame.getContentPane().add(screen);
		frame.getContentPane().add(instructions);
		frame.getContentPane().add(field);
		frame.getContentPane().add(name);
		frame.getContentPane().add(nameField);
		frame.getContentPane().add(startButton);
		
		//Starts the frame
		frame.setVisible(true);
	}
	
	public String start() {
		start = false;
		
		//Empty loop keeping the frame alive until the start button is pressed
		while(!start) {
			try {
				Thread.sleep(50);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Clicked");
		
		String address = field.getText();
		if(address.isEmpty()) { System.out.println(address); address = "10.30.133.161"; }
		
		String name = nameField.getText();
		
		field.setText(null);
		return address + ";" + name;
	}
	
	public void close() {
		frame.dispose();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(e.getActionCommand().equals("start")) {
			start = true;
			System.out.println("registered click");
		}
	}
}
