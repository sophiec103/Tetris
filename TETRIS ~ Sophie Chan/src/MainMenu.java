/*
 * Tetris
 * ~ Sophie Chan 2021
 * Classic tetris game: blocks will descend and stack; rows will clear when filled.
 * Players should try to obtain a higher score by clearing as many rows as possible.
 * This can be done by moving and rotating the blocks and using other functionalities.
 * The game will end when a block exceeds the top of the board.
 */

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

//contains what is necessary for Main Menu, Controls, and Credits page graphics and functionalities
public class MainMenu extends JPanel{
	
	private static JButton startButton, scoreboardButton, controlsButton, creditsButton;
	private static ImageIcon startbutton, scoreboardbutton, controlsbutton, creditsbutton;
	private static Image controlstitle, creditstitle, pressm;
	private static Image controls, credits;
	
	private String page;
	private static int pressmCounter = 0;
	private static boolean pressmCheck;
	
	private static Image logo = Toolkit.getDefaultToolkit().createImage("logo.png");
	
	private static Game mainGame = new Game();
	
	//Description: constructor that initializes the jcomponents and adds them to each other
	//Parameters: string dictating the page to be displayed ; Return: n/a
	public MainMenu(String page){
		setPreferredSize (new Dimension (434, 583));
		
		this.page = page;
		
		if (page.equals("main")){
			startbutton = new ImageIcon ("startbutton.png");
			scoreboardbutton = new ImageIcon ("scoreboardbutton.png");
			controlsbutton = new ImageIcon ("controlsbutton.png");
			creditsbutton = new ImageIcon ("creditsbutton.png");

			GridBagConstraints gbc = new GridBagConstraints();
			startButton = new JButton (startbutton);		//create buttons
			scoreboardButton = new JButton (scoreboardbutton);
			controlsButton = new JButton (controlsbutton);
			creditsButton = new JButton (creditsbutton);

			gbc.fill = GridBagConstraints.HORIZONTAL;	//place buttons using gridbag layout
			gbc.gridx = 0;	
			gbc.gridy = 0;	
			add(Box.createRigidArea(new Dimension(500, 260)), gbc);
			gbc.gridy = 1;
			add(startButton, gbc);	
			gbc.gridy = 2;	
			add(scoreboardButton, gbc);	
			gbc.gridy = 3;	
			add(controlsButton, gbc);
			gbc.gridy = 4;	
			add(creditsButton, gbc);

			startButton.setBorderPainted(false);	//get rid of the button backgrounds
			startButton.setFocusPainted(false);
			startButton.setContentAreaFilled(false);
			scoreboardButton.setBorderPainted(false);
			scoreboardButton.setFocusPainted(false);
			scoreboardButton.setContentAreaFilled(false);
			controlsButton.setBorderPainted(false);
			controlsButton.setFocusPainted(false);
			controlsButton.setContentAreaFilled(false);
			creditsButton.setBorderPainted(false);
			creditsButton.setFocusPainted(false);
			creditsButton.setContentAreaFilled(false);

			//Description: actionlisteners that allow for the buttons to function as intended
			//Parameters: ActionEvent e ; Return: n/a
			startButton.addActionListener(new ActionListener() {	
				public void actionPerformed(ActionEvent e) {
					Driver.switchScreen(2);
					mainGame.newGame();
				}
			});
			scoreboardButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Driver.switchScreen(3);
				}
			});
			controlsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Driver.switchScreen(4);
				}
			});
			creditsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Driver.switchScreen(5);
				}
			});
			controlsButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					Driver.switchScreen(4);
				}
			});
			
		}else {
			controlstitle = Toolkit.getDefaultToolkit().createImage("controlsbutton.png");
			creditstitle = Toolkit.getDefaultToolkit().createImage("creditsbutton.png");
			pressm = Toolkit.getDefaultToolkit().createImage("pressm.png");
			controls = Toolkit.getDefaultToolkit().createImage("controls.png");
			credits = Toolkit.getDefaultToolkit().createImage("credits.png");
		}
		
	    //add a tracker for all of the images necessary when program first runs
        MediaTracker tracker = new MediaTracker (this);
        tracker.addImage (logo, 0);
        tracker.addImage (controlstitle, 1);
        tracker.addImage (creditstitle, 2);
        tracker.addImage (controls, 3);
        tracker.addImage (credits, 4);
        tracker.addImage (pressm, 5);

        //wait until all of the images are loaded
        try
        {
        	tracker.waitForAll ();
        }
        catch (InterruptedException e)
        {
        }
	}
	
	//Description: paintcomponent which draws the graphics
	//Parameters: Graphics g; Return: n/a
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		if (page.equals("main")) {		//draw logo on main page
			g.drawImage(logo, 50, 75, 333, 169, this);
		}else{							//draw controls or credits page
			if (page.equals("controls")) {
				g.drawImage(controls, 42, 45, 352, 467, this);
				g.drawImage(controlstitle, 85, 50, 263, 50, this);
			}else if (page.equals("credits")) {
				g.drawImage(credits, 43, 45, 350, 466, this);
				g.drawImage(creditstitle, 85, 50, 263, 50, this);
			}
			if (pressmCounter % 400 == 0) {	//flash the pressm indicator
				pressmCheck = true;
			}else if (pressmCounter % 200 == 0){
				pressmCheck = false;
			}
			if (pressmCheck) g.drawImage(pressm, 66, 522, 296, 10, this);
			pressmCounter++;
		}
		repaint();
	}
	
	//getter
	public static Game getMainGame() {
		return mainGame;
	}

}
