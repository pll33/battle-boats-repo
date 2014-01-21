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

import javax.swing.JPanel;

public class BoardUI extends JPanel {
	
	private static final int CELL_WIDTH = 30; //px
	private static final int CELL_HEIGHT = 30; //px
	private int numRows;
	private int numCols;
	private List<Rectangle> boardCells;
	private Point selectedCell; // expand to list of points for multiple cell selection?
	
	public BoardUI() {
		this(10, 10, true);
	}
	
	public BoardUI(int rows, int cols, boolean isEditable) {
		numRows = rows + 1;
		numCols = cols + 1;
		//isEditable affects whether mouseAdapter is added or not
		
		boardCells = new ArrayList<Rectangle>(numRows * numCols);
		MouseAdapter mouseHandler = new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
                int cellWidth = getWidth() / numCols;
                int cellHeight = getHeight() / numRows;

                int col = e.getX() / cellWidth;
                int row = e.getY() / cellHeight;

                if (col > 0 && row > 0) 
                	selectedCell = new Point(col, row);
                repaint();

            }
		};
	}
	
	public Dimension getPreferredSize() {
        return new Dimension(400, 400); // fix dimensions and offsets for row/col heads
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g.create();

        int width = getWidth();
        int height = getHeight();

        int cellWidth = CELL_WIDTH; //width / numCols;
        int cellHeight = CELL_HEIGHT; //height / numRows;

        int xOffset = (width - (numCols * cellWidth)) / 2;
        int yOffset = (height - (numRows * cellHeight)) / 2;

        if (boardCells.isEmpty()) 
        {
        	// column heading: numbers
        	int xHeadOffset;
        	int yHead = (yOffset*2) - 15;
        	//System.out.println(yHead);
        	for (int col = 1; col < numCols; col++) {
        		if (col >= 10) {
        			xHeadOffset = cellWidth / 3;
        		} else {
        			xHeadOffset = cellWidth / 2;
        		}
        		g2d.drawString(String.valueOf(col), xOffset + (col * cellWidth) + xHeadOffset, yHead);
        	}
        	
        	char rowHead = 'A'; //row heading: letters
            for (int row = 1; row < numRows; row++) {
            	System.out.println(yOffset + (row * cellHeight) + (cellHeight / 2));
            	g2d.drawString(String.valueOf(rowHead), 55, yOffset + (row * cellHeight) + (cellHeight / 2) + 5);
            	rowHead++;
                for (int col = 1; col < numCols; col++) {
                    Rectangle cell = new Rectangle(
                    		xOffset + (col * cellWidth), 
          					yOffset + (row * cellHeight),
                            cellWidth,
                            cellHeight);
                    boardCells.add(cell);
                }
            }
        }

        if (selectedCell != null) {
            int index = selectedCell.x + (selectedCell.y * numCols);
            Rectangle cell = boardCells.get(index);
            g2d.setColor(Color.YELLOW);
            g2d.fill(cell);
        }

        g2d.setColor(Color.GRAY);
        for (Rectangle cell : boardCells) {
            g2d.draw(cell);
        }

        g2d.dispose();
    }
}
