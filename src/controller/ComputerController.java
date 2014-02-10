package controller;

import core.GameSettings;
import core.PlayerType;

public class ComputerController extends Thread {
	
	private GameController gc;
	private GameSettings settings;
	
	public ComputerController(final GameSettings settings){
		this.settings = settings;
	}
	
	@Override
	public void run(){
		this.gc = new GameController(false, settings, PlayerType.COMPUTER);
		while(true){
			
		}
	}

}
