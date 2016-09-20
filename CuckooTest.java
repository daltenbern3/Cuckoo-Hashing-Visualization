import static org.junit.Assert.*;
import org.junit.Test;


public class CuckooTest {

	@Test//(timeout = TIMEOUT)
	public void add(){
		CuckooHashTable cuckoo = new CuckooHashTable(500);
		int[] toAdd = {34,61,72,8,92,13,17,39,41,54,87,3,33,30,79};
		for(int x:toAdd){
			cuckoo.add(x);
		}
		
		Integer Null = (Integer)null;
		Integer[] table1test = {Null,17,34,3,Null,Null,54,39,8,41,Null,Null,92,13,30,79};
		Integer[] table2test = {Null,Null,33,61,72,87,Null,Null,Null,Null,Null,Null,Null,Null,Null,Null};
		
		PositionValue[] table1 = cuckoo.getTable1();
		PositionValue[] table2 = cuckoo.getTable2();
		printTable(table1);
		printTable(table2);
		assertEquals(table1test.length,table1.length);
		assertEquals(table2test.length,table2.length);
		
		for(int i=0;i<table1.length;i++){
			if(table1[i]==null){
				assertEquals(table1test[i],table1[i]);
			}else{
				assertEquals(table1test[i],(Integer)table1[i].getVal());
			}if(table2[i]==null){
				assertEquals(table2test[i],table2[i]);
			}else{
				assertEquals(table2test[i],(Integer)table2[i].getVal());
			}
		}
	}
	
	private void printTable(PositionValue[] table){
		for(PositionValue x:table){
			if(x==null) System.out.println("null");
			else System.out.println(x.getVal());
		}System.out.println();
	}
}
