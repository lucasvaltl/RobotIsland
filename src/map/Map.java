package map;

import java.util.ArrayList;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import robot.Entity;
import tests.Driver;

public class Map {
	/** Class that represents the map **/
	
	
	private int colSize;
	private int rowSize;
	private int[][] grid;
	private static int[][] blocksToGrid;
	public static ArrayList<Entity> blocks = new ArrayList<Entity>(256);
	public static ArrayList<Entity> chargingstation = new ArrayList<Entity>(4);
	public static ArrayList<Entity> finishLine = new ArrayList<Entity>(2);
	public static ArrayList<Entity> antiCheatLine = new ArrayList<Entity>(2);
	int YBLOCKSIZE;
	int XBLOCKSIZE;
	
	public Map (int[][] grid) {
		/** Class constructor **/
		this.grid = grid;
		this.rowSize = grid.length;
		this.colSize = grid[0].length;
	}
	
	public int getColSize() {
		/** Return the grid column size **/
		return this.colSize;
	}
	
	public int getRowSize() {
		/** Return the grid row size **/
		return this.rowSize;
	}
	
	public int[][] getGrid() {
		/** Returns the grid array **/
		return this.grid;
	}
	
	public void setGrid(int[][] grid) {
		/** Method to reset the grid array **/
		this.grid = grid;
		// reset the rowSize and colSize
		this.rowSize = grid.length;
		this.colSize = grid[0].length;
	}
	
	
	public int[] getGridRow(int index) {
		/** Returns a row from the grid given an index **/
		return this.grid[index];
	}
	
	public int[] getGridCol(int colIndex) {
		/** Returns a column from the grid at a given index **/
		int[] colArray = new int[this.rowSize];
		for (int i = 0; i < this.rowSize; i++) {
			// iterate through a col and assign to array.
			colArray[i] = grid[i][colIndex];
		}
		return colArray;
		
	}
	
	public static Entity getBlocks(int i){
		return blocks.get(i);
	}
	
	public static int getBlockPosition(int i, int j){
		return blocksToGrid[i][j];
	}
	
	
	
	
	/**
	 * Renders a 2D version of the map and adds it to an ArrayList of blocks
	 * that is used for collision detection
	 * 
	 * 
	 * @param gc: insert graphics context
	 * @param screenEqualsMap: indicates whether the screen (window size) is equal to the map size
	 */
	
	/** Description: Returns the height of each map block.
	 * 
	 * @return: The height of a map block in pixels.
	 */
	public int getBlockHeight() {
		return Driver.SCREENHEIGHT / this.getRowSize();
	}
		
	/** Description: Returns the width of each map block.
	 * 
	 * @return: The width of a map block in pixels.
	 */
	public int getBlockWidth() {
		return Driver.SCREENWIDTH / this.getColSize();
	}
	
	public void render2DMap(GraphicsContext gc, boolean screenEqualsMap) {
		if (screenEqualsMap == true) {
			// divide screen dimensions by map dimensions to derive block sizes
			YBLOCKSIZE = Driver.SCREENHEIGHT / this.rowSize;
			XBLOCKSIZE = Driver.SCREENWIDTH / this.colSize;
		} else {
			YBLOCKSIZE = 32; // powers of 2 are quicker because of shift operations
			XBLOCKSIZE = 32; 
		}
		
		
		gc.setFill(Color.BLACK);
		gc.setStroke(Color.GREY);
		gc.setLineWidth(5);
		
		
		blocksToGrid = new int[this.grid.length][this.grid[0].length];
		
		
		// iterate over map array
		for (int row = 0; row < this.grid.length; row++) {
			for (int col = 0; col < this.grid[row].length; col++) {
				if (this.grid[row][col] != 0) {
					// if map value is non zero, create square of correct size
					// and add to the Pane.
					Entity block = new Entity((col*XBLOCKSIZE),(row*YBLOCKSIZE), XBLOCKSIZE, YBLOCKSIZE);
					block.setFill(Color.TRANSPARENT);
					blocks.add(block);
					//save position of the block to the map of blocks for easier access when detecting collision
					blocksToGrid[row][col] = blocks.indexOf(block);
				}
			}
		} Driver.root.getChildren().addAll(blocks);
		
		//add blocks from the refueling station to the respective ArrayList
		int numberofblocks = 2+4;
		for (int i=4; i < numberofblocks;i++){
			Entity block = new Entity((i*XBLOCKSIZE),(3*YBLOCKSIZE), XBLOCKSIZE, YBLOCKSIZE);
			block.setFill(Color.TRANSPARENT);
			chargingstation.add(block);
		}
		Driver.root.getChildren().addAll(chargingstation);	
		
		
		for (int i=1; i < 3;i++){
			Entity block = new Entity((i*XBLOCKSIZE),(3*YBLOCKSIZE), XBLOCKSIZE, 5);
			block.setFill(Color.TRANSPARENT);
			finishLine.add(block);
		}
		Driver.root.getChildren().addAll(finishLine);	
		
		for (int i=13; i < 15;i++){
			Entity block = new Entity((i*XBLOCKSIZE),(7*YBLOCKSIZE), XBLOCKSIZE, 5);
			block.setFill(Color.TRANSPARENT);
			antiCheatLine.add(block);
		}
		Driver.root.getChildren().addAll(antiCheatLine);
	}
}

