package model.player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

import utils.RandomHelper;

import model.Boat;
import model.Game;
import model.Move;
import core.MoveState;
import core.Orientation;

/**
 * A class to represent the local Computer Player. If the game is not set to multiplayer, this is the AI that will be used.
 *
 */
public class ComputerPlayer extends Player{

	private ArrayList<Move> possibleMoves;
	private static Random rand;
	
	/**
	 * Creates a new ComputerPlayer.
	 * @param game The game object that this player will be playing in.
	 */
	public ComputerPlayer(Game game) {
		super(game, "Computer");
		int width = game.getWidth();
		int height = game.getHeight();
		
		possibleMoves = new ArrayList<Move>(width * height);
		for(int i=0;i<width;i++){
			for(int j=0;j<height;j++){
				Move move = new Move(i,j);
				possibleMoves.add(move);
			}
		}
	}
	
	@Override
	public boolean placeBoats(ArrayList<Boat> boats){
		Boat boat;
		for(Integer i : game.getBoatSizes()){
			boolean placed = false;
			while(!placed){
				Move spot = pickSpot(i);
				Orientation orientation = RandomHelper.getRandomOrientation(rand);
				boat = new Boat(spot.x,spot.y,orientation,i);
				if(this.gameBoard.addBoat(boat)){
					placed = true;
				}
			}
		}
		return true;
	}

	private Move pickSpot(Integer i) {
		Move move = new Move(rand.nextInt(game.getHeight()), rand.nextInt(game.getHeight()));		
		return move;
	}
	
	@Override
	public boolean makeMove(Move move) {
		move = generateMove();
		MoveState state = gameBoard.move(move);
		if(state == MoveState.INVALID){
			return makeMove(generateMove());
		}
		this.movedBoard.move(move);
		return true;
	}
	
	private Move generateMove(){
		Collections.shuffle(possibleMoves);
		Move move = possibleMoves.remove(0);
		return move;
	}
	
}
