package view;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dialog.ModalityType;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

import controller.GameController;

public class BattleBoatsUI extends JFrame {

	private final Container contentPane;
	private CardLayout contentPaneCL;
	
	private static final float FONT_SIZE_H1 = 24.0f;
	//private static final float FONT_SIZE_H2 = 14.0f;
	//private static final float FONT_SIZE_H3 = 12.0f; // default size
	
	private static final int PANE_XINIT = 10;
	private static final int PANE_YINIT = 10;
	private static final int PANE_XPAD = 6;
	private static final int PANE_YPAD = 6;
	
	public BattleBoatsUI() {
		setTitle("BattleBoats");
		//frame = new JFrame("BattleBoats");
		contentPane = getContentPane();
		contentPane.setLayout(new CardLayout());
		contentPaneCL = (CardLayout) contentPane.getLayout();

		createComponents();
	}

	private void createComponents() {
		//TODO GUI formatting
		createTitleScreen();
		createNewGameScreen();
		createLoadGameScreen();
		contentPaneCL.show(contentPane, "title");
	}

	private void createTitleScreen() {
		JPanel titlePane = new JPanel();
		titlePane.setLayout(new BoxLayout(titlePane, BoxLayout.Y_AXIS));
		addLabel(titlePane, "BattleBoats", FONT_SIZE_H1);

		JPanel buttonPane = new JPanel(new SpringLayout());
		
//		addButton(buttonPane, "Connect to Server", new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				// TODO show option dialog with username input
//			}
//		});
		
		addButton(buttonPane, "New Game", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPaneCL.show(contentPane, "newgame");
			}
		}, true);
		addButton(buttonPane, "Load Game", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPaneCL.show(contentPane, "loadgame");
			}
		}, true);
		addButton(buttonPane, "Exit", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opt = JOptionPane.showConfirmDialog(null,
						"Are you sure you would like to exit BattleBoats?",
						"Exit?", JOptionPane.YES_NO_OPTION);
				if (opt == 0) System.exit(0);
			}
		}, true);
		setSpringLayout(buttonPane, 3, 1);
		
		titlePane.add(buttonPane);
		contentPane.add(titlePane, "title");
	}

	private void createNewGameScreen() {
		JPanel newGamePane = new JPanel();
		newGamePane.setLayout(new BoxLayout(newGamePane, BoxLayout.Y_AXIS));
		addLabel(newGamePane, "New Game", FONT_SIZE_H1);
		
		JPanel buttonPane = new JPanel(new SpringLayout());
		addButton(buttonPane, "vs. Computer", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// show dialog for 1 player
				showGameSettingsDialog(1);
			}
		}, true);
		addButton(buttonPane, "vs. Player", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				// show dialog for 2 players
				showGameSettingsDialog(2);
			}
		}, true);
		//addButton(newGamePane, "vs. Player (online)", new PvPNetListener()); // move this to "connect server" option
		addButton(buttonPane, "Back", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPaneCL.show(contentPane, "title");
			}
		}, true);

		setSpringLayout(buttonPane, 3, 1);
		
		newGamePane.add(buttonPane);
		contentPane.add(newGamePane, "newgame");		
	}
	
	private void createLoadGameScreen() {
		/// Load Game Screen
		JPanel loadGamePane = new JPanel();
		loadGamePane.setLayout(new BoxLayout(loadGamePane, BoxLayout.Y_AXIS));
		addLabel(loadGamePane, "Load Game", FONT_SIZE_H1);
		
		JPanel buttonPane = new JPanel(new SpringLayout());
		
		// placeholder load buttons
		// if user has saved games, generate list of last 3 saved games,
		// else, generate list of 3 <empty> games
		addButton(buttonPane, "<Empty>", new LoadGameListener(), false);
		addButton(buttonPane, "<Empty>", new LoadGameListener(), false);
		//addButton(loadGamePane, "<Empty>", new LoadGameListener(), false);
		addButton(buttonPane, "Back", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPaneCL.show(contentPane, "title");
			}
		}, true);

		setSpringLayout(buttonPane, 3, 1);
		loadGamePane.add(buttonPane);
		
		contentPane.add(loadGamePane, "loadgame");
		loadGamePane.setLayout(new BoxLayout(loadGamePane, BoxLayout.Y_AXIS));
	}
	
	private void addLabel(Container container, String text, float fontSize) {
		JLabel label = new JLabel(text);
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		label.setFont(label.getFont().deriveFont(fontSize));
		container.add(label);
	}
	
	private void addButton(Container container, String text, ActionListener al, boolean enabled) {
		JButton button = new JButton(text);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.addActionListener(al);
		button.setEnabled(enabled);
		container.add(button);
	}
	
	private void setSpringLayout(Container container, int numRows, int numCols) {
		SpringUtilities.makeCompactGrid(container,
                numRows, numCols,		//rows, cols
                PANE_XINIT, PANE_YINIT,	//initX, initY
                PANE_XPAD, PANE_YPAD); 	//xPad, yPad
	}

	protected void showGameSettingsDialog(int numPlayers) {
		GameSettingsDialog gsDialog = new GameSettingsDialog(this, numPlayers);
		gsDialog.pack();
		gsDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		gsDialog.setLocationRelativeTo(null);
		gsDialog.setResizable(false);
		gsDialog.setVisible(true);
		
		if (gsDialog.changesMade) {
			// TODO use game settings to create PlacementUI
			GameController gc = new GameController(gsDialog.getPlayerOneName(), 
						gsDialog.getPlayerTwoName(),
						gsDialog.getNumberOfRows(),
						gsDialog.getNumberOfCols(),
						gsDialog.getBoatSizes(), 
						false);
			PlacementUI placeUI = new PlacementUI();
			placeUI.pack();
			placeUI.setLocationRelativeTo(null);
			placeUI.setResizable(false);
			placeUI.setVisible(true);
			setVisible(false);
		}
	}
	
	protected void launchPlacement() {
		
	}
	
	private class LoadGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO load game if not <empty>
		}
	}
}
