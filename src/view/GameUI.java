package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.Timer;

import model.Game;

import controller.GameController;

public class GameUI extends JFrame {

	private static final long serialVersionUID = 5892131800795551733L;
	private final Container contentPane;
	private CardLayout contentPaneCL;
	private JTextField timerLabel;
	private Timer timer;
	
	private Game game;
	private GameController gc;
	// consists of 2 boardUIs per player
	
	public GameUI() {
		contentPane = getContentPane();
//		super(frame, true);
//		content = getContentPane();
//		this.numPlayers = numPlayers;
//		createComponents();
	}
	
	public GameUI(Game g) {
		this.game = g;
		
		setTitle("BattleBoats");
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		createComponents();
	}
	
	private void createComponents() {
		createPlayerScreen(1);
		
		// 1-player
		//createPlayerScreen(); // creates timer, boards, buttons
		
		// online 2-player scenario
		//createPlayerScreen(); // creates timer, boards, buttons
		//createNextTurnScreen(); 
		
		// 2-player scenario
		//createNextTurnScreen(); // creates overlay between turns
		//createPlayerScreen(); // creates timer, boards, buttons
		//createNextTurnScreen(); 
		//createPlayerScreen();
		
		//createSurrenderScreen();
	}
	
	private void createPlayerScreen(int playerNumber) {
		JPanel playerScreen = new JPanel();
//		createTimer();
		createBoards(game.getGameSettings().getPlayer1Name(), game.getGameSettings().getPlayer2Name());
		createButtons();
		
		contentPane.add(playerScreen, "player"+playerNumber);
	}
	
	private void createTimer() {
		JPanel timerPane = new JPanel();
		timerPane.setLayout(new BoxLayout(timerPane, BoxLayout.Y_AXIS));
		timerPane.setAlignmentY(CENTER_ALIGNMENT);
		
		JLabel label = new JLabel("Time remaining");
		timerLabel = new JTextField();
		timerLabel.setEditable(false);
		timerPane.add(label);
		
		timer = new Timer(1000, new TimerListener());
		//timerPane.add(timer);
		
		contentPane.add(timerPane, BorderLayout.NORTH);
	}
	
	private void createBoards(String player1, String player2) {
		JPanel boardsPane = new JPanel();
		JLabel label;
		boardsPane.setLayout(new SpringLayout());
		
		JPanel player1Pane = new JPanel();
		label = new JLabel(player1);
		label.setFont(label.getFont().deriveFont(14.0f));
		player1Pane.add(label);
		boardsPane.add(player1Pane);
		
		JPanel player2Pane = new JPanel();
		label = new JLabel(player2);
		label.setFont(label.getFont().deriveFont(14.0f));
		player2Pane.add(label);
		boardsPane.add(player2Pane);
		
		SpringUtilities.makeCompactGrid(boardsPane,
                1, 2, 	//rows, cols
                10, 10,	//initX, initY
                6, 6);	//xPad, yPad
		contentPane.add(boardsPane, BorderLayout.CENTER);
	}

	private void createButtons() {
		JPanel buttonPane = new JPanel();
		JButton button;
		
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		buttonPane.setAlignmentY(RIGHT_ALIGNMENT);
		
		button = new JButton("Forfeit");
		button.addActionListener(new ForfeitGameListener());
		buttonPane.add(button);
		
		button = new JButton("Save Game");
		button.addActionListener(new SaveGameListener());
		buttonPane.add(button);
		
		button = new JButton("End Turn");
		button.addActionListener(new NextTurnListener());
		buttonPane.add(button);
		
		contentPane.add(buttonPane, BorderLayout.SOUTH);
		
	}
	
	private void nextTurn() {
		//TODO
	}
	
	private class TimerListener implements ActionListener {
	    int remainingSecs = 60;

	    public void actionPerformed(ActionEvent evt){
	    	remainingSecs--;
	    	timerLabel.setText(String.valueOf(remainingSecs));
	    	if(remainingSecs == 0) {
	    		timer.stop();
	    		//endTurn(); //TODO
	        }
	    }

	}
	
	private class ForfeitGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO
		}
	}
	
	private class SaveGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			// TODO
		}
	}
	
	private class NextTurnListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			nextTurn(); //TODO
		}
	}
}
