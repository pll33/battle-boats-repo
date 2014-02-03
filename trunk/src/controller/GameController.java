package controller;

import java.util.ArrayList;

import model.Game;

public class GameController {

	/**
	 * The game.
	 */
	private Game game;

	/**
	 * GameController constructor that creates a Game with default settings.
	 */
	public GameController() {
		ArrayList<Integer> sizes = new ArrayList<Integer>();
		sizes.add(2);
		sizes.add(3);
		sizes.add(3);
		sizes.add(4);
		sizes.add(5);
		game = new Game("Player 1", "Player2", 10, 10, sizes, false);
	}

	/**
	 * GameController constructor that creates a Game with supplied settings.
	 * @param player1 Player 1's name
	 * @param player2 Player 2' name
	 * @param width The width of the game board
	 * @param height The height of the game board
	 * @param boatSizes A List<Integer> indicating how many boats and their respective sizes.
	 * @param multiplayer Whether this game is two humans players (true) or against computer (false)
	 */
	public GameController(String player1, String player2, int width,
			int height, ArrayList<Integer> boatSizes, boolean multiplayer) {
		game = new Game(player1, player2, width, height, boatSizes, multiplayer);
	}

	/**
	 * The main game loop
	 */
	public void startGame() {
		boolean gameOn = true;
		while (gameOn) {

		}
	}
}
