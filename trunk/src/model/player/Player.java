package model.player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import model.Board;
import model.Boat;
import model.Game;
import model.Move;
import core.MoveState;

public abstract class Player {

	protected Board gameBoard;
	protected Board movedBoard;
	protected Game game;
	protected Socket socket;
	protected ObjectOutputStream out;
	protected ObjectInputStream in;

	private String playerName;

	public Player(final Game game, final String name) {
		this(game, name, true);
	}

	public Player(final Game game, final String name,
			final boolean connectToServer) {
		movedBoard = new Board(game.getWidth(), game.getHeight());
		gameBoard = new Board(game.getWidth(), game.getHeight());
		playerName = name;

		if (connectToServer == true) {
			System.out.println("Player " + name + " attempting to connect to server");
			// will have to change this from being hardcoded
			String ip = "127.0.0.1";
			int port = 8080;

			try {
				this.socket = new Socket(ip, port);
				//this.out = new ObjectOutputStream(socket.getOutputStream());
				//this.in = new ObjectInputStream(socket.getInputStream());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public boolean placeBoats(final ArrayList<Boat> boats) {
		return gameBoard.addBoats(boats);
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((game == null) ? 0 : game.hashCode());
		result = prime * result
				+ ((gameBoard == null) ? 0 : gameBoard.hashCode());
		result = prime * result
				+ ((movedBoard == null) ? 0 : movedBoard.hashCode());
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
		Player other = (Player) obj;
		if (game == null) {
			if (other.game != null)
				return false;
		} else if (!game.equals(other.game))
			return false;
		if (gameBoard == null) {
			if (other.gameBoard != null)
				return false;
		} else if (!gameBoard.equals(other.gameBoard))
			return false;
		if (movedBoard == null) {
			if (other.movedBoard != null)
				return false;
		} else if (!movedBoard.equals(other.movedBoard))
			return false;
		return true;
	}

	public boolean makeMove(Move move) {
		MoveState state = game.makeMove(this, move);
		if (state == MoveState.INVALID) {
			return false;
		}

		return false;
	}

	public MoveState update(Move move) {
		return gameBoard.move(move);
	}

	public Board getGameBoard() {
		return gameBoard;
	}

	public ArrayList<MoveState> updatePosition(ArrayList<Move> moves) {
		ArrayList<MoveState> list = new ArrayList<MoveState>();
		for (Move move : moves) {
			MoveState state = gameBoard.move(move);
			list.add(state);
		}
		return list;
	}

	public void submitMove(Move move) {
		try {
			out.writeObject(move);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
