/*
 * Tetris
 * ~ Sophie Chan 2021
 * Classic tetris game: blocks will descend and stack; rows will clear when filled.
 * Players should try to obtain a higher score by clearing as many rows as possible.
 * This can be done by moving and rotating the blocks and using other functionalities.
 * The game will end when a block exceeds the top of the board.
 */

//Thread that allows for the blocks to descend automatically in periodic intervals
public class DescentThread extends Thread{

	private static int gameSpeed = 15;

	//Description: the continuously running method of the descent thread 
	//Parameters: n/a ; Return: n/a
	public void run ()
	{
		while(true) {
			while(!Game.getGameOver()) {
				try {
					Thread.sleep(500);		//pause to update
				}catch(Exception e) {
				}
				while(!Game.getGenerate()&&!Game.getPause()){
					Game.move("down", false);
					try {
						Thread.sleep(10000/gameSpeed);		//controls how fast the blocks fall
					}catch(Exception e) {
					}
				}
				gameSpeed = 15+3*Game.getLevel();
			}
			try {
				Thread.sleep(500);		//pause to update
			}catch(Exception e) {
			}
		}

	}

}
