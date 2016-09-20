import java.util.ArrayList;


/**
 * Represents a Cuckoo Hash Table.
 * 
 * @author Daltenbern
 *
 */
public class CuckooHashTable {

	private final int DEFAULT_TABLE_SIZE = 8, startX, startY;
	private PositionValue[] table1;
	private PositionValue[] table2;
	private PositionValue[][] resizedTable;
	private final int HEIGHT;
	static ArrayList<Integer> xValues = new ArrayList<Integer>();
	static ArrayList<Integer> xPointValues = new ArrayList<Integer>();
	static ArrayList<Integer> yPointValues = new ArrayList<Integer>();
	static ArrayList<Integer> xStartValues = new ArrayList<Integer>();
	
	/**
	 * The Cuckoo constructor. Sets the default table sizes.
	 */
	public CuckooHashTable(int height){
		table1 = new PositionValue[DEFAULT_TABLE_SIZE];
		table2 = new PositionValue[DEFAULT_TABLE_SIZE];
		HEIGHT = height;
		updateConstants();
		this.startX = xPointValues.get(0) + 115;
		this.startY = yPointValues.get((int) (table1.length / 2)-2);
		
	}
	
	/**
	 * The method used to add to the Cuckoo hash table.
	 * 
	 * @param value
	 * @return returns true if added and false if not
	 */
	public PositionValue add(Integer value){
		if(value == null || value<0 || value>99) return null;
		boolean resized = false;
		for(int i=0;i<100;i++){
			//doesn't add if value already exists
			if(contains(value)) return null;
			//tries to add to table
			PositionValue added = addToTable(value);
			if(added!=null){
				if(resized)added.setResize(true);
				return added;
			}
			//if it can't add to table, resizes
			resize();
			resized = true;
		}
		return null;
	}
	
	public PositionValue remove(int value){
		int pos1 = hashCode1(value);
		int pos2 = hashCode2(value);
		PositionValue temp;
		if (!contains(value)) return null;
		if(table1[pos1].getVal() == value){
			temp = table1[pos1];
			table1[pos1] = null;
			return temp;
		}
		if(table2[pos2].getVal() == value){
			temp = table2[pos2];
			table2[pos2] = null;
			return temp;
		}
		return null;
	}
	
	/**
	 * Resizes the tables to twice their original size
	 */
	private void resize(){
		PositionValue[] oldTable1 = table1;
		PositionValue[] oldTable2 = table2;
		table1 = new PositionValue[table1.length*2];
		table2 = new PositionValue[table2.length*2];
		updateConstants();
		for(PositionValue x:oldTable1){
			if(x!=null){
				add(x.getVal());
			}
		}
		
		for(PositionValue x:oldTable2){
			if(x!=null){
				add(x.getVal());
			}
		}
		resizedTable = new PositionValue[2][table1.length];
		for(int i=0;i<table1.length;i++){
			if (table1[i] != null) updateTable1(table1[i]);
			resizedTable[0][i] = table1[i];
		}
		for(int j=0;j<table2.length;j++){
			if (table2[j] != null) updateTable2(table2[j]);
			resizedTable[1][j] = table2[j];
		}
	}
	
	/**
	 * The helper function used to add to the table.
	 * @param value
	 * @return returns true if added and false if not
	 */
	private PositionValue addToTable(int value){
		PositionValue toAdd = new PositionValue(value,hashCode1(value),hashCode2(value));
		updatePoint(toAdd);
		PositionValue toReturn = toAdd;
		int tablePos = hashCode1(value);//initial table position for table 1
		int curVal = value;
		int loopCount = 0;
		while(curVal != value || loopCount <= table1.length){//fails if it loops back to the original value
			//trying to add to first table
			if(tablePos == hashCode1(curVal)){
				int pos2 = hashCode2(curVal);//position of the value in the second table
				if(table1[tablePos] == null){//checks first table
					
					table1[tablePos] = toAdd;
					return toReturn;
				}else if(table2[pos2] == null){//checks second
					table2[pos2] = toAdd;
					return toReturn;
				}else{//replaces first and repeats while loop with new value
					PositionValue temp = table1[tablePos];
					table1[tablePos] = toAdd;
					toAdd = temp;
					curVal = toAdd.getVal();
					tablePos = hashCode2(curVal);
				}
			}
			//trying to add to second table
			else if(tablePos == hashCode2(curVal)){
				if(table2[tablePos] == null){//checks table
					table2[tablePos] = toAdd;
					return toReturn;
				}else{//if full replaces value and repeats
					PositionValue temp = table2[tablePos];
					table2[tablePos] = toAdd;
					toAdd = temp;
					curVal = toAdd.getVal();
					tablePos = hashCode1(curVal);
				}
			}
			loopCount++;
		}
		return null;//time to resize
	}
	
	/**
	 * finds the value put in
	 * @param value
	 * @return the value if found and -1 if not found.
	 */
	public boolean contains(int value){
		int index1 = hashCode1(value);
		int index2  = hashCode2(value);
		//checks first spot
		if(table1[index1]!=null && table1[index1].getVal() == value){
			return true;
		//checks second spot
		}else if(table2[index2] != null && table2[index2].getVal() == value){
			return true;
		//didn't find it
		}else{
			return false;
		}
	}
	
	/**
	 * hash for the first table
	 * @param value
	 * @return index for the value
	 */
	public int hashCode1(int value){
		return value%table1.length;
	}
	
	/**
	 * hash for the second table
	 * @param value
	 * @return index for the value
	 */
	public int hashCode2(int value){
		return (value/table2.length)%table2.length;
	}
	
	/**
	 * 
	 * @return The first table
	 */
	public PositionValue[] getTable1(){
		return table1;
	}
	
	/**
	 * 
	 * @return The second table
	 */
	public PositionValue[] getTable2(){
		return table2;
	}
	
	public PositionValue[][] resizeHelper(){
		return resizedTable;
	}
	
	public void updateConstants(){
		int boxWidth = HEIGHT / (table1.length + 2);
		xValues.clear();
		xPointValues.clear();
		yPointValues.clear();
		
		xValues.add(boxWidth*2);
		xValues.add(HEIGHT - 3 * (int) (1.2 * boxWidth));
		
		int tileWidth = HEIGHT / (table1.length + 2);
		int shift = ((tileWidth - 4 ) / 4);

		xPointValues.add(xValues.get(0) + shift+ 2);
		xPointValues.add(xValues.get(1) + shift+ 2);

		for (int i = 0; i < table1.length; i++){
			yPointValues.add(((1 + i)*tileWidth) + (tileWidth - shift));
		}

	}
	
	public void updatePoint(PositionValue point){

		point.setStartX(startX);
		point.setStartY(startY);

		point.setCurrentX(startX);
		point.setCurrentY(startY);

		point.setX1(xPointValues.get(0));
		point.setY1(yPointValues.get(point.getIndex1()));
		point.setX2(xPointValues.get(1));
		point.setY2(yPointValues.get(point.getIndex2()));
	}
	
	public void updateTable1(PositionValue point){
		
		point.setStartX(startX);
		point.setStartY(startY);
		
		point.setCurrentX(xPointValues.get(0));
		point.setCurrentY(yPointValues.get(point.getIndex1()));
		
		point.setX1(xPointValues.get(0));
		point.setY1(yPointValues.get(point.getIndex1()));
		point.setX2(xPointValues.get(1));
		point.setY2(yPointValues.get(point.getIndex2()));
		
	}
	
public void updateTable2(PositionValue point){
	
		point.setStartX(startX);
		point.setStartY(startY);
		
		point.setCurrentX(xPointValues.get(1));
		point.setCurrentY(yPointValues.get(point.getIndex2()));
		
		point.setX1(xPointValues.get(0));
		point.setY1(yPointValues.get(point.getIndex1()));
		point.setX2(xPointValues.get(1));
		point.setY2(yPointValues.get(point.getIndex2()));
	}
}