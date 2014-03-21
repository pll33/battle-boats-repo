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

import core.Constants;
import core.GameSettings;

public class GameSettingsDialog extends JDialog {

	protected boolean changesMade;
	
	private static final long serialVersionUID = 8467864338848501456L;
	private Container content;
	private JComboBox<Integer> cbNumRows;
	private JComboBox<Integer> cbNumCols;
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
		GameSettings settings = new GameSettings(getNumberOfCols(), getNumberOfRows(), getBoatSizes());
		settings.setVsComputer(numPlayers == 1);
		settings.setPlayer1Name("Player 1");
		settings.setPlayer2Name(numPlayers == 1 ? "Computer" : "Player 2");
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
		Integer[] arrBoardSize = { 5, 10, 15, 20, 25 }; 
		
		label = new JLabel("Players"); 
		label.setFont(label.getFont().deriveFont(14.0f));
		pane.add(label);
		pane.add(new JLabel(""));
		
		label = new JLabel("Number of players: ", JLabel.TRAILING);
		pane.add(label);
		JLabel playerLabel = new JLabel(String.valueOf(numPlayers));
		label.setLabelFor(playerLabel);
		pane.add(playerLabel);
		
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
		
		label = new JLabel("Boat sizes: ", JLabel.TRAILING);
		pane.add(label);
		tfBoatSizes = new JTextField();
		tfBoatSizes.setText("2, 3, 3, 4, 5"); // TODO placeholder
		// expand to allow user input of boat sizes
		// validation needed so user does not input a size greater than the length of a row/col
//		tfBoatSizes.setEnabled(false);
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
				// validate boat size textbox
				if (boatSizesValid()) {
					changesMade = true;
					setVisible(false);
				} else {
					tfBoatSizes.setBackground(Constants.TEXTFIELD_ERROR);
				}
			}

			private boolean boatSizesValid() {
				// weird hacky validation
				String rawBroatSizes = tfBoatSizes.getText();
				String boatSizeInteger = rawBroatSizes.replaceAll(",","").replaceAll(" ", "");
				try {
					Integer b = Integer.parseInt(boatSizeInteger);
					if (b > 0) return true;
					else return false;
				} catch (NumberFormatException ex) {
					return false;
				}
			}	
		});
		pane.add(button);
		
		SpringUtilities.makeCompactGrid(pane,
                6, 2, 	//rows, cols
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
