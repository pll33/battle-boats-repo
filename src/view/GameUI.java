package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;

import view.GameBoardUI.MouseMoveAdapter;

import model.Board;
import model.Game;
import model.Move;
import controller.GameController;

public class GameUI extends JFrame {

	private static final long serialVersionUID = 5892131800795551733L;
	private final Container contentPane;
	private JPanel gamePane;

//	private JLabel turnStatus;
	private JButton endTurnButton;
	private TitledBorder turnTitle;
	
	private ArrayList<Move> moves;
	
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
		gamePane = new JPanel(new BorderLayout());
		contentPane.add(gamePane);
		
		int rows = playerBoard.getHeight(),
			cols = playerBoard.getWidth();
		playerBoardUI = new GameBoardUI(rows, cols, playerBoard, false);
		opposingBoardUI = new GameBoardUI(rows, cols);
		createComponents();
		moves = new ArrayList<Move>();
		gc.setGameBoard(playerBoard);
		gc.setParentContainer(this);
		
		this.addMouseMotionListener(new MouseMoveAdapter());
	}
	
	public void init() {
		pack();
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //TODO show BattleBoatsUI on close

		gc.start();
	}

	public void update(boolean playerTurn) {
		endTurnButton.setEnabled(playerTurn);
		updateTurnTitle(playerTurn);
	}
	
	private void createComponents() {
		createPlayerScreen();
		
		gamePane.setBorder(new EmptyBorder(10,0,10,0));
	}
	
	private void createPlayerScreen() {
		createPanels();
		createButtons();
	}

	private void createPanels() {
		JPanel boardsPane = new JPanel(),
			   playerBoardPane = new JPanel(),
			   opposingBoardPane = new JPanel();
		JLabel label = new JLabel();
		
		boardsPane.setLayout(new BoxLayout(boardsPane, BoxLayout.X_AXIS));
		boardsPane.add(Box.createHorizontalGlue());
//		Border borderStyle = BorderFactory.createLineBorder(Color.BLACK);
		Border borderStyle = BorderFactory.createEmptyBorder();
		TitledBorder title = BorderFactory.createTitledBorder(
				borderStyle, playerName);
		title.setTitleFont(label.getFont().deriveFont(14.0f));
		title.setTitleJustification(TitledBorder.CENTER);
		playerBoardUI.setBorder(title);
		playerBoardPane.add(playerBoardUI);
		boardsPane.add(playerBoardPane);
		boardsPane.add(Box.createHorizontalStrut(25));
		title = BorderFactory.createTitledBorder(
				borderStyle, opponentName);
		title.setTitleFont(label.getFont().deriveFont(14.0f));
		title.setTitleJustification(TitledBorder.CENTER);
		opposingBoardUI.setBorder(title);
		opposingBoardUI.addPropertyChangeListener(new TargetSelectedListener());
		opposingBoardPane.add(opposingBoardUI);
		//opposingBoardPane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		boardsPane.add(opposingBoardPane);
		boardsPane.add(Box.createHorizontalGlue());
		
		String turnStr = gc.getTurn() ? "YOUR TURN" : "OPPONENT'S TURN";
		turnTitle = BorderFactory.createTitledBorder(
				borderStyle, turnStr);
		turnTitle.setTitleFont(label.getFont().deriveFont(16.0f));
		turnTitle.setTitleJustification(TitledBorder.CENTER);
		boardsPane.setBorder(turnTitle);

		gamePane.add(boardsPane, BorderLayout.CENTER);
	}

	private void createButtons() {
		JPanel bottomPane = new JPanel();
		bottomPane.setLayout(new BoxLayout(bottomPane, BoxLayout.Y_AXIS));
		
		JPanel buttonPane = new JPanel();
		JButton button;
		
		buttonPane.setLayout(new BoxLayout(buttonPane, BoxLayout.X_AXIS));
		buttonPane.setAlignmentY(RIGHT_ALIGNMENT);
		
		buttonPane.add(Box.createHorizontalGlue());
		button = new JButton("Forfeit");
		button.addActionListener(new ForfeitGameListener());
		buttonPane.add(button);
		buttonPane.add(Box.createHorizontalStrut(10));
		
		endTurnButton = new JButton("End Turn");
		endTurnButton.addActionListener(new NextTurnListener());
		endTurnButton.setEnabled(false);
		buttonPane.add(endTurnButton);
		buttonPane.add(Box.createHorizontalGlue());
		
		bottomPane.add(buttonPane);
		gamePane.add(bottomPane, BorderLayout.SOUTH);
		
	}
	
	
	private void nextTurn() {
		Move move = new Move(opposingBoardUI.originLocation.x, opposingBoardUI.originLocation.y);
		moves.add(move);
		
		// send move to opponent
		gc.getGame().getPlayer().setNextMove(move);
		
		updateTurnTitle(false); // set title to "opponent's turn"
		playerBoardUI.updateBoard(gc.getGame().getGameBoard());
		opposingBoardUI.clearSelectedCell();
		opposingBoardUI.updateBoard(gc.getGame().getPlayer().getMovedBoard()); 
		try {
			Thread.sleep(900);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (gc.getGame().getGameSettings().isVsComputer()) {
			updateTurnTitle(true);
		}
		playerBoardUI.repaint();
		opposingBoardUI.repaint();
	}
	
	
	private void updateTurnTitle(boolean myTurn) {
		String title = myTurn ? "YOUR TURN" : "OPPONENT'S TURN";
		turnTitle.setTitle(title);
	}
	
	private class MouseMoveAdapter extends MouseAdapter {
    	@Override
    	public void mouseMoved(MouseEvent e) {
    		if (!game.getGameSettings().isVsComputer()) {
    			update(gc.getTurn());
    		}
        }
    }
	
	private class TargetSelectedListener implements PropertyChangeListener {
		public void propertyChange(PropertyChangeEvent evt) {
			if (evt.getPropertyName().equals(GameBoardUI.TARGET_ACQUIRED)) {
				Point currentSelected = (Point) evt.getNewValue();
				if (currentSelected != null && gc.getTurn()) {
					playerBoardUI.updateBoard(gc.getGame().getGameBoard());
					playerBoardUI.repaint();
					endTurnButton.setEnabled(true);
					
				}
			}	
		}
	}
	
	private class ForfeitGameListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			int opt = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to forfeit the game?\n This will close the application.",
					"Forfeit Game?", JOptionPane.YES_NO_OPTION);
			if (opt == 0) {
				System.exit(0);
			}
		}
	}
	
	private class NextTurnListener implements ActionListener {
		public void actionPerformed(ActionEvent e) {
			if (opposingBoardUI.originLocation != null) {
				endTurnButton.setEnabled(false);
				nextTurn();
			}
		}
	}
	
	
}
