package controller;

import java.util.List;
import java.util.Random;

import model.Board;
import model.Boat;
import core.Constants;
import core.GameSettings;
import core.PlayerType;

public class ComputerController extends Thread {

	private GameController gc;
	private GameSettings settings;

	public ComputerController(final GameSettings settings) {
		this.settings = settings;
	}

	@Override
	public void run() {
		this.gc = new GameController(false, settings, Constants.LOCAL_IP, PlayerType.COMPUTER);
		setBoats();
		
		gc.getGame().setReady();
		gc.start();
	}

	private void setBoats() {
		final Board board = gc.getGame().getGameBoard();
		final Random rand = new Random();

		do {
			
			for (final Integer size : settings.getBoatSizes()) {
				final List<Boat> possibleLocations = board
						.getValidPlacementsForBoat(size);

				if (possibleLocations.size() > 0) {
					final Boat boat = possibleLocations.get(rand
							.nextInt(possibleLocations.size()));
					board.addBoat(boat);
				} else {
					board.clearBoats();
					break;
				}

			}

		} while (board.getBoats().size() < settings.getBoatSizes().size());

		
	}

}
