package model;

import java.util.List;
import java.util.ArrayList;

import core.MoveState;
import core.Orientation;
import core.SquareState;

/**
 * A class representing the Board(s) of each Player.
 * 
 */
public class Board {

	/**
	 * A list of boats placed on this Board.
	 */
	private ArrayList<Boat> boats;

	/**
	 * The Board tiles represented as SquareStates
	 */
	private ArrayList<ArrayList<SquareState>> boardState;

	/**
	 * The width of the board.
	 */
	int width;

	/**
	 * The height of the board.
	 */
	int height;

	/**
	 * Create an empty Board with the specified width and height.
	 * 
	 * @param width
	 *            The width of the board.
	 * @param height
	 *            The height of the board.
	 */
	public Board(int width, int height) {
		this.width = width;
		this.height = height;
		boats = new ArrayList<Boat>();
		reset();
	}

	/**
	 * Create a copy of a Board.
	 * 
	 * @param other
	 *            The Board to be copied.
	 */
	public Board(Board other) {
		this.boats = other.boats;
		this.boardState = other.boardState;
		this.width = other.width;
		this.height = other.height;
	}

	/**
	 * Does a move on this Board and updates the Board accordingly.
	 * 
	 * @param move
	 *            The Move to execute
	 * @return Whether the move was a Hit or a Miss.
	 */
	public SquareState move(Move move) {
		SquareState state = boardState.get(move.y).get(move.x);

		if (state == SquareState.BOAT) {
			boardState.get(move.y).set(move.x, SquareState.HIT);
			return SquareState.HIT;
		} else {
			return SquareState.MISS;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((boardState == null) ? 0 : boardState.hashCode());
		result = prime * result + ((boats == null) ? 0 : boats.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Board other = (Board) obj;
		if (boardState == null) {
			if (other.boardState != null)
				return false;
		} else if (!boardState.equals(other.boardState))
			return false;
		if (boats == null) {
			if (other.boats != null)
				return false;
		} else if (!boats.equals(other.boats))
			return false;
		return true;
	}

	public boolean addBoats(ArrayList<Boat> boats) {
		for (Boat boat : boats) {
			// Sets every space occupied by boat to boat
			if (!addBoat(boat)) {
				reset();
				return false;
			}
		}
		return true;
	}

	/**
	 * Sets the whole board to empty.
	 */
	private void reset() {
		boardState = new ArrayList<ArrayList<SquareState>>(width);
		// Set whole boardState to empty
		for (int i = 0; i < width; i++) {
			ArrayList<SquareState> temp = new ArrayList<SquareState>(height);
			for (int j = 0; j < height; j++) {
				temp.add(SquareState.EMPTY);
			}
			boardState.add(temp);
		}
	}

	public List<Boat> getValidPlacementsForBoat(final int size) {
		ArrayList<Boat> validMoves = new ArrayList<Boat>();

		for (int i = 0; i < width; i++) {
			for (int n = 0; n < height; n++) {

				for (final Orientation orient : Orientation.values()) {

					Boat temp = new Boat(i, n, orient, size);
					if (addBoat(temp)) {
						validMoves.add(temp);
						removeBoat(temp);
					}

				}

			}
		}

		return validMoves;
	}

	/**
	 * Determines the valid board locations for a Boat being placed at the given
	 * (x,y) coordinates of the given size based on orientation.
	 * 
	 * @param x
	 *            The x coordinate
	 * @param y
	 *            The y coordinate
	 * @param size
	 *            The size of the Boat
	 * @return A List<Point> of valid orientations for a Boat being placed at
	 *         (x,y) of the given size.
	 */
	public ArrayList<Move> getValidPlacementLocations(int x, int y, int size) {
		ArrayList<Move> validLocations = new ArrayList<Move>();
		ArrayList<Move> validSquares = new ArrayList<Move>();
		Move origin = new Move(x, y);

		for (Orientation orient : Orientation.values()) {
			Boat tempBoat = new Boat(x, y, orient, size);
			if (addBoat(tempBoat)) {
				validSquares.addAll(tempBoat.getSquares());
				removeBoat(tempBoat);
			}
		}

		// add all non-origin squares to validLocations
		for (Move sq : validSquares) {
			if (!(sq.x == origin.x && sq.y == origin.y)) {
				validLocations.add(sq);
			}
		}

		// validLocations.add(origin); // disabled for now to show origin
		// separately in UI
		return validLocations;
	}

	public boolean removeBoat(Boat boat) {
		for (Move move : boat.getSquares()) {
			try {
				if (boardState.get(move.y).get(move.x) != SquareState.BOAT) {
					return false;
				}
			} catch (IndexOutOfBoundsException e) {
				return false;
			}
		}
		for (Move move : boat.getSquares()) {
			boardState.get(move.y).set(move.x, SquareState.EMPTY);
		}
		this.boats.remove(boat);
		return true;
	}

	/**
	 * If possible, places the given Boat on the Board
	 * 
	 * @param boat
	 *            The Boat to places
	 * @return true if the Boat was successfully placed, false otherwise.
	 */
	public boolean addBoat(Boat boat) {
		for (Move move : boat.getSquares()) {
			try {
				if (boardState.get(move.y).get(move.x) != SquareState.EMPTY) {
					return false;
				}
			} catch (IndexOutOfBoundsException e) {
				return false;
			}
		}
		for (Move move : boat.getSquares()) {
			boardState.get(move.y).set(move.x, SquareState.BOAT);
		}
		this.boats.add(boat);
		return true;
	}

	/**
	 * Checks if board has boats added to it
	 * 
	 * @return true if the list of boats has one or more elements, false
	 *         otherwise
	 */
	public boolean hasBoats() {
		return boats.size() > 0;
	}

	/**
	 * Gets the list of boats
	 * 
	 * @return list of boats
	 */
	public ArrayList<Boat> getBoats() {
		return boats;
	}

	/**
	 * Clears the board of all placed ships and resets board states to empty
	 */
	public void clearBoats() {
		boats.clear();
		reset();
	}

	public SquareState getSquareState(int row, int col) {
		return boardState.get(col).get(row);
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public void setState(Move move, SquareState state) {
		boardState.get(move.x).set(move.y,state);
	}

}
