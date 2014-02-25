package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;

import controller.GameController;
import core.GameSettings;

public class PlacementUI extends JFrame {
	
	private static final long serialVersionUID = -8341984086894414855L;
	
	private final Container contentPane;
	private PlacementBoardUI boardPane;
	private List<JToggleButton> boatButtons;
	private ButtonGroup boatButtonGroup;
	private JButton clearButton;
	private JButton submitButton;
	
	private List<Integer> boatSizes;
	private GameSettings settings;
	private GameController gc;
	
	public PlacementUI(GameSettings settings, GameController gc) {
		boatSizes = settings.getBoatSizes();
		boardPane = new PlacementBoardUI(settings.getHeight(), settings.getWidth(), boatSizes); 
		
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		contentPane.add(boardPane, BorderLayout.CENTER);
		boatButtons = new ArrayList<JToggleButton>();
		
		this.settings = settings;
		this.gc = gc;
		
		setTitle("BattleBoats Placement");
		createComponents();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // TODO change to show BattleBoatsUI on close
	}

	private void createComponents() {
		JPanel boatlistPane = new JPanel();
		JButton button;
		JToggleButton tb;
		boatlistPane.setLayout(new SpringLayout());
		
		int numBoats = boatSizes.size();
		boatButtonGroup = new ButtonGroup();

		for (int boatNum = 0; boatNum < boatSizes.size(); boatNum++)
		{
			final int boatIndex = boatNum;
			tb = new JToggleButton(boatSizes.get(boatNum).toString());
			tb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					boardPane.setPlacementBoatIndex(boatIndex);
					System.out.println("set: " + boatIndex);
				}
			});
			boatButtons.add(tb);
			boatButtonGroup.add(tb);			
			boatlistPane.add(tb);
			//createBoat(boatSizes.get(boatNum)); //TODO draw/create boat button based on size
			
		}
		
		button = new JButton("Randomize");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boardPane.randomize();
				boatButtonGroup.clearSelection();
				setEnabledAllBoatButtons(false);
				submitButton.setEnabled(true);
				clearButton.setEnabled(true);
			}
		});
		boatlistPane.add(button);
		
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boardPane.clearPlacementBoard();
				boatButtonGroup.clearSelection();
				setEnabledAllBoatButtons(true);
				submitButton.setEnabled(false);
				clearButton.setEnabled(false);
			}
		});
		clearButton.setEnabled(false);
		boatlistPane.add(clearButton);
		
		String submitText;
		if (settings.getPlayer2Name().equals("Computer")) {
			submitText = "Start Game";
		} else {
			submitText = "Submit";
		}
		submitButton = new JButton(submitText);
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				//TODO
				// create game UI for player, send placed boats board to gameUI
				//GameUI gameUI = new GameUI();
				//startGame(boardPane.getPlacementBoats());
				
				System.out.println("Placement Submit pressed");
				gc.getGame().getGameBoard().addBoats(boardPane.getPlacementBoats().getBoats());
				gc.getGame().setReady();
			}
		});
		submitButton.setEnabled(false);
		boatlistPane.add(submitButton);
		
		SpringUtilities.makeCompactGrid(boatlistPane,
                numBoats+3, 1, 	//rows, cols
                10, 10,			//initX, initY
                6, 6);			//xPad, yPad
		
		contentPane.add(boatlistPane, BorderLayout.EAST);
		
		boardPane.addPropertyChangeListener(new PropertyChangeListener() {

			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals(PlacementBoardUI.BOAT_INDEX)) {
					int oldIndex = (int) evt.getOldValue(),
						newIndex = (int) evt.getNewValue();
					
					if (newIndex == -1) {
						setEnabledBoatButton(oldIndex, false);
						clearButton.setEnabled(true);
						boatButtonGroup.clearSelection();
					} else if (newIndex >= 0) {
						setEnabledBoatButton(newIndex, true);
						boatButtonGroup.setSelected(boatButtons.get(newIndex).getModel(), true);
					}
				}	
				
				int enabledCount = 0;
				for (int j = 0; j < boatButtons.size(); j++) {
					if (boatButtons.get(j).isEnabled() == true) {
						enabledCount++;
					}
				}
				
				if (enabledCount == 0) {
					// activate submit button when all boat buttons are disabled
					submitButton.setEnabled(true);
				} else if (enabledCount == boatButtons.size()) {
					// de-activate clear button when all boat buttons are enabled 
					clearButton.setEnabled(false);
				}
			}
			
		});
	}
	
	private void setEnabledBoatButton(int index, boolean enable) {
		if (index >= 0 && index < boatButtons.size()) {
			boatButtons.get(index).setEnabled(enable);
		}
	}
	
	private void setEnabledAllBoatButtons(boolean enable) {
		for (JToggleButton b : boatButtons) {
			b.setEnabled(enable);
		}
	}
}
