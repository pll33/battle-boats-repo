package model;

public class Move {

	public int x;
	public int y;
	public Move(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public Move(final String move){
		Move m = parseMove(move);
		this.x = m.x;
		this.y = m.y;
	}
	
	public String serializeToString(){
		return "x:" + x + ",y:" + y;
	}
	
	private Move parseMove(final String move){
		String[] xAndY = move.split(",");
		String[] xTemp = xAndY[0].split(":");
		String[] yTemp = xAndY[1].split(":");
		int x = Integer.parseInt(xTemp[1]);
		int y = Integer.parseInt(yTemp[1]);
		return new Move(x,y);
	}
}
