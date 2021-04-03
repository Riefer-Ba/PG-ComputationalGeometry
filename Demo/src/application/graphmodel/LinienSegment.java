package application.graphmodel;

import java.util.ArrayList;
import java.util.Comparator;

public class LinienSegment implements Comparable<LinienSegment>{
	Punkt endpkt1; 
	Punkt endpkt2;
	public double gewicht;
	
	 

	
	public LinienSegment(Punkt x, Punkt y) {
		this.endpkt1 = x;
		this.endpkt2 = y;
		this.setGewicht(Laenge(x,y));
	}
	
	public Punkt Mittelpunkt() {
		double a = (this.endpkt1.getX() + this.endpkt2.getX()) *0.5;
		double b = (this.endpkt1.getY() + this.endpkt2.getY()) *0.5;
		Punkt m = new Punkt(a,b);
		return m;
	}
	
	public void printLs() {
		System.out.println("("+this.endpkt1.getX() +", "+ this.endpkt1.getY()+")");
		System.out.println("("+this.endpkt2.getX() +", "+ this.endpkt2.getY()+")");
        System.out.println("------");
	}
	
	public double Laenge(Punkt a , Punkt b) {
		
		double temp1= Math.pow((a.getX() - b.getX()), 2);
		double temp2= Math.pow((a.getY() - b.getY()), 2);
		
		double laenge = Math.sqrt(temp1 + temp2);
		return laenge;
	}
	
	public Punkt getEndpkt1(){
		return endpkt1;
	}
	
	public Punkt getEndpkt2(){
		return endpkt2;
	}
	
	public boolean sameEdge(LinienSegment l) {
		if ((this.endpkt1.getX() == l.endpkt1.getX()) &&
			(this.endpkt2.getX() == l.endpkt2.getX()) ) {
				if ((this.endpkt1.getY() == l.endpkt1.getY()) &&
					(this.endpkt2.getY() == l.endpkt2.getY())) {
					return true;
				}
		}
		else if((this.endpkt1.getX() == l.endpkt2.getX()) &&
			(this.endpkt2.getX() == l.endpkt1.getX()) ) {
				if ((this.endpkt1.getY() == l.endpkt2.getY()) &&
					(this.endpkt2.getY() == l.endpkt1.getY())) {
					return true;
				}
		}
		else return false;

		
		return null != null;
	}
	
	public Punkt leftoverPoint(Dreieck d){
		Punkt Temp1[]= {this.endpkt1, this.endpkt2};
		Punkt Temp2[]= {d.getEndpkt1(), d.endpkt2, d.endpkt3};
		for (int i =0; i<3 ; i++) {
			if(!((Temp1[0].getX() == Temp2[i].getX()) && (Temp1[0].getY() == Temp2[i].getY()))
				&& !((Temp1[1].getX() == Temp2[i].getX()) && (Temp1[1].getY() == Temp2[i].getY()))) {
				return Temp2[i];
			}
		}
		return null;
	}



	public double getGewicht() {
		return gewicht;
	}

	public void setGewicht(double gewicht) {
		this.gewicht = gewicht;
	}

	@Override
	public int compareTo(LinienSegment c) {
		 if(this.getGewicht()<c.getGewicht())
	          return -1;
	    else if(c.getGewicht()<this.getGewicht())
	          return 1;
	    return 0;
	}


	public void SortMstLaenge(ArrayList<LinienSegment> mst) {
		int minPos;
		int einf =0;
		do{
			minPos = einf;
			for (int i= einf ; i< mst.size() ; i++) {
				if( mst.get(i).gewicht < mst.get(minPos).gewicht) {
					minPos = i;
				}
			}
			swap(mst, minPos, einf);
			einf ++;
		} while (einf < mst.size());
	}

	public void swap(ArrayList<LinienSegment> mst, int i, int j) {
		LinienSegment temp = mst.get(j);
		mst.set(j, mst.get(i));
		mst.set(i, temp);
	}
	

	public double TourLaenge(ArrayList<LinienSegment> ls) {
		double tour =0;
		for (LinienSegment l : ls){
			tour += l.gewicht;
		}
		return tour;
	}
	
}
