package model.player;

import java.util.Random;

import utils.RandomHelper;
import model.Move;
import core.PlayerType;

/**
 * A class to represent the local Computer Player. If the game is not set to
 * multiplayer, this is the AI that will be used.
 * 
 */
public class ComputerPlayer extends Player {

	/**
	 * Creates a new ComputerPlayer.
	 * 
	 * @param game
	 *            The game object that this player will be playing in.
	 */
	public ComputerPlayer() {
		super("Computer", PlayerType.COMPUTER);
	}

	@Override
	public Move getMove() {
		return RandomHelper.getRandomValidMove(new Random(), movedBoard);
	}

}