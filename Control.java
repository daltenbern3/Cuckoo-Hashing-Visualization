import javax.swing.*;


import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.*;

/**
 * This is the ControlPanel for the Serengeti. It allows the
 * user to pick which Animal it would like to add next.
 * 
 * @author Sundeep
 * @version 1.0
 */
@SuppressWarnings("serial")
public class Control extends JPanel
{

	private JLabel inputLabel;
	private JTextField input;
	private static int number = -1,removeNumber;
	int toReturn;


	public Control()
	{
		setPreferredSize(new Dimension(CuckooDriver.HEIGHT, 50));
		setBackground(Color.white);

		inputLabel = new JLabel ("Enter a number 0-99 to Cuckoo Hash!");

		input = new JTextField (5);
		input.addActionListener (new InputListener());

		add (inputLabel);
		add (input);

		//implement timer speed control if you feel adventurous
	}

	/**
	 * Invoked by SerengetiPanel to determine which Animal
	 * was chosen
	 * 
	 * @return The currently selected Animal type 
	 */
	public int getValue()
	{
		return number;
	}
	
	public int getRemoveValue()
	{
		
			String text = input.getText();
			
			if (!text.isEmpty()) number  = (Integer.parseInt (text));  //converting a String to an int

			input.setText ("");

		
		return number;
	}
	
	

	private class InputListener implements ActionListener
	{
		//--------------------------------------------------------------
		//  Performs the conversion when the enter key is pressed in
		//  the text field.
		//--------------------------------------------------------------
		public void actionPerformed (ActionEvent event)
		{
			String text = input.getText();
			
			if (!text.isEmpty()) number  = (Integer.parseInt (text));  //converting a String to an int

			input.setText ("");
		}
	}
}
