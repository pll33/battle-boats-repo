package view;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SpringLayout;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import model.Board;
import model.Game;

import controller.GameController;

public class GameUI extends JFrame {

	private static final long serialVersionUID = 5892131800795551733L;
	private final Container contentPane;
	//private CardLayout contentPaneCL;
	private JTextField timerLabel;
	private Timer timer;
	
	private Game game;
	private GameController gc;
	
	// consists of 2 boardUIs per player
	private GameBoardUI playerBoardUI;
	private GameBoardUI opposingBoardUI;
	
	private String playerName;
	private String opponentName;
	
	public GameUI() {
		contentPane = getContentPane();
//		super(frame, true);
//		content = getContentPane();
//		this.numPlayers = numPlayers;
//		createComponents();
	}
	
	public GameUI(GameController gc, Board playerBoard) {
		this.gc = gc;
		this.game = gc.getGame();
		this.playerName = game.getGameSettings().getPlayer1Name();
		this.opponentName = game.getGameSettings().getPlayer2Name();
		
		String matchTitle = playerName + " vs. " + opponentName;
		setTitle("BattleBoats - " + matchTitle);
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		int rows = playerBoard.getHeight(),
			cols = playerBoard.getWidth();
		playerBoardUI = new GameBoardUI(rows, cols, playerBoard, false);
		opposingBoardUI = new GameBoardUI(rows, cols);
		createComponents();
	}
	
	public void init() {
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //TODO show BattleBoatsUI on close
		
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
//		JPanel playerScreen = new JPanel();
//		createTimer();
		createPanels();
		createButtons();
		
//		contentPane.add(playerScreen, "player"+playerNumber);
//		contentPane.add(playerScreen);
	}
	
	private void createTimer() {
		JPanel timerPane = new JPanel();
		timerPane.setLayout(new BoxLayout(timerPane, BoxLayout.Y_AXIS));
		timerPane.setAlignmentY(CENTER_ALIGNMENT);
		
		JLabel label = new JLabel("Time remaining");
		timerLabel = new JTextField();
		timerLabel.setEditable(false);
		timerPane.add(label);
		
//		timer = new Timer(1000, new TimerListener());
//		timerPane.add(timer);
		
		contentPane.add(timerPane, BorderLayout.NORTH);
	}
	
	private void createPanels() {
		JPanel boardsPane = new JPanel(),
			   playerBoardPane = new JPanel(),
			   opposingBoardPane = new JPanel();
		JLabel label = new JLabel();
		boardsPane.setLayout(new SpringLayout());
		
//		Border borderStyle = BorderFactory.createLineBorder(Color.BLACK);
		Border borderStyle = BorderFactory.createEmptyBorder();
		TitledBorder title = BorderFactory.createTitledBorder(
				borderStyle, playerName);
		title.setTitleFont(label.getFont().deriveFont(14.0f));
		title.setTitleJustification(TitledBorder.CENTER);
		playerBoardUI.setBorder(title);
		playerBoardPane.add(playerBoardUI);
		boardsPane.add(playerBoardPane);
		
		title = BorderFactory.createTitledBorder(
				borderStyle, opponentName);
		title.setTitleFont(label.getFont().deriveFont(14.0f));
		title.setTitleJustification(TitledBorder.CENTER);
		opposingBoardUI.setBorder(title);
		opposingBoardUI.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				// TODO Auto-generated method stub
				
			}
			
		});
		opposingBoardPane.add(opposingBoardUI);
		//opposingBoardPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		boardsPane.add(opposingBoardPane);
		
		SpringUtilities.makeCompactGrid(boardsPane,
                1, 2, 	//rows, cols
                10, 10,	//initX, initY
                6, 6);	//xPad, yPad
		contentPane.add(boardsPane, BorderLayout.CENTER);
	}

	private void createButtons() {
		JPanel buttonPane = new JPanel();
		buttonPane.setBorder(new EmptyBorder(0, 0, 10, 0));
		JButton button;
		
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		buttonPane.setAlignmentY(RIGHT_ALIGNMENT);
		
		buttonPane.add(Box.createHorizontalGlue());
		button = new JButton("Forfeit");
		button.addActionListener(new ForfeitGameListener());
		buttonPane.add(button);
		buttonPane.add(Box.createHorizontalStrut(10));
		
		button = new JButton("Save Game");
		button.addActionListener(new SaveGameListener());
		buttonPane.add(button);
		buttonPane.add(Box.createHorizontalStrut(10));
		
		button = new JButton("End Turn");
		button.addActionListener(new NextTurnListener());
		buttonPane.add(button);
		buttonPane.add(Box.createHorizontalGlue());
		
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
			int opt = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to forfeit the game?",
					"Forfeit Game?", JOptionPane.YES_NO_OPTION);
			if (opt == 0) {
				//TODO
				// shut down server (if host)
				// return to main menu
			}
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
