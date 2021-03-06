package application.graphmodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import javafx.scene.control.TextField;

public class ClusteringRandomKmeans {

	

	List<Point> punkte = new ArrayList<Point>();
	List<List<Point>> clustersFinal = new ArrayList<List<Point>>();
	int k = Math.round(punkte.size()/4);
	ArrayList<clusterCentroid> centers = new ArrayList<clusterCentroid>();	
	int clSize;
	
	boolean bed;						
	boolean init = false;
	
	public ClusteringRandomKmeans(List<Point> pts, boolean choice, int size) {
		
		this.punkte = pts;
		this.bed = choice;
		this.clSize = size;
	}
	
	
	public void execute() {
		
		createKmeans(punkte, k);
		init = true;
		rec(clustersFinal);
		postOpt();
		
	}
	
	public void createKmeans(List<Point> punkteT, int centerCount) {
		
		//ArrayList<clusterCentroid> centers = new ArrayList<clusterCentroid>();	
		ArrayList<clusterCentroid> centersTemp = new ArrayList<clusterCentroid>();
		
		if(centerCount>4) {centerCount = 4;}
		if(centerCount < 2) {centerCount = 2;}

		boolean flag = true;
		double maxX = 0;																		
		double minX = punkteT.get(0).getX();													
		double maxY = 0;																			
		double minY = punkteT.get(0).getY();	
		
		for (Point p : punkteT) {																
			
			if (maxX < p.getX() ) {maxX = p.getX();}
			if (minX > p.getX() ) {minX = p.getX();}
			if (maxY < p.getY() ) {maxY = p.getY();}
			if (minY > p.getY() ) {minY = p.getY();}
			
		}				
		
		for (int i = 0; i<centerCount;i++) {	
			
			if (bed == false || init == true) {
				List<Point> clusterList = new ArrayList<>();
				clustersFinal.add(clusterList);
			
				double xCord = (Math.random() * (maxX-minX)) + minX;
				double yCord = (Math.random() * (maxY-minY)) + minY;
			
				centers.add( new clusterCentroid(xCord, yCord));
			}
			
			else {
				double[] cord  = neuerCentroid(punkteT, centers);
				
				centers.add(new clusterCentroid(cord[0], cord[1]));
				
			}
		}
		
		
		do{
			int trigger = 0;
			clustersFinal.clear();
			centersTemp.clear();
			
			for (int i = 0; i<centerCount;i++) {
				
				List<Point> clusterList = new ArrayList<>();
				clustersFinal.add(clusterList);
			}
				
				for (Point p : punkteT) {										//f????r alle punkte die minimale Distanz zum aktuellen Zentrum berechnen
				
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
			if (vgl.isEmpty()) {
				iterator.remove();
			}
			
		}
		
		
		
	}
	
	public List<List<Point>> rec (List<List<Point>> cFinal){
		boolean trigger = false;
		
		

		
		do {
			List<List<Point>> newCl = new ArrayList<List<Point>>();
			Iterator <List<Point>> iterator = cFinal.iterator();
			//List<Point> tmp = null;
		
			while (iterator.hasNext()) {
			
				List<Point> vgl = iterator.next();
				if (vgl.size() >clSize) {
				
					
					//tmp = vgl;
					ClusteringRandomKmeans Temp = new ClusteringRandomKmeans(vgl, bed,clSize);
					Temp.createKmeans(vgl,2);
					List<List<Point>> tempCl = Temp.getClusters();
				
					for(List<Point> cl : tempCl) {
					
						newCl.add(cl);
					}
				
					iterator.remove();
					//centers.remove(clustersFinal.indexOf(vgl));
				}
			
			}
			trigger = true;
			
			for (List<Point> l : newCl) {
				
				clustersFinal.add(l);
			}
			
			for (List<Point> cluster : clustersFinal) {
				
				if (cluster.size()> clSize){
					System.out.println("cluster gesplittet");
					trigger = false;
					break;
					
				}
				
			}
			
			
			
			
			
			
		} while(trigger == false);
		
		
		return clustersFinal;
	}
	
		private double[] neuerCentroid(List<Point> punkte, ArrayList<clusterCentroid> centers) {
		double[] cord = {0,0};
		double minDist = 10000;
		ArrayList<Double> DistArray = new ArrayList<Double>();
		ArrayList<Double> DuplikatedDist = new ArrayList<Double>();
		double globalMin =0;
		
		for(int i=0; i< punkte.size(); i++) {
			for(int k=0; k< centers.size(); k++) { //sucht den min Abstand 
				double temp = Math.pow(centers.get(k).distance(punkte.get(i)), 2);
				if(temp < minDist) minDist=temp; 
			}
			DistArray.add(minDist);
			
			if(globalMin == 0) {
				globalMin=minDist; //gloablMin zur anpassung der Wahrscheinlichkeit
			}
			else if (globalMin > minDist){
				globalMin = minDist;
			}
			
			minDist = 10000; // reset minDist
			
		}
		
		for(int i=0; i< punkte.size(); i++) {
			double repeat = DistArray.get(i)/globalMin;	//D^2 fuer alle minDist*Einheiten einmal einfuegen
			for(int k=0; k < repeat ; k++) {			
				DuplikatedDist.add(DistArray.get(i));
			}
		}
		
		
		Random rnd = new Random();
		double r = DuplikatedDist.get(rnd.nextInt(DuplikatedDist.size()));
		
		cord[0]=punkte.get(DistArray.indexOf(r)).getX();
		cord[1]=punkte.get(DistArray.indexOf(r)).getY(); //fixed ; double x macht kein sinn.
		
		return cord;
	}
	
	public List<List<Point>> getClusters(){
		
		return clustersFinal;
	}
	
	
	public List<List<Point>> postOpt(){
		
		
		List<List<Point>> tempCl = getClusters();
		
		for (List<Point> cluster : tempCl) {
			
			if (cluster.size() < 3) {
				
				for(Point p : cluster) {
					double nearest = 10000;
					Point newP = p;
					
					for(Point t : punkte) {
						
						double dist = Math.sqrt(Math.pow(p.getX() - t.getX(),2) +  Math.pow(p.getY() - t.getY(),2));
						if(dist < nearest && t != p && !cluster.contains(t)) {
							
							nearest = dist;
							newP = t;	
						}
					}
					//tempCl.get(cluster.indexOf(newP)).add(p);
					
					for (List<Point> clstr : tempCl) {
						
						if(clstr.contains(newP)) {
							
							clstr.add(p);
						}
					}
				}
				
			}
			
		}
		
		Iterator <List<Point>> iterator = tempCl.iterator();
		
		while (iterator.hasNext()) {
			
			List<Point> delC = iterator.next();
			
			if(delC.size() < 3) {
				
				iterator.remove();
			}
			
		}
		
		clustersFinal = tempCl;
		return clustersFinal;
	}
}
