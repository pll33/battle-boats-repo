package model.player;

import java.io.IOException;

import model.Game;
import model.Move;

public class NetworkedPlayer extends Player {

	public NetworkedPlayer(final Game game, final String name) {
		super(game, name, false);
	}

	public Move getMove() {
		String input = "";
		try {
			input = in.readLine();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new Move(input);
	}

}
