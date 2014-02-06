package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JPanel;

import utils.RandomHelper;

import core.Orientation;

import model.Board;
import model.Boat;
import model.Move;

public class BoardUI extends JPanel {
	
	protected static final int CELL_WIDTH = 25; //px
	protected static final int CELL_HEIGHT = 25; //px
	protected static final int BOARD_PADDING = 2;
	protected static final int BOARD_OFFSETX = CELL_WIDTH*2;
	protected static final int BOARD_OFFSETY = CELL_HEIGHT*2;
	protected static final Color SELECTED_CELL_DEFAULT = Color.LIGHT_GRAY;
	protected static final Color SELECTED_CELL_ORIENT = Color.GREEN;
	protected static final Color SELECTED_CELL_MOVE = Color.YELLOW;
	protected static final Color ORIENT_CELL = Color.PINK;
	protected static final Color PLACED_CELL = Color.GRAY;
	protected static final Color BOARD_OUTLINE = Color.DARK_GRAY;
	protected static final Color BOARD_HEADING = Color.DARK_GRAY;
	
	
	protected int numRows, numCols;
	protected Board placeBoard;
	
	protected List<Rectangle> boardCellsUI; // UI representation of board cells
	
	/*public BoardUI() {
		this(10, 10, false);
	}*/
	
	public BoardUI(int rows, int cols) {
		numRows = rows;
		numCols = cols;
		placeBoard = new Board(rows, cols);
		//currentSelectedCells = new ArrayList<Point>();
		//isEditable affects whether mouseAdapter is added or not
		
		boardCellsUI = new ArrayList<Rectangle>(numRows * numCols);
	}
	
	public Dimension getPreferredSize() {
        return new Dimension(CELL_WIDTH*(numCols+BOARD_PADDING+1), CELL_HEIGHT*(numRows+BOARD_PADDING+1)); // fix dimensions and offsets for row/col heads
    }
	
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // draw rectangles to make up board UI cells
        if (boardCellsUI.isEmpty()) 
        {
            for (int row = 0; row < numRows; row++) {            	
                for (int col = 0; col < numCols; col++) {
                    Rectangle cell = new Rectangle(
                    		BOARD_OFFSETX + (col * CELL_WIDTH), 
          					BOARD_OFFSETY + (row * CELL_HEIGHT),
                            CELL_WIDTH,
                            CELL_HEIGHT);
                    boardCellsUI.add(cell);
                }
            }
        }
        
        // draw placed ships (if any)
        if (placeBoard.hasBoats())
        {
        	ArrayList<Boat> placedBoats = placeBoard.getBoats();
        	for (Boat boat : placedBoats) {
        		ArrayList<Move> boatSquares = boat.getSquares();
        		for (Move cell : boatSquares) {
        			Rectangle boatCell = boardCellsUI.get(getCellIndex(new Point(cell.x, cell.y))); //TODO place on (X,Y) location
        			g2d.setColor(PLACED_CELL);
        			g2d.fill(boatCell);
        		}
        	}
        }
        
        // draw board cells outlines
        g2d.setColor(BOARD_OUTLINE);
        for (Rectangle cell : boardCellsUI) {
            g2d.draw(cell);
        }
        
        // draw board headings
        g2d.setColor(BOARD_HEADING);
    	int xHeadCol,
    		yHeadCol = BOARD_OFFSETY - (CELL_HEIGHT/3),
    		xHeadRow = BOARD_OFFSETX - 3*(CELL_WIDTH/5), // TODO fix arbitrary values
    		yHeadRow;
    	
    	// column heading: numbers
    	for (int col = 1; col <= numCols; col++) {
    		xHeadCol = BOARD_OFFSETX + ((col-1) * CELL_WIDTH);
    		if (col >= 10) {
    			xHeadCol += (CELL_WIDTH / 3);
    		} else {
    			xHeadCol += (CELL_WIDTH / 2);
    		}
    		g2d.drawString(String.valueOf(col), xHeadCol, yHeadCol);
    	}
    	
    	// row headings: letters
    	char rowHead = 'A';
    	for (int row = 0; row < numRows; row++) {
    		yHeadRow = BOARD_OFFSETY + (row * CELL_HEIGHT) + (CELL_HEIGHT / 2) + 5;
    		g2d.drawString(String.valueOf(rowHead), xHeadRow, yHeadRow);
        	rowHead++;
    	}
    	
        g2d.dispose();
    }
    
    protected int getCellIndex(Point cell) {
    	return cell.x + (cell.y * numCols);
    }
    
    protected Point getCurrentCell(int eX, int eY) {
    	int xLoc = eX- BOARD_OFFSETX,
			yLoc = eY - BOARD_OFFSETY,
            col = xLoc / CELL_WIDTH,
            row = yLoc / CELL_HEIGHT;

//    	System.out.println("mouse: " + eX + ", " + eY);
//    	System.out.println("c, r: " + col + ", " + row);
    	
    	if ((xLoc >= 0 && yLoc >= 0) &&
    			(col >= 0 && col < numCols) && 
    			(row >= 0 && row < numRows)) 
    		return new Point(col, row);
    	else
    		return null;
    }
    
}
