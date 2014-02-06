package view;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Boat;
import model.Move;

import utils.RandomHelper;
import core.Orientation;

public class PlacementBoardUI extends BoardUI {

	private static final long serialVersionUID = 3619946983002807972L;
	private int placeShipSize, placementMouseMode;
	private ArrayList<Integer> orientLocationIndices;
	private List<Integer> boatSizes;
	
	private Point currentSelectedCell;
	private Point prevSelectedCell; 
	private static Random rand = new Random();

	public PlacementBoardUI(int rows, int cols, List<Integer> boatSizes) {
		super(rows, cols);
		this.boatSizes = boatSizes;
		this.orientLocationIndices = new ArrayList<Integer>();
		this.placementMouseMode = 0;
		this.placeShipSize = 0;
		
		addMouseMotionListener(new MouseMoveAdapter());		
		addMouseListener(new MousePressAdapter());
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
        
        // draw and fill ship origin
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

        // draw and fill ship orientation locations
        if (!orientLocationIndices.isEmpty()) {
        	for (Integer i : orientLocationIndices) {
        		Rectangle orientCell = boardCellsUI.get(i);
        		g2d.setColor(ORIENT_CELL);
        		g2d.fill(orientCell);
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
    
	public void setPlacementShipSize(int size) {
		placeShipSize = size;
	}

	/**
	 * Very inefficient randomization function.
	 */
	public void randomize(){
		// clear and update if there are already placed boats
		this.clear();
		
		// randomly place ships
		for (int size : boatSizes) {
			boolean placedShip = false;
			do{
				Point p = new Point(rand.nextInt(this.numRows),rand.nextInt(this.numCols));
				Orientation orientation = RandomHelper.getRandomOrientation(rand);
				placedShip = this.isValidPlacement(p, orientation, size);
			} while(!placedShip);
		}
		repaint(); // force redraw
	}
	
	public void clear() {
		if (placeBoard.hasBoats()) {
			placeBoard.clearBoats();
			repaint();
		}
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
    
	private void getValidOrientLocations(Point currentCellPoint) {
		orientLocationIndices.clear();
		int endFromOrigin = placeShipSize-1; 
		// check locations placeShipSize away from origin
		// horizontal
		if (currentCellPoint != null) {
			if ((currentCellPoint.x + endFromOrigin < numCols) &&
					(currentCellPoint.x - endFromOrigin >= 0)) { // horizontal check
				// check locations if another ship isn't already placed
				
			}
			else if ((currentCellPoint.y + endFromOrigin < numRows) &&
						(currentCellPoint.y - endFromOrigin >= 0)) {
				// check locations if another ship isn't placed
			}
		}
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
			prevSelectedCell = currentSelectedCell;
			currentSelectedCell = getCurrentCell(e.getX(),  e.getY());
			if (placementMouseMode == 0) {
				// TODO get valid orientation locations for current cell
				getValidOrientLocations(currentSelectedCell);
				placementMouseMode = 1;
				//currentSelectedCells.clear();
				//currentSelectedCells.add(getCurrentCell(e.getX(), e.getY()));
			} else if (placementMouseMode == 1) {
//				if currentSelectedCell=validCell
//						placeShip in orientation
				if (false){//isValidPlacement(currentSelectedCell,orientation,size)) {
					// TODO place ship 
				} else {
					placementMouseMode = 0;
				}
			} 
			repaint();
			System.out.println(placementMouseMode);
		}
    }
}
