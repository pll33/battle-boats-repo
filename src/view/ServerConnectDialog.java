package view;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;

import controller.GameController;

import core.Constants;

public class ServerConnectDialog extends JDialog {

	// Connect to Hosted Game
		// Server IP: <input textbox>
			
	// Connect to Server button (connects to server when textbox.text is valid IP address, 
	//		show serverConnectDialog)
	// Cancel button (returns to main menu)
	
	private static final long serialVersionUID = -5892146762277902504L;
	private static final int TXTBOX_LENGTH = 10;
	
	private JFrame mainMenu;
	private Container content;
	private JTextField serverIP;
	
	protected boolean changesMade;
	
	public ServerConnectDialog(JFrame frame) {
		super(frame, true);
		mainMenu = frame;
		content = getContentPane();
		changesMade = false;
		
		createComponents();
	}
	
	public void init() {
		pack();
		setTitle("BattleBoats - Join Game");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	private void createComponents() {
		JLabel label;
		JButton button;
		
		JPanel pane = new JPanel(new SpringLayout());
		
		label = new JLabel("Connect to Game"); 
		label.setFont(label.getFont().deriveFont(14.0f));
		pane.add(label);
		pane.add(new JLabel(""));
		
//		label = new JLabel("Player name: ", JLabel.TRAILING);
//		pane.add(label);
//		JTextField playerName = new JTextField(TXTBOX_LENGTH);
//		playerName.setToolTipText("Player Name");
//		playerName.setText("Player");
//		//player1Name.addFocusListener(new TextFieldFocusListener());
//		label.setLabelFor(playerName);
//		pane.add(playerName);
		
		label = new JLabel("Server IP Address: ", JLabel.TRAILING);
		pane.add(label);
		serverIP = new JTextField(TXTBOX_LENGTH);
		serverIP.setText("");
		serverIP.setDisabledTextColor(Constants.TEXTFIELD_DISABLED);
		label.setLabelFor(serverIP);
		pane.add(serverIP);
		
		button = new JButton("Cancel");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				dispose();
			}	
		});
		pane.add(button);
		
		button = new JButton("Connect");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
				// validate serverIP textfield
				if (serverIP.getText().matches(Constants.IPV4_REGEX)) {
					//serverIP.setBackground(Constants.TEXTFIELD_DEFAULT);
					String IPstr = serverIP.getText();
					
					try {
						GameController gc = new GameController(false, null, IPstr);
						
						// create placementUI
						PlacementUI placeUI = new PlacementUI(gc.getGame().getGameSettings(), gc);
						placeUI.init();
						mainMenu.setVisible(false);
						setVisible(false);
					} catch (IOException ex) {
						JOptionPane.showMessageDialog(content, "Error: Could not connect to server.",
								"Connection Error", JOptionPane.ERROR_MESSAGE); 
						// dialog with "Error: Could not connect to server." //TODO
					}
				} else {
					serverIP.setBackground(Constants.TEXTFIELD_ERROR);
				}
			}	
		});
		pane.add(button);
		
		SpringUtilities.makeCompactGrid(pane,
                3, 2, 	//rows, cols
                10, 10,	//initX, initY
                6, 6);	//xPad, yPad
		
		content.add(pane);
	}
}
