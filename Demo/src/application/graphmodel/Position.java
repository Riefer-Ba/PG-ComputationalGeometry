package application.graphmodel;

import application.graphmodel.Position;
import javafx.scene.layout.Pane;

public class Position {

	private double x;
	private double y;
	
	
	public Position(double x, double y) {
		
		this.x = x;
		this.y = y;
		
	}
	
	public Position(Pane world, int radius) {
		
		this(radius + Math.random() * (world.getWidth() - 2*radius), 					//x 
			 radius + Math.random() * world.getHeight() - 2*radius); 					//y
																						// - 2*radius damit punkte nicht auﬂerhalb des fensters liegen		
	}
	
	public double getX() {
		
		return x;
		
	}
	
	public double getY() {
		
		return y;
		
	}
	
public void setX(double xpos) {
		
		 x = xpos;
		
	}
	
	public void setY(double ypos) {
		
		 y = ypos;
		
	}
	
	public double dist(Position other) {
		
		return Math.sqrt(Math.pow(this.x - other.x,2) + 		//x
						 Math.pow(this.y - other.y,2));	  		//y
		
	}
}
