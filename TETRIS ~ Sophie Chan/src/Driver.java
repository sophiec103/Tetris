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
import java.io.File;
import javax.sound.sampled.*;
import javax.swing.*;

//the main control class that displays and manoeuvres through the menus
public class Driver extends JPanel {
	
	private static JPanel panel = new JPanel();
	private static JPanel mainMenu = new JPanel();
	private static JPanel game = new JPanel();
	private static JPanel scoreboard = new JPanel();
	private static JPanel controls = new JPanel();
	private static JPanel credits = new JPanel();
	
	private static CardLayout cl = new CardLayout();
	private static Clip music;
	private static boolean mute = false;
	
	//Description: constructor that initializes the jcomponents and adds them to each other
	//Parameters: n/a ; Return: n/a
	public Driver(){
		panel.setLayout(cl);				//set cardlayout and add appropriate pages to the corresponding panels
		mainMenu.add(new MainMenu("main"));
		controls.add(new MainMenu("controls"));
		credits.add(new MainMenu("credits"));
		game.add(MainMenu.getMainGame());
		scoreboard.add(new Scoreboard());
		MainMenu.getMainGame().setGameOver(true);
		panel.add(mainMenu, "1");
		panel.add(game, "2");
		panel.add(scoreboard, "3");
		panel.add(controls, "4");
		panel.add(credits, "5");
        cl.show(panel, "1");
        
        Image icon = Toolkit.getDefaultToolkit().createImage("icon.png");
        
        try {	
			AudioInputStream background = AudioSystem.getAudioInputStream(new File ("backgroundmusic.wav"));	//add background music
			music = AudioSystem.getClip();
			music.open(background);		
			music.loop(Clip.LOOP_CONTINUOUSLY);
		} 
		catch (Exception e) {
		}
        
		//makes a brand new JFrame
		JFrame frame = new JFrame ("TETRIS ~ Sophie Chan");
		frame.add(panel);
		//set up the icon image (tracker not needed for the icon image)
		frame.setIconImage (icon);
		//to allow keyboard input
		frame.addKeyListener(MainMenu.getMainGame());
		frame.setFocusable(true);
		//some weird method that must be run
		frame.pack();
		//places the frame in the middle of the screen
		frame.setLocationRelativeTo(null);
		//without this, the thread will keep running even when the window is closed!
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//do not allow resize
		frame.setResizable(false);
		//to make frame visible
		frame.setVisible(true);
	
	}
	
	//Description: switches the visible screen
	//Parameters: the number of the screen that is to be made visible; Return: n/a
	public static void switchScreen(int screen) {
    	if (screen == 2) cl.show(panel, "2"); //game page
    	else {
    		MainMenu.getMainGame().setGameOver(true);
    		if (screen == 1) cl.show(panel, "1");
    		else if (screen == 4) cl.show(panel, "4");
    		else if (screen == 5) cl.show(panel, "5");
    		else if (screen == 3) {	//update scoreboard page
    			scoreboard.removeAll();		//remove before calling new
    			scoreboard.add(new Scoreboard());
    			cl.show(panel, "3");
    		}
    	}
	}
	
	//Description: mute and unmute the music
	//Parameters: n/a ; Return: n/a
	public static void muteMusic() {
		if(!mute) {
			music.stop();
			mute = true;
		}else {
			music.loop(Clip.LOOP_CONTINUOUSLY);
			mute = false;
		}
	}
	
	//Description: main method that calls the constructor of the control class
	//Parameters: String[] args ; Return: n/a
	public static void main(String[] args) {
		new Driver();
	}
}
