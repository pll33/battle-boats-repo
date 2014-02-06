package view;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;

public class PlacementUI extends JFrame {
	
	private final Container contentPane;
	private PlacementBoardUI boardPane;
	private List<Integer> boatSizes;
	private JButton clearButton;
	private JButton submitButton;
	
	public PlacementUI() {
		this("","",10,10,(ArrayList<Integer>) Arrays.asList(2, 3, 3, 4, 5));
	}
	
	public PlacementUI(String playerOneName, String playerTwoName,
			Integer numberOfRows, Integer numberOfCols,
			ArrayList<Integer> boatSizes) {
		
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());

		this.boatSizes = boatSizes; 

		boardPane = new PlacementBoardUI(numberOfRows,numberOfCols, boatSizes); 
		
		contentPane.add(boardPane, BorderLayout.CENTER);
		
		setTitle("BattleBoats Placement");
		createComponents();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // change to show BattleBoatsUI on close
	}

	private void createComponents() {
		//JPanel shipList = new ShipListUI();
		JPanel boatlistPane = new JPanel();
		JButton button;
		JToggleButton tb;
		boatlistPane.setLayout(new SpringLayout());
		
		int numBoats = 5; // TODO set number ships based on game settings
		ButtonGroup group = new ButtonGroup();
		
		for (int boatNum = 0; boatNum < boatSizes.size(); boatNum++)
		{
			tb = new JToggleButton(boatSizes.get(boatNum).toString());
			tb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JToggleButton selectedTB = (JToggleButton) e.getSource();
					//System.out.println(selectedTB.isSelected());
					//if (selectedTB.isSelected());
					int shipSize = Integer.parseInt(selectedTB.getText());
					enterPlacementMode(shipSize);
					//TODO
					
				}
			});
			group.add(tb);			
			boatlistPane.add(tb);
			//createBoat(boatSizes.get(boatNum)); //TODO draw/create boat based on size
			
		}
		
		button = new JButton("Randomize");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boardPane.randomize();
				submitButton.setEnabled(true);
				clearButton.setEnabled(true);
			}
		});
		boatlistPane.add(button);
		
		clearButton = new JButton("Clear");
		clearButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				boardPane.clear();
				submitButton.setEnabled(false);
				clearButton.setEnabled(false);
			}
		});
		clearButton.setEnabled(false);
		boatlistPane.add(clearButton);
		
		submitButton = new JButton("Submit");
		submitButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
				
			}
		});
		submitButton.setEnabled(false);
		boatlistPane.add(submitButton);
		
		SpringUtilities.makeCompactGrid(boatlistPane,
                numBoats+3, 1, 	//rows, cols
                10, 10,			//initX, initY
                6, 6);			//xPad, yPad
		
		contentPane.add(boatlistPane, BorderLayout.EAST);
	}
	
	private void enterPlacementMode(int shipSize) {
		boardPane.setPlacementShipSize(shipSize);
	}
	
	private void placeShip() {
		// if togglebutton is pressed, 
		// if cell contains ship, pick up ship
		
		
	}
	
}
