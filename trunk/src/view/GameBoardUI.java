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

import core.SquareState;
import model.Board;
import model.Boat;
import model.Move;

public class GameBoardUI extends BoardUI {

	private static final long serialVersionUID = -1393648178219426556L;
	protected static final String TARGET_ACQUIRED = "currentSelectedCell";
	
	protected static final Color SELECTED_CELL_ORIGIN = Color.GREEN;
	protected static final Color HIT_CELL = Color.RED;
			
	private Board boatBoard; // board w/ boats (player's left board)
	private Board moveBoard; // board w/ hit/miss states (player's right board)
	
	private boolean isActive;
	
	//TODO possible split into ActiveGameBoard and InactiveGameBoard classes
	public GameBoardUI(int rows, int cols, Board board, boolean active) {
		super(rows, cols);
		
		this.boatBoard = board;
		this.moveBoard = null;
		this.isActive = active;
		this.addMouseMotionListener(new MouseMoveAdapter());		
		
		if (isActive) {
			this.addMouseListener(new MousePressAdapter());
		}
	}
	
	public GameBoardUI(int rows, int cols) {
		super(rows, cols);
		
		this.boatBoard = new Board(cols, rows);
		this.moveBoard = null;
		this.isActive = true;
		this.addMouseMotionListener(new MouseMoveAdapter());		
		
		if (isActive) {
			this.addMouseListener(new MousePressAdapter());
		}
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(CELL_WIDTH*(numCols+BOARD_PADDING+1), CELL_HEIGHT*(numRows+BOARD_PADDING)); // fix dimensions and offsets for row/col heads
    }
	
	public void updateBoard(Board b) {
		if (isActive) {
			moveBoard = b;
		} else {
			boatBoard = b;
		}
	}
	
	public void clearSelectedCell() {
		currentSelectedCell = null;
		originLocation = null;
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		
        // draw placed boats (if any and board is inactive)
		//	(ex: player can only see their boats, not opposing player's)
	    if (!isActive)
	    {
//	    	// draw boats based on square state
//	    	ArrayList<Boat> placedBoats = boatBoard.getBoats();
//	    	for (Boat boat : placedBoats) {
//	    		ArrayList<Move> boatSquares = boat.getSquares();
//	      		for (Move cell : boatSquares) {
//	      			Rectangle boatCell = boardCellsUI.get(getCellIndex(new Point(cell.x, cell.y)));
//	      			Color stateColor = getSquareStateColor(boatBoard, boatBoard.getSquareState(cell.x, cell.y));
//	      			g2d.setColor(stateColor);
//	      			g2d.fill(boatCell);
//	      		}
//	    	}
	    	// draw states (hit/miss/boat)
	    	if (boatBoard != null) {
		    	for (int col = 0; col < boatBoard.getHeight(); col++) {
	    			for (int row = 0; row < boatBoard.getWidth(); row++) {
	    				SquareState st = boatBoard.getSquareState(row, col);
	    				Rectangle boatCell = boardCellsUI.get(getCellIndex(new Point(col, row)));
	    	      		if (st == SquareState.HIT || st == SquareState.MISS || st == SquareState.BOAT) {
	    	      			g2d.setColor(getSquareStateColor(boatBoard, st));
		    	      		g2d.fill(boatCell);
	    	      		}
	    			}
	    		}
	    	}
	    } else {
	    	// draw hit/miss states
	    	if (moveBoard != null) {
	    		for (int col = 0; col < boatBoard.getHeight(); col++) {
	    			for (int row = 0; row < boatBoard.getWidth(); row++) {
	    				SquareState st = moveBoard.getSquareState(row, col);
	    				Rectangle boatCell = boardCellsUI.get(getCellIndex(new Point(col, row)));
	    	      		if (st == SquareState.HIT || st == SquareState.MISS) {
	    	      			g2d.setColor(getSquareStateColor(moveBoard, st));
		    	      		g2d.fill(boatCell);
	    	      		}
	    			}
	    		}
	    	}
	    	
	    	// draw current highlighted cell
	    	if (currentSelectedCell != null) {
	        	int currentIndex = getCellIndex(currentSelectedCell);
			    Rectangle currentCell = boardCellsUI.get(currentIndex);
			    g2d.setColor(HIGHLIGHTED_CELL);
			    g2d.fill(currentCell);
	        }
	    	
	    	// draw current pressed cell
	    	if (originLocation != null) {
        		Rectangle origin = boardCellsUI.get(getCellIndex(originLocation));
        		g2d.setColor(SELECTED_CELL_ORIGIN);
        		g2d.fill(origin);
        	}
	    }
	    
	    // fill selected cell with colors based on boardState

	    paintBoardGrid(g2d);
	    g2d.dispose();
	}
	
	private Color getSquareStateColor(Board b, SquareState state) {
		switch(state) {
			case BOAT:
				return BOAT_CELL;
			case MISS:
				return MISS_CELL;
			case HIT:
				return HIT_CELL;
			default:
				return BG_CELL;
		}
	}
	
    private class MouseMoveAdapter extends MouseAdapter {
    	@Override
    	public void mouseMoved(MouseEvent e) {
			currentSelectedCell = getCurrentCell(e.getX(), e.getY());
			if (currentSelectedCell != null) {
				setCursor(CROSSHAIR_CURSOR);
				if (!isActive) {
					// show squareState as mouse tooltip if !isActive
					String squareState = boatBoard.getSquareState(currentSelectedCell.x, 
							currentSelectedCell.y).toString();
					setToolTipText(squareState);
				}
			} else {
				setCursor(DEFAULT_CURSOR);
				setToolTipText(null);
			}
            repaint();
        }
    }
    
    private class MousePressAdapter extends MouseAdapter {
    	@Override
		public void mousePressed(MouseEvent e) {
			//System.out.println("press: " + e.getX() + ", " + e.getY());
			prevSelectedCell = currentSelectedCell;
			currentSelectedCell = getCurrentCell(e.getX(),  e.getY());
			
			if (isActive) {
				// set currentSelectedCell as target
				originLocation = currentSelectedCell;
				firePropertyChange(TARGET_ACQUIRED, null, currentSelectedCell);
			}
			repaint();
		}
    }

}
