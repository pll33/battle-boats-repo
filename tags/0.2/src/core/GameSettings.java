package core;

import java.io.Serializable;
import java.util.List;

public class GameSettings implements Serializable {
	
	private static final long serialVersionUID = -8425133080642882372L;
	private List<Integer> boatSizes;
	private int width;
	private int height;
	private boolean vsComputer;
	private String player1Name;
	private String player2Name;
	
	public GameSettings(final int width, final int height, final List<Integer> boatSizes){
		this.width = width;
		this.height = height;
		this.boatSizes = boatSizes;
		this.vsComputer = true;
		this.player1Name = "Player 1";
		this.player2Name = "Player 2";
	}

	public List<Integer> getBoatSizes() {
		return boatSizes;
	}

	public void setBoatSizes(List<Integer> boatSizes) {
		this.boatSizes = boatSizes;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getPlayer1Name() {
		return player1Name;
	}

	public void setPlayer1Name(String player1Name) {
		this.player1Name = player1Name;
	}

	public String getPlayer2Name() {
		return player2Name;
	}

	public void setPlayer2Name(String player2Name) {
		this.player2Name = player2Name;
	}

	public boolean isVsComputer() {
		return vsComputer;
	}

	public void setVsComputer(boolean vsComputer) {
		this.vsComputer = vsComputer;
	}
	
}
