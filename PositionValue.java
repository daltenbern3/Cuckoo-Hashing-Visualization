import java.awt.Graphics;


public class PositionValue {

	/**
	 * @param args
	 */
	int val, x1, x2, y1, y2, index1, index2, startX, startY, currentX, currentY;
	boolean side = true;
	boolean resize = false;
	
	public PositionValue(int val, int index1, int index2) {
		this.val = val;
		this.index1 = index1;
		this.index2 = index2;
	}
	
	public int getIndex1(){
		return index1;
	}

	public int getIndex2(){
		return index2;
	}

	public int getX1(){
		return x1;
	}

	public int getY1(){
		return y1;
	}
	
	public int getCurrentX(){
		return currentX;
	}

	public int getCurrentY(){
		return currentY;
	}

	public int getStartX(){
		return startX;
	}

	public int getStartY(){
		return startY;
	}
	
	public int getX2(){
		return x2;
	}

	public int getY2(){
		return y2;
	}

	public int getVal(){
		return val;
	}
	
	public boolean getResize(){
		return resize;
	}
	
	public void setResize(boolean resize){
		this.resize = resize;
	}
	
	public boolean getSide(){
		return side;
	}
	
	public void setIndex1(int index1){
		this.index1 = index1;
	}

	public void setIndex2(int index2){
		this.index2 = index2;
	}

	public void setX1(int x1){
		this.x1 =  x1;
	}

	public void setY1(int y1){
		this.y1 = y1;
	}
	
	public void setX2(int x2){
		this.x2 =  x2;
	}

	public void setStartY(int startY){
		this.startY = startY;
	}

	public void setStartX(int startX){
		this.startX = startX;
	}

	public void setY2(int y2){
		this.y2 = y2;
	}

	public void setVal(int val){
		this.val = val;
	}
	
	public void setCurrentY(int currentY){
		this.currentY = currentY;
	}

	public void setCurrentX(int currentX){
		this.currentX = currentX;
	}
	
	public void setCurrent(boolean side){
		this.side = side;
	}
	
	public void switchSides(){
		this.side = !side;
	}
	

	public void draw(Graphics page){
			if (val < 10) page.drawString((" " + val), currentX, currentY);   
			else page.drawString(("" + val), currentX, currentY); 
	}
}
