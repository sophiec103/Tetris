/*
 * Tetris
 * ~ Sophie Chan 2021
 * Classic tetris game: blocks will descend and stack; rows will clear when filled.
 * Players should try to obtain a higher score by clearing as many rows as possible.
 * This can be done by moving and rotating the blocks and using other functionalities.
 * The game will end when a block exceeds the top of the board.
 */

//contains the necessary methods to initialize and access scores and their related information
public class Score implements Comparable <Score>{
	private String name;
	private int level;
	private double score;
	
	//Description: score constructor that initializes the instance variables of a specific score
	//Parameters: the name of the scorer, the level that they reached, and the score achieved ; Return: n/a
	public Score(String name, int level, double score) {
		this.name = name;
		this.level = level;
		this.score = score;
	}
	
	//getters
	public String getName() {
		return name;
	}
	public int getLevel() {
		return level;
	}
	public double getScore() {
		return score;
	}
	
	//Description: compare method that checks if the score of the first specified object is greater than, less than, or equal to the second (numerically)
	//Parameters: the two objects being checked ; Return: positive/negative integer (negative if the first one is greater, positive if it's smaller, zero if it's equal)
	public int compareTo (Score sco) {
		return (int) ((sco.score-this.score)*1000);
	}


}
