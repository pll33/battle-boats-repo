package core;

import java.io.Serializable;

public class PlayerName implements Serializable {
	
	private static final long serialVersionUID = -1857784429465077824L;
	private String name;
	
	public PlayerName(final String name){
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
