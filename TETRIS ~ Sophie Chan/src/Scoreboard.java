/*
 * Tetris
 * ~ Sophie Chan 2021
 * Classic tetris game: blocks will descend and stack; rows will clear when filled.
 * Players should try to obtain a higher score by clearing as many rows as possible.
 * This can be done by moving and rotating the blocks and using other functionalities.
 * The game will end when a block exceeds the top of the board.
 */

import java.awt.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

//contains what is necessary for Scoreboard page graphics and functionalities
public class Scoreboard extends JPanel{

	private static Image [] b = new Image[7];
	private static Image scoreboard, pressm;
	private static JLabel [] scoreText = new JLabel[7];
	private static JLabel [] levelText = new JLabel[7];
	private static JLabel [] nameText = new JLabel[7];
	private static int pressmCounter = 0;
	private static boolean pressmCheck = false;

	//Description: constructor that initializes the jcomponents and adds them to each other
	//Parameters: n/a ; Return: n/a
	public Scoreboard() {
		setPreferredSize (new Dimension (434, 583));

		setBorder(new EmptyBorder(105, 38, 63, 50)); //int top, int left, int bottom, int right

		//placeholder high scores
//		Game.getScores().put(15.0, new Score (":(", 3, 15.0));
//		Game.getScores().put(100000.0, new Score (":D", 100, 100000.0));
//		Game.getScores().put(12000.0, new Score ("player2", 28, 12000.0));
//		Game.getScores().put(2000.1, new Score ("player3", 12, 2000.1));
//		Game.getScores().put(20000.2, new Score ("player1", 65, 20000.1));
//		Game.getScores().put(200.3, new Score (">_<", 8, 200.3));
//		Game.getScores().put(20000.6, new Score (":P", 65, 20000.6));

		ArrayList <Score> scorers = new ArrayList <Score>();
		scorers.addAll(Game.getScores().values());	
		Collections.sort(scorers);

		setLayout (new GridLayout(8, 3));
		Color grey = new Color(191, 191, 191);
		JLabel label1 = new JLabel("Score ", SwingConstants.RIGHT);	//place jcomonents with gridlayout
		JLabel label2 = new JLabel("Lvl  ", SwingConstants.RIGHT);
		JLabel label3 = new JLabel("Name ", SwingConstants.RIGHT);
		label1.setFont(new Font("Emulogic",1,12));
		label1.setForeground(grey);
		label2.setFont(new Font("Emulogic",1,12));
		label2.setForeground(grey);
		label3.setFont(new Font("Emulogic",1,12));
		label3.setForeground(grey);
		add(label1);
		add(label2);
		add(label3);
		int counter;
		if (scorers.size()<7) counter = scorers.size();
		else counter = 7;
		for (int i = 0; i<counter; i++) {	//display the top scorers
			scoreText[i] = new JLabel((int)scorers.get(i).getScore() + " ", SwingConstants.RIGHT);
			scoreText[i].setFont(new Font("Emulogic",1,12));
			scoreText[i].setForeground(grey);	
			levelText[i] = new JLabel(scorers.get(i).getLevel() + "  ", SwingConstants.RIGHT);
			levelText[i].setFont(new Font("Emulogic",1,12));
			levelText[i].setForeground(grey);	
			nameText[i] = new JLabel(scorers.get(i).getName() + " ", SwingConstants.RIGHT);
			nameText[i].setFont(new Font("Emulogic",1,12));
			nameText[i].setForeground(grey);
			add(scoreText[i]);
			add(levelText[i]);
			add(nameText[i]);
		}
		for (int i = counter; i<7; i++) {
			add(new JLabel(""));
			add(new JLabel(""));
			add(new JLabel(""));
		}

		for (int i = 0; i<7; i++) {	//initialize block images
			b[i] = Toolkit.getDefaultToolkit().createImage("b" + (i+1) + ".png");
		}
		scoreboard = Toolkit.getDefaultToolkit().createImage("scoreboardbutton.png");
		pressm = Toolkit.getDefaultToolkit().createImage("pressm.png");
	}

	//Description: paintcomponent which draws the graphics
	//Parameters: Graphics g; Return: n/a
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		g.drawImage(scoreboard, 85, 50, 263, 50, this);
		for (int i = 0; i<7; i++) {	//initialize block images
			g.drawImage(b[i], 50, 175+i*51, 20, 20, this);
		}
		
		if (pressmCounter % 500 == 0) {	//flash the pressm indicator
			pressmCheck = true;
		}else if (pressmCounter % 250 == 0){
			pressmCheck = false;
		}
		if (pressmCheck) g.drawImage(pressm, 63, 522, 296, 10, this);
		pressmCounter++;
		
		repaint();
	}
}
