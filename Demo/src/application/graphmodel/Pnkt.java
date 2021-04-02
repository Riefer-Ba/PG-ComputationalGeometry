package application.graphmodel;

import java.util.ArrayList;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.Locale;
import java.util.Scanner;

public class Pnkt {
	
	
	
	////////////////Delaunay-Ordnung
	public static class Arrayind{
		private ArrayList<Pnkt> ar;
		private int i;
		public Arrayind(ArrayList<Pnkt> pkts, int k) {
			i=k;
			ar=new ArrayList<Pnkt>();
			for(int l=i-1;l>=0;l--) {
				ar.add(pkts.get(l));
			}
			for(int l=pkts.size()-1; l>i;l-- ) {
				ar.add(pkts.get(l));
			}
		}
	}
	/////////////////
	
	
	private double x;
    private double y;
    private double z;
    private int n;
    public Pnkt(double x, double y, int n){
        this.x=x;
        this.y=y;
        this.n=n;
        z=x*x+y*y;
    }
    public Pnkt(double x, double y, double z){
        this.x=x;
        this.y=y;
        this.z=z;
    }
    public Pnkt(double x, double y){
        this.x=x;
        this.y=y;
    }
    public double getX(){
        return x;
    }
    public double getY(){
        return y;
    }
    public double getZ(){
        return z;
    }
    public int getN() {
    	return n;
    }
    
    ///////////////Delaunay-Test
    private void norm() {
    	double nor =this.dot(this);
    	if(nor!=0) {
    	this.x=this.x/nor;
    	this.y=this.y/nor;
    	this.z=this.z/nor;
    }}
    public double dot(Pnkt pnkt) {
    	return this.x*pnkt.x+this.y*pnkt.y+this.z*pnkt.z;
    }
    public double dist(Punkt p) {
    	return Math.sqrt((this.getX()-p.getX())*(this.getX()-p.getX())+(this.getY()-p.getY())*(this.getY()-p.getY()));
    }
    /////////////////
    
    
    public void print() {
    	System.out.println("Knoten "+n+":");
    	System.out.println(x +", "+ y);
    }
    public static ArrayList<Pnkt> lesen(String Datei){
        
        ArrayList<Pnkt> liste= new ArrayList<Pnkt>();
        Scanner scanner;
        try {
            scanner = new Scanner(new File(Datei),
                    "UTF-8");
            scanner.useLocale(Locale.GERMANY);
            double x,y;
            int n;
           
            while (scanner.hasNext()) {
                n=Integer.parseInt(scanner.next())-1;
                if(scanner.hasNext()){
                    x= Double.parseDouble(scanner.next());
                    if(scanner.hasNext()){
                        y=Double.parseDouble(scanner.next());
                    }
                    else{
                        throw(new FileNotFoundException());
                    }
                }
                else{
                    throw(new FileNotFoundException());
                }
                liste.add(new Pnkt(x,y,n));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
            System.out.println("Die Datei wurde nicht gefunden oder aber es gibt eine Koordinate zu wenig. BITTE ÜBERPTÜFEN!");
        }
        return liste;
    }
 public static ArrayList<Pnkt> TnmLesen(String Datei){
        
        ArrayList<Pnkt> liste= new ArrayList<Pnkt>();
        Scanner scanner;
        try {
            scanner = new Scanner(new File(Datei),
                    "UTF-8");
            scanner.useLocale(Locale.GERMANY);
            double x,y;
            int n;
           
            while (scanner.hasNext()) {
                n=Integer.parseInt(scanner.next());
                if(scanner.hasNext()){
                    x= Double.parseDouble(scanner.next());
                    if(scanner.hasNext()){
                        y=Double.parseDouble(scanner.next());
                    }
                    else{
                        throw(new FileNotFoundException());
                    }
                }
                else{
                    throw(new FileNotFoundException());
                }
                liste.add(new Pnkt(x,y,n));
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
            System.out.println("Die Datei wurde nicht gefunden oder aber es gibt eine Koordinate zu wenig. BITTE ÜBERPTÜFEN!");
        }
        return liste;
    }
    
    
    public static ArrayList<Integer> tourLesen (String Datei){
    	ArrayList<Integer> liste= new ArrayList<Integer>();
        Scanner scanner;
        try {
            scanner = new Scanner(new File(Datei),
                    "UTF-8");
            scanner.useLocale(Locale.GERMANY);
            
            int n;
           
            while (scanner.hasNext()) {
                n=Integer.parseInt(scanner.next());
               
                liste.add(n);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            
            e.printStackTrace();
            System.out.println("Die Datei wurde nicht gefunden!");
        }
        return liste;
    
    }
    
    
    
    /////////////Delaunay-Ordnung
    public static Pnkt[] OP(ArrayList<Pnkt> punkte, int p, int q) {
    	
    	Pnkt[] pkt= new Pnkt[punkte.size()];
    	p--;
    	q--;
    	if(p==q) {
    		System.out.println("Ein Punkt folgt auf sich selber. Problem");
    		return pkt;
    	}
    	
    	double x= punkte.get(p).x-punkte.get(q).x;
    	double y= punkte.get(p).y-punkte.get(q).y;
    	double z= punkte.get(p).z-punkte.get(q).z;
    	
    	Pnkt u1,u2;
    	//Aufgrund der Beschaffenheit des Paraboloiden können nicht x und y 0 sein. 
    	if(x!=0) {
    		u1= new Pnkt(-z/x,0.0,1.0);
    		u2= new Pnkt(-x*y/(z*z+x*x),1.0,-y*z/(z*z+x*x));
    	}
    	else if (y!=0) {
    		u1= new Pnkt(0.0,-z/y,1.0);
    		u2= new Pnkt(1,-x*y/(y*y+z*z),-x*z/(y*y+z*z));
    	}
    	else {
    		System.out.println("Problem. Dieser Fall sollte nie eintreten.");
    		u1=new Pnkt(0,0,0.0);
    		u2=new Pnkt(0,0,0.0);
    	}
    	u1.norm();
    	u2.norm();
    	Pnkt e3= new Pnkt(punkte.get(p).x,punkte.get(p).y,punkte.get(p).z+1.0); 
    	pkt[1]= new Pnkt(e3.dot(u1),e3.dot(u2));
    	pkt[0]=new Pnkt(punkte.get(p).dot(u1),punkte.get(p).dot(u2));
    	int k=2;
    	
    	for(int i=0; i< punkte.size();i++) {
    		if(i!=p && i!=q) {
    			pkt[k]=new Pnkt(punkte.get(i).dot(u1),punkte.get(i).dot(u2));
    			k++;
    		}
    	}
    	return pkt;
    }
    public int orient(Pnkt start, Pnkt ziel) {
    	int dir;
    	double a= ziel.x-start.x;
    	double b=this.x-start.x;
    	double c= ziel.y-start.y;
    	double d= this.y-start.y;
    	if(a*d>b*c) {
    		dir=1;
    	}
    	else if (a*d< b*c) {
    		dir=-1;
    	}
    	else {
    		dir=0;
    	}
    	return dir;
    }
    public static Arrayind sortieren(Pnkt[] pkt) {
    	ArrayList<Pnkt> pkts=sort(pkt);
    	int k=0;
    	while(pkts.get(k)!=pkt[1]) {
    		k++;
    	}
    	return new Arrayind(pkts, k);
    }
    public static ArrayList<Pnkt> sort(Pnkt[] pkt) {
    	AVLBaum baum = new AVLBaum();
    	for (int i=1; i<pkt.length; i++) {
    		baum.einfuegen(pkt[i], pkt[0]);
    	}
    	ArrayList<Pnkt> pkts=baum.uebertragen();
    	
    	return pkts;
    	
    }
    public static int delaunayOrd(ArrayList<Pnkt> punkte, int start, int ziel) {
		Pnkt[] p=Pnkt.OP(punkte, start, ziel);
    	Arrayind a= Pnkt.sortieren(p);
    	
    	if(a.i==0) {
    		return 0;
    	}
		for (int k=0; k<=punkte.size()/2;k++) {
			for (int j=0; j<a.i; j++) {
				if(j+k+1>=a.ar.size()) {
					return k;
				}
				if(a.ar.get(k+j+1).orient(p[0], a.ar.get(j))==-1) {
					return k;
				}
			}
		}
		return -1;
	}
    ////////////////////////
    
    
   
    public static void randSample(int anzahl, int max, int nummer) {
    	if (anzahl>max*max/2) {
    		System.out.println("Bitte bei dieser Punktzahl bereich vergrößern");
    	}
    	else {
    		try {
  		      File myObj = new File("Sample"+anzahl+"_"+max+"_"+nummer);
  		      if (myObj.createNewFile()) {
  		        System.out.println("File created: " + myObj.getName());
  		      } else {
  		        System.out.println("File already exists.");
  		      }
  		    } catch (IOException e) {
  		      System.out.println("An error occurred.");
  		      e.printStackTrace();
  		    }
  	    try {
  	        FileWriter myWriter = new FileWriter("Sample"+anzahl+"_"+max+"_"+nummer);
  	        for (int i=0; i<anzahl;i++) {
  	        	boolean[][] schonbelegt=new boolean[max][max];
  	        	int x=(int) (max*Math.random());
  	        	int y=(int) (max*Math.random());
  	        	double xkoord= ((double) x) +Math.random()-0.5;
  	        	double ykoord= ((double) y) +Math.random()-0.5;
  	        	if(schonbelegt[x][y]!=true) {
  	        		schonbelegt[x][y]=true;
  	        		myWriter.write(i+" "+xkoord+" "+ykoord+"\n");
  	        	}
  	        	else {
  	        		i--;
  	        	}
  	        		
  	        	
  	        }
  	        myWriter.close();
  	        System.out.println("Successfully wrote to the file.");
  	      } catch (IOException e) {
  	        System.out.println("An error occurred.");
  	        e.printStackTrace();
  	      }
    	}
    }
}

