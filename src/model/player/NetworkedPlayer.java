package model.player;

import java.io.IOException;

import model.Game;
import model.Move;

public class NetworkedPlayer extends Player {

	public NetworkedPlayer(final Game game, final String name) {
		super(game, name, false);
	}

	public Move getMove() {
		
		Move m = null;
		try {
			m = (Move) in.readObject();
		} catch (ClassNotFoundException | IOException e) {
			e.printStackTrace();
		}
		return m;
	}

}
