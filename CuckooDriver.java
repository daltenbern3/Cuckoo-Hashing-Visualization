import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import java.util.ArrayList;




@SuppressWarnings("serial")
public class CuckooDriver extends JPanel{  

	public static final int WIDTH = 500, HEIGHT = 500, IMAGE_SIZE = 35;
	private static int tableSize = 8;
	Boolean caught = false;
	private static PositionValue[][] positions = new PositionValue[2][100];
	private Color emptyTile = Color.BLACK;
	private Timer timer;
	static int move = 0;
	static int moved = 0;
	static PositionValue test;
	static ArrayList<Integer> xValues = new ArrayList<Integer>();
	static ArrayList<Integer> xPointValues = new ArrayList<Integer>();
	static ArrayList<Integer> yPointValues = new ArrayList<Integer>();
	static ArrayList<Integer> xStartValues = new ArrayList<Integer>();
	private CuckooHashTable hashTable = new CuckooHashTable(HEIGHT);

	JButton button, removeButton;
	private PositionValue[] moving = new PositionValue[2];
	Control control;
	int val = -1, removeVal = -1;
	int unitChange;
	int moveLength;
	PositionValue movingDriver;
	int direction;

	int[] newXLocation =  {0,0};
	int[] newYLocation =  {0,0};

	int[] xUnits =  {0,0};
	int[] yUnits =  {0,0};

	static boolean leftOrRight = true;


	int hash1;
	int hash2;

	public JLabel label;



	public CuckooDriver(Control control) {
		setPreferredSize(new Dimension(WIDTH,HEIGHT));
		setBackground(Color.white);
		
		button = new JButton("Next Step");
		button.addActionListener(new ButtonListener());
		removeButton = new JButton("Remove");
		removeButton.addActionListener(new RemoveListener());
		label = new JLabel("-- hashes to index -- in the first table and index -- in the second table");

		label.setLocation(10, 0);
		add (label);
		add (button);
		add (removeButton);
		
		
		timer = new Timer(3, new TimerListener());
		timer.start();
		this.control = control;

		updateConstants();
	}

	public void updateConstants(){

		xValues.clear();
		int boxWidth = HEIGHT / (tableSize + 2);

		xValues.add(boxWidth*2);
		xValues.add(HEIGHT - 3 * (int) (1.2 * boxWidth));

		int tileWidth = HEIGHT / (tableSize + 2);
		int shift = ((tileWidth - 4 ) / 4);
		xPointValues.clear();


		xPointValues.add(xValues.get(0) + shift+ 2);
		xPointValues.add(xValues.get(1) + shift+ 2);

		for (int i = 0; i < tableSize; i++){
			yPointValues.add(((1 + i)*tileWidth) + (tileWidth - shift));
		}

	}

	public void paintComponent (Graphics page) {
		super.paintComponent(page);

		int tileWidth = HEIGHT / (tableSize + 2);

		Font trb = new Font("TimesRoman", Font.BOLD, tileWidth - 4);
		page.setFont(trb);
		page.setColor(emptyTile);
		ArrayList<Integer> list = new ArrayList<Integer>();
		list.add(tileWidth*2);
		list.add(HEIGHT - 3 * tileWidth);
		for (int x : xValues) {
			for (int y = 1; y < tableSize + 1; y++) {
				page.draw3DRect(x, (y * tileWidth), (int) (1.7 * tileWidth), tileWidth, true); 
			}
		}

		if (moving[1] != null) moving[1].draw(page);

		if (test != null) test.draw(page);

		for (int i = 0; i < 2; i++){
			for(int j = 0; j < tableSize; j++){
				if (positions[i][j] != null) positions[i][j].draw(page);
			}
		}
	}


	public void swap(PositionValue point){

		PositionValue temp;
		moving[0] = null;
		moving[1] = null;

		if (positions[0][point.getIndex1()] == null){
			point.setCurrent(true);
			moving[0] = (point);
			newXLocation[0] = point.getX1();
			newYLocation[0] = point.getY1();
			positions[0][point.getIndex1()] = point;
			setMove(moving, newXLocation, newYLocation);
			test = null;
		}

		else if (positions[1][point.getIndex2()] == null){
			point.setCurrent(false);
			moving[0] = (point);
			newXLocation[0] = point.getX2();
			newYLocation[0] = point.getY2();
			positions[1][point.getIndex2()]= point;
			setMove(moving, newXLocation, newYLocation);
			test = null;
		}
		else{
			if (leftOrRight){
				temp = positions[0][point.getIndex1()];

				positions[0][point.getIndex1()] = point;

				moving[0] = positions[0][point.getIndex1()];
				moving[1] = temp;

				newXLocation[0] = positions[0][point.getIndex1()].getX1();
				newYLocation[0] = positions[0][point.getIndex1()].getY1();

				newXLocation[1] = temp.getStartX();
				newYLocation[1] = temp.getStartY();

				setMove(moving, newXLocation, newYLocation);
				leftOrRight = !leftOrRight;
			}
			else{

				temp = positions[1][point.getIndex2()];

				positions[1][point.getIndex2()] = point;

				moving[0] = positions[1][point.getIndex2()];
				moving[1] = temp;

				newXLocation[0] = positions[1][point.getIndex2()].getX2();
				newYLocation[0] = positions[1][point.getIndex2()].getY2();

				newXLocation[1] = temp.getStartX();
				newYLocation[1] = temp.getStartY();

				setMove(moving, newXLocation, newYLocation);
				leftOrRight = !leftOrRight;
			}
			test = temp;
		}
	}


	public void setMove(PositionValue[] moving, int[] newXLocation, int[] newYLocation){

		this.newXLocation = newXLocation;
		this.newYLocation = newYLocation;
		this.moving = moving;
		int[] X = new int[2];
		int[] Y = new int[2];

		if (moving[0] != null){
			X[0] = findDirection(moving[0].getCurrentX(),newXLocation[0]);
			Y[0] = findDirection(moving[0].getCurrentY(),newYLocation[0]);
		}

		if (moving[1] != null){
			X[1] = findDirection(moving[1].getCurrentX(),newXLocation[1]);
			Y[1] = findDirection(moving[1].getCurrentY(),newYLocation[1]);
		}

		this.xUnits = X;
		this.yUnits = Y;

	}

	public int findDirection(int x, int newX){
		if ((x - newX) < 0) return 1;
		else if (x - newX > 0) return -1;
		else return 0;
	}

	public void move(){

		for (int i = 0; i < 2; i++){

			if (moving[i] != null){
				if(moving[i].getCurrentX() != newXLocation[i]){
					moving[i].setCurrentX(moving[i].getCurrentX() + xUnits[i]);
				}
				if(moving[i].getCurrentY() != newYLocation[i]){
					moving[i].setCurrentY(moving[i].getCurrentY() + yUnits[i]);
				}
			}
		}
	}


	public static void main (String[] args) {
		JFrame frame = new JFrame ("Cuckoo Hashing");
		frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
		Control control = new Control();
		frame.add(control, BorderLayout.NORTH);
		frame.getContentPane().add(new  CuckooDriver(control));
		frame.pack();
		frame.setVisible(true);
	}

	private class TimerListener implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			if (control.getValue() != val && control.getValue() !=-1 && test == null){
				val = control.getValue();
				val = val%100;
				if (!hashTable.contains(val)) test = hashTable.add(val);

				if (test != null && test.getResize()){
					positions = hashTable.resizeHelper();
					tableSize = positions[1].length;
					updateConstants();
				}

				if (test != null) {
					label.setText(val + " hashes to index " + test.getIndex1() + " in the first table and index " + test.getIndex2() + " in the second table");

					swap(test);
				}
			}
			move();
			button.setLocation(200, 30);
			label.setLocation(10, 0); 
			removeButton.setLocation(205, 60);
			repaint();

		}
	}

	public class ButtonListener implements ActionListener
	{ 
		public void actionPerformed(ActionEvent e)
		{
			if (test != null) {
				label.setText(test.getVal() + " hashes to index " + test.getIndex1() + "  in the first table and  index " + test.getIndex2() + "  in the second table");

				swap(test);
			}

		}
	}

	public class RemoveListener implements ActionListener
	{ 
		public void actionPerformed(ActionEvent e)
		{

			PositionValue toRemove;
			test = null;
			removeVal = control.getRemoveValue();
			val = removeVal;
			if (removeVal>=0 && removeVal < 100) {
				toRemove = hashTable.remove(removeVal);

				if (toRemove != null){
					if(positions[0][toRemove.getIndex1()].getVal() == removeVal){
						positions[0][toRemove.getIndex1()] = null;

					}
					else if(positions[1][toRemove.getIndex2()].getVal() == removeVal){
						positions[1][toRemove.getIndex2()] = null;

					}
				}
				repaint();
			}
		}
	}
}