package application.graphmodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ClusteringRandomKmeans {

	
	ArrayList<clusterCentroid> centers = new ArrayList<clusterCentroid>();	
	ArrayList<clusterCentroid> centersTemp = new ArrayList<clusterCentroid>();
	ArrayList<Point> punkte = new ArrayList<Point>();
	List<List<Point>> clustersFinal = new ArrayList<List<Point>>();
	int k = Math.round(punkte.size()/4);
	
							
	
	
	public ClusteringRandomKmeans(ArrayList<Point> pts) {
		
		this.punkte = pts;
		
	}
	
	
	public void execute() {
		
		createKmeans();
		
	}
	
	public void createKmeans() {
		
		if(k<4) {k = 4;}

		boolean flag = true;
		double maxX = 0;																			
		double minX = punkte.get(0).getX();													
		double maxY = 0;																			
		double minY = punkte.get(0).getY();	
		
		for (Point p : punkte) {																
			
			if (maxX < p.getX() ) {maxX = p.getX();}
			if (minX > p.getX() ) {minX = p.getX();}
			if (maxY < p.getY() ) {maxY = p.getY();}
			if (minY > p.getY() ) {minY = p.getY();}
			
		}				
		
		for (int i = 0; i<k;i++) {	
			
			List<Point> clusterList = new ArrayList<>();
			clustersFinal.add(clusterList);
			
			double xCord = (Math.random() * (maxX-minX)) + minX;
			double yCord = (Math.random() * (maxY-minY)) + minY;
			
			centers.add( new clusterCentroid(xCord, yCord));
			
		}
		
		
		do{
			int trigger = 0;
			clustersFinal.clear();
			centersTemp.clear();
			
			for (int i = 0; i<k;i++) {
				
				List<Point> clusterList = new ArrayList<>();
				clustersFinal.add(clusterList);
			}
				
				for (Point p : punkte) {										//für alle punkte die minimale Distanz zum aktuellen Zentrum berechnen
				
					int cl = 0;
					double minDist = centers.get(0).distance(p); 
				
					for (clusterCentroid c : centers) {
					
						double dist = c.distance(p);
						if (dist < minDist) {
						
							cl = centers.indexOf(c);
							minDist = dist;
						}
					}
				
				clustersFinal.get(cl).add(p);
			}
			for (clusterCentroid c : centers) {										// cluster zentren updaten
				int index = centers.indexOf(c);
				int count = 0;
				double newX = 0;
				double newY = 0;
				double valueX = 0;
				double valueY = 0;
					
				for (Point t : clustersFinal.get(index)) {
					count += 1;
					newX += t.getX();
					newY += t.getY();
				}
					
				valueX = Math.round(newX / count);
				valueY = Math.round(newY / count);
				
				if (Math.round(c.getX()) == valueX && Math.round(c.getY()) == valueY) {		//abrruchbedingung (keine zentren-updates mehr)
					
					trigger +=1;
				}
				
				c.setX(valueX);
				c.setY(valueY);
				
			}
			
			if (trigger == k) flag = false;
				

		}while(flag == true);
		
		Iterator <List<Point>> iterator = clustersFinal.iterator();
		
		while (iterator.hasNext()) {
			
			List<Point> vgl = iterator.next();
			if (iterator.next().isEmpty()) {
				iterator.remove();;
			}
			
		}
		
		
		
	}
	
	public List<List<Point>> getClusters(){
		
		return clustersFinal;
	}
}
