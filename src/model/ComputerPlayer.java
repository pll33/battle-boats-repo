package model;

import java.util.ArrayList;
import java.util.Collections;

import core.MoveState;

public class ComputerPlayer extends Player{

	ArrayList<Move> possibleMoves;
	
	public ComputerPlayer(Game game) {
		super(game);
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
