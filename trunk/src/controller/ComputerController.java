package controller;

import java.io.IOException;
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
		try {
			this.gc = new GameController(false, settings, Constants.LOCAL_IP, PlayerType.COMPUTER);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
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
