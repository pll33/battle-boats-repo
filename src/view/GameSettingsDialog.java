package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

public class GameSettingsDialog extends JDialog {
	
	private static final int TXTBOX_LENGTH = 6;
	private Container content;

	protected String player1Name;
	protected String player2Name;
	protected int numPlayers;
	protected int numShips;
	protected int numRows;
	protected int numCols;
	
	
	public GameSettingsDialog(JFrame frame, int numPlayers) {
		super(frame, true);
		content = getContentPane();
		this.numPlayers = numPlayers;
		createComponents();
	}

	private void createComponents() {
		JLabel label;
		JTextField textBox;
		JButton button;
		JComboBox<String> comboBox;
		JPanel pane = new JPanel(new SpringLayout());
		
		// custom settings:
		// 	-# of ships [default: 5]
		// 	-board size [default: 10x10]
		String[] cbNumShips = { "5" }; // TODO expand?
		String[] cbBoardSize = { "10x10" };  // TODO expand?
		
		label = new JLabel("Players"); 
		label.setFont(label.getFont().deriveFont(14.0f));
		pane.add(label);
		pane.add(new JLabel(""));
		
		label = new JLabel("Number of players: ", JLabel.TRAILING);
		pane.add(label);
		textBox = new JTextField(TXTBOX_LENGTH);
		textBox.setText(String.valueOf(numPlayers));
		textBox.setEnabled(false);
		textBox.setDisabledTextColor(Color.DARK_GRAY);
		label.setLabelFor(textBox);
		pane.add(textBox);
		
		label = new JLabel("Player 1 name: ", JLabel.TRAILING);
		pane.add(label);
		textBox = new JTextField(TXTBOX_LENGTH);
		label.setLabelFor(textBox);
		pane.add(textBox);
		
		label = new JLabel("Player 2 name: ", JLabel.TRAILING);
		pane.add(label);
		textBox = new JTextField(TXTBOX_LENGTH);
		if (numPlayers == 1) {
			textBox.setText("Computer");
			textBox.setEnabled(false);
			textBox.setDisabledTextColor(Color.DARK_GRAY);
		}
		label.setLabelFor(textBox);
		pane.add(textBox);
		
		label = new JLabel("Custom");
		label.setFont(label.getFont().deriveFont(14.0f));
		pane.add(label);
		pane.add(new JLabel(""));
		
		label = new JLabel("Number of ships: ", JLabel.TRAILING);
		pane.add(label);
		comboBox = new JComboBox<String>(cbNumShips);
		label.setLabelFor(comboBox);
		pane.add(comboBox);
		
		label = new JLabel("Board size: ", JLabel.TRAILING);
		pane.add(label);
		comboBox = new JComboBox<String>(cbBoardSize);
		label.setLabelFor(comboBox);
		pane.add(comboBox);
		
		button = new JButton("Cancel");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}	
		});
		pane.add(button);
		
		button = new JButton("Start Game");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				// TODO use game settings to create GameUI
			}	
		});
		pane.add(button);
		
		SpringUtilities.makeCompactGrid(pane,
                8, 2, 	//rows, cols
                10, 10,	//initX, initY
                6, 6);	//xPad, yPad
		
		content.add(pane);
	}
}
