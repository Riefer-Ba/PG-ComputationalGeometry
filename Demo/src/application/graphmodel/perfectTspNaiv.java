import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class perfectTspNaiv{
	ArrayList<double[][]> allTsp = new ArrayList<double[][]>();

	
	public void execute(List<Punkt> clusters, ArrayList<LinienSegment> DelaunayK) {
			
		double[][] Adj = setupAdjMatrix(clusters , DelaunayK); //clusters.get(i)
		printAdj(Adj);
		System.out.println("$$$");
		
		if(clusters.size() > 3) {
			getTspTour(clusters, Adj);
		}
		else {
			//bereits fertig.
			allTsp.add(Adj);
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
	//	printAdj(AdjMatrix);
		
		return AdjMatrix;
	}
	

	private boolean getTspTour(List<Punkt> cluster, double[][] adj) {
		int m = cluster.size(); 
		boolean firstTour =true;
		double laengeCurrentBest =100000;
		
		int[][] indexTour = new int[m][2]; //speichert die Tsp Kanten als 2 Pkte
		int[] visitedN = new int[m];
		double[][] currentBest = new double[m][m];
		
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
		
		printAdj(visitedB);
		
	
		for (int i=0 ; i < m ; i++) {	//reicht es hier nur für die erste reihe den algo? sind damit alle fälle drin?
			for (int k=1 ; k < m ; k++) { //i+1 gehen auf hauptdiagonale aber nur im ersten schritt
			
				if(visitedB[i][k] == false) {
					visitedB[i][k] = true;
					if (adj[i][k]  == 1) {
						
						indexTour[i][0] = i; 
						indexTour[i][1] = k;

						visitedN[i]++;
						visitedN[k]++;
												
						
						if(visitedN[i] >2 || visitedN[k] > 2) {
							System.out.println("abbruch Punkt mehr als 2x verwendet");
							//ein punkt hat mehr als 2 Nachbarn in dieser Tour, abbruch!
							

							reset(indexTour, visitedB, visitedN, i,k);
							System.out.println("TODO%%%%%%"); //TODO adapt für nicht letzter schritt
							int ind[] = resetLetzterSchritt(indexTour, visitedB, visitedN, i); //vorher auch k
							i = ind[0];
							k = ind[1] -1;
							
							if(ind[0] == -7  || ind[1] == -7) {
								System.out.println("fertig die beste Tour:");
								printAdj(currentBest);
							//	printIndexTour(indexTour); ist hier schon gibberish.
								System.out.println(visitedN[0]);
								System.out.println(visitedN[1]);
								System.out.println(visitedN[2]);
								System.out.println(visitedN[3]);
								return true;
							}
							
							
						}		//soviele zeilen hochgehen, bis das erste false gefunden!
						
						else {
						//	i++;		//Tour ist noch gültig, eine Zeile hochgehen
							break;
						}

					}
					else {
						System.out.println("do nothing");
						break; //break? TODO
					}
				}		
								
				//letzte Wert über der Hauptdiagonalen wurde bearbeitet.
				
			//	if( i == 0 && )
				
				
				if(k == m-1 && i == m-1) { //&& i== m-2 hinzugefügt
					System.out.println("obere diagonale, unten rechts");
					// suche die 2 visited N == 1(x,y), wenn  allTsp.get(l)[x][y]  == 1 -> tour gefunden
					int v1 =m+3; int v2=m+3;
					
//					System.out.println("%%%%%%%%");
//					System.out.println("%%%%%%%%");
//					System.out.println("%%%%%%%%");
//					
//					System.out.println(visitedN[0]);
//					System.out.println(visitedN[1]);
//					System.out.println(visitedN[2]);
//					System.out.println(visitedN[3]);
//					
//					printIndexTour(indexTour);
//					
//					System.out.println("%%%%%%%%");
//					System.out.println("%%%%%%%%");
//					System.out.println("%%%%%%%%");
					
					int count = 0;
					for(int h=0 ; h< m -1; h++) {
						if (visitedN[h] == 1) {
							if( count == 0) {
							v1= h;
							count++;
							}
							else {
							v2 = h;
							break;
							}
						}

					}
					
					if(v1 == m+3 || v2== m+3) {
						System.out.println("Fehler beim zusammenführen der letzten 2 Pkte, reset tour"); 
						int ind[] = resetLetzterSchritt(indexTour, visitedB, visitedN, i); //vorher auch k
						i = ind[0];
						k = ind[1] -1;
						 // reset gibt indx von letztem false aus.die for loop erhöht k um 1. preemtive strike -1.
					}
					
					
					else if( adj[v1][v2] == 1 ) { //tsp tour gefunden
							System.out.println("neue Tour gefunden. to test");
							indexTour[m-1][0] = v1;
							indexTour[m-1][1] = v2;
							
							
							printIndexTour(indexTour);
							double[][] AdjTest = erzeugeAdj(indexTour);
								
							//dies können auch untersch touren sein, aber nur eine gebraucht.
							if(uniqueTour(AdjTest, laengeCurrentBest, cluster,firstTour ) == true) {
								System.out.println("neue beste Tour");
								currentBest = AdjTest;	
								
								int ind[] = resetLetzterSchritt(indexTour, visitedB, visitedN, i); //vorher auch k
								i = ind[0];
								k = ind[1] -1;
								
								if(ind[0] == -7  || ind[1] == -7) {
									System.out.println("fertig");
									return true;
								}
							}
							else {
								System.out.println("selbe Tour oder schlechter als die derzeitig beste");
								//hier muss auch ein reset passieren!!!!
							}
							
					}
//					else if ( adj[v1][v2] == 0 ){ //keine tsp tour, verbindung nicht möglich. 2 reihen hoch. reset visited reihe k-2?1?
//						reset(indexTour, visitedB, visitedN, i,k);
//					}
				}
				
				if( i == 0 ) {
					//wenn alle visited bool == true sind -> abbruch. Alle Touren gefunden.
					if( k == m-1) {
						System.out.println("omg i made it to the end");
					}
				}
			
			}
			
			//reset all visited bool für alle "größeren" Reihen auf false, wenn in eine untere Reihe gegangen wird
				
			
			//reset all visited N auf 0? ALLE!? (ja wenn zurück in der ersten Reihe!)
			
		}
		
		allTsp.add(currentBest);
		System.out.println("hier");
		printAdj(allTsp.get(0));
		
		return true;
	}
	
	private void reset(int[][] indexTour, boolean[][] visitedB, int[] visitedN, int i, int k) {
		// TODO Auto-generated method stub
		System.out.println("in the normal reset");
		printIndexTour(indexTour);
		
		
	}

	private int[] resetLetzterSchritt(int[][] indexTour, boolean[][] visitedB, int[] visitedN, int i) {
		// TODO Auto-generated method stub
		//ungültige Tour gefunden
		i--;
		int m = visitedB.length;
		int indexReset[] = {-7, -7};
		
		
		System.out.println("@@@@ Before");
		printAdj(visitedB);
		
		//rückwarts das array hochlaufen
		for ( int o=i ; o >= 0 ; o--) {	//int o=i ; o >= 0 ; o--
			System.out.println("hi");
			for (int h=o+1 ; h < m  ; h++) {
			//	if (o != h) {
					
					if( (ganzeReiheBesucht(visitedB, o, indexTour, visitedN)) == true) {
						for(int p=o+1; p< m; p++) {
							visitedB[o][p] = false;
							}
							//2. die Anzahl, mit der ein Punkt in der Tour vorkommt 
							visitedN[o]--;
							visitedN[m-1]--;
							
//							System.out.println(visitedN[0]);
//							System.out.println(visitedN[1]);
//							System.out.println(visitedN[2]);
//							System.out.println(visitedN[3]);
							
							//3. die Kante aus der IndexTour herrausnehmen
							for( int w=0 ; w < m; w++) {
								
								//TODO Indexe richtig?
								if (indexTour[w][0] == o && indexTour[w][1] == m-1 ||
										indexTour[w][0] == m-1 && indexTour[w][1] == o) {
									indexTour[w][0] =-2;
									indexTour[w][1] =-2;
								}
								indexTour[m-1][0] =-22;	//TODO potentiell problematisch für groeßere Matrix
								indexTour[m-1][1] =-22;
							}
							break;
					}
					
					
					
					else { // das erste false in der Reihe finden
						if(visitedB[o][h] == false) {
							if( o == 0) System.out.println("that will explain everything");
						//das true vor dem ersten false ist derzeit in der index tour, rausnhemen.
							if( o == h-1 ) {
								System.out.println("das darf nicht passieren! auf diagonale");
								System.out.println(o);
								System.out.println(h);
							}
						
						visitedN[o]--;
						visitedN[h-1]--;	
						
//						System.out.println(visitedN[0]);
//						System.out.println(visitedN[1]);
//						System.out.println(visitedN[2]);
//						System.out.println(visitedN[3]);
						
				//		printIndexTour(indexTour);
						
						for( int w=0 ; w < m; w++) {
							if (indexTour[w][0] == o && indexTour[w][1] == h-1 ||
									indexTour[w][0] == h-1 && indexTour[w][1] == o) {
								indexTour[w][0] =-2;
								indexTour[w][1] =-2;
							}
						}
					//	printIndexTour(indexTour);
							
						System.out.println("reset: Index des ersten false   i:"+ o+"k: "+ h);
						indexReset[0] = o;
						indexReset[1] = h;
						
						System.out.println("@@@@@ After:");
						printAdj(visitedB);
						System.out.println("@@@@@");
						 
								
						return indexReset;
						
						}
						
					}
					
//					if (visitedB[o][h] == true) { //update der 3 Hilfsstrukturen
//						//TODO hier muss! erstmal gecheckt werden ob o,h auch in der index tour ist! ? oder nicht?
//						if (o != h) {
//							//1. den boolean
//							visitedB[o][h] = false;
//							//2. die Anzahl, mit der ein Punkt in der Tour vorkommt 
//							visitedN[o]--;
//							visitedN[h]--;
//							
//							System.out.println(visitedN[0]);
//							System.out.println(visitedN[1]);
//							System.out.println(visitedN[2]);
//							System.out.println(visitedN[3]);
//							
//							//3. die Kante aus der IndexTour herrausnehmen
//									//TODO test ob korrekt mitgeführt wird.
//							for( int w=0 ; w < m; w++) {
//								if (indexTour[w][0] == i && indexTour[w][1] == k ||
//										indexTour[w][0] == k && indexTour[w][1] == i) {
//									indexTour[w][0] =-2;
//									indexTour[w][1] =-2;
//
//								}
//							}
//						}
//						else System.out.println("do nothing einfach hoch");
//					}
//					else {
//						
//					}
				//}
			}
		}

		
		if(indexReset[0] == -7) {
			//alle booleans sind auf true. die letzte Tour ist nicht möglich
			//die beste Tour wurde gefunden.
			System.out.println("ups");
			
		}
		
		return indexReset;
	}

	private boolean ganzeReiheBesucht(boolean[][] visitedB, int o, int[][] indexTour, int[] visitedN) {
		int m= visitedB.length;
		
		//ein element nicht besucht
		for(int i=0; i< m ; i++) {
			if (visitedB[o][i] == false) return false;
		}
		//alle elemente besucht, alle 3 Hilfsstrukturen updaten:
		// das letzte element der reihe ist in der derzeitigen tsp tour -> rausnehmen.
		
		return true;
	}

	private boolean uniqueTour(double[][] adjTest, double laengeCurrentBest, List<Punkt> cluster, boolean firstTour) {
		// TODO test
		
		System.out.println("unique tour");
		
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
		System.out.println(newLaenge);
		System.out.println(laengeCurrentBest);
		
		if( firstTour == true) {
			laengeCurrentBest = newLaenge;
			System.out.println(laengeCurrentBest);
			System.out.println(newLaenge);
			System.out.println("firstTour ist true");
			
			return true;
		}
		else {
			if( newLaenge < laengeCurrentBest) {
				System.out.println("neue Tour nicht besser");
				
				System.out.println("@@@@@");
				System.out.println("@@@@@");
				System.out.println(newLaenge);
				System.out.println(laengeCurrentBest);
				
				System.out.println("should return false weil das die selbe Tour ist");
				
				printAdj(adjTest);
				printAdj(allTsp.get(0));
				
				System.out.println("@@@@@");
				System.out.println("@@@@@");
				
				laengeCurrentBest = newLaenge;
				return true;
			}
			
			else {
				System.out.println("neue Tour nicht besser");
				return false;
			}
		}
		//hier kann tsp schon als ls Tour abgespeichert werden.
	}

	private double[][] erzeugeAdj(int[][] indexTour) {
		int m = indexTour.length;
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

		return AdjMatrix;
	}

	public void printAdj(double[][] AdjMatrix ) {
		for ( int i=0; i<AdjMatrix.length ;i++) {
			for ( int j=0; j<AdjMatrix.length ;j++) {
				System.out.print(" : "+ AdjMatrix[i][j] );
				if( j == AdjMatrix.length -1 ) {
					System.out.println(" ");
				}
			}
		}
	}
	
	public void printAdj(boolean[][] AdjMatrix ) {
		for ( int i=0; i<AdjMatrix.length ;i++) {
			for ( int j=0; j<AdjMatrix.length ;j++) {
				System.out.print(" : "+ AdjMatrix[i][j] );
				if( j == AdjMatrix.length -1 ) {
					System.out.println(" ");
				}
			}
		}
	}
	
	private void printIndexTour(int[][] indexTour) {
		for(int i=0; i< indexTour.length; i++) {
			System.out.println(" "+indexTour[i][0] +" "+indexTour[i][1]);
		}
	}

}
