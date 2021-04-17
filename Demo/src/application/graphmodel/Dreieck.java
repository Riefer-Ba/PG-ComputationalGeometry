package application.graphmodel;

public class Dreieck{	
	private Punkt endpkt1; 
	Punkt endpkt2;
	Punkt endpkt3;
	
	public Dreieck(Punkt x, Punkt y, Punkt z) {
		this.setEndpkt1(x);
		this.endpkt2 = y;
		this.endpkt3 = z;
	}
	
	public double radius() {
		// r = (abc)/(4*a)
		double r,a,b,c,flaeche;
		LinienSegment ls = new LinienSegment(getEndpkt1(), endpkt2);
		 a = ls.Laenge(endpkt3, endpkt2);
		 b = ls.getGewicht();
		 c = ls.Laenge(endpkt3, getEndpkt1());
		 r= a*b*c;
		 flaeche = this.Flaecheninhalt(this);
		 return r/(4*flaeche);
	}
	
	public Punkt Mittelpunkt() {
		double TempX[] = { this.getEndpkt1().getX(), this.endpkt2.getX(), this.endpkt3.getX(), this.getEndpkt1().getX(), this.endpkt2.getX()};
		double TempY[] = { this.getEndpkt1().getY(), this.endpkt2.getY(), this.endpkt3.getY(), this.getEndpkt1().getY(), this.endpkt2.getY()};
		double d =0, a = 0, b= 0;
		for( int i =0; i<3 ; i++) {
				d +=  TempX[i]*(TempY[i+1]-TempY[i+2]);
				a += (TempX[i]*TempX[i]+ TempY[i]*TempY[i])*(TempY[i+1]-TempY[i+2]);
				b += (TempX[i]*TempX[i]+ TempY[i]*TempY[i])*(TempX[i+2]-TempX[i+1]);
		}
		d = d*2.0;
		Punkt u = new Punkt((a/d),(b/d));
		return u;
	}
	
	public void printDreieck() {
		System.out.println("("+this.getEndpkt1().getX() +", "+ this.getEndpkt1().getY()+")");
		System.out.println("("+this.endpkt2.getX() +", "+ this.endpkt2.getY()+")");
		System.out.println("("+this.endpkt3.getX() +", "+ this.endpkt3.getY()+")");
        System.out.println("------");
	}
	
	public LinienSegment sharedEdge(Dreieck d2) {
		int counter = 0;
		Punkt Temp1[]= {this.getEndpkt1(), this.endpkt2, this.endpkt3 };
		Punkt Temp2[]= {d2.getEndpkt1(), d2.endpkt2, d2.endpkt3 };
		Punkt temp = null,temp2 = null;
		
		for (int i =0; i<3 ; i++) {
			for (int j =0; j<3 ; j++) {
				if((Temp1[i].getX() == Temp2[j].getX()) && (Temp1[i].getY() == Temp2[j].getY())){			//1.Eckpkt gleich
					if(counter == 0){
						temp = Temp1[i];
						counter ++;
						break;
					}
					else if(counter == 1){	//2. Eckpkt gleich
						temp2= Temp1[i];
						counter++;
						break;
					}
					else if(counter == 2) { //das ist das selbe Dreieck
						return null;
					}
				}
			}
		}
		if(temp == null || temp2 == null) {
			return null;
		}
		LinienSegment ls = new LinienSegment(temp, temp2);
		return ls;	
	}
	
	public double Flaecheninhalt(Dreieck d) { // für Test ob Pkt im Dreieck
		double e= (getEndpkt1().getX()*(endpkt2.getY()-endpkt3.getY()));
		double z= (endpkt2.getX()*(endpkt3.getY()-getEndpkt1().getY()));
		double dr=(endpkt3.getX()*(getEndpkt1().getY()-endpkt2.getY()));
		double flaeche=  Math.abs(1.0/2 * (e+z+dr));
		return flaeche;
	}
	
	
	// zum Test ob Punkt im Dreieck ist. O(1)
	public double FlaechenInhaltMax3(Dreieck d, Punkt p) {
		double newMax = 0;
		double flaeche = 0;
		for ( int i=0; i<3 ; i++) {
			newMax = FlaechenInhalt3(d,p,i);
			if (newMax > flaeche) {
				flaeche = newMax;
			}
	    }
		return flaeche;
	}
	
	public double FlaechenInhalt3(Dreieck d, Punkt p, int i) {
		double flaeche = 0;
		double TempX[] = {getEndpkt1().getX(), endpkt2.getX(), endpkt3.getX(), p.getX()};
		double TempY[] = {getEndpkt1().getY(), endpkt2.getY(), endpkt3.getY(), p.getY()};
		if(i == 1) {
			swap(TempX, 2,3);
			swap(TempY, 2,3);
		}
		else if (i == 2) {
			swap(TempX,1,2);
			swap(TempY,1,2);
		}
		for (int j=0 ; j<4 ; j++) {
			flaeche += (TempY[j] + TempY[(j+1)% 4])* (TempX[j]-TempX[(j+1) %4]);
		}
		return Math.abs(0.5*flaeche);
	}
	
	public boolean imKreis(Punkt p) {
		Punkt m = this.Mittelpunkt();
		LinienSegment r = new LinienSegment(m, p);	
		LinienSegment r1 = new LinienSegment(m, this.getEndpkt1());
		if( (r1.getGewicht() - r.getGewicht()) > 0.0000001) { //rounding error...
			return true;
		}
		else return false;
	}
	
	public void swap(double[] X, int i, int j) {
		double temp = X[j];
		X[j] = X[i];
		X[i] = temp;
	}

	public Punkt getEndpkt1() {
		return endpkt1;
	}

	public void setEndpkt1(Punkt endpkt1) {
		this.endpkt1 = endpkt1;
	}
	
	public Punkt getEndpkt2() {
		return endpkt2;
	}

	public void setEndpkt2(Punkt endpkt2) {
		this.endpkt2 = endpkt2;
	}
	
	public Punkt getEndpkt3() {
		return endpkt3;
	}

	public void setEndpkt3(Punkt endpkt3) {
		this.endpkt3 = endpkt3;
	}
	
}

