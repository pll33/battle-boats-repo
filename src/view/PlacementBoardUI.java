package view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import model.Board;
import model.Boat;
import model.Move;

import utils.OrientationHelper;
import utils.RandomHelper;
import core.Orientation;
import core.SquareState;

public class PlacementBoardUI extends BoardUI {

	private static final long serialVersionUID = 3619946983002807972L;
	protected static final String BOAT_INDEX = "placementBoatIndex";
	
	private int placementBoatSize, placementBoatIndex, placementMouseMode;
	private List<Integer> boatSizes;
	private List<Boolean> boatPlacement;
	private boolean allowPickup;
	
	private Board placeBoard;
	private ArrayList<Point> orientLocations;
	
	private static Random rand = new Random();

	public PlacementBoardUI(int rows, int cols, List<Integer> boatSizes) {
		super(rows, cols);
		this.boatSizes = boatSizes;
		this.orientLocations = new ArrayList<Point>();
		this.originLocation = null;
		this.placementMouseMode = 0; // change to enum TODO
		this.placementBoatIndex = -1;
		this.placementBoatSize = 0;
		this.allowPickup = false;
		this.placeBoard = new Board(cols, rows);
		this.boatPlacement = new ArrayList<Boolean>(boatSizes.size());
		for (int i = 0; i < boatSizes.size(); i++) {
			boatPlacement.add(i, false);
		}
		this.addMouseMotionListener(new MouseMoveAdapter());		
		this.addMouseListener(new MousePressAdapter());
	}
	
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        // draw placed boats (if any)
	    if (placeBoard.hasBoats())
	    {
	    	ArrayList<Boat> placedBoats = placeBoard.getBoats();
	    	for (Boat boat : placedBoats) {
	    		ArrayList<Move> boatSquares = boat.getSquares();
	      		for (Move cell : boatSquares) {
	      			Rectangle boatCell = boardCellsUI.get(getCellIndex(new Point(cell.x, cell.y)));
	      			g2d.setColor(BOAT_CELL);
	      			g2d.fill(boatCell);
	      		}
	    	}
	    }	
	    
        // draw and fill boat origin
        if (currentSelectedCell != null) {
        	int currentIndex = getCellIndex(currentSelectedCell);
		    Rectangle currentCell = boardCellsUI.get(currentIndex);
		    g2d.setColor(HIGHLIGHTED_CELL);
		    g2d.fill(currentCell);
        }

        if (prevSelectedCell != null && placementMouseMode == 0) {
    		int prevIndex = getCellIndex(prevSelectedCell);
    		Rectangle prevCell = boardCellsUI.get(prevIndex);
    		g2d.setColor(SELECTED_CELL_ERROR);
    		g2d.fill(prevCell);
    	}
        
        // draw and fill boat orientation locations
        if (!orientLocations.isEmpty()) {
        	for (Point p : orientLocations) {
        		Rectangle orientCell = boardCellsUI.get(getCellIndex(p));
        		g2d.setColor(ORIENT_CELL);
        		g2d.fill(orientCell);
        	}
        	
        	if (originLocation != null) {
        		Rectangle origin = boardCellsUI.get(getCellIndex(originLocation));
        		g2d.setColor(SELECTED_CELL_ORIGIN);
        		g2d.fill(origin);
        	}
        }
        
        paintBoardGrid(g2d);
        g2d.dispose();
    }
    
	public void setPlacementBoatIndex(int idx) {
		if (idx >= 0 && idx < boatSizes.size()) {
			placementBoatIndex = idx;
			placementBoatSize = boatSizes.get(idx);
			
			// update locations if size changes
			if (placementMouseMode == 1 && prevSelectedCell != null) {
				getValidPlacementLocations(prevSelectedCell);
				repaint();
			}
		}
	}

	public Board getPlacementBoats() {
		return this.placeBoard;
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(CELL_WIDTH*(numCols+BOARD_PADDING), CELL_HEIGHT*(numRows+BOARD_PADDING)); // fix dimensions and offsets for row/col heads
    }
	
	/**
	 * Very inefficient randomization function.
	 */
	// TODO place boats from largest to smallest will probably increase efficiency 
	// (especially with boats greater than half the length of a row/col)
	protected void randomize(){
		// clear and update if there are all boats have already been placed
		if (placeBoard.getBoats().size() == boatSizes.size()) {
			this.clearPlacementBoard();
		} else { 
			this.clearBoard();
		}
		
		// Randomly places boats based on number of boats already placed. If no boats are
		//  placed. randomize all. If >0 boats are placed, randomize remaining boats.
		for (int i = 0; i < boatSizes.size(); i++) {
			if (boatPlacement.get(i) == false) {
				boolean placedBoat = false;
				do{
					Point p = new Point(rand.nextInt(this.numCols),rand.nextInt(this.numRows));
					Orientation orientation = RandomHelper.getRandomOrientation(rand);
					placedBoat = this.isValidPlacement(p, orientation, boatSizes.get(i));
				} while(!placedBoat);
				boatPlacement.set(i, true);
			}
		}
		
		allowPickup = true;
		
		// force redraw
		repaint(); 
	}
	
	protected void clearBoard() {
		orientLocations.clear();
		originLocation = null;
		resetPlacementBoatIndex(-1);
		placementMouseMode = 0;
		placementBoatSize = 0;
		allowPickup = false;
		prevSelectedCell = null;
		repaint();
	}
	
	protected void clearPlacementBoard() {	
		clearBoard();
		
		for (int i = 0; i < boatPlacement.size(); i++) {
			boatPlacement.set(i, false);
		}
		
		if (placeBoard.hasBoats()) {
			placeBoard.clearBoats();
			repaint();
		}
	}

	protected boolean isBoatIndexPlaced (int idx) {
		return boatPlacement.get(idx);
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
    
	private void getValidPlacementLocations(Point currentCellPoint) {
		orientLocations.clear();
		originLocation = currentCellPoint;
		ArrayList<Move> validLocations = placeBoard.getValidPlacementLocations(currentCellPoint.x, currentCellPoint.y, placementBoatSize);
		
		for (Move loc : validLocations) {
			orientLocations.add(new Point(loc.x, loc.y));
		}
	}
	
	private void resetPlacementBoatIndex(int newIndex) {
		int oldValue = this.placementBoatIndex,
			newValue = newIndex;
		this.placementBoatIndex = newValue;
		
		// notify placementUI that value has changed
		firePropertyChange(BOAT_INDEX, oldValue, newValue);
	}
	
	private void placeBoat() {
		Orientation orient = OrientationHelper.getOrientationFromOrigin(originLocation, currentSelectedCell);
//		System.out.print("Placing boat: " + originLocation.x + ", " + originLocation.y + 
//				" facing " + orient.toString() + " ("+boatSizes.get(placementBoatIndex)+")\n" );
		isValidPlacement(originLocation, orient, boatSizes.get(placementBoatIndex));
		boatPlacement.set(placementBoatIndex, true);
		orientLocations.clear();
		originLocation = null;
		prevSelectedCell = null;
		resetPlacementBoatIndex(-1);
		System.out.println(placeBoard.toString());
	}
	
	// find boat based on boatLocation and remove
	private void removeBoat(Point boatLocation) {
		Boat boatToRemove = null;
		ArrayList<Boat> boats = placeBoard.getBoats();
		for (Boat b : boats) {
			ArrayList<Move> boatSquares = b.getSquares();
			for (Move m : boatSquares) {
				Point sq = new Point(m.x, m.y);
				if (sq.equals(boatLocation)) 
				{
					boatToRemove = b;
					break;
				}
			}
		}
		
		if (boatToRemove != null) {
			// remove boat
			placeBoard.removeBoat(boatToRemove);
			
			// Kind of hacky solution: finds (almost) exact index of boat removed so the 
			// 	corresponding boatButton in placementUI can be re-enabled
			// For games with many occurrences of the same boat size (ex: 2,2,2,2,2), the first 
			//  occurrence that is disabled will be re-enabled
			int size = boatToRemove.getSize();
			for (int i = 0; i < boatSizes.size(); i++) {
				if (boatSizes.get(i) == size && boatPlacement.get(i) == true) {
					// reset placement boolean
					boatPlacement.set(i, false); 
					placementBoatSize = boatSizes.get(i);
					resetPlacementBoatIndex(i);
					return;
				}
			}
		}
	}
    
    private class MouseMoveAdapter extends MouseAdapter {
    	@Override
    	public void mouseMoved(MouseEvent e) {
    		if (placementBoatSize > 0 || allowPickup) {
				currentSelectedCell = getCurrentCell(e.getX(), e.getY());
				if (currentSelectedCell != null) {
					setCursor(CROSSHAIR_CURSOR);
				} else {
					setCursor(DEFAULT_CURSOR);
				}
    		} else {
    			setCursor(DEFAULT_CURSOR);
    		}
    		repaint();
        }
    }
    
    // TODO some cleanup needed
    private class MousePressAdapter extends MouseAdapter {
    	@Override
		public void mousePressed(MouseEvent e) {
    		System.out.println(placementBoatSize);
    		if (placementBoatSize > 0 || allowPickup) {
				//System.out.println("press: " + e.getX() + ", " + e.getY());
				//System.out.println("placeMode: " + placementMouseMode);
				prevSelectedCell = currentSelectedCell;
				currentSelectedCell = getCurrentCell(e.getX(),  e.getY());
				if (currentSelectedCell != null && placementBoatIndex > -1) {
					System.out.println("pt: " + currentSelectedCell.x + ", " + currentSelectedCell.y);
					if (placementMouseMode == 0) {
						SquareState currentCellState = placeBoard.getSquareState(currentSelectedCell.x, currentSelectedCell.y);
						if (currentCellState == SquareState.EMPTY) { // selected cell = cell state is empty
					 		// get valid placement locations
							getValidPlacementLocations(currentSelectedCell);
						} else if (currentCellState == SquareState.BOAT) { // selected cell = cell state is boat
							// remove boat, get valid placement locations
							removeBoat(currentSelectedCell);
							getValidPlacementLocations(currentSelectedCell);
						}
					} else if (placementMouseMode == 1) { // orientation selection mode
						// currentSelectedCell=validCell (not including origin)
						if (!(currentSelectedCell.equals(originLocation)) &&
								(orientLocations.contains(currentSelectedCell))) {
							// place boat, placementMouseMode = 0
							placeBoat();
						} else if (currentSelectedCell.equals(originLocation)) {
							placementMouseMode = 1;
							return;
						} else if (currentSelectedCell != null) { // selected cell = origin or any other cell
							SquareState currentCellState = placeBoard.getSquareState(currentSelectedCell.x, currentSelectedCell.y);
							if (currentCellState == SquareState.BOAT) {
								// remove boat
								removeBoat(currentSelectedCell);
							}
							getValidPlacementLocations(currentSelectedCell);
							repaint();
							placementMouseMode = 1;
							return;
						}
					} 
					placementMouseMode = (placementMouseMode == 0) ? 1 : 0;
					//System.out.println("\nmode after press: " + placementMouseMode);
				} else if (currentSelectedCell != null && placementBoatIndex == -1) {
					SquareState currentCellState = placeBoard.getSquareState(currentSelectedCell.x, currentSelectedCell.y);
					if (currentCellState == SquareState.BOAT) {
						removeBoat(currentSelectedCell);
					}
				}
			}
			repaint();
			
		}
    }
}
