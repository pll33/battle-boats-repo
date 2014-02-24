package utils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import model.Game;
import model.player.Player;
import model.server.ClientThread;
import model.server.Server;

public class Logger {

	public static final String logLocation = System.getenv("APPDATA") + "\\BattleBoats\\log.txt";
	private static StringBuilder sb = null;
	private static int count = 0;

	public static void log(final String message, final Object logger) {

		if (sb == null) {
			sb = new StringBuilder();
		}
		
		if(count == 10){
			writeToFile();
			count = 0;
		}

		String editedMessage;

		if (logger instanceof Server) {
			editedMessage = "Server: " + message;
		}else if(logger instanceof Player){
			editedMessage = "Player: " + message;
		}else if(logger instanceof ClientThread){
			editedMessage = "Client: " + message;
		}else if(logger instanceof Game){
			editedMessage = "Game: " + message;
		} else {
			editedMessage = message;
		}
		
		editedMessage = getCurrentTime() + " - " + editedMessage;

		sb.append(editedMessage);

		System.out.println(editedMessage);
		count++;

	}
	
	private static String getCurrentTime(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
		
		return sdf.format(cal.getTime());
	}
	
	public static void writeToFile(){
		
		try(BufferedWriter wrt = new BufferedWriter(new FileWriter(new File(logLocation), true))){
			wrt.write(sb.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
