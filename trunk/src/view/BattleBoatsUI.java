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
		// TODO add button for "connect to server"?
		addButton(buttonPane, "New Game", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPaneCL.show(contentPane, "newgame");
			}
		});
		addButton(buttonPane, "Load Game", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPaneCL.show(contentPane, "loadgame");
			}
		});
		addButton(buttonPane, "Exit", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opt = JOptionPane.showConfirmDialog(null,
						"Are you sure you would like to exit BattleBoats?",
						"Exit?", JOptionPane.YES_NO_OPTION);
				if (opt == 0) System.exit(0);
			}
		});
		setSpringLayout(buttonPane, 3, 1);
		
		titlePane.add(buttonPane);
		contentPane.add(titlePane, "title");
	}

	private void createNewGameScreen() {
		JPanel newGamePane = new JPanel();
		newGamePane.setLayout(new BoxLayout(newGamePane, BoxLayout.Y_AXIS));
		addLabel(newGamePane, "New Game", FONT_SIZE_H1);
		
		JPanel buttonPane = new JPanel(new SpringLayout());
		addButton(buttonPane, "vs. Computer", new PvCListener());
		addButton(buttonPane, "vs. Player", new PvPListener());
		//addButton(newGamePane, "vs. Player (online)", new PvPNetListener()); // move this to "connect server" option
		addButton(buttonPane, "Back", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPaneCL.show(contentPane, "title");
			}
		});

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
		addButton(buttonPane, "<Empty>", new LoadGameListener());
		addButton(buttonPane, "<Empty>", new LoadGameListener());
		//addButton(loadGamePane, "<Empty>", new LoadGameListener());
		addButton(buttonPane, "Back", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPaneCL.show(contentPane, "title");
			}
		});

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
	private void addButton(Container container, String text, ActionListener al) {
		JButton button = new JButton(text);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.addActionListener(al);
		container.add(button);
	}
	private void setSpringLayout(Container container, int numRows, int numCols) {
		SpringUtilities.makeCompactGrid(container,
                numRows, numCols,		//rows, cols
                PANE_XINIT, PANE_YINIT,	//initX, initY
                PANE_XPAD, PANE_YPAD); 	//xPad, yPad
	}

	protected void showGameSettingsDialog(int numPlayers) {
		// show dialog
		GameSettingsDialog gsDialog = new GameSettingsDialog(this, numPlayers);
		gsDialog.setTitle("Game Settings");
		gsDialog.pack();
		gsDialog.setModalityType(ModalityType.APPLICATION_MODAL);
		gsDialog.setLocationRelativeTo(null);
		gsDialog.setResizable(false);
		gsDialog.setVisible(true);
	}
	
	private class LoadGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO load game if not <empty>
		}
	}

	private class PvCListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO create GameUI for PvC
			showGameSettingsDialog(1);
		}
	}

	private class PvPListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO create GameUI for PvP
			showGameSettingsDialog(2);
		}
	}
	
	private class PvPNetListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//TODO
		}
	}
	

}
