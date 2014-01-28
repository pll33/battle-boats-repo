package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.SpringLayout;

public class PlacementUI extends JFrame {
	
	private final Container contentPane;
	private BoardUI boardPane;
	
	public PlacementUI() {
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		boardPane = new BoardUI(10,10,true); //TODO create based on game settings
		contentPane.add(boardPane, BorderLayout.CENTER);
		
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
		List<Integer> boatSizes = Arrays.asList(2, 3, 3, 4, 5); //TODO allow player to customize boat sizes 
		ButtonGroup group = new ButtonGroup();
		
		for (int boatNum = 0; boatNum < boatSizes.size(); boatNum++)
		{
			tb = new JToggleButton(boatSizes.get(boatNum).toString());
			tb.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JToggleButton selectedTB = (JToggleButton) e.getSource();
					//System.out.println(selectedTB.isSelected());
					//if (selectedTB.isSelected());
					enterPlacementMode(Integer.parseInt(selectedTB.getText()));
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
				//TODO
				
			}
		});
		boatlistPane.add(button);
		
		button = new JButton("Submit");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//TODO
				
			}
		});
		button.setEnabled(false);
		boatlistPane.add(button);
		
		SpringUtilities.makeCompactGrid(boatlistPane,
                numBoats+2, 1, 	//rows, cols
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
