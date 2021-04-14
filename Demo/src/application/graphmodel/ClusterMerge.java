package application.graphmodel;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClusterMerge {
		ArrayList<LinienSegment> FinalTsp = new ArrayList<LinienSegment>();
		ArrayList<double[][]> tspLinked = new ArrayList<double[][]>();

		public void execute(List<List<Punkt>> clusters, ArrayList<LinienSegment> mst, ArrayList<double[][]> tsp) {
			
			System.out.println("start");
			
			int m = clusters.size() -1;
			
			for (int i=0; i< m ;i++) {
				System.out.println(i);
			findClosestCluster(clusters, mst, tsp);
//			//verbindet Cluster, behaltet Tsp Eigenschaften bei.

			}
			
			FinalTsp(tsp, clusters);
		}

		//TODO test
		public void execute(List<List<Punkt>> clusters, ArrayList<LinienSegment> mst, List<LinkedList<Punkt>> tsp) {
			
			
			int m = clusters.size();
			
			for (int i=0; i<clusters.size(); i++) {
				setupAdjMatrix(clusters.get(i), tsp.get(i));
			}
			
			for (int i=0; i< m ;i++) {

			 findClosestCluster(clusters, mst, tspLinked);
//			//verbindet Cluster, behaltet Tsp Eigenschaften bei.

			}
			
			FinalTsp(tspLinked, clusters);
		}
		

		public void showcase(List<List<Punkt>> clusters, ArrayList<LinienSegment> mst, ArrayList<double[][]> tsp) {
			//verbindet nur zwei Cluster mit einander. Zu Demo zwecken.
			findClosestCluster(clusters, mst, tspLinked);
			FinalTsp(tspLinked, clusters);
		}


		//TODO test if i und j passt
		public void findClosestCluster(List<List<Punkt>> clusters, ArrayList<LinienSegment> mst, ArrayList<double[][]> tsp) {
			
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
		
		private boolean ConnectTsp(LinienSegment mstEdge, int i, int j, ArrayList<double[][]> tsp, List<List<Punkt>> clusters) {
			LinienSegment bestEdge = null;
			double g0,g1;
			int a=-7,b=-7;
			LinienSegment c1,c2;
			
			a = FixIndex(clusters.get(i), i, mstEdge.endpkt1);
			b = FixIndex(clusters.get(j), j, mstEdge.endpkt2);
			
			Punkt[] p1=  zweiNachbarn(i, a, tsp, clusters, mstEdge.endpkt1);		
			Punkt[] p2=  zweiNachbarn(j, b, tsp, clusters, mstEdge.endpkt2);		
			
			
			LinienSegment m11 = new LinienSegment(mstEdge.endpkt1, p1[0]);
			LinienSegment m22 = new LinienSegment(mstEdge.endpkt1, p1[1]);
			
			LinienSegment m21 = new LinienSegment(mstEdge.endpkt2, p2[0]);
			LinienSegment m12 = new LinienSegment(mstEdge.endpkt2, p2[1]);
			
			LinienSegment c[] = candidate(p1, p2, mstEdge); 
			c1 = c[0];
			c2 = c[1];
			
			
			
			System.out.println("%%%%%%%%%%");
			
			if (c1 != null  && c2 != null) {
				g0= c1.gewicht - m11.gewicht - m12.gewicht;
				g1= c2.gewicht - m21.gewicht - m22.gewicht;
				
				if (g0 < g1) {
					bestEdge =c1;
				}
				else {
					bestEdge =c2;
				}
				
				TspCombine( i, j, tsp, clusters, bestEdge, mstEdge);
				return true;
			}
			
			else if(c1 == null && c2 != null) {
				bestEdge = c2;
				TspCombine( i, j, tsp, clusters, bestEdge, mstEdge);
				return true;
			}
			
			else if(c2 == null && c1 != null) {
				bestEdge = c1;
				TspCombine( i, j, tsp, clusters, bestEdge, mstEdge);
				return true;
			}
			else if (c2 == null && c1 ==null){
				System.out.println("fuck me");
			}
			
			
			System.out.println("fuck");
			
			return false;
			

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
		
		//TODO test
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

			for(int p= 0; p < tsp.get(i).length ; p++) {
				if (tsp.get(i)[a][p] == 1 && counter == 0) {
					zwei[0] = clusters.get(i).get(p);
					counter++;
					}
				else if (tsp.get(i)[a][p] == 1 && counter == 1) {
					zwei[1] = clusters.get(i).get(p);
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

		//TODO test
		private LinienSegment[] candidate(Punkt[] p1, Punkt[] p2, LinienSegment mst) {

			LinienSegment m11 = new LinienSegment(mst.endpkt1, p1[0]);
			LinienSegment m22 = new LinienSegment(mst.endpkt1, p1[1]);
			
			LinienSegment m21 = new LinienSegment(mst.endpkt2, p2[0]);
			LinienSegment m12 = new LinienSegment(mst.endpkt2, p2[1]);

			// in i und j die 2 endpkte finden tsp.get(i)
			
			//TODO test if richtigen Segmente
			LinienSegment c11 = new LinienSegment(p1[0], p2[0]);
			LinienSegment c12 = new LinienSegment(p1[0], p2[1]);
			
			LinienSegment c21 = new LinienSegment(p1[1], p2[0]);
			LinienSegment c22 = new LinienSegment(p1[1], p2[1]);
			
			LinienSegment c[] = {null, null};
			
			
			 if( kreuzungsfreiBool(c11, mst) == true) {
				// if(kreuzungsfreiBool(c11, m22) == true) {
				//	 if(kreuzungsfreiBool(c11, m12) == true){
						 c[0]= c11;
						 System.out.println("c[0] =c11");
				//	 }
				// }
			 }
			 else if( kreuzungsfreiBool(c12, mst) == true) {
				// if(kreuzungsfreiBool(c12, m22) == true) {
				//	 if(kreuzungsfreiBool(c12, m21) == true){
						 c[0]= c12;
						 System.out.println("c[0] =c12");
				//	 }
				// }
			 }
			 else {
				 System.out.println("both crossing mst. or crossing wrong edge  c2:/");
			 }
					
			 
			 if( kreuzungsfreiBool(c21, mst) == true) {
			//	 if(kreuzungsfreiBool(c21, m12) == true) {
			//		 if(kreuzungsfreiBool(c21, m11) == true){
						 c[1]= c21;
						 System.out.println("c[1] =c21");
			//		 }
			//	 }
			 }
			 else if( kreuzungsfreiBool(c22, mst) == true) {
			//	 if(kreuzungsfreiBool(c22, m21) == true) {
			//		 if(kreuzungsfreiBool(c22, m11) == true){
						 c[1]= c22;
						 System.out.println("c[1] =c22");
			//		 }
			//	 }
			 }
			 else {
				 System.out.println("both crossing mst. or worse  c1 :/");
			 }
			 

			return c;
		}
		
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

		//TODO test
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
		
		
		//TODO test
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
