package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

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

	protected int numPlayers;
	protected boolean changesMade;
	
	private static final int TXTBOX_LENGTH = 6;
	private Container content;

	private JTextField player1Name;
	private JTextField player2Name;
	private JComboBox<Integer> numRows;
	private JComboBox<Integer> numCols;
	private JComboBox<Integer> numBoats;
	
	public GameSettingsDialog(JFrame frame, int numPlayers) {
		super(frame, true);
		content = getContentPane();
		this.numPlayers = numPlayers;
		this.changesMade = false;
		createComponents();
	}
	
	public String getPlayerOneName() {
		return player1Name.getText(); // default: "Player 1" if not set
	}
	
	public String getPlayerTwoName() {
		return player2Name.getText(); // default: "Player 2" for PvP if not set, 
									  //		  "Computer" for PvC
	}

	public Integer getNumberOfRows() {
		return (Integer) numRows.getSelectedItem();
	}
	
	public Integer getNumberOfCols() {
		return (Integer) numCols.getSelectedItem();
	}
	
	public Integer getNumberOfBoats() {
		return (Integer) numBoats.getSelectedItem();
	}
	
	private void createComponents() {
		JLabel label;
		JTextField textBox;
		JButton button;
		JComboBox<Integer> comboBox;
		JPanel pane = new JPanel(new SpringLayout());
		
		// custom settings:
		// 	-# of boats [default: 5]
		// 	-board size [default: 10x10]
		Integer[] cbNumBoats = { 5 }; // TODO expand?
		Integer[] cbBoardSize = { 10 };  // TODO expand?
		
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
		player1Name = new JTextField(TXTBOX_LENGTH);
		player1Name.setToolTipText("Player 1");
		player1Name.addFocusListener(new TextFieldFocusListener());
		label.setLabelFor(player1Name);
		pane.add(player1Name);
		
		label = new JLabel("Player 2 name: ", JLabel.TRAILING);
		pane.add(label);
		player2Name = new JTextField(TXTBOX_LENGTH);
		if (numPlayers == 1) {
			
			player2Name.setText("Computer");
			player2Name.setEnabled(false);
			player2Name.setDisabledTextColor(Color.DARK_GRAY);
		}
		else {
			player2Name.setToolTipText("Player 2");
			player2Name.addFocusListener(new TextFieldFocusListener());
		}
		label.setLabelFor(player2Name);
		pane.add(player2Name);
		
		label = new JLabel("Custom");
		label.setFont(label.getFont().deriveFont(14.0f));
		pane.add(label);
		pane.add(new JLabel(""));
		
		label = new JLabel("Board size: ", JLabel.TRAILING);
		pane.add(label);
		JPanel boardSizePanel = new JPanel();
		boardSizePanel.setLayout(new BoxLayout(boardSizePanel, BoxLayout.X_AXIS));
		numRows = new JComboBox<Integer>(cbBoardSize);
		numCols = new JComboBox<Integer>(cbBoardSize);
		label.setLabelFor(numRows);
		boardSizePanel.add(numRows);
		boardSizePanel.add(new JLabel(" x "));
		boardSizePanel.add(numCols);
		pane.add(boardSizePanel);
		
		label = new JLabel("Number of boats: ", JLabel.TRAILING);
		pane.add(label);
		numBoats = new JComboBox<Integer>(cbNumBoats);
		label.setLabelFor(numBoats);
		pane.add(numBoats);
		
		button = new JButton("Cancel");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changesMade = false;
				dispose();
			}	
		});
		pane.add(button);
		
		button = new JButton("Start Game");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changesMade = true;
				setVisible(false);
			}	
		});
		pane.add(button);
		
		SpringUtilities.makeCompactGrid(pane,
                8, 2, 	//rows, cols
                10, 10,	//initX, initY
                6, 6);	//xPad, yPad
		
		content.add(pane);
	}
	
	private class TextFieldFocusListener implements FocusListener {
		public void focusGained(FocusEvent e) {
			if (e.getComponent() != null)
			{
				JTextField txtField = (JTextField) e.getComponent();
				if (txtField.getText().equals(txtField.getToolTipText()))
				{
					txtField.setText("");
				}
			}
		}
			
		public void focusLost(FocusEvent e) {
			if (e.getComponent() != null)
			{
				JTextField txtField = (JTextField) e.getComponent();
				if (txtField.getText().equals(""))
				{
					txtField.setText(txtField.getToolTipText());
				}
			}
		}
	}
	
}