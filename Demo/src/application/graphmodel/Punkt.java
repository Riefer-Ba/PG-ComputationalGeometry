package application.graphmodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Punkt{
    private double x;
    private double y;
    ArrayList <Punkt> reachable = new ArrayList<Punkt>();
    private boolean visited;
    private ArrayList<Punkt> nb = new ArrayList<Punkt>();
   
    public Punkt(double x, double y){
        this.x=x;
        this.y=y;
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
   
    public void printPunkt() {
    	System.out.println("("+this.getX() +", "+ this.getY()+")");
        System.out.println("------");
    }

    public static double findMax(ArrayList<Punkt> p) {
    	//hier gehe ich davon aus das es keine negativen Koordinaten gibt??
		double max =0;
		for (int i=0; i< p.size(); i++) {
			if (max < p.get(i).getX()) {
				max = p.get(i).getX();
			}
			if (max < p.get(i).getY()) {
				max = p.get(i).getY();
			}
		}
		return max;
	}
    
    public void addReachable(Punkt p) {
    	
    	this.reachable.add(p);
    	
    }
    
    public ArrayList<Punkt> getReachable(){
    	
    	return reachable;
    }
    
 
    public boolean getvisited() {
    	
    	return visited;
    }
    
    public void setvisited() {
    	
    	visited = true;
    }
    
    public double getDistance (Punkt other) {
    	
    	return Math.sqrt(Math.pow(this.x - other.x,2) + 		//x
				 Math.pow(this.y - other.y,2));
    	
    }

	public ArrayList<Punkt> getNb() {
		return nb;
	}

	public void addNb(Punkt n) {
		this.nb.add(n);
	}
	
	public boolean inCluster(List<Punkt> clusters) {
		for(int i =0 ; i< clusters.size(); i++) {
			if (clusters.get(i).getX() == this.getX()) {
				if (clusters.get(i).getY() == this.getY()) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean samePoint(Punkt other) {
		if ((this.getX() == other.getX()) &&
			(this.getY() == other.getY()) ) {
			return true;
		}
		return false;
	}
}
