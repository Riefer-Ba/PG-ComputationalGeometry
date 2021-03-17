package application.graphmodel;

import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Point {
	
	public static int radius = 5;
	private Position loc;
	public Circle c;
	private Pane world;
	
	double orgSceneX, orgSceneY;
    double orgTranslateX, orgTranslateY;
	
	public Point(Pane world) {
		
		this.loc = new Position(world, radius);
		this.setC(new Circle(radius, Color.BLUE));
		this.getC().setCursor(Cursor.HAND);
		c.setStroke(Color.BLACK);							// Darstellung
		world.getChildren().add(getC());							// grafische Einbindung
		
	}	


	public Position getPos() {
		
		return loc;
		
	}
	
	public double getX() {
		
		return loc.getX();
	}
	
	public double getY() {
		
		return loc.getY();
	}
	
	
	public void setPos(double xpos, double ypos) {
		
		loc.setX(xpos);
		loc.setY(ypos);
		
	}
	
	
	public void draw() { 									// optische Darstellung
		
		c.setRadius(radius);
		c.setTranslateX(loc.getX());
		c.setTranslateY(loc.getY());
	}
	
  
	public void move () {
		
		
		this.c.setOnMousePressed(circleOnMousePressedEventHandler);      
		this.c.setOnMouseDragged(circleOnMouseDraggedEventHandler);
	}
	
	
	public void showCoords() {
		
		this.c.setOnMouseEntered(mouseOverCoords);
	}

	
	
	public Circle getC() {
		return c;
	}


	public void setC(Circle c) {
		this.c = c;
	}



	EventHandler<MouseEvent> circleOnMousePressedEventHandler = 
	        new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent t) {
	        	if (t.isControlDown() == true) {
	            orgSceneX = t.getSceneX();
	            orgSceneY = t.getSceneY();
	            orgTranslateX = ((Circle)(t.getSource())).getTranslateX();
	            orgTranslateY = ((Circle)(t.getSource())).getTranslateY();
	        	}
	        }
	    };
	    
	    
	    EventHandler<MouseEvent> circleOnMouseDraggedEventHandler = 
	        new EventHandler<MouseEvent>() {

	        @Override
	        public void handle(MouseEvent t) {
	        	if (t.isControlDown() == true) {
	        		double offsetX = t.getSceneX() - orgSceneX;
	        		double offsetY = t.getSceneY() - orgSceneY;
	        		double newTranslateX = orgTranslateX + offsetX;
	        		double newTranslateY = orgTranslateY + offsetY;
	        		loc.setX(newTranslateX);
	        		loc.setY(newTranslateY);
	            
	        		((Circle)(t.getSource())).setTranslateX(newTranslateX);
	        		((Circle)(t.getSource())).setTranslateY(newTranslateY);
	            }
	        }
	    };
	    
		
	    EventHandler<MouseEvent> mouseOverCoords = new EventHandler<MouseEvent> () {
	    	
	    		
	    		@Override
	    		public void handle(MouseEvent t) {
	    			 Tooltip cord = new Tooltip(getX() +  " | " + getY());
	    		        Tooltip.install(c, cord);
	    			
	    		}
	    	
	    	
	
	  		};
  
	    

		
		    
	    
}


