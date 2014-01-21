package view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SpringLayout;

public class PlacementUI extends JFrame {
	
	private final Container contentPane;
	
	public PlacementUI() {
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		createComponents();
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // change to show BattleBoatsUI on close
	}
	
	private void createComponents() {
		JPanel boardPane = new BoardUI(); //TODO create based on game settings
		contentPane.add(boardPane, BorderLayout.CENTER);
		
		//JPanel shipList = new ShipListUI();
		JPanel boatlistPane = new JPanel();
		JButton button;
		boatlistPane.setLayout(new SpringLayout());
		
		int numBoats = 5; // TODO set number ships based on game settings
		List<Integer> boatSizes = Arrays.asList(2, 3, 3, 4, 5); //TODO allow player to customize boat sizes 
		
		for (int boatNum = 0; boatNum < boatSizes.size(); boatNum++)
		{
			button = new JButton(boatSizes.get(boatNum).toString());
			boatlistPane.add(button);
			//createBoat(boatSizes.get(boatNum)); //TODO draw/create boat based on size
			
		}
		
		button = new JButton("Randomize");
		boatlistPane.add(button);
		
		button = new JButton("Submit");
		boatlistPane.add(button);
		
		SpringUtilities.makeCompactGrid(boatlistPane,
                numBoats+2, 1, 	//rows, cols
                10, 10,			//initX, initY
                6, 6);			//xPad, yPad
		
		contentPane.add(boatlistPane, BorderLayout.EAST);
	}
	

}
