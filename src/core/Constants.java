package core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Constants {
	
	public static final int PORT = 8080;
	public static final String LOCAL_IP = "127.0.0.1";
	
	public static final List<Integer> DEFAULT_BOAT_SIZES = new ArrayList<Integer>(Arrays.asList(new Integer[]{2,3,3,4,5}));
	public static final int DEFAULT_WIDTH = 10;
	public static final int DEFAULT_HEIGHT = 10;
	
	public static final String IPV4_REGEX = "\\A(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)(\\.(25[0-5]|2[0-4]\\d|[0-1]?\\d?\\d)){3}\\z";
	
}

