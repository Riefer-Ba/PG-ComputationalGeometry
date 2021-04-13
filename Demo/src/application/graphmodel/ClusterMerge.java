package application.graphmodel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;

public class ClusterMerge {
	ArrayList<LinienSegment> FinalTsp = new ArrayList<LinienSegment>();
	ArrayList<double[][]> tspLinked = new ArrayList<double[][]>();
	
	
	public void execute(List<List<Punkt>> clusters, ArrayList<LinienSegment> mst, ArrayList<double[][]> tsp) {
		
		mst.get(0).SortMstLaenge(mst);
		int m = clusters.size() -1 ;
		for (int i=0; i< m ;i++) { //-1?
			
			
		LinienSegment mstEdge =  findClosestCluster(clusters, mst, tsp);
//		//verbindet Cluster, behaltet Tsp Eigenschaften bei.

		}
		
		FinalTsp(tsp, clusters);
	}
	
	public void execute(List<List<Punkt>> clusters, ArrayList<LinienSegment> mst, List<LinkedList<Punkt>> tsp) {
		for (int i=0; i<clusters.size(); i++) {
			setupAdjMatrix(clusters.get(i), tsp.get(i));
		}
		mst.get(0).SortMstLaenge(mst);

		int m = clusters.size() -1 ;
		
		for (int i=0; i< m ;i++) {
			
		LinienSegment mstEdge =  findClosestCluster(clusters, mst, tspLinked);
//		//verbindet Cluster, behaltet Tsp Eigenschaften bei.
		
		
		//PostOptKreuzungsCheck() //TODO
		
		
		}
		
		FinalTsp(tspLinked, clusters);
	}



	//TODO test if i und j passt
	public LinienSegment findClosestCluster(List<List<Punkt>> clusters, ArrayList<LinienSegment> mst, ArrayList<double[][]> tsp) {
		LinienSegment ConnectingMstEdge = null;
		int a[]= {0,0,0};
		//findet immer connect Kante, solange mind 2 cluster
		//TODO gefühl manchmal keine mst Kante?
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
		//Test ob die beiden Endpkte NICHT dem selben Cluster gehoeren
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
		int a=-7,b=-7;
		LinienSegment c1,c2;

		//beachte switchen von i und j in manchen fällen.

		a = FixIndex(clusters.get(i), i, mstEdge.endpkt1);
		b = FixIndex(clusters.get(j), j, mstEdge.endpkt2);

		
		if( a == -1 || b == -1) {
			System.out.println("selbes Spiel. index switch");		
			//swap i,j	
			int temp =i;
			i = j;
			j = temp;
					
			a=clusters.get(i).indexOf(mstEdge.endpkt1);
			b=clusters.get(j).indexOf(mstEdge.endpkt2);
		}
		
		Punkt[] p1=  zweiNachbarn(i, a, tsp, clusters, mstEdge.endpkt1);		
		Punkt[] p2=  zweiNachbarn(j, b, tsp, clusters, mstEdge.endpkt2);		
		
		
		LinienSegment m11 = new LinienSegment(mstEdge.endpkt1, p1[0]);
		LinienSegment m22 = new LinienSegment(mstEdge.endpkt1, p1[1]);
		
		LinienSegment m21 = new LinienSegment(mstEdge.endpkt2, p2[0]);
		LinienSegment m12 = new LinienSegment(mstEdge.endpkt2, p2[1]);
		
		
		if( clusters.get(i).size() > 2 && clusters.get(j).size() > 2) {
			LinienSegment c[] = candidate(p1, p2, mstEdge); 
			c1 = c[0];
			c2 = c[1];
		}
		else {
			LinienSegment c[] = spezialCandidate(p1, p2, mstEdge, i, j, clusters); 
			c1 = c[0];
			c2 = c[1];
		}

		g0= c1.gewicht - m11.gewicht - m12.gewicht;
		g1= c2.gewicht - m21.gewicht - m22.gewicht;
		
//		System.out.println(g0);
//		System.out.println(g1);
		
		
		if (g0 < g1) {
			bestEdge = c1;
		}
		else bestEdge =c2;

		
		bestEdge.printLs();
		
		
		if( clusters.get(i).size() > 2 && clusters.get(j).size() > 2) {
			TspCombine( i, j, tsp, clusters, bestEdge, mstEdge);
		}
		else {
			spezialTspCombine( i, j, tsp, clusters, bestEdge, mstEdge);
		}


//		System.out.println("connecting through:");
//		bestEdge.printLs();
		
		
		return bestEdge;
	}
	
	private LinienSegment[] spezialCandidate(Punkt[] p1, Punkt[] p2, LinienSegment mst, int i, int j, List<List<Punkt>> clusters) {
		LinienSegment c[] = {null, null};

		// in i und j die 2 endpkte finden tsp.get(i)
		if(clusters.get(i).size() < 3) {
			//mst ist in p1[1]
			LinienSegment c11 = new LinienSegment(p1[0], p2[0]);
			LinienSegment c12 = new LinienSegment(p1[0], p2[1]);
			c[0] = c11;
			c[1] = c12;
		}
		else {
			//mst ist in p2[1]
			LinienSegment c21 = new LinienSegment(p1[1], p2[0]);
			LinienSegment c22 = new LinienSegment(p1[1], p2[1]);
			c[0] = c21;
			c[1] = c22;
		}
		//TODO test if richtigen Segmente
//		LinienSegment c11 = new LinienSegment(p1[0], p2[0]);
//		LinienSegment c12 = new LinienSegment(p1[0], p2[1]);
//		
//		LinienSegment c21 = new LinienSegment(p1[1], p2[0]);
//		LinienSegment c22 = new LinienSegment(p1[1], p2[1]);
//		
//		
//		 if( kreuzungsfreiBool(c11, mst) == true) {
//			 c[0]= c11;
//		 }
//		 else if( kreuzungsfreiBool(c12, mst) == true) {
//			 c[0]= c12;
//		 }
//		 else {
//			 System.out.println("both crossing mst. :/");
//			 
//		 }
//				
//		 
//		 if( kreuzungsfreiBool(c21, mst) == true) {
//			 c[1]= c21;
//		 }
//		 else if( kreuzungsfreiBool(c22, mst) == true) {
//			 c[1]= c22;
//		 }
//		 else {
//			 System.out.println("both crossing mst. :/");
////			 c21.printLs();
////			 c22.printLs();
////			 mst.printLs();
////			 c[1]= c22;
//		 }

		return c;
	
	}

	public int FixIndex (List<Punkt> cluster , int i, Punkt p) {
		int a = -7;
		
		for(int q=0 ; q < cluster.size(); q++) {
			if( cluster.get(q).samePoint(p) == true) {
				a=q;
				break; //TODO break?
			}
		}
		
		return a;
	}
	
	private void spezialTspCombine(int i, int j, ArrayList<double[][]> tsp, List<List<Punkt>> clusters, LinienSegment bestEdge, LinienSegment mst) {
		int a=-1,b=-1,c=-1,d=-1;
		
		int iSize = clusters.get(i).size();
		int jSize = clusters.get(j).size();
		
		a = FixIndex(clusters.get(i), i, mst.endpkt1);
		b = FixIndex(clusters.get(j), j, mst.endpkt2);
		c = FixIndex(clusters.get(i), i, bestEdge.endpkt1);
		d = FixIndex(clusters.get(j), j, bestEdge.endpkt2);	
		
		//neue Adj Matrix, die die beiden Touren verbindet. Zuerst aufstellen.
		int size=  iSize+jSize;
		double[][] combined = new double[size][size];
		
		
	//	Arrays.fill(combined, 0);
		
		System.out.println("only here if Ls");
		
		for (int h =0; h < iSize ;h++) {
			for (int g =0; g < iSize ;g++) {
				combined[h][g] = tsp.get(i)[h][g]; 
			}
		}
		
		int x=0;int y=0;
		for (int h = iSize; h < iSize+jSize ;h++, x++) { //wenn index fehler, i+j-1!
			for (int g =iSize; g < iSize+jSize ;g++, y++) {
				combined[h][g] = tsp.get(j)[x][y]; 
				if( g == iSize+jSize -1) {
					y=-1;
				}
			}
		}
		//setup complete
		
		if (jSize == 2) {
		
			//neue Nachbarn setzen:
			//1. mst eckpkte verbinden.
			combined[a][b+iSize] = 1;
			combined[b+iSize][a] = 1;
			
			//2.mst vom jeweiligen bestedge eckpkt lösen
			combined[a][c] = 0;
			combined[c][a] = 0;
			
	//		combined[b+iSize][d+iSize] = 0;
	//		combined[d+iSize][b+iSize] = 0;
			
			//bestedge verbinden
			combined[c][d+iSize] = 1;
			combined[d+iSize][c] = 1;
		}
		
		else {
			//neue Nachbarn setzen:
			//1. mst eckpkte verbinden.
			combined[a][b+iSize] = 1;
			combined[b+iSize][a] = 1;
			
			//2.mst vom jeweiligen bestedge eckpkt lösen
//			combined[a][c] = 0;
//			combined[c][a] = 0;
			
			combined[b+iSize][d+iSize] = 0;
			combined[d+iSize][b+iSize] = 0;
			
			//bestedge verbinden
			combined[c][d+iSize] = 1;
			combined[d+iSize][c] = 1;
		}
			
		
		tsp.remove(i);
	
		if( i < j ) {
			tsp.remove(j-1);
		}
		else tsp.remove(j);
		
		
		tsp.add(combined);
		
	}

	private void TspCombine(int i, int j, ArrayList<double[][]> tsp, List<List<Punkt>> clusters, LinienSegment bestEdge, LinienSegment mst) {
		// TODO test
		int a=-1,b=-1,c=-1,d=-1;
		
		int iSize = clusters.get(i).size();
		int jSize = clusters.get(j).size();

		a = FixIndex(clusters.get(i), i, mst.endpkt1);
		b = FixIndex(clusters.get(j), j, mst.endpkt2);
		c = FixIndex(clusters.get(i), i, bestEdge.endpkt1);
		d = FixIndex(clusters.get(j), j, bestEdge.endpkt2);	
		
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
				combined[h][g] = tsp.get(j)[x][y]; 
				if( g == iSize+jSize -1) {
					y=-1;
				}
			}
		}
		//setup complete
		
		
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

	private Punkt[] zweiNachbarn(int i, int a, ArrayList<double[][]> tsp, List<List<Punkt>> clusters, Punkt mstP) {
		Punkt[] zwei = new Punkt[2];
		int counter =0;
		
		
		if( tsp.get(i).length >2) {
		
			for(int p= 0; p < tsp.get(i).length ; p++) {
				if (tsp.get(i)[a][p] == 1 && counter == 0) {
					zwei[0] = clusters.get(i).get(p);
					counter++;
					}
				else if (tsp.get(i)[a][p] == 1 && counter == 1) {
					zwei[1] = clusters.get(i).get(p);
					}
			}
		}
		else {	
			if (clusters.get(i).get(0).samePoint(mstP) == true ){
				zwei[1] = clusters.get(i).get(0); 
				zwei[0] = clusters.get(i).get(1); 
			}
			else {
				zwei[0] = clusters.get(i).get(0); 
				zwei[1] = clusters.get(i).get(1); 
			}
			
		}
		

		
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

	private LinienSegment[] candidate(Punkt[] p1, Punkt[] p2, LinienSegment mst) {
		LinienSegment c[] = {null, null};

		// in i und j die 2 endpkte finden tsp.get(i)
		
		//TODO test if richtigen Segmente
		LinienSegment c11 = new LinienSegment(p1[0], p2[0]);
		LinienSegment c12 = new LinienSegment(p1[0], p2[1]);
		
		LinienSegment c21 = new LinienSegment(p1[1], p2[0]);
		LinienSegment c22 = new LinienSegment(p1[1], p2[1]);
		
		System.out.println("%%$%$%$%$%$%$$%");
		System.out.println("wieso ist das ne manchmal keine mst Kante??.??");
		mst.printLs();
		System.out.println("%%$%$%$%$%$%$$%");
		
		
		 if( kreuzungsfreiBool(c11, mst) == true) {
			 c[0]= c11;
		 }
		 else if( kreuzungsfreiBool(c12, mst) == true) {
			 c[0]= c12;
		 }
		 else {
			 System.out.println("both crossing mst. :/");
			 
		 }
				
		 
		 if( kreuzungsfreiBool(c21, mst) == true) {
			 c[1]= c21;
		 }
		 else if( kreuzungsfreiBool(c22, mst) == true) {
			 c[1]= c22;
		 }
		 else {
			 System.out.println("both crossing mst. :/");
//			 c21.printLs();
//			 c22.printLs();
//			 mst.printLs();
//			 c[1]= c22;
		 }

		return c;
	}
	
	//TODO test ob das stimmt, Formel? wikipedia falsch
	public boolean kreuzungsfreiBool(LinienSegment c1, LinienSegment mst) {
		//check if c1 und mst sich kreuzen
		
		double t,u;
		
		double nenner1 = (c1.endpkt2.getX() - c1.endpkt1.getX())*(mst.endpkt2.getY()- mst.endpkt1.getY());
		double nenner2 = (c1.endpkt2.getY() - c1.endpkt1.getY())*(mst.endpkt2.getX()- mst.endpkt1.getX());
		
		double t1= (mst.endpkt2.getX() - mst.endpkt1.getX())*(c1.endpkt1.getY()- mst.endpkt1.getY());
		double t2= (mst.endpkt2.getY() - mst.endpkt1.getY())*(c1.endpkt1.getX()- mst.endpkt1.getX());
		t = (t1 - t2)/(nenner1 - nenner2);
		
		double u1= (c1.endpkt2.getX() - c1.endpkt1.getX())*(c1.endpkt1.getY()- mst.endpkt1.getY());
		double u2= (c1.endpkt2.getY() - c1.endpkt1.getY())*(c1.endpkt1.getX()- mst.endpkt1.getX());
		u = (u1-u2)/(nenner1 - nenner2);

		
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

		
//		System.out.println("final tsp printing @@@@@@");
//		
//		System.out.println(tsp.size());
//		System.out.println(FinalTsp.size());
		
//		for (int i=0; i< FinalTsp.size(); i++) {
//			FinalTsp.get(i).printLs();
//		}
		
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
	
	private void setupAdjMatrix(List<Punkt> cluster, LinkedList<Punkt> tsp) {
		int m = cluster.size();
		double[][] AdjMatrix = new double[m][m];
		int a = 100,b = 100;
		
		//fill with 0. Array fill doesnt work
		for (int h =0; h < m ;h++) {
			for (int g =0; g < m ;g++) {
				AdjMatrix[h][g] = 0; 
			}
		}
		
		for (int i=0 ; i < tsp.size() -1; i++) { 

			for(int j=0 ; j < cluster.size(); j++) {
				if( cluster.get(j).samePoint(tsp.get(i)) == true) {
					a=j;
				}
				if( cluster.get(j).samePoint(tsp.get(i+1)) == true) {
					b = j;
				}
				
			}

			AdjMatrix[a][b] = 1;
			AdjMatrix[b][a] = 1;
		}
		
		for(int j=0 ; j < cluster.size(); j++) {
			if( cluster.get(j).samePoint(tsp.getLast()) == true) {
				a=j;
			}
			if( cluster.get(j).samePoint(tsp.getFirst()) == true) {
				b = j;
			}
			
		}
		
		AdjMatrix[a][b] = 1;
		AdjMatrix[b][a] = 1;
		
		
		tspLinked.add(AdjMatrix);
	}

}

