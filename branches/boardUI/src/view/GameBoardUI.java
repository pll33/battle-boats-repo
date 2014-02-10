package view;

import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import model.Board;
import model.Boat;
import model.Move;

public class GameBoardUI extends BoardUI {

	private static final long serialVersionUID = -1393648178219426556L;
	protected static final String TARGET_ACQUIRED = "currentSelectedCell";
	
	private Board boatBoard;
	
	private Point currentSelectedCell;
	private Point prevSelectedCell; 
	private boolean isActive;
	
	public GameBoardUI(int rows, int cols, Board boats, boolean active) {
		super(rows, cols);
		
		this.boatBoard = boats;
		this.isActive = active;
		this.addMouseMotionListener(new MouseMoveAdapter());		
		
		if (isActive) {
			this.addMouseListener(new MousePressAdapter());
		}
	}
	
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D) g.create();
		
        // draw placed boats (if any and board is inactive)
		//	(ex: player can only see their boats, not opposing player's)
	    if (!isActive)
	    {
	    	ArrayList<Boat> placedBoats = boatBoard.getBoats();
	    	for (Boat boat : placedBoats) {
	    		ArrayList<Move> boatSquares = boat.getSquares();
	      		for (Move cell : boatSquares) {
	      			Rectangle boatCell = boardCellsUI.get(getCellIndex(new Point(cell.x, cell.y)));
	      			g2d.setColor(BOAT_CELL);
	      			g2d.fill(boatCell);
	      		}
	    	}
	    }	
	    
	    // fill selected cell with colors based on boardState

	    paintBoardGrid(g2d);
	    g2d.dispose();
	}
	
    private class MouseMoveAdapter extends MouseAdapter {
    	@Override
    	public void mouseMoved(MouseEvent e) {
			currentSelectedCell = getCurrentCell(e.getX(), e.getY());
    		setCursor(new Cursor(Cursor.CROSSHAIR_CURSOR));
    		
    		// show squareState as mouse tooltip if !isActive
    		// TODO
            repaint();
        }
    }
    
    private class MousePressAdapter extends MouseAdapter {
    	@Override
		public void mousePressed(MouseEvent e) {
			//System.out.println("press: " + e.getX() + ", " + e.getY());
			prevSelectedCell = currentSelectedCell;
			currentSelectedCell = getCurrentCell(e.getX(),  e.getY());
			// set currentSelectedCell as target
			//TODO
			// if prevSelectedCell == currentSelectedCell, do nothing
			
			
			repaint();
		}
    }

}
