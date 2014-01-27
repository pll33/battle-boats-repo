package controller;

import java.util.ArrayList;

import model.Game;

public class GameController {

	private Game game;

	public GameController() {
		ArrayList<Integer> sizes = new ArrayList<Integer>();
		sizes.add(2);
		sizes.add(3);
		sizes.add(3);
		sizes.add(4);
		sizes.add(5);
		game = new Game("Player 1", "Player2", 10, 10, sizes, false);
	}

	public GameController(String player1, String player2, int width,
			int height, ArrayList<Integer> boatSizes, boolean multiplayer) {
		game = new Game(player1, player2, width, height, boatSizes, multiplayer);
	}

	public void startGame() {
		boolean gameOn = true;
		while (gameOn) {

		}
	}
}
