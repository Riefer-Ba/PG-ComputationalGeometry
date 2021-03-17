package application.graphmodel;

public class clusterCentroid {

	
	public double xCord;
	public double yCord;
	
	
	public clusterCentroid(double x, double y) {
		
		xCord = x;
		yCord = y;
		
		
	}
	
	
	
	
	
	public double getX() {
		
		return this.xCord;
	}
	
	
	public double getY() {
		
		return this.yCord;
	}
	
	
	public void setX(double x) {
		
		xCord = x;
		
	}
	
	public void setY(double y) {
		
		yCord = y;
	}
	
	public double distance (Point other) {
		
		return Math.sqrt(Math.pow(xCord - other.getX(),2) + 		//x
				 Math.pow(yCord - other.getY(),2));	  				//y
	}
}
