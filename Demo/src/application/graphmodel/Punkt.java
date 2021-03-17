package application.graphmodel;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class Punkt{
    private double x;
    private double y;
    ArrayList <Punkt> reachable = new ArrayList<Punkt>();
   
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
    
    
}