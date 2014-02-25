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
	
	private Logger(){}

	private static final String logLocation = System.getenv("APPDATA") + "\\BattleBoats\\log.txt";
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

		String editedMessage = formatMessage(message, logger);

		sb.append(editedMessage);
		sb.append(System.getProperty("line.separator"));

		System.out.println(editedMessage);
		count++;

	}
	
	private static String formatMessage(final String message, final Object logger){
		String editedMessage;

		if (logger instanceof Server) {
			editedMessage = "Server: " + message;
		}else if(logger instanceof Player){
			editedMessage = "Player: " + message;
		}else if(logger instanceof ClientThread){
			editedMessage = "Client: " + message;
		}else if(logger instanceof Game){
			editedMessage = "Game: " + message;
		}else if(logger instanceof Logger){
			editedMessage = "Logger: " + message;
		} else {
			editedMessage = message;
		}
		
		editedMessage = getCurrentTime() + " - " + editedMessage;
		
		return editedMessage;
	}
	
	private static String getCurrentTime(){
		Calendar cal = Calendar.getInstance();
		SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss:SSS");
		
		return sdf.format(cal.getTime());
	}
	
	public static void writeToFile(){
		
		final File file = new File(logLocation);
		file.getParentFile().mkdirs();
		try(final BufferedWriter wrt = new BufferedWriter(new FileWriter(file, true))){
			wrt.write(sb.toString());
		} catch (IOException e) {
			String message = "Encountered a critical error:" + e.toString();
			System.out.println(formatMessage(message, new Logger()));
		}

	}
	
	public static String getLogLocation(){
		return Logger.logLocation;
	}

}
