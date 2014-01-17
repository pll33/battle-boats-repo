package model;

import java.util.ArrayList;

import core.SquareState;

public class GameWorld {

	private ArrayList<Boat> boats;
	private ArrayList<ArrayList<SquareState>> boardState;
	
	public GameWorld(int width, int height){
		boardState = new ArrayList<ArrayList<SquareState>>(width);
		for(int i=0; i<width; i++){
			ArrayList<SquareState> temp = new ArrayList<SquareState>(height);
			for(int j=0; j<height;j++){
				temp.set(j,SquareState.EMPTY);
			}
			boardState.add(temp);
		}
	}
	
	
}
