import java.util.ArrayList;
import java.util.List;

public class perfectTspNaiv {
	ArrayList<double[][]> allTsp = new ArrayList<double[][]>();
	ArrayList<LinienSegment> FinalTspK = new ArrayList<LinienSegment>();
	
workbranch-simon
	public void execute(List<Punkt> clusters, List<LinienSegment> DelaunayK) {

		
		double[][] Adj = setupAdjMatrix(clusters , DelaunayK);
		
		printAdj(Adj);		//for test
		
		if(clusters.size() > 3) {
			getTspTour(clusters, Adj);
			System.out.println(allTsp.size());
			bestTour(clusters);
		}
		else {
			//bereits fertig.
			allTsp.add(Adj);
			bestTour(clusters); //oder nicht?  //TODO
		}
				
	}
	
	private boolean getTspTour(List<Punkt> cluster, double[][] adj) {
		int m = cluster.size(); 		
		int[][] indexTour = new int[m][2]; //speichert die Tsp Kanten als 2 Pkte 
		int[] visitedN = new int[m];
		boolean[][] visitedB = new boolean[m][m];
		int p;
		boolean reset =false;
		
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
		
		for (int i=0 ; i < m ; i++) {	//reicht es hier nur für die erste reihe den algo? sind damit alle fälle drin?
			for (int k=i+1 ; k < m ; k++) { //i+1 gehen auf hauptdiagonale aber nur im ersten schritt
			
				if(visitedB[i][k] == false) {
					
					visitedB[i][k] = true;
					
					if (adj[i][k]  == 1) {
						reset=false; //für tour abbruch
						
						//nicht besuchte Kante einfügen. 
						for(p=0 ;p< m; p++) {
							if(indexTour[p][0] == 0 && indexTour[p][1] == 0) {
								
								indexTour[p][0] = i; 
								indexTour[p][1] = k;
								
								visitedN[i]++;
								visitedN[k]++;
								break;
							}
						}
						//Testen ob eingefügte Kante gültig.
						//neue Kante verletzt tsp eigenschaften. rausnehmen
						if(visitedN[i] >2 || visitedN[k] > 2) {
							//reset
						//	System.out.println("abbruch Punkt mehr als 2x verwendet");		
							
							//wenn es noch ein adj[i][k] gibt, das nicht besucht wurde, letztes element rausnehmen.
							for (int h=i; h < m ; h++) {
								for (int j=k ; j < m ; j++) {
									if(visitedB[h][j] == false) {	//&& adj[h][j]  == 1 bessere Laufzeit. Problem: unten rechts keine 1
										//weiter mit nächstem element. tour noch möglich
										//alle 3 Hilfsstrukturen updaten.
							 			visitedN[indexTour[p][0]]--;
							 			visitedN[indexTour[p][1]]--;
										indexTour[p][0] = 0; 
							 			indexTour[p][1] = 0;
							 			reset=true;
							 			break;
									}
								}
								if(reset == true) {
									break;
								}
							}
							
//							if (reset == false) {
//								//derzeitige Tour kann nicht mehr gültig werden.
//								System.out.println("in reset false");
//								
//								//hier auch ein reset
//							}
						}
						
						//neue Kante gültig. Teste ob m-1 Kanten in index Tour sind.
						else if( indexTour[m-1][0] == 0 && indexTour[m-1][1] == 0  ) {
							if( indexTour[m-2][0] != 0 || indexTour[m-2][1] != 0) {

								int v1 =m+3; int v2=m+3;
								//sucht ob es 2 Punkte gibt, die nur 1mal benutzt werden.
								int count = 0;
								for(int h=0 ; h< m ; h++) {
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
								//checken ob Kante schon drinne ist.
								for (int z =0; z < m; z++) {
									if( indexTour[z][0] == v1 && indexTour[z][1] == v2) {
										//System.out.println("Kante schon drinne");
										v1 =m+3;
										break; //TODO break neu eingeführt, testen
									}
								}
								//TODO check ob subtouren gefunden.
								
								//gibt es nicht. oder in sich geschlossen
								if(v1 == m+3 || v2== m+3 || v2 == v1 || adj[v1][v2]  == 0) {
							//		System.out.println("Fehler beim zusammenführen der letzten 2 Pkte, reset tour"); 

						 			visitedN[indexTour[p][0]]--;
						 			visitedN[indexTour[p][1]]--;
									indexTour[p][0] = 0; 
						 			indexTour[p][1] = 0;
						 			
						 			
						 			if (k == m-1 && i == m-2) {

							 			visitedN[indexTour[p-1][0]]--;
							 			visitedN[indexTour[p-1][1]]--;
							 			
										i = indexTour[p-1][0];
										k = indexTour[p-1][1];
										
										indexTour[p-1][0] = 0; 
							 			indexTour[p-1][1] = 0;
							 			
							 			int leave =0;
										for (int h= i ; h < m ; h++) {
											for (int o= 1 ; o < m ; o++) { //ree aufpassen, was o ist!
												if( h == i && leave ==0) {
													o=k;
													leave++;
												}
												if( o > h) {
													visitedB[h][o] = false;
												}
												//alle elemente nach dem zuletzt rausgenommenen auf false
											}
										}
										
						 				
						 			}
									
								}
								//gibt es aber kante bereits in der Tour.
								else{
								//System.out.println("Tour gefunden");
								//letzten Punkt einfügen
								visitedN[v1]++;
								visitedN[v2]++;
								indexTour[m-1][0]= v1;
								indexTour[m-1][1]= v2;
								
								double[][] currentTour = erzeugeAdj(indexTour); //auch möglich nur die aktuell beste Tour zu speichern
								allTsp.add(currentTour);
								
								//und wieder rausnehmen, die letzten 2! einträge
								
								visitedN[indexTour[m-1][0]]--;
								visitedN[indexTour[m-1][1]]--;
								visitedN[indexTour[m-2][0]]--;
								visitedN[indexTour[m-2][1]]--;
								indexTour[m-1][0]= 0;
								indexTour[m-1][1]= 0;
														
								i = indexTour[m-2][0];
								k = indexTour[m-2][1];
											
								indexTour[m-2][0]= 0;
								indexTour[m-2][1]= 0;

								if (k == m-1 && i == m-2) {
									visitedN[indexTour[m-3][0]]--;
									visitedN[indexTour[m-3][1]]--;
															
									i = indexTour[m-3][0];
									k = indexTour[m-3][1];
												
									indexTour[m-3][0]= 0;
									indexTour[m-3][1]= 0;
								}
								
								//und noch reset bool ab hier
								int leave =0;
								for (int h= i ; h < m ; h++) {
									for (int o= 1 ; o < m ; o++) { //ree aufpassen, was o ist!
										if( h == i && leave ==0) {
											o=k;
											leave++;
										}
										if( o > h) {
											visitedB[h][o] = false;
										}
										//alle elemente nach dem zuletzt rausgenommenen auf false
									}
								}							
								}
							}
						}
					}
					
					
					
					else {
					//	System.out.println("ist false. suche next true");

						if(k == m-1 && i == m-2) { //hier abbruch bedingung, wenn keine Tour mehr gefunden werden kann 
							// auch schon früher als unten rechts
							//besser länge index Tour und noch mögliche kanten (übrig geblieben ==1 -1, wegen letztem schritt)
						//	System.out.println("Tour abbgebrochen.");
							
							for(int z = 0 ;z< m; z++) {
								if(indexTour[z][0] == 0 && indexTour[z][1] == 0) {
									
									if( z == 0) {
										System.out.println("Sonderfall boop wenn oben rechts fertig.");
										return true;
									}
									visitedN[indexTour[z-1][0]]--;
									visitedN[indexTour[z-1][1]]--;
									
									i = indexTour[z-1][0];
									k = indexTour[z-1][1];
									
									//und noch reset bool ab hier
									int leave =0;
									for (int h= i ; h < m ; h++) {
										for (int o= 1 ; o < m ; o++) { 
											if( h == i && leave ==0) {
												o=k;
												leave++;
											}
											if( o > h) {
												visitedB[h][o] = false;
											}
											//alle elemente nach dem zuletzt rausgenommenen auf false
										}
									}
									indexTour[z-1][0] = 0; 
									indexTour[z-1][1] = 0;
								
									break;
								}
							}
							
							//letztes obj in index tour finden. rausnehmen. alle visitedB danach auf false setzen.
								// i und k reseten auf das element nach dem zu letzt rausgenommenen.
						}
					}
					
					if(k == m-1 && i == m-2) {						
						if( indexTour[0][0] == m-2 && indexTour[0][1] == m-1) {
							return true; // Sonderfall erstes Element ist das über der Hauptdiagonalen
						}

//						System.out.println("remove letztes != 0 element aus index Tour");
//						System.out.println("wenn letztes element 0 m-1 algo fertig.");

						for(int z = 0 ;z< m; z++) {
							if(indexTour[z][0] == 0 && indexTour[z][1] == 0) {
								
								if( z == 0) {
								//	System.out.println("Sonderfall wenn oben rechts fertig.");
									return true;
								}
								visitedN[indexTour[z-1][0]]--;
								visitedN[indexTour[z-1][1]]--;
								visitedN[indexTour[z-2][0]]--;
								visitedN[indexTour[z-2][1]]--;
								
								i = indexTour[z-2][0];
								k = indexTour[z-2][1];
								
								//und noch reset bool ab hier
								int leave =0;
								for (int h= i ; h < m ; h++) {
									for (int o= 1 ; o < m ; o++) { //ree aufpassen, was o ist!
										if( h == i && leave ==0) {
											o=k;
											leave++;
										}
										if( o > h) {
											visitedB[h][o] = false;
										}
										//alle elemente nach dem zuletzt rausgenommenen auf false
									}
								}
								
								indexTour[z-1][0] = 0; 
								indexTour[z-1][1] = 0;
								indexTour[z-2][0] = 0; 
								indexTour[z-2][1] = 0;
								
							
								break;
							}	
						}

						
						}	
					}
					
				}
			}
		//}
		return false;
		
	}


	private double[][] setupAdjMatrix(List<Punkt> cluster, List<LinienSegment> DelaunayK) {

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
		return AdjMatrix;
	}
	
	private void bestTour(List<Punkt> cluster) {
		// TODO test für n>4		
		double newLaenge, laengeCurrentBest =999999;
		int indexBesteTour =0;	
	
		for (int i=0 ; i < allTsp.size() ; i++) {	
			ArrayList<LinienSegment> tspSegmente = new ArrayList<LinienSegment>();
			
			for (int j =0 ; j < allTsp.get(i).length ; j++) {
				for (int k=j+1 ; k < allTsp.get(i).length ; k++) {
					if (allTsp.get(i)[j][k] == 1) {
						tspSegmente.add(new LinienSegment(cluster.get(j),cluster.get(k)));
					}
				}
			}		
				newLaenge = tspSegmente.get(0).TourLaenge(tspSegmente);
			
				if( i == 0) {
					laengeCurrentBest = newLaenge;
				}
				else {
					if( newLaenge < laengeCurrentBest) {
						laengeCurrentBest = newLaenge;
						indexBesteTour = i;
					}
				}
		}

		System.out.println("beste Tour hat Laenge: "+ laengeCurrentBest);
		LsAusAdjMatrix(allTsp.get(indexBesteTour), cluster);
	}	
	
	private void LsAusAdjMatrix(double[][] currentBest, List<Punkt> clusters) {
		for (int i=0; i< currentBest.length ; i++) {
			for (int j=i; j< currentBest.length ; j++) {
				if (currentBest[i][j] == 1) {
					FinalTspK.add(new LinienSegment(clusters.get(i), clusters.get(j)));
				}
			}
		}
		for (int i=0; i< FinalTspK.size(); i++) {
			FinalTspK.get(i).printLs();
		}
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

	private void printAdj(double[][] AdjMatrix ) {
		for ( int i=0; i<AdjMatrix.length ;i++) {
			for ( int j=0; j<AdjMatrix.length ;j++) {
				System.out.print(" : "+ AdjMatrix[i][j] );
				if( j == AdjMatrix.length -1 ) {
					System.out.println(" ");
				}
			}
		}
	}

	public ArrayList<LinienSegment> getFinal() {
    return FinalTspK;
  }

	public ArrayList<LinienSegment> finalEdges(double[][] currentBest){

		
	return FinalTspK;
	}
}
