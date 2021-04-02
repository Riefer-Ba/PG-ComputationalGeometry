package application.graphmodel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ClusterMerge {
	ArrayList<LinienSegment> FinalTsp = new ArrayList<LinienSegment>();

	public void execute(List<List<Punkt>> clusters, ArrayList<LinienSegment> mst, ArrayList<double[][]> tsp) {
		
		for (int i=0; i< clusters.size() ;i++) {

		LinienSegment mstEdge =  findClosestCluster(clusters, mst, tsp);
//		//verbindet Cluster, behaltet Tsp Eigenschaften bei.

		}
		
		FinalTsp(tsp, clusters);
	}



	//TODO test if i und j passt
	public LinienSegment findClosestCluster(List<List<Punkt>> clusters, ArrayList<LinienSegment> mst, ArrayList<double[][]> tsp) {
		LinienSegment ConnectingMstEdge = null;
		int a[]= {0,0,0};
		//findet immer connect Kante, solange mind 2 cluster
		for (int i=0 ; i < mst.size(); i++) {
			a = ConnectsTwoClusters(clusters, mst.get(i));
			if( a[0] == 1 ) {
				ConnectingMstEdge = mst.get(i);
				break;
			}
			if( a[0] == 1 && i == mst.size()-1) System.out.println("ups");
		}
				
		ConnectTsp(ConnectingMstEdge,a[1] , a[2], tsp, clusters);
		ClusterCombine(a[1] , a[2] , clusters);
		return ConnectingMstEdge;
		
	}

	
	private int[] ConnectsTwoClusters(List<List<Punkt>> clusters, LinienSegment ls) {
		//Test ob die beiden Endpkte NICHT dem selben Cluster gehÃ¶ren
		//1.Cluster finden
		int a[]= {0,0,0}; 
		for( int i =0 ; i < clusters.size(); i++) {
			
			if ((ls.endpkt1.inCluster(clusters.get(i))) == true) {
							
				for( int j =0 ; j < clusters.size(); j++) {
					if(i != j) {
						
						if ((ls.endpkt2.inCluster(clusters.get(j))) == true) {	
							a[0] =1; a[1] = i; a[2] =j;
							return a;
						}
						else if ((ls.endpkt2.inCluster(clusters.get(i))) == true) {	
							return a;
						}
						
						//2. Cluster finden

						}
					}
				}
				}
			
		return a;
	}
	
	private LinienSegment ConnectTsp(LinienSegment mstEdge, int i, int j, ArrayList<double[][]> tsp, List<List<Punkt>> clusters) {
		LinienSegment bestEdge = null;
		double g0,g1;
		int a,b;
			
		//double[][] temp= new double[10000][100000];
		
		//beachte switchen von i und j in manchen fällen.
		
		a=clusters.get(i).indexOf(mstEdge.endpkt1);
		b=clusters.get(j).indexOf(mstEdge.endpkt2);
		
		if( a == -1 || b == -1) {
			System.out.println("selbes Spiel. index switch");
			System.out.println(a);
			System.out.println(b);
			//swap i,j	
		}
		
		Punkt[] p1=  zweiNachbarn(i, a, tsp, clusters);		
		Punkt[] p2=  zweiNachbarn(j, b, tsp, clusters);		
		
		
		LinienSegment m11 = new LinienSegment(mstEdge.endpkt1, p1[0]);
		LinienSegment m22 = new LinienSegment(mstEdge.endpkt1, p1[1]);
		
		LinienSegment m21 = new LinienSegment(mstEdge.endpkt2, p2[0]);
		LinienSegment m12 = new LinienSegment(mstEdge.endpkt2, p2[1]);
		
		LinienSegment c[] = candidate(p1,p2,tsp, mstEdge); 
		LinienSegment c1 = c[0];
		LinienSegment c2 = c[1];
	
//		System.out.println("@@@@@@");
//		c1.printLs();
//		m11.printLs();
//		m12.printLs();
//		
//		System.out.println("@@@@@@");
//		
//		c2.printLs();
//		m21.printLs();
//		m22.printLs();
//		
//		System.out.println("@@@@@@");
//
		g0= c1.gewicht - m11.gewicht - m12.gewicht;
		g1= c2.gewicht - m21.gewicht - m22.gewicht;
//		
//		System.out.println(g0);
//		System.out.println(g1);
		
		if (g0 < g1) {
			bestEdge = c1;
		}
		else bestEdge =c2;

		bestEdge.printLs();
		
		TspCombine( i, j, tsp, clusters, bestEdge, mstEdge);
			//oder cluster übergeben und dafür a,b,isize,jsize nicht


//		System.out.println("connecting through:");
//		bestEdge.printLs();
		
		
		return bestEdge;
	}
	
	
	private void TspCombine(int i, int j, ArrayList<double[][]> tsp, List<List<Punkt>> clusters, LinienSegment bestEdge, LinienSegment mst) {
		// TODO test
		int iSize = clusters.get(i).size();
		int jSize = clusters.get(j).size();
		int a=clusters.get(i).indexOf(mst.endpkt1);
		int b=clusters.get(j).indexOf(mst.endpkt2);
		
		int c=clusters.get(i).indexOf(bestEdge.endpkt1);
		int d=clusters.get(j).indexOf(bestEdge.endpkt2);
		
		if ( a == -1 ) System.out.println("get fucked");
		if ( c == -1 ) System.out.println("fucked again");
		
		
		//neue Adj Matrix, die die beiden Touren verbindet. Zuerst aufstellen.
		int size=  iSize+jSize;
		double[][] combined = new double[size][size];
		
		
	//	Arrays.fill(combined, 0);
		
		
		
		for (int h =0; h < iSize ;h++) {
			for (int g =0; g < iSize ;g++) {
				combined[h][g] = tsp.get(i)[h][g]; 
			}
		}
		System.out.println(combined.length);
		int x=0;int y=0;
		for (int h = iSize; h < iSize+jSize ;h++, x++) { //wenn index fehler, i+j-1!
			for (int g =iSize; g < iSize+jSize ;g++, y++) {
				
				System.out.println(h+" "+g+" "+x+" "+y);
				combined[h][g] = tsp.get(j)[x][y]; // j hat kein h und g!!! will be outofbounds
				if( g == iSize+jSize -1) {
					y=-1;
				}
			}
		}
		//setup complete
		
		printAdj(tsp.get(i));
		printAdj(tsp.get(j));
		
		mst.printLs();
		bestEdge.printLs();
		
		//neue Nachbarn setzen:
		//1. mst eckpkte verbinden.
		combined[a][b+iSize] = 1;
		combined[b+iSize][a] = 1;
		
		//2.mst vom jeweiligen bestedge eckpkt lösen
		combined[a][c] = 0;
		combined[c][a] = 0;
		
		combined[b+iSize][d+iSize] = 0;
		combined[d+iSize][b+iSize] = 0;
		
		//bestedge verbinden
		combined[c][d+iSize] = 1;
		combined[d+iSize][c] = 1;
		
		tsp.remove(i);
	
		if( i < j ) {
			tsp.remove(j-1);
		}
		else tsp.remove(j);
		
		
		tsp.add(combined);

	}




	private Punkt[] zweiNachbarn(int i, int a, ArrayList<double[][]> tsp, List<List<Punkt>> clusters) {
		Punkt[] zwei = new Punkt[2];
		int counter =0;
		
		for(int p= 0; p < tsp.get(i).length ; p++) {
			if (tsp.get(i)[a][p] == 1 && counter == 0) {
				zwei[0] = clusters.get(i).get(p);
				counter++;
				}
			else if (tsp.get(i)[a][p] == 1 && counter == 1) {
				zwei[1] = clusters.get(i).get(p);
				}
		}
		zwei[0].printPunkt();
		zwei[1].printPunkt();
		
		return zwei;
	}



	public void ClusterCombine(int i, int j, List<List<Punkt>> clusters) {
		List<Punkt> temp= new ArrayList<Punkt>();
		
		clusters.get(i).addAll(clusters.get(j));
		temp = clusters.get(i);
		
		clusters.remove(i);
		if( i < j ) {
			clusters.remove(j-1);
		}
		else clusters.remove(j);
		
		clusters.add(temp);
	}

	private LinienSegment[] candidate(Punkt[] p1, Punkt[] p2 , ArrayList<double[][]> tsp, LinienSegment mst) {
		LinienSegment c[] = {null, null};

		// in i und j die 2 endpkte finden tsp.get(i)
		
		
		//TODO test if richtigen Segmente
		LinienSegment c11 = new LinienSegment(p1[0], p2[0]);
		LinienSegment c12 = new LinienSegment(p1[0], p2[1]);
		
		LinienSegment c21 = new LinienSegment(p1[1], p2[0]);
		LinienSegment c22 = new LinienSegment(p1[1], p2[1]);
		
		
		
		 if( kreuzungsfreiBool(c11, mst) == true) {
			 c[0]= c11;
		 }
		 else if( kreuzungsfreiBool(c12, mst) == true) {
			 c[0]= c12;
		 }
		 else {
			 System.out.println("both crossing mst. :/");
			 c11.printLs();
			 c12.printLs();
			 mst.printLs();
			 c[0]= c11;
			 
		 }
				
		 
		 if( kreuzungsfreiBool(c21, mst) == true) {
			 c[1]= c21;
		 }
		 else if( kreuzungsfreiBool(c22, mst) == true) {
			 c[1]= c22;
		 }
		 else {	//ein else weg!
			 System.out.println("both crossing mst. :/");
			 c21.printLs();
			 c22.printLs();
			 mst.printLs();
			 c[1]= c22;
		 }

		return c;
	}
	
	//TODO test ob das stimmt
	private boolean kreuzungsfreiBool(LinienSegment c1, LinienSegment mst) {
		//check if c1 und mst sich kreuzen, wenn ja kreuzen sich c2 und mst nicht
		
		double t,u;
		
		double nenner1 = (c1.endpkt2.getX() - c1.endpkt1.getX())*(mst.endpkt2.getY()- mst.endpkt1.getY());
		double nenner2 = (c1.endpkt2.getY() - c1.endpkt1.getY())*(mst.endpkt2.getX()- mst.endpkt1.getX());
		

	//	double t1= (c1.endpkt1.getX() - mst.endpkt1.getX())*(mst.endpkt1.getY()- mst.endpkt2.getY());
	//	double t2= (c1.endpkt1.getY() - mst.endpkt1.getY())*(mst.endpkt1.getX()- mst.endpkt2.getX());
		
		double t1= (mst.endpkt2.getX() - mst.endpkt1.getX())*(c1.endpkt1.getY()- mst.endpkt1.getY());
		double t2= (mst.endpkt2.getY() - mst.endpkt1.getY())*(c1.endpkt1.getX()- mst.endpkt1.getX());
		

		t = (t1 - t2)/(nenner1 - nenner2);
	//	System.out.println(t);
		
		double u1= (c1.endpkt2.getX() - c1.endpkt1.getX())*(c1.endpkt1.getY()- mst.endpkt1.getY());
		double u2= (c1.endpkt2.getY() - c1.endpkt1.getY())*(c1.endpkt1.getX()- mst.endpkt1.getX());

		u = (u1-u2)/(nenner1 - nenner2);
	//	System.out.println(u);
		
		if (u < 1.0 && u > 0.0 && t < 1.0 && t > 0.0) return false;	//<= gibt false auch wenn in einem endpkt schneiden </> mit =?
		
		return true;
		
	}

	

	private void FinalTsp(ArrayList<double[][]> tsp, List<List<Punkt>> clusters) {
		System.out.println(tsp.size());
		System.out.println(tsp.get(0).length);
		printAdj(tsp.get(0));
		for (int i=0; i< tsp.get(0).length ; i++) {
			for (int j=i; j< tsp.get(0).length ; j++) {
				if (tsp.get(0)[i][j] == 1) {
					FinalTsp.add(new LinienSegment(clusters.get(0).get(i), clusters.get(0).get(j)));
				}
			}

		}

		
		System.out.println("final tsp printing @@@@@@");
		
		System.out.println(tsp.size());
		System.out.println(FinalTsp.size());
		
		for (int i=0; i< FinalTsp.size(); i++) {
			FinalTsp.get(i).printLs();
		}
		
	}

	public void printAdj(double[][] AdjMatrix ) {
		for ( int i=0; i<AdjMatrix.length ;i++) {
			for ( int j=0; j<AdjMatrix.length ;j++) {
				System.out.print(": "+ AdjMatrix[i][j] );
				if( j == AdjMatrix.length -1 ) {
					System.out.println(" ");
				}
			}
		}
	}


	public ArrayList<LinienSegment> getTsp(){
		
		return FinalTsp;
	}

}

