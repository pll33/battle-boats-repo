package view;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;

public class BoardUI extends JPanel {

	private static final long serialVersionUID = 3676567175579720127L;
	
	protected static final int CELL_WIDTH = 25; //px
	protected static final int CELL_HEIGHT = 25; //px
	protected static final int BOARD_PADDING = 2;
	protected static final int BOARD_OFFSETX = CELL_WIDTH+15;//*2;
	protected static final int BOARD_OFFSETY = CELL_HEIGHT+15;//*2;

	protected static final Color BOAT_CELL = Color.GRAY; //128, 128, 128
	protected static final Color BG_CELL = new Color(0,191,255);
	protected static final Color HIGHLIGHTED_CELL = Color.LIGHT_GRAY; //192,192,192
	protected static final Color SELECTED_CELL_ORIGIN = new Color(255,100,100);
	protected static final Color SELECTED_CELL_ERROR = Color.YELLOW;
	protected static final Color ORIENT_CELL = Color.PINK;
	protected static final Color BOARD_OUTLINE = Color.DARK_GRAY;
	protected static final Color BOARD_HEADING = Color.DARK_GRAY;
	protected static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
	protected static final Cursor CROSSHAIR_CURSOR = new Cursor(Cursor.CROSSHAIR_CURSOR);
	
	protected int numRows, numCols;
	protected Point currentSelectedCell;
	protected Point prevSelectedCell; 
	protected Point originLocation;
	
	protected List<Rectangle> boardCellsUI; // UI representation of board cells
	
	public BoardUI(int rows, int cols) {
		numRows = rows;
		numCols = cols;
		
		boardCellsUI = new ArrayList<Rectangle>(numRows * numCols);
		initializeBoardCells();
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(CELL_WIDTH*(numCols+BOARD_PADDING+1), CELL_HEIGHT*(numRows+BOARD_PADDING+1)); // fix dimensions and offsets for row/col heads
    }
	
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();
        
    	// draw board cells background
//      g2d.setColor(BG_CELL);
//      for (Rectangle cell : boardCellsUI) {
//          g2d.fill(cell);
//      }
        
        
        g2d.dispose();
    }
    
    protected void paintBoardGrid(Graphics g) {
    	Graphics2D g2d = (Graphics2D) g.create();
        
    	// draw board cells outlines and background
        for (Rectangle cell : boardCellsUI) {
        	//g2d.setColor(BG_CELL); //TODO
            //g2d.fill(cell);
            g2d.setColor(BOARD_OUTLINE);
            g2d.draw(cell);
            
        }
        
        // draw board headings
        g2d.setColor(BOARD_HEADING);
    	int xHeadCol,
    		yHeadCol = BOARD_OFFSETY - (CELL_HEIGHT/3),
    		xHeadRow = BOARD_OFFSETX - 3*(CELL_WIDTH/5),
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
    	
    	//TODO row headings should be right or center-aligned
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
    
    private void initializeBoardCells() {
    	 // draw rectangles to make up board UI cells
        if (boardCellsUI.isEmpty()) {
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
    }
}
