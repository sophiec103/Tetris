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
import java.io.*;
import java.util.*;
import javax.swing.*;

//contains what is necessary for Tetris game functionalities
public class Game extends JPanel implements KeyListener, Runnable{

	private Thread thread;
	private static LinkedList <int[]> board = new LinkedList <int[]>();	//contains the rows of the board
	private static HashMap <Double, Score> scores = new HashMap <Double, Score> (); //contains all the scores
	private static Image [] numbers = new Image [10];
	private static Image [] blocks = new Image [7];

	private Image offScreenImage;
	private Graphics offScreenBuffer;
	private static int score = 0;
	private static int level = 1;
	private static int linesCleared = 0;

	private final static int SQSIZE = 26;	//static variables for display
	private final static int YOFFSET = 50;
	private final static int BORDER = 12;
	private final static int FPS = 100;

	private static boolean generate = true;			//to alert for a the block to be generated to a new one	
	private static boolean gameOver = false; 		//current game
	private static boolean pause = false; 			//game paused
	private static boolean showGameOver = false; 	//flashing game over is shown
	private static boolean showPause = false; 		//flashing pause is shown
	private static boolean moving = false;   		//block is currently moving
	private static boolean land = false;	 		//block is checking for landing
	private static boolean rowclearing = false;		//row is being cleared
	private static boolean updating = false;		//position is being updated
	private static boolean switched = false;		//only allow switching to hold piece once per new block
	private static boolean newG = false;			//first run in a newGame

	private static Image scoreG, nextG, levelG, holdG, pausedG, gameOverG;
	private static ImageIcon icon;

	private static Block current;	//current block object
	private static Block next;	//next block object
	private static Block hold;	//next block object

	//Description: constructor that initializes the graphics and starts the thread
	//Parameters: n/a ; Return: n/a
	public Game() {
		//sets up JPanel
		setPreferredSize (new Dimension ((10 * SQSIZE + 2 * BORDER + 150), (20) * SQSIZE + YOFFSET + BORDER + 1));
		setVisible(true);

		//load label images
		scoreG = Toolkit.getDefaultToolkit().createImage("score.png");
		levelG = Toolkit.getDefaultToolkit().createImage("level.png");
		nextG = Toolkit.getDefaultToolkit().createImage("next.png");
		holdG = Toolkit.getDefaultToolkit().createImage("hold.png");
		pausedG = Toolkit.getDefaultToolkit().createImage("paused.png");
		gameOverG = Toolkit.getDefaultToolkit().createImage("gameover.png");
		icon = new ImageIcon ("icon.png");
		//load the block images
		for (int i = 0; i<7; i++) {
			blocks[i] = Toolkit.getDefaultToolkit().createImage("block"+(i+1)+".png");
		}
		//load the number images for the score
		for (int i = 0; i<10; i++) {
			numbers[i] = Toolkit.getDefaultToolkit().createImage(i+".png");
		}

		//add a tracker for all of the images necessary when program first runs
		MediaTracker tracker = new MediaTracker (this);
		tracker.addImage (scoreG, 0);
		tracker.addImage (levelG, 1);
		tracker.addImage (nextG, 2);
		tracker.addImage (holdG, 3);
		tracker.addImage (pausedG, 4);
		tracker.addImage (gameOverG, 5);
		tracker.addImage (icon.getImage(), 6);
		for (int i = 0; i<7; i++) {
			tracker.addImage (blocks[i], 7+i);
		}
		for (int i = 0; i<10; i++) {
			tracker.addImage (numbers[i], 14+i);
		}

		//wait until all of the images are loaded
		try
		{
			tracker.waitForAll ();
		}
		catch (InterruptedException e)
		{
		}

		newGame();

		//starting the thread
		thread = new Thread(this);	//game thread
		thread.start();
		DescentThread descent = new DescentThread(); //descent thread
		descent.start();

	} //constructor

	//Description: the continuously running method of the main tetris thread 
	//Parameters: n/a ; Return: n/a
	public void run() {
		while(true) {
			while(!gameOver) { 	//while same game
				if(!newG) { //change current and next block if not first time running
					current = next;	
					next = new Block();
					predictDrop();
				}
				generate = false;
				switched = false;
				newG = false;
				while(!generate) {	//update movements while new piece is not being generated
					updatePosition();
					this.repaint();
					try {
						Thread.sleep(1000/FPS);		//allows program to run at FPS frames per second
					}catch(Exception e) {
					}
					while (pause) {		//flash pause indicator
						for (int i = 0; i<20; i++) {
							try {
								Thread.sleep(50);
							}catch(Exception e) {
							}
							if(!pause)break;
						}
						showPause = !showPause;
						repaint();
					}
				}
				while (gameOver) {		//flash gameover indicator
					for (int i = 0; i<20; i++) {
						try {
							Thread.sleep(50);
						}catch(Exception e) {
						}
						if(!gameOver)break;
					}
					showGameOver = !showGameOver;
					repaint();
				}
			}	
			try {
				Thread.sleep(200);
			}catch(Exception e) {
			}
		}
	}

	//Description: paintcomponent which draws the graphics and updates the board according to the contents of the board array
	//Parameters: Graphics g; Return: n/a
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		if (offScreenBuffer == null)
		{
			offScreenImage = createImage (this.getWidth (), this.getHeight ());
			offScreenBuffer = offScreenImage.getGraphics ();
		}

		// clear the offScreenBuffer
		offScreenBuffer.clearRect (0, 0, this.getWidth (), this.getHeight ());

		for (int row = 1; row <= 20; row++) {
			for (int column = 2; column <= 11; column++){
				// Find the x and y positions for each row and column
				int xPos = 24 + (column - 3) * SQSIZE + BORDER;
				int yPos = YOFFSET + (row - 1) * SQSIZE;

				// Draw each piece, depending on the value in board
				if (board.get(row)[column] != 0) {
					offScreenBuffer.setColor(new Color (255, 255, 255));
					offScreenBuffer.fillRect (xPos, yPos, SQSIZE, SQSIZE);
					offScreenBuffer.fillRect (xPos+2, yPos+2, SQSIZE, SQSIZE);
				}
				if (board.get(row)[column] == 1) {
					offScreenBuffer.setColor(new Color (255, 173, 205));
					offScreenBuffer.fillRect (xPos+2, yPos+2, SQSIZE-2, SQSIZE-2);
				}else if (board.get(row)[column] == 2) {
					offScreenBuffer.setColor(new Color (255, 173, 176));
					offScreenBuffer.fillRect (xPos+2, yPos+2, SQSIZE-2, SQSIZE-2);
				}else if (board.get(row)[column] == 3) {
					offScreenBuffer.setColor(new Color (255, 215, 173));
					offScreenBuffer.fillRect (xPos+2, yPos+2, SQSIZE-2, SQSIZE-2);
				}else if (board.get(row)[column] == 4) {
					offScreenBuffer.setColor(new Color (255, 254, 173));
					offScreenBuffer.fillRect (xPos+2, yPos+2, SQSIZE-2, SQSIZE-2);
				}else if (board.get(row)[column] == 5) {
					offScreenBuffer.setColor(new Color (173, 255, 189));
					offScreenBuffer.fillRect (xPos+2, yPos+2, SQSIZE-2, SQSIZE-2);
				}else if (board.get(row)[column] == 6) {
					offScreenBuffer.setColor(new Color (173, 233, 255));
					offScreenBuffer.fillRect (xPos+2, yPos+2, SQSIZE-2, SQSIZE-2);
				}else if (board.get(row)[column] == 7) {
					offScreenBuffer.setColor(new Color (173, 203, 255));
					offScreenBuffer.fillRect (xPos+2, yPos+2, SQSIZE-2, SQSIZE-2);
				}else if (board.get(row)[column] == 8) {
					offScreenBuffer.setColor(new Color (223, 223, 223));
					offScreenBuffer.fillRect (xPos+2, yPos+2, SQSIZE-2, SQSIZE-2);
				}

				// Draw the squares
				if(row>2) {
					offScreenBuffer.setColor (Color.WHITE);
					offScreenBuffer.drawRect (xPos, yPos, SQSIZE, SQSIZE);
					offScreenBuffer.drawRect (xPos+1, yPos+1, SQSIZE, SQSIZE);
				}
			}
		}
		//draw the border around the board
		offScreenBuffer.setColor (new Color (200, 200, 200));
		offScreenBuffer.drawRect (BORDER-4, (YOFFSET-2 + SQSIZE*2), SQSIZE*10+4, SQSIZE*18+4);
		offScreenBuffer.drawRect (BORDER-3, (YOFFSET-1 + SQSIZE*2), SQSIZE*10+4, SQSIZE*18+4);

		//draw the labels
		offScreenBuffer.drawImage(scoreG, 280, 130, 80, 16, this);
		offScreenBuffer.drawImage(levelG, 280, 230, 80, 16, this);
		offScreenBuffer.drawImage(nextG, 280, 330, 80, 16, this);
		offScreenBuffer.drawImage(holdG, 280, 430, 80, 16, this);

		//to display the score using the images
		Image [] digitsScore = new Image[6];
		int remainder;
		int number = score;
		int x = 0;
		while (number>0) {	//determines images required to display score
			remainder = number%10;
			digitsScore[x] = numbers[remainder];
			number = number/10;
			x++;
		}
		//draws score on top left
		for (int i = (x-1); i>=0; i--) {
			offScreenBuffer.drawImage(digitsScore[i], 280+15*(x-1-i), 150, 15, 15, this);
		}if (score==0)offScreenBuffer.drawImage(numbers[0], 280, 150, 15, 15, this);
		//to display the level using the images
		number = level;
		x = 0;
		Image [] digitsLevel = new Image[4];
		while (number>0) {	//determines images required to display level
			remainder = number%10;
			digitsLevel[x] = numbers[remainder];
			number = number/10;
			x++;
		}
		//draws level on top left
		for (int i = (x-1); i>=0; i--) {
			offScreenBuffer.drawImage(digitsLevel[i], 280+12*(x-1-i), 250, 15, 15, this);
		}
		//draw next block
		offScreenBuffer.drawImage(blocks[next.getArrayNum()], 280, 350, blocks[next.getArrayNum()].getWidth(this)/2, blocks[next.getArrayNum()].getHeight(this)/2, this);
		//draw hold block
		if (hold!=null) offScreenBuffer.drawImage(blocks[hold.getArrayNum()], 280, 450, blocks[hold.getArrayNum()].getWidth(this)/2, blocks[hold.getArrayNum()].getHeight(this)/2, this);		

		//draw pause
		if(pause&&showPause) {		//flash pause if game is paused
			offScreenBuffer.drawImage(pausedG, 280, 530, 87, 16, this);
		}
		//draw gameOver
		if(gameOver&&showGameOver&&!land) {	//flash game over if game has ended (and pop up resolved)
			offScreenBuffer.drawImage(gameOverG, 280, 530, 123, 16, this);
		}
		//transfer the offScreenBuffer to the screen
		g.drawImage (offScreenImage, 0, 0, this);
	}//paintComponent

	//Description: resets the game and the board when a new game is called
	//Parameters: n/a; Return: n/a
	public void newGame() {
		score = 0;
		level = 1;
		linesCleared = 0;
		gameOver = false;
		pause = false;
		current = new Block();
		next = new Block();
		hold = null;
		newG = true;
		board.clear();	//clear board and add starting values
		int [] m = {-1, -1, 9, 9, 9, 9, 9, 9, 9, 9, 9, 9, -1, -1}; //9 = wall, -1 = side wall
		board.add(m);
		for(int i = 0; i<20;i++) {
			int [] n = {-1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1};
			board.add(n);
		}
		board.add(m);
		updatePosition();
		predictDrop();
		repaint();
	}

	//Description: updates the board array according to the position of the current piece
	//Parameters: n/a; Return: n/a
	public static void updatePosition() {
		if(!updating) {
			clearPrevious();
			updating = true;
			for(int r = 0; r < current.getArray().length; r++) { //run through the current block position to enter it into the array
				for(int c = 0; c < current.getArray()[0].length; c++) {
					if(board.get(current.getRow()+r)[current.getCol()+c]==0||board.get(current.getRow()+r)[current.getCol()+c]==8&&current.getArray()[r][c]!=0) {
						board.get(current.getRow()+r)[current.getCol()+c] = current.getArray()[r][c];
					}
				}
			}
			updating = false;
		}
	}

	//Description: moves the current block with the action with which the method is called
	//Parameters: string that dictates which move action should be performed, whether the drop is user initiated or not; Return: n/a
	public static void move(String action, boolean drop) {
		if(!generate&&!moving&&!rowclearing&&!land) {	//don't clear or move if piece is changing or already moving (otherwise causes glitches)
			if(action.equals("left")&&checkMove(-1, 0)&&current.getCol()!=0) {	//if pass in move in certain direction and move is allowed, perform move
				moving = true;
				current.updatePrevious();
				current.setCol(current.getCol()-1);
				updatePosition();
				moving = false;
			}
			else if(action.equals("right")&&checkMove(1, 0)&&current.getCol()!=10) {
				moving = true;
				current.updatePrevious();
				current.setCol(current.getCol()+1);
				updatePosition();
				moving = false;
			}
			else if(action.equals("down")&&checkMove(0, 1)&&!moving&&!generate&&!rowclearing&&!land) {
				moving = true;
				current.updatePrevious();
				current.setRow(current.getRow()+1);
				updatePosition();
				if (drop) score+=1;
				moving = false;
			}
			else if(action.equals("down")&&!checkMove(0, 1)&&!moving&&!generate&&!rowclearing) {
				land = true;
				clearCurrent();
				checkClearRow();
				generate = true;	
				if (current.getRow()==1||current.getRow()==2&&current.getArrayNum()!=0) {
					gameOver = true;	//if board is filled
					pause = false;
					String name;
					name = (String) JOptionPane.showInputDialog (null, "Enter your name to record your score \n(7 character max)","Tetris Scoreboard",JOptionPane.INFORMATION_MESSAGE,icon,null,"");	
					if (name!=null) {
						name = name.trim();
						if(!(name.length()<=7))name = name.substring(0,7);
						double currentScore = (double) score;
						while(scores.containsKey(currentScore)) currentScore+=0.001;	//if score already exists, allow to store anyway, and rank new score/player above previous
						Score result = new Score(name, level, currentScore);
						scores.put(currentScore, result);
					}	
				}land = false;
			}else updatePosition();  
		}
	}	

	//Description: checks whether it is possible to move the block in the direction passed in
	//Parameters: amount to move in x direction, amount to move in y direction; Return: whether the move is possible or not
	public static boolean checkMove(int dirx, int diry) {
		clearCurrent();
		for(int r = 0; r < current.getArray().length; r++) {
			for(int c = 0; c < current.getArray()[0].length; c++) {
				if(current.getArray()[r][c]!=0&&board.get(current.getRow()+r+diry)[current.getCol()+c+dirx]!=0 && current.getArray()[r][c]!=0&&board.get(current.getRow()+r+diry)[current.getCol()+c+dirx]!=8) {
					return false;
				}
			}
		}
		return true;
	}

	//Description: checks whether it is possible to move the block in the direction passed in
	//Parameters: string that dictates which rotation action should be performed; Return: n/a
	public static void rotate(String action) {
		if(!generate&&!moving&&!rowclearing&&!land) {		//don't clear or rotate if piece is changing or already moving (otherwise causes glitches)
			if(action.equals("rright")&&checkRotate(7)) {
				current.updatePrevious();
				if(current.getArrayNum()<=20) current.setArrayNum(current.getArrayNum()+7);
				else current.setArrayNum(current.getArrayNum()-21);	//circulate array
				current.updateArrays();
			}
			else if(action.equals("rleft")&&checkRotate(-7)) {
				current.updatePrevious();
				if(current.getArrayNum()>=7) current.setArrayNum(current.getArrayNum()-7);
				else current.setArrayNum(current.getArrayNum()+21);	//circulate array
				current.updateArrays();
			}
		}
	}

	//Description: checks whether it is possible to rotate the block in the direction passed in
	//Parameters: amount to rotate; Return: whether the rotation is possible or not
	public static boolean checkRotate(int dir) {
		clearCurrent();
		int[][]checkArray = new int[4][4];
		if(dir>0) checkArray = current.getRArray();		
		else checkArray = current.getLArray();
		boolean shift = false;
		boolean rshift = false;
		boolean lshift = false;
		boolean rshiftI = false;
		boolean lshiftI = false;
		for(int r = 0; r < checkArray.length; r++) {
			for(int c = 0; c < checkArray[0].length; c++) {
				if(((current.getArrayNum()>6&&current.getArrayNum()<14&&current.getArrayNum()!=10)||(current.getArrayNum()>20&&current.getArrayNum()!=24))&&(checkArray[r][c]!=0&&board.get(current.getRow()+r)[current.getCol()+c]==-1)){	
					shift = true;
					if(current.getArrayNum()==7&&current.getCol()==0) rshiftI = true;	//I piece slightly different, so consider cases
					else if(current.getArrayNum()==21&&current.getCol()==10) lshiftI = true;
					else if(current.getCol()==1) rshift = true;			
					else if(current.getCol()==10||current.getArrayNum()==7&&current.getCol()==9) lshift = true;
				}else if(checkArray[r][c]!=0&&board.get(current.getRow()+r)[current.getCol()+c]!=0&&board.get(current.getRow()+r)[current.getCol()+c]!=8&&!shift) {	//check if rotation without shift will cause collisions
					return false;
				}else if(shift) {	//check if shifting will cause collisions
					if(rshiftI&&checkArray[r][c]!=0&&board.get(current.getRow()+r)[current.getCol()+c+2]!=0&&board.get(current.getRow()+r)[current.getCol()+c+2]!=8) return false;
					else if(lshiftI&&checkArray[r][c]!=0&&board.get(current.getRow()+r)[current.getCol()+c-2]!=0&&board.get(current.getRow()+r)[current.getCol()+c-2]!=8) return false;
					else if(rshift&&checkArray[r][c]!=0&&board.get(current.getRow()+r)[current.getCol()+c+1]!=0&&board.get(current.getRow()+r)[current.getCol()+c+1]!=8) return false;
					else if(lshift&&checkArray[r][c]!=0&&board.get(current.getRow()+r)[current.getCol()+c-1]!=0&&board.get(current.getRow()+r)[current.getCol()+c-1]!=8) return false;
				}
			}
		}if(shift){ //shift pieces accordingly
			if(rshiftI) current.setCol(current.getCol()+2); 	
			else if(lshiftI) current.setCol(current.getCol()-2);
			else if(rshift) current.setCol(current.getCol()+1);			
			else if(lshift) current.setCol(current.getCol()-1); 
		}
		return true;
	}

	//Description: checks how far the block can drop and drops it by that distance
	//Parameters: n/a; Return: n/a
	public static void drop() {
		if(!generate&&!rowclearing&&!land) {
			land = true;
			int counter = 1;
			while (checkMove(0, counter) == true) {
				counter++;
			}
			counter--;
			if (counter>1) {			//drop as many spots as available
				score+=counter*2;
				moving = true;
				current.updatePrevious();
				current.setRow(current.getRow()+counter);
				updatePosition();
				moving = false;
				land = false;
				move("down", false);
			}else {					//if only one spot to move, move the one spot
				land = false;
				move("down", false);
			}
		}
	}

	//Description: checks how far the block can drop and stores a shadow of the possible drop
	//Parameters: n/a; Return: n/a
	public static void predictDrop() {
		if(!rowclearing&&!land) {
			clearPredict();
			int counter = 1;
			while (checkMove(0, counter) == true) {	//check how far block can drop
				counter++;
			}
			counter--;
			for(int r = 0; r < current.getArray().length; r++) {	//store shadow of possible drop
				for(int c = 0; c < current.getArray()[0].length; c++) {
					if(board.get(current.getRow()+r+counter)[current.getCol()+c]==0&&current.getArray()[r][c]!=0) {
						board.get(current.getRow()+r+counter)[current.getCol()+c] = 8;
					}
				}
			}
			current.setPredCol(current.getCol());	//update position of predicted piece
			current.setPredRow(current.getRow()+counter);
		}
	}

	//Description: clears the block from its current location in the board array
	//Parameters: n/a; Return: n/a
	public static void clearCurrent() {
		for(int r = 0; r < current.getArray().length; r++) {
			for(int c = 0; c < current.getArray()[0].length; c++) {
				if(board.get(current.getRow()+r)[current.getCol()+c] == current.getArray()[r][c]) {
					board.get(current.getRow()+r)[current.getCol()+c] = 0;
				}
			}
		}
	}

	//Description: clears the block from its previous location in the board array
	//Parameters: n/a; Return: n/a
	public static void clearPrevious() {
		for(int r = 0; r < current.getPArray().length; r++) {
			for(int c = 0; c < current.getPArray()[0].length; c++) {
				if(board.get(current.getPrevRow()+r)[current.getPrevCol()+c] == current.getPArray()[r][c]) {
					board.get(current.getPrevRow()+r)[current.getPrevCol()+c] = 0;
				}
			}
		}
	}

	//Description: clears the predicted shadow block from its current location in the board array
	//Parameters: n/a; Return: n/a
	public static void clearPredict() {
		for(int r = 0; r < current.getPArray().length; r++) {
			for(int c = 0; c < current.getPArray()[0].length; c++) {
				if(board.get(current.getPredRow()+r)[current.getPredCol()+c] == 8) {
					board.get(current.getPredRow()+r)[current.getPredCol()+c] = 0;
				}
			}
		}
	}

	//Description: checks whether a row is full or not and clears it if it is
	//Parameters: n/a; Return: n/a
	public static void checkClearRow() {
		rowclearing = true;
		int rows = 0;
		updatePosition();
		for(int r = 0; r < current.getArray().length; r++) {
			for(int c = 0; c < board.get(current.getRow()+r).length; c++) {	//check if any column in the row is empty
				if(board.get(current.getRow()+r)[c]==0) {
					break;
				}else if (c == (board.get(current.getRow()+r).length-1)) {	//if none empty, remove row and add new row at the top
					board.remove(current.getRow()+r);
					int [] n = {-1, -1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, -1, -1};
					board.add(1, n);
					rows++;
					linesCleared++;
					if(linesCleared%5==0)level++;
				}
			}
		}if (rows==1)score+=100;	//update scores
		else if (rows==2)score+=300;
		else if (rows==3)score+=500;
		else if (rows==4)score+=800;
		rowclearing = false;
	}

	//Description: switches the block on hold with the saved block or with the next block if none are saved
	//Parameters: n/a; Return: n/a
	public static void switchHold() {
		if (!switched) {
			clearCurrent();
			current.updatePrevious();
			clearPredict();
			while (current.getArrayNum()>=7) {	//revert rotated block to original orientation
				current.setArrayNum(current.getArrayNum()-7);
			}
			if (hold==null) {	//if no blocks saved, save current block and generate new block
				hold = current;
				current = next;	
				next = new Block();
			}else {				//if there is block saved, switch saved block and current block
				Block temp = hold;
				hold = current;
				current = temp;
				current.setRow(1);
				current.setCol(5);
			}
			switched = true;
			predictDrop();
		}
	}

	//getters
	public static Block getCurrentBlock() {
		return current;
	}
	public static HashMap <Double, Score> getScores() {
		return scores;
	}
	public static int getLevel() {
		return level;
	}
	public static boolean getGameOver() {
		return gameOver;
	}
	public static boolean getPause() {
		return pause;
	}
	public static boolean getGenerate() {
		return generate;
	}

	//setters
	public static void setGameOver(boolean newGameOver) {
		gameOver = newGameOver;
	}

	//Description: keylistener that allows for user input from keyboard
	//Parameters: KeyEvent e; Return: n/a
	public void keyPressed(KeyEvent e) {
		int key = e.getKeyCode();
		if(key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) {			
			move("left", false);
			if(!generate)predictDrop();
		}else if(key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) {
			move("right", false);
			if(!generate)predictDrop();
		}else if(key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) {
			move("down", true);
		}else if(key == KeyEvent.VK_W || key == KeyEvent.VK_UP) {
			rotate("rright");
			if(!generate)predictDrop();
		}else if(key == KeyEvent.VK_Z) {
			rotate("rleft");
			if(!generate)predictDrop();
		}else if(key == KeyEvent.VK_C) {
			switchHold();
		}else if(key == KeyEvent.VK_SPACE) {
			drop();
		}else if(key == KeyEvent.VK_P) {
			pause = !pause;
		}else if(key == KeyEvent.VK_Y) {
			Driver.muteMusic();
		}else if(key == KeyEvent.VK_N) {
			newGame();
			Driver.switchScreen(2);
			generate = true;
			moving = false;
		}else if(key == KeyEvent.VK_M) {
			Driver.switchScreen(1);
		}
	}
	public void keyReleased(KeyEvent e) {
	}
	public void keyTyped(KeyEvent e) {
	}
}
