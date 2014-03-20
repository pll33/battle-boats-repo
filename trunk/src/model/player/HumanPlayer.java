
package model.player;

import model.Board;
import model.Move;
import core.PlayerType;

/**
 * A class representing a local human Player.
 *
 */
public class HumanPlayer extends Player {

        public HumanPlayer(String playerName, final Board board) {
                super(playerName, PlayerType.HUMAN,board);
        }

        @Override
        public Move getMove() {
                Move m = this.nextMove;
        		//Move m = new Move(this.nextMove.x, this.nextMove.y);
                this.nextMove = null;
                return m;
        }

        @Override
        public void setNextMove(final Move m) {
                this.nextMove = m;
        }
}