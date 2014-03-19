package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import core.GameSettings;

public class GameSettingsDialog extends JDialog {

	protected boolean changesMade;
	
	private static final long serialVersionUID = 8467864338848501456L;
	private static final int TXTBOX_LENGTH = 6;
	
	private Container content;
	private JTextField player1Name;
	private JTextField player2Name;
	private JComboBox<Integer> cbNumRows;
	private JComboBox<Integer> cbNumCols;
	private JComboBox<Integer> cbNumBoats;
	private JTextField tfBoatSizes;
	
	private int numPlayers;
	
	public GameSettingsDialog(JFrame frame, int numPlayers) {
		super(frame, true);
		content = getContentPane();
		this.numPlayers = numPlayers;
		this.changesMade = false;
		
		createComponents();
	}
	
	public void init() {
		pack();
		setTitle("BattleBoats Settings");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	public GameSettings getSettings() {
		GameSettings settings = new GameSettings(getNumberOfCols(), getNumberOfRows(),  getBoatSizes());
		settings.setVsComputer(numPlayers == 1);
		
		// default: "Player 1" if not set
		settings.setPlayer1Name(player1Name.getText()); 
		
		// default: "Player 2" for PvP if not set, 
		//		  	"Computer" for PvC
		//String player2name = (numPlayers == 1) ? "Computer" : "";
		//settings.setPlayer2Name(player2name);
		settings.setPlayer2Name(player2Name.getText());
		
		return settings;
	}

	private Integer getNumberOfRows() {
		return (Integer) cbNumRows.getSelectedItem();
	}
	
	private Integer getNumberOfCols() {
		return (Integer) cbNumCols.getSelectedItem();
	}
	
	private ArrayList<Integer> getBoatSizes() {
		ArrayList<Integer> tfBoatSizesArr = new ArrayList<Integer>(); 
		String[] boatSizeStr = tfBoatSizes.getText().split(",");
		for (int i = 0; i < boatSizeStr.length; i++) {
			tfBoatSizesArr.add(Integer.parseInt(boatSizeStr[i].trim()));
		}
		return tfBoatSizesArr;
	}
	
	private void createComponents() {
		JLabel label;
		JTextField textBox;
		JButton button;
		JPanel pane = new JPanel(new SpringLayout());
		
		// custom settings:
		// 	-# of boats [default: 5]
		// 	-board size [default: 10x10]
		Integer[] arrNumBoats = { 5 }; // TODO expand?
		Integer[] arrBoardSize = { 5, 10, 15, 20, 25 }; 
		
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
		player1Name.setText("Player 1");
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
		} else {
			// TODO if PvP, let player 2 set their name when they connect to server
			player2Name.setText("Player 2");
			player2Name.setToolTipText("Player 2");
			player2Name.setEnabled(false);
			player2Name.setDisabledTextColor(Color.DARK_GRAY);
//			player2Name.addFocusListener(new TextFieldFocusListener());
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
		cbNumRows = new JComboBox<Integer>(arrBoardSize);
		cbNumRows.setSelectedItem(10);
		cbNumCols = new JComboBox<Integer>(arrBoardSize);
		cbNumCols.setSelectedItem(10);
		label.setLabelFor(cbNumRows);
		boardSizePanel.add(cbNumRows);
		boardSizePanel.add(new JLabel(" x "));
		boardSizePanel.add(cbNumCols);
		pane.add(boardSizePanel);
		
		label = new JLabel("Number of boats: ", JLabel.TRAILING);
		pane.add(label);
		cbNumBoats = new JComboBox<Integer>(arrNumBoats);
		label.setLabelFor(cbNumBoats);
		pane.add(cbNumBoats);
		
		label = new JLabel("Boat sizes: ", JLabel.TRAILING);
		pane.add(label);
		tfBoatSizes = new JTextField();
		tfBoatSizes.setText("2, 3, 3, 4, 5"); // TODO placeholder
		// expand to allow user input of boat sizes
		// validation needed so user does not input a size greater than the length of a row/col
		tfBoatSizes.setEnabled(false);
		tfBoatSizes.setDisabledTextColor(Color.DARK_GRAY);
		label.setLabelFor(tfBoatSizes);
		pane.add(tfBoatSizes);
		
		button = new JButton("Cancel");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changesMade = false;
				dispose();
			}	
		});
		pane.add(button);
		
		button = new JButton("Proceed");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				changesMade = true;
				setVisible(false);
			}	
		});
		pane.add(button);
		
		SpringUtilities.makeCompactGrid(pane,
                9, 2, 	//rows, cols
                10, 10,	//initX, initY
                6, 6);	//xPad, yPad
		
		content.add(pane);
	}
	
	private class TextFieldFocusListener implements FocusListener {
		public void focusGained(FocusEvent e) {
			if (e.getComponent() != null) {
				JTextField txtField = (JTextField) e.getComponent();
				if (txtField.getText().equals(txtField.getToolTipText())) {
					txtField.setText("");
				}
			}
		}
			
		public void focusLost(FocusEvent e) {
			if (e.getComponent() != null) {
				JTextField txtField = (JTextField) e.getComponent();
				if (txtField.getText().equals("")) {
					txtField.setText(txtField.getToolTipText());
				}
			}
		}
	}
	
}
