package core;

import java.io.Serializable;
import java.util.List;

public class GameSettings implements Serializable {
	
	private static final long serialVersionUID = -8425133080642882372L;
	private List<Integer> boatSizes;
	private int width;
	private int height;
	
	public GameSettings(final int width, final int height, final List<Integer> boatSizes){
		this.width = width;
		this.height = height;
		this.boatSizes = boatSizes;
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
	
}
