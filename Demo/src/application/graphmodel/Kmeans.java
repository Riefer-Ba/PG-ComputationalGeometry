package application.graphmodel;

import java.util.ArrayList;
import java.util.List;

public class Kmeans {
	static ArrayList<Point> punkte = new ArrayList<Point>();
	static ArrayList<clusterCentroid> centers = new ArrayList<clusterCentroid>();
	static List<List<Point>> clusters = new ArrayList<List<Point>>();
	static int k = 4;
	static double maxX = 0;
	static double minX = punkte.get(0).getX();
	static double maxY = 0;
	static double minY = punkte.get(0).getY();
	
	
	
	
	
	public static void initialize(ArrayList<Point> punkte ) {
		
		for (Point p : punkte) {
			
			if (maxX < p.getX() ) {maxX = p.getX();}
			if (minX > p.getX() ) {minX = p.getX();}
			if (maxY < p.getY() ) {maxY = p.getY();}
			if (minY > p.getY() ) {minY = p.getY();}
			
		}
		
		for (int i = 0; i<k;i++) {
			
			List<Point> clusterList = new ArrayList<>();
			clusters.add(clusterList);
			
			double xCord = (Math.random() * (maxX-minX)) + minX;
			double yCord = (Math.random() * (maxY-minY)) + minY;
			
			centers.add( new clusterCentroid(xCord, yCord));

			
		}
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
