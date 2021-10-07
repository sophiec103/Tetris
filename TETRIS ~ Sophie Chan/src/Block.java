/*
 * Tetris
 * ~ Sophie Chan 2021
 * Classic tetris game: blocks will descend and stack; rows will clear when filled.
 * Players should try to obtain a higher score by clearing as many rows as possible.
 * This can be done by moving and rotating the blocks and using other functionalities.
 * The game will end when a block exceeds the top of the board.
 */

import java.util.ArrayList;

//contains the necessary methods to initialize and access blocks and their related information
public class Block {
	private int [][] array = new int[4][4];	//current array
	private int [][] rarray = new int[4][4]; //right rotation array
	private int [][] larray = new int[4][4]; //left rotation array
	private int [][] parray = new int[4][4]; //previous array
	private int arrayNum;
	private int colPos;
	private int rowPos;
	private int prevColPos;
	private int prevRowPos;
	private int predColPos;
	private int predRowPos;
	private static boolean initialize;
	private static final ArrayList <int[][]> BLOCKS = new ArrayList <int[][]>();
	
	//original
	private static final int[][] ONE = 		{{0, 0, 0, 0},	//I block
										 	 {1, 1, 1, 1}};
	private static final int[][] TWO = 		{{2, 0, 0},		//J block
									 		 {2, 2, 2}};
	private static final int[][] THREE =	{{0, 0, 3},		//L block
									 		 {3, 3, 3}};
	private static final int[][] FOUR =		{{0, 4, 4},		//O block
									 		 {0, 4, 4}};
	private static final int[][] FIVE =		{{0, 5, 5},		//S block
									 		 {5, 5, 0}};
	private static final int[][] SIX =		{{0, 6, 0},		//T block
									 		 {6, 6, 6}};
	private static final int[][] SEVEN =	{{7, 7, 0},		//Z block
									 		 {0, 7, 7}};
	//1st rotation right 
	private static final int[][] ONER1 = 	{{0, 0, 1},		//I block
									 		 {0, 0, 1},
									 		 {0, 0, 1},
									 		 {0, 0, 1}};
	private static final int[][] TWOR1 = 	{{0, 2, 2},		//J block
									 		 {0, 2, 0},
									 		 {0, 2, 0}};
	private static final int[][] THREER1 =	{{0, 3, 0},		//L block
			 						 		 {0, 3, 0},
			 						 		 {0, 3, 3}};
	private static final int[][] FIVER1 =	{{0, 5, 0},		//S block
			 						 		 {0, 5, 5},
			 						 		 {0, 0, 5}};
	private static final int[][] SIXR1 =	{{0, 6, 0},		//T block
									 		 {0, 6, 6},
									 		 {0, 6, 0}};
	private static final int[][] SEVENR1 =	{{0, 0, 7},		//Z block
											 {0, 7, 7},
											 {0, 7, 0}};
	//2nd rotation right 
	private static final int[][] ONER2 = 	{{0, 0, 0, 0},	//I block
									 		 {0, 0, 0, 0},
									 		 {1, 1, 1, 1}};
	private static final int[][] TWOR2 = 	{{0, 0, 0},		//J block
											 {2, 2, 2},
											 {0, 0, 2}};
	private static final int[][] THREER2 =	{{0, 0, 0},		//L block
			 						 		 {3, 3, 3},
			 						 		 {3, 0, 0}};
	private static final int[][] FIVER2 =	{{0, 0, 0},		//S block
			 						 		 {0, 5, 5},
			 						 		 {5, 5, 0}};
	private static final int[][] SIXR2 =	{{0, 0, 0},		//T block
											 {6, 6, 6},
											 {0, 6, 0}};
	private static final int[][] SEVENR2 =	{{0, 0, 0},		//Z block
									 		 {7, 7, 0},
									 		 {0, 7, 7}};
	//3rd rotation right 
	private static final int[][] ONER3 = 	{{0, 1},		//I block
									 		 {0, 1},
									 		 {0, 1},
									 		 {0, 1}};
	private static final int[][] TWOR3 = 	{{0, 2},		//J block
									 		 {0, 2},
									 		 {2, 2}};
	private static final int[][] THREER3 =	{{3, 3},		//L block
			 						 		 {0, 3},
			 						 		 {0, 3}};
	private static final int[][] FIVER3 =	{{5, 0},		//S block
			 						 		 {5, 5},
			 						 		 {0, 5}};
	private static final int[][] SIXR3 =	{{0, 6},		//T block
									 		 {6, 6},
									 		 {0, 6}};
	private static final int[][] SEVENR3 =	{{0, 7},		//Z block
									 		 {7, 7},
									 		 {7, 0}};
	
	//Description: block constructor that initializes the instance variables of a specific block
	//Parameters: n/a ; Return: n/a
	public Block() {
		if (!initialize)initialize();
		do arrayNum = (int) (Math.random() * (7)); //randomize block chosen
		while(Game.getCurrentBlock()!=null&&arrayNum==Game.getCurrentBlock().getArrayNum());	//do not allow for same block to be generated twice in a row
		array = BLOCKS.get(arrayNum);
		updateArrays();
		rowPos = 1;	//starting positions
		colPos = 5;
	}
	
	//Description: initializes the blocks array list 
	//Parameters: n/a ; Return: n/a
	public void initialize(){
		BLOCKS.add(ONE);
		BLOCKS.add(TWO);
		BLOCKS.add(THREE);
		BLOCKS.add(FOUR);
		BLOCKS.add(FIVE);
		BLOCKS.add(SIX);
		BLOCKS.add(SEVEN);
		BLOCKS.add(ONER1);
		BLOCKS.add(TWOR1);
		BLOCKS.add(THREER1);
		BLOCKS.add(FOUR);
		BLOCKS.add(FIVER1);
		BLOCKS.add(SIXR1);
		BLOCKS.add(SEVENR1);
		BLOCKS.add(ONER2);
		BLOCKS.add(TWOR2);
		BLOCKS.add(THREER2);
		BLOCKS.add(FOUR);
		BLOCKS.add(FIVER2);
		BLOCKS.add(SIXR2);
		BLOCKS.add(SEVENR2);
		BLOCKS.add(ONER3);
		BLOCKS.add(TWOR3);
		BLOCKS.add(THREER3);
		BLOCKS.add(FOUR);
		BLOCKS.add(FIVER3);
		BLOCKS.add(SIXR3);
		BLOCKS.add(SEVENR3);
		initialize = true;	//prevent re-initializing
	}
	
	//Description: update the array values for rotation purposes
	//Parameters: n/a ; Return: n/a
	public void updateArrays() {
		if(arrayNum<=20) rarray = BLOCKS.get(arrayNum+7);
		else rarray = BLOCKS.get(arrayNum-21);	//circulate array
		if(arrayNum>=7) larray = BLOCKS.get(arrayNum-7);
		else larray = BLOCKS.get(arrayNum+21);	//circulate array
	}
	
	//Description: store previous values
	//Parameters: n/a ; Return: n/a
	public void updatePrevious() {
		parray = array;
		prevColPos = colPos;
		prevRowPos = rowPos;
	}
	
	//getters
	public int getRow () {
		return rowPos;
	}
	public int getCol () {
		return colPos;
	}
	public int getPrevRow () {
		return prevRowPos;
	}
	public int getPrevCol () {
		return prevColPos;
	}
	public int getPredRow () {
		return predRowPos;
	}
	public int getPredCol () {
		return predColPos;
	}
	public int[][] getArray() {
		return array;
	}
	public int getArrayNum() {
		return arrayNum;
	}
	public int[][] getRArray() {
		return rarray;
	}
	public int[][] getLArray() {
		return larray;
	}
	public int[][] getPArray() {
		return parray;
	}
	//setters
	public void setRow (int rowPos) {
		this.rowPos = rowPos;
	}
	public void setCol (int colPos) {
		this.colPos = colPos;
	}
	public void setPrevRow (int prevRowPos) {
		this.prevRowPos = prevRowPos;
	}
	public void setPrevCol (int prevColPos) {
		this.prevColPos = prevColPos;
	}
	public void setPredRow (int predRowPos) {
		this.predRowPos = predRowPos;
	}
	public void setPredCol (int predColPos) {
		this.predColPos = predColPos;
	}
	//Description: updates the array number and the block being displayed (quasi-setter)
	//Parameters: the array number ; Return: n/a
	public void setArrayNum(int arrayNum) {
		this.arrayNum = arrayNum;
		array = BLOCKS.get(arrayNum);
	}
}
