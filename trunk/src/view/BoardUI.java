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

import core.Orientation;

import model.Board;
import model.Boat;

public class BoardUI extends JPanel {
	
	private static final int CELL_WIDTH = 25; //px
	private static final int CELL_HEIGHT = 25; //px
	private static final int BOARD_PADDING = 2;
	private static final int BOARD_OFFSETX = CELL_WIDTH*2;
	private static final int BOARD_OFFSETY = CELL_HEIGHT*2;
	private static final Color SELECTED_CELL_DEFAULT = Color.LIGHT_GRAY;
	private static final Color SELECTED_CELL_ORIENT = Color.GREEN;
	private static final Color SELECTED_CELL_MOVE = Color.YELLOW;
	private static final Color ORIENT_CELL = Color.PINK;
	private static Random rand = new Random();
	
	private int numRows, numCols, placeShipSize, placementMouseMode;
	private boolean isPlacement;
	private List<Rectangle> boardCellsUI; // UI representation of board cells
	//private ArrayList<Boat> boardCells; // in-game representation 
	private ArrayList<Integer> orientLocationIndices;
	private List<Integer> boatSizes;
	//private ArrayList<Point> currentSelectedCells; 
	private Point currentSelectedCell; // expand to list of points for multiple cell selection?
	private Point prevSelectedCell; 
	private Board placeBoard;
	
	/*public BoardUI() {
		this(10, 10, false);
	}*/
	
	// TODO make subclasses of BoardUI (PlaceBoardUI, PlayBoardUI) 
	public BoardUI(int rows, int cols, List<Integer>boatSizes, boolean placement) {
		numRows = rows;
		numCols = cols;
		isPlacement = placement;
		placementMouseMode = 0;
		currentSelectedCell = null;
		this.boatSizes = boatSizes;
		placeBoard = new Board(rows, cols);
		//currentSelectedCells = new ArrayList<Point>();
		//isEditable affects whether mouseAdapter is added or not
		
		orientLocationIndices = new ArrayList<Integer>();
		boardCellsUI = new ArrayList<Rectangle>(numRows * numCols);

		addMouseMotionListener(new MouseMoveAdapter());		
		addMouseListener(new MousePressAdapter());
	}
	
	public Dimension getPreferredSize() {
        return new Dimension(CELL_WIDTH*(numCols+BOARD_PADDING+1), CELL_HEIGHT*(numRows+BOARD_PADDING+1)); // fix dimensions and offsets for row/col heads
    }

	public void setPlacementShipSize(int size) {
		placeShipSize = size;
	}
	
	/**
	 * Very inefficient randomization function.
	 */
	public void randomize(){
		for(int size : boatSizes){
			boolean placedShip = false;
			do{
				Point p = new Point(rand.nextInt(this.numRows),rand.nextInt(this.numCols));
				Orientation orientation;
				if(rand.nextBoolean()){
					orientation = Orientation.HORIZONTAL;
				} else {
					orientation = Orientation.VERTICAL;
				}
				placedShip = this.isValidPlacement(p, orientation, size);
			} while(!placedShip);
		}
	}
	
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

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
        
        // draw and fill ship origin
//      if (currentSelectedCells.size() > 0) {
//        	for (Point currentSelectedCell : currentSelectedCells) {
        		if (currentSelectedCell != null) {
		            int currentIndex = getCellIndex(currentSelectedCell);
		            //System.out.println("index: " + index);
		            Rectangle currentCell = boardCellsUI.get(currentIndex);
		            g2d.setColor(SELECTED_CELL_DEFAULT);
		            g2d.fill(currentCell);
		            
		            if (placementMouseMode == 0) { // change to enum TODO
		            	if (prevSelectedCell != null) {
		            		int prevIndex = getCellIndex(prevSelectedCell);
		            		Rectangle prevCell = boardCellsUI.get(prevIndex);
		            		g2d.setColor(SELECTED_CELL_MOVE);
		            		g2d.fill(prevCell);
		            	}
					} else if (placementMouseMode == 1) {
						g2d.setColor(SELECTED_CELL_ORIENT);
						g2d.fill(currentCell);
					} else {
						placementMouseMode = 0;
					}
        		}
//        	}
//        }

        // draw and fill ship orientation locations
        if (!orientLocationIndices.isEmpty()) {
        	for (Integer i : orientLocationIndices) {
        		Rectangle orientCell = boardCellsUI.get(i);
        		g2d.setColor(ORIENT_CELL);
        		g2d.fill(orientCell);
        	}
        }
        // draw board cells outlines
        g2d.setColor(Color.GRAY);
        for (Rectangle cell : boardCellsUI) {
            g2d.draw(cell);
        }
        
        // draw board headings
        g2d.setColor(Color.DARK_GRAY);
     
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
    
    private int getCellIndex(Point cell) {
    	return cell.x + (cell.y * numCols);
    }
    
    private Point getCurrentCell(int eX, int eY) {
    	int xLoc = eX- BOARD_OFFSETX,
			yLoc = eY - BOARD_OFFSETY,
            col = xLoc / CELL_WIDTH,
            row = yLoc / CELL_HEIGHT;

    	//System.out.println("mouse: " + e.getX() + ", " + e.getY());
    	//System.out.println("c, r: " + col + ", " + row);
    	
    	if ((xLoc >= 0 && yLoc >= 0) &&
    			(col >= 0 && col < numCols) && 
    			(row >= 0 && row < numRows)) 
    		return new Point(col, row);
    	else
    		return null;
    }
    /**
     * THIS METHOD PLACES THE BOAT IF IT RETURNS TRUE< ELSE IT DOES NOT PLACE IT.
     * @param cell
     * @param orientation
     * @param size
     * @return
     */
    private boolean isValidPlacement(Point cell, Orientation orientation, int size) {
    	Boat boat = new Boat(cell.x, cell.y, orientation,size);
    	return placeBoard.addBoat(boat);
    }
    
    private class MouseMoveAdapter extends MouseAdapter {
    	@Override
    	public void mouseMoved(MouseEvent e) {
			//currentSelectedCells.clear();
			//currentSelectedCells.add(getCurrentCell(e.getX(), e.getY()));
			currentSelectedCell = getCurrentCell(e.getX(), e.getY());
    		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
            repaint();
        }
    }
    
    private class MousePressAdapter extends MouseAdapter {
    	@Override
		public void mousePressed(MouseEvent e) {
			System.out.println("press: " + e.getX() + ", " + e.getY());
			if (isPlacement) {
				prevSelectedCell = currentSelectedCell;
				currentSelectedCell = getCurrentCell(e.getX(),  e.getY());
				if (placementMouseMode == 0) {
					// TODO get valid orientation locations for current cell
					getValidOrientLocations(currentSelectedCell);
					placementMouseMode = 1;
					//currentSelectedCells.clear();
					//currentSelectedCells.add(getCurrentCell(e.getX(), e.getY()));
				} else if (placementMouseMode == 1) {
//					if currentSelectedCell=validCell
//							placeShip in orientation
					if (false){//isValidPlacement(currentSelectedCell,orientation,size)) {
						// TODO place ship 
					} else {
						placementMouseMode = 0;
					}
				} 
				repaint();
				System.out.println(placementMouseMode);
			} else {
				//TODO default behavior for all non-placement boardUIs
			}
		}
    }

	private void getValidOrientLocations(Point currentCellPoint) {
		orientLocationIndices.clear();
		int endFromOrigin = placeShipSize-1; 
		// check locations placeShipSize away from origin
		// horizontal
		if ((currentCellPoint.x + endFromOrigin < numCols) ||
				(currentCellPoint.x - endFromOrigin >= 0)) { // horizontal check
			// check locations if another ship isn't already placed
			
		}
		else if ((currentCellPoint.y + endFromOrigin < numRows) ||
					(currentCellPoint.y - endFromOrigin >= 0)) {
			// check locations if another ship isn't placed
		}
		
	}
    
}
