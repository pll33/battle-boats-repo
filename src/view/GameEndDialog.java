package view;

import java.awt.Container;

import javax.swing.JDialog;
import javax.swing.JFrame;

public class GameEndDialog extends JDialog {

	private Container content;
	private String winLabelStr;
	
	public GameEndDialog(JFrame frame, String opponentName, boolean winnerIsYou) {
		super(frame, true);
		
		content = getContentPane();
		
		if (winnerIsYou) {
			winLabelStr = "Congratulations,\nYou win!";
		} else {
			winLabelStr = "You lose!\nPlayer " + opponentName + "wins.";
		}
		createComponents();
	}

	private void createComponents() {
		// TODO Auto-generated method stub
		
	}
}
