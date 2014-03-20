package view;

import java.awt.Container;

import javax.swing.JOptionPane;

public class ThreadedDialog extends Thread{

	private String display ;
	private Container contentPane;
	
	public ThreadedDialog(String text,Container contentPane){
		display = text;
		this.contentPane = contentPane;
	}
	
	public ThreadedDialog(String text){
		display = text;
		this.contentPane = new Container();
	}
	
	public void run(){
		JOptionPane.showMessageDialog(contentPane, display); 
		System.exit(0);
	}
	
}
