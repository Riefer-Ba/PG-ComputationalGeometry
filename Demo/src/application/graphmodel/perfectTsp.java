package application.graphmodel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class perfectTsp {
		ArrayList<double[][]> allTsp = new ArrayList<double[][]>();
		
		
		public void execute(List<List<Punkt>> clusters, ArrayList<LinienSegment> DelaunayK) {
			for (int i=0 ; i< clusters.size() ; i++) { // 1 vs clusters.size()
				double[][] Adj = setupAdjMatrix(clusters.get(i), DelaunayK); //clusters.get(i)
				
				if(clusters.get(i).size() > 3) {
					getTspTour(clusters.get(i), i, Adj);
				}
				else {
					//bereits fertig.
					allTsp.add(Adj);
				}
			}
			
		}
		

		private double[][] setupAdjMatrix(List<Punkt> cluster, ArrayList<LinienSegment> DelaunayK) {
			int m = cluster.size();
			boolean added = false;
			double[][] AdjMatrix = new double[m][m];
			
			for (int i=0 ; i < m ; i++) { 
				for (int j=0 ; j < m ; j++) {
					if (i == j) AdjMatrix[i][j] = 0;
					else {
						for (int k = 0 ; k < DelaunayK.size(); k++) {
							if ( (DelaunayK.get(k).sameEdge(new LinienSegment(cluster.get(i),cluster.get(j))) == true)) {
								AdjMatrix[i][j] = 1;
								added = true;
								break;
							}
							if (added == false) AdjMatrix[i][j] = 0;
						}
					}
				}
			}
		//	allTsp.add(AdjMatrix);
			printAdj(AdjMatrix);
			
			return AdjMatrix;
		}
		

		private void getTspTour(List<Punkt> cluster, int l, double[][] adj) {
			int m = cluster.size(); 
			int[][] indexTour = new int[m][1]; //muss das nicht 2 sein?
			int[] visitedN = new int[m];
			double[][] currentBest = new double[m][m];
			double laengeCurrentBest =-1;
			
			
			boolean[][] visitedB = new boolean[m][m];
			for (int i=0 ; i < m ; i++) {
				for (int k=0 ; k < m ; k++) {
					if( k <= i) {
						visitedB[i][k]= true;
					}
					else {
					//untere Dreiecksmatrix auf true setzen. rest false
					visitedB[i][k] = false;
					}
				}
			}
			//visited[i][k] == false
			
			for (int i=0 ; i < m ; i++) {	//reicht es hier nur für die erste reihe den algo? sind damit alle fälle drin?
				for (int k=i+1 ; k < m ; k++) {
					if(visitedB[i][k] == false) {
						if (allTsp.get(l)[i][k]  == 1) {
							
							indexTour[i][0] = i; 
							indexTour[i][1] = k;
							visitedB[i][k] = true;
							
							visitedN[i]++;
							visitedN[k]++;
							
							
							
							if(visitedN[i] >2 || visitedN[k] > 2) {
								//ein punkt hat mehr als 2 Nachbarn in dieser Tour, abbruch!
								visitedN[i]--;
								visitedN[k]--;
								reset(indexTour, visitedB, visitedN, i,k);
							}		//soviele zeilen hochgehen, bis das erste false gefunden!
							

						}
						else { //also bereits besucht
							
						}		
								
					}
									
					//letzte Wert über der Hauptdiagonalen wurde abarbeitet.
					if(k == m-1) {
						// suche die 2 visited N == 1(x,y), wenn  allTsp.get(l)[x][y]  == 1 -> tour gefunden
						int v1 =m+3; int v2=m+3;
						for(int h=0 ; h< m; h++) {
							if (visitedN[h] == 1) {
								v1= h;
								h++;
							}
							if (visitedN[h] ==1) {
								v2 = h;
								break;
							}
						}
						if( adj[v1][v2] == 1 ) { //tsp tour gefunden
							if(v1 == m+3 || v2== m+3) {
								System.out.println("error im zusammenführen der letzten 2 Pkte"); 
							}
							else {
								indexTour[m][0] = v1;
								indexTour[m][1] = v2;
								double[][] AdjTest = erzeugeAdj(indexTour);
									
								//dies können auch untersch touren sein, aber nur eine gebraucht.
								if(uniqueTour(AdjTest, laengeCurrentBest, cluster) == true) {
									currentBest = AdjTest;
								}
							}
						}
						else { //keine tsp tour, verbindung nicht möglich. 2 reihen hoch. reset visited reihe k-2?1?
							
						}
					}
					
					if( i == m-1 ) {
						//wenn alle visited bool == true sind -> abbruch. Alle Touren gefunden.
						if( k == m-1) {
							
						}
					}
				
				}
				//reset all visited bool für alle "größeren" Reihen auf false, wenn in eine untere Reihe gegangen wird
					
				
				//reset all visited N auf 0? ALLE!? (ja wenn zurück in der ersten Reihe!)
				
			}
			
			allTsp.add(currentBest);
		}
		
		private void reset(int[][] indexTour, boolean[][] visitedB, int[] visitedN, int i, int k) {
			// TODO Auto-generated method stub
			
		}

		private boolean uniqueTour(double[][] adjTest, double laengeCurrentBest, List<Punkt> cluster) {
			// TODO test
			double newLaenge;
			int m = cluster.size();
			ArrayList<LinienSegment> tspSegmente = new ArrayList<LinienSegment>();

			for (int i=0 ; i < m ; i++) {	
				for (int k=i+1 ; k < m ; k++) {
					if (adjTest[i][k] == 1) {
						tspSegmente.add(new LinienSegment(cluster.get(i),cluster.get(k)));
					}
				}
			}
			
			newLaenge = tspSegmente.get(0).TourLaenge(tspSegmente);
			if( laengeCurrentBest == -1) {
				laengeCurrentBest = newLaenge;
				return true;
			}
			else if(newLaenge < laengeCurrentBest) {
				laengeCurrentBest = newLaenge;
				return true;
			}
			
			return false;
		}

		//wenn cluster size =4 und k-order <2, dann gibt es nur eine tour und algo kann abbrechen.

		//TODO test
		private double[][] erzeugeAdj(int[][] indexTour) {
			int m = indexTour.length; //gibt das die richtige laenge? also m?
			double[][] AdjMatrix = new double[m][m];
			
			//erst mit 0en füllen...
			for (int i=0 ; i < m ; i++) { 
				for (int j=0 ; j < m ; j++) {
					AdjMatrix[i][j] = 0;
				}
			}
			
			//dann alle Punkte verbinden
			for (int i=0; i< m; i++) { 
				int a = indexTour[i][0];
				int b = indexTour[i][1];
				
				AdjMatrix [a][b] =1;
				AdjMatrix [b][a] =1;
			}

			return null;
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
			
	}
