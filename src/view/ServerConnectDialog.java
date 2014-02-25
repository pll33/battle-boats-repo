package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
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

import core.GameSettings;

public class ServerConnectDialog extends JDialog {

	// player 2 is connected to server (status: connected)
	
	// server IP: Jtextbox (disabled)
	// display game information/settings (disabled)
	// cancel or proceed jbuttons
	
	private static final long serialVersionUID = -1049485771759327657L;
	private static final int TXTBOX_LENGTH = 6;
	private Container content;
	private GameSettings settings;
	
	public ServerConnectDialog(JFrame frame, GameSettings gs) {
		super(frame, true);
		
		content = getContentPane();
		settings = gs;
		createComponents();
	}
	
	public void init() {
		pack();
		setTitle("BattleBoats - Connected to Server");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	private void createComponents() {
		JLabel label;
		JTextField textBox;
		JButton button;
		
		JPanel pane = new JPanel(new SpringLayout());
		
		label = new JLabel("Player Details"); 
		label.setFont(label.getFont().deriveFont(14.0f));
		pane.add(label);
		pane.add(new JLabel(""));
		
		label = new JLabel("Player 1 Name: ", JLabel.TRAILING);
		pane.add(label);
		addDisabledTextField(pane, settings.getPlayer1Name(), label);
		
		label = new JLabel("Player 2 Name: ", JLabel.TRAILING);
		pane.add(label);
		JTextField player2Name = new JTextField(TXTBOX_LENGTH);
		//player2Name.setText(""); //TODO
		//player2Name.addFocusListener(new TextFieldFocusListener());
		label.setLabelFor(player2Name);
		pane.add(player2Name);
		
		label = new JLabel("Game Details"); 
		label.setFont(label.getFont().deriveFont(14.0f));
		pane.add(label);
		pane.add(new JLabel(""));
		
		label = new JLabel("Server IP Address: ", JLabel.TRAILING);
		pane.add(label);
		//addDisabledTextBox(pane, serverIP, label); //TODO get serverIP from setting or previous menu
		textBox = new JTextField(TXTBOX_LENGTH);
		textBox.setDisabledTextColor(Color.DARK_GRAY);
		textBox.setEnabled(false);
		//player1Name.addFocusListener(new TextFieldFocusListener());
		label.setLabelFor(textBox);
		pane.add(textBox);
		
		label = new JLabel("Board size: ", JLabel.TRAILING);
		pane.add(label);
		JPanel boardSizePanel = new JPanel();
		boardSizePanel.setLayout(new BoxLayout(boardSizePanel, BoxLayout.X_AXIS));
		textBox = new JTextField();
		addDisabledTextField(boardSizePanel, String.valueOf(settings.getHeight()), label);	
		boardSizePanel.add(new JLabel(" x "));
		addDisabledTextField(boardSizePanel, String.valueOf(settings.getWidth()), null);
		pane.add(boardSizePanel);
		
		label = new JLabel("Number of boats: ", JLabel.TRAILING);
		pane.add(label);
		addDisabledTextField(pane, String.valueOf(settings.getBoatSizes().size()), label);
		
		label = new JLabel("Boat sizes: ", JLabel.TRAILING);
		pane.add(label);
		addDisabledTextField(pane, settings.getBoatSizes().toString(), label);
		
		button = new JButton("Cancel");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//changesMade = false;
				dispose();
			}	
		});
		pane.add(button);
		
		button = new JButton("Proceed");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//changesMade = true;
				//setVisible(false);
			}	
		});
		pane.add(button);
		
		SpringUtilities.makeCompactGrid(pane,
                9, 2, 	//rows, cols
                10, 10,	//initX, initY
                6, 6);	//xPad, yPad
		
		content.add(pane);
	}
	
	public void addDisabledTextField(JPanel pane, String text, JLabel associatedLabel) {
		JTextField textBox = new JTextField(TXTBOX_LENGTH);
		textBox.setText(text);
		textBox.setDisabledTextColor(Color.DARK_GRAY);
		textBox.setEnabled(false);
		if (associatedLabel != null) {
			associatedLabel.setLabelFor(textBox);
		}
		pane.add(textBox);
	}
}
