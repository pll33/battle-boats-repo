
package model.player;

import model.Move;
import core.PlayerType;

/**
 * A class representing a local human Player.
 *
 */
public class HumanPlayer extends Player {

        public HumanPlayer(String playerName) {
                super(playerName, PlayerType.HUMAN);
        }

        @Override
        public Move getMove() {
                return this.nextMove;
        }

        @Override
        public void setNextMove(final Move m) {
                this.nextMove = m;
        }
}