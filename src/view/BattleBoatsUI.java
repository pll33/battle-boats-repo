package view;

import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

public class BattleBoatsUI {

	private JFrame frame;
	private final Container contentPane;
	private CardLayout contentPaneCL;

	public static void main(String[] args) {
		BattleBoatsUI app = new BattleBoatsUI();
		app.frame.pack();
		app.frame.setResizable(false);
		app.frame.setVisible(true);
		app.frame.setLocationRelativeTo(null);
		app.frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public BattleBoatsUI() {
		frame = new JFrame("BattleShip");
		contentPane = frame.getContentPane();
		contentPane.setLayout(new CardLayout());
		contentPaneCL = (CardLayout) contentPane.getLayout();

		createComponents();
	}

	public void createComponents() {
		JLabel label;
		JPanel titlePane = new JPanel();
		titlePane.setLayout(new BoxLayout(titlePane, BoxLayout.Y_AXIS));

		//TODO GUI formatting
		/// Title Screen
		label = new JLabel("BattleBoats");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		titlePane.add(label);

		// TODO add button for "connect to server"?
		addButton(titlePane, "New Game", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPaneCL.show(contentPane, "newgame");
			}
		});
		addButton(titlePane, "Load Game", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPaneCL.show(contentPane, "loadgame");
			}
		});
		addButton(titlePane, "Exit", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int opt = JOptionPane.showConfirmDialog(null,
						"Are you sure you would like to exit BattleBoats?",
						"Exit?", JOptionPane.YES_NO_OPTION);
				if (opt == 0) System.exit(0);
			}
		});
		contentPane.add(titlePane, "title");

		// / New Game Screen
		JPanel newGamePane = new JPanel();
		label = new JLabel("New Game");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		newGamePane.add(label);
		newGamePane.setLayout(new BoxLayout(newGamePane, BoxLayout.Y_AXIS));

		addButton(newGamePane, "vs. Computer", new PvCListener());
		addButton(newGamePane, "vs. Player", new PvPListener());
		//addButton(newGamePane, "vs. Player (online)", new PvPNetListener()); // move this to "connect server" option
		addButton(newGamePane, "Back", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPaneCL.show(contentPane, "title");
			}
		});

		contentPane.add(newGamePane, "newgame");

		// / Load Game Screen
		JPanel loadGamePane = new JPanel();
		label = new JLabel("Load Game");
		label.setAlignmentX(Component.CENTER_ALIGNMENT);
		loadGamePane.add(label);
		loadGamePane.setLayout(new BoxLayout(loadGamePane, BoxLayout.Y_AXIS));

		// placeholder load buttons
		addButton(loadGamePane, "<Empty>", new LoadGameListener());
		addButton(loadGamePane, "<Empty>", new LoadGameListener());
		//addButton(loadGamePane, "<Empty>", new LoadGameListener());
		addButton(loadGamePane, "Back", new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				contentPaneCL.show(contentPane, "title");
			}
		});

		contentPane.add(loadGamePane, "loadgame");
		loadGamePane.setLayout(new BoxLayout(loadGamePane, BoxLayout.Y_AXIS));

		contentPaneCL.show(contentPane, "title");

	}

	private void addButton(Container container, String text, ActionListener al) {
		JButton button = new JButton(text);
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		button.addActionListener(al);
		container.add(button);
	}

	private class LoadGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO load game if not <empty>
		}
	}

	private class PvCListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO create GameUI for PvC
		
		}
	}

	private class PvPListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO create GameUI for PvP
		}
	}
	
	private class PvPNetListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			//TODO
		}
	}
}
