package map;

import java.util.ArrayList;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import tests.Driver;

public class Map {
	/** Class that represents the map **/
	
	
	private int colSize;
	private int rowSize;
	private int[][] grid;
	public static ArrayList<Rectangle> blocks = new ArrayList<Rectangle>(256);
	
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

	public void render2DMap(GraphicsContext gc, boolean screenEqualsMap) {
		/** Renders a 2D version of the map and adds it to an ArrayList of blocks
		 * that is used for collision detection
		 * **/
		
		int YBLOCKSIZE;
		int XBLOCKSIZE;
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
		
		
		// iterate over map array
		for (int row = 0; row < this.grid.length; row++) {
			for (int col = 0; col < this.grid[row].length; col++) {
				if (this.grid[row][col] != 0) {
					// if map value is non zero, create square of correct size
					// and add to the Pane.
					Rectangle block = new Rectangle((col*XBLOCKSIZE),(row*YBLOCKSIZE), XBLOCKSIZE, YBLOCKSIZE);
					block.setFill(Color.YELLOW);
					blocks.add(block);
				}
			}
		} Driver.root.getChildren().addAll(blocks);
	}
}
