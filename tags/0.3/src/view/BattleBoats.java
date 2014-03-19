package view;

import java.awt.Dimension;
import javax.swing.JFrame;

public class BattleBoats {

	public static void main(String[] args) {
		BattleBoatsUI app = new BattleBoatsUI();
		app.setMinimumSize(new Dimension(250,100));
		app.pack();
		app.setResizable(false);
		app.setVisible(true);
		app.setLocationRelativeTo(null);
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

}
