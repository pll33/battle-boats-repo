package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.SwingConstants;

public class ServerWaitDialog extends JDialog {

	
	// Waiting for Players
		// Players: <player1Name> 
	    // 			<player2Name> 
		// (update status per player: CONNECTING,PLACEMENT,WAITING,READY)
		
	// Cancel Game button (quits game, closes server; becomes "Forfeit" if other player is connected)
	// Start Game button (activated when both players are connected)
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 5405300148615059594L;
	
	private Container content;
	private JLabel player1;
	private JLabel player2;
	private JButton startGameButton;
	
	
	public ServerWaitDialog(JFrame frame, String player1Name) {
		super(frame, true);
		content = getContentPane();
		
		player1 = new JLabel(player1Name + " (Waiting)");
		player2 = new JLabel("");

		createComponents();
	}
	
	public void init() {
		pack();
		setTitle("BattleBoats - Waiting for Players");
		setModalityType(ModalityType.APPLICATION_MODAL);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}
	
	private void createComponents() {
		// TODO set player labels to: playerName + status
		// TODO add column with ready? checkboxes, quit game button, "back to placement" button
		
		JLabel label;
		JButton button;
		JPanel pane = new JPanel(new BorderLayout());
		
		label = new JLabel("Player Status"); 
		label.setFont(label.getFont().deriveFont(14.0f));
		label.setHorizontalAlignment(SwingConstants.CENTER);
		pane.add(label, BorderLayout.NORTH);
		
		JPanel mainPane = new JPanel(new SpringLayout());
		
		label = new JLabel("Players: ", JLabel.TRAILING);
		mainPane.add(label);
		//player1Name.addFocusListener(new TextFieldFocusListener());
		label.setLabelFor(player1);
		mainPane.add(player1);
		
		mainPane.add(new JLabel(""));
		label.setLabelFor(player2);
		mainPane.add(player2);
		
		button = new JButton("Quit");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//changesMade = false;
				dispose();
			}	
		});
		mainPane.add(button);
		
		startGameButton = new JButton("Start Game");
		startGameButton.setEnabled(false);
		startGameButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//changesMade = true;
				//setVisible(false);
			}	
		});
		mainPane.add(startGameButton);
		
		SpringUtilities.makeCompactGrid(mainPane,
                3, 2, 	//rows, cols
                10, 10,	//initX, initY
                6, 6);	//xPad, yPad
		
		pane.add(mainPane,BorderLayout.CENTER);
		content.add(pane);
		
	}
}
