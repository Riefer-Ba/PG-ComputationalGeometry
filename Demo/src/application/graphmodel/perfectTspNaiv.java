import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class perfectTspNaiv{
	ArrayList<double[][]> allTsp = new ArrayList<double[][]>();
	ArrayList<LinienSegment> FinalTspK = new ArrayList<LinienSegment>();

	
	public void execute(List<Punkt> clusters, ArrayList<LinienSegment> DelaunayK) {
			
		double[][] Adj = setupAdjMatrix(clusters , DelaunayK);
		
		if(Adj[Adj.length-2][Adj.length-1] == 0) {
			Adj[Adj.length-2][Adj.length-1] =1;
			Adj[Adj.length-1][Adj.length-2] =1;
		}
		
//		if(Adj[Adj.length-2][Adj.length-1] == 0) {
//			if(Adj[0][Adj.length-1] == 1) {
//				Punkt temp =clusters.get(0);
//				clusters.add(0, clusters.get(Adj.length-2)); //-1?
//				clusters.remove(1);
//				clusters.add(Adj.length, temp);
//				
//			}
//		}
		
		printAdj(Adj);		//for test
		System.out.println("$$$");
		
		if(clusters.size() > 3) {
			getTspTour(clusters, Adj);
			System.out.println(allTsp.size());
			bestTour(clusters);
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
						
						
						
						
						//das hier muss geämdert werden für nuen algo
//						indexTour[i][0] = i; 
//						indexTour[i][1] = k;

					//	for(int p=i ;p< m; p++) {
						//	if(indexTour[p][0] == 0 && indexTour[p][1] ==0) {
								indexTour[i][0] = i; 
								indexTour[i][1] = k;
							//	break;
						//	}
					//	}
						
						
						/////
						
						printIndexTour(indexTour);
						System.out.println("%%%%%%"); 
						
						visitedN[i]++;
						visitedN[k]++;
												
						
						if(visitedN[i] >2 || visitedN[k] > 2) {
							System.out.println("abbruch Punkt mehr als 2x verwendet");
							//ein punkt hat mehr als 2 Nachbarn in dieser Tour, abbruch!
							
							System.out.println("%%%%%%%%");
							System.out.println(visitedN[0]);
							System.out.println(visitedN[1]);
							System.out.println(visitedN[2]);
							System.out.println(visitedN[3]);
							System.out.println(visitedN[4]);
							printIndexTour(indexTour);		
							System.out.println("%%%%%%%%");

							reset(indexTour, visitedB, visitedN, i,k, currentBest);
							
							
							System.out.println("TODO%%%%%%"); //TODO adapt für nicht letzter schritt
							
							
							
							int ind[] = resetLetzterSchritt(indexTour, visitedB, visitedN, i); //vorher auch k
							i = ind[0];
							k = ind[1] -1;
							
							if(ind[0] == -7  || ind[1] == -7) {
								System.out.println("fertig, alle Touren gefunden:");
									//	printIndexTour(indexTour); ist hier schon gibberish.
							//	LsAusAdjMatrix(currentBest, cluster);
								return true;
							}
							
							
						}		//soviele zeilen hochgehen, bis das erste false gefunden!
						
						else break; //if(indexTour[m-2][0] != 0 && indexTour[m-2][0] != 0){
						//	i++;		//Tour ist noch gültig, eine Zeile hochgehen
						//	break;
							
							
							//genau das selbe wie unten der code hier. auslagern?
							
							
							//this is the case
							
					}
					else {
						System.out.println("do nothing");
					//	break; //break? TODO
						//hier gibt es keine nicht bearbeitete 1 in der Reihe
					}
				}		
								
				//letzte Wert über der Hauptdiagonalen wurde bearbeitet.
				
			//	if( i == 0 && )			
				if(k == m-1 && i == m-1) { //&& i== m-2 hinzugefügt
				
					//Tour ist bis auf den letzten Punkt vollstänsig. Testet ob Tour geschlossen werden kann
					//testeet auch ob alle möglichen Touren schon bearbeitet wurden.
					int[] finished = LastIndexTestAusgelagert(indexTour, visitedB, visitedN, i, k, adj);
					
					if (finished[2] == 1) {
						return true;
					}
					else {
						i= finished[0];
						k= finished[1];
					}				
				} 	
			}		
		}
		System.out.println("das darf nicht ausgegeben werden!");
		
		return true;
	}

	private int[] LastIndexTestAusgelagert(int[][] indexTour, boolean[][] visitedB, int[] visitedN, int i, int k, double[][] adj) {
		int[] returns = {-3,-3, 0};
		
		
		System.out.println("in ausgelagert");
		
		int m = visitedN.length;
		int v1 =m+3; int v2=m+3;
		
		System.out.println("%%%%%%%%");
		System.out.println(visitedN[0]);
		System.out.println(visitedN[1]);
		System.out.println(visitedN[2]);
		System.out.println(visitedN[3]);
		printIndexTour(indexTour);		
		System.out.println("%%%%%%%%");
		
		
		System.out.println("obere diagonale, unten rechts");
		// suche die 2 visited N == 1(x,y), wenn  allTsp.get(l)[x][y]  == 1 -> tour gefunden
		
		
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
		
		if(v1 == m+3 || v2== m+3) {
			System.out.println("Fehler beim zusammenführen der letzten 2 Pkte, reset tour"); 
			int ind[] = resetLetzterSchritt(indexTour, visitedB, visitedN, i); //vorher auch k
			i = ind[0];
			k = ind[1] -1;
			 // reset gibt indx von letztem false aus.die for loop erhöht k um 1. preemtive strike -1.
			returns[0] =i;
			returns[1] =k;
			
			if(ind[0] == -7  || ind[1] == -7) {
				System.out.println("fertig die beste Tour bestimmen");
				//return true;
				returns[2] =1;
			}	
		}
		
		
		else if( adj[v1][v2] == 1 ) { //tsp tour gefunden
				System.out.println("neue Tour gefunden. to test");
				indexTour[m-1][0] = v1;
				indexTour[m-1][1] = v2;
				
				
				printIndexTour(indexTour);
				double[][] AdjTest = erzeugeAdj(indexTour);
				
				printAdj(AdjTest);
				allTsp.add(AdjTest);
				
				System.out.println(visitedN[0]);
				System.out.println(visitedN[1]);
				System.out.println(visitedN[2]);
				System.out.println(visitedN[3]);
				System.out.println(visitedN[4]);
				
				int ind[] = resetLetzterSchritt(indexTour, visitedB, visitedN, i); //vorher auch k
				i = ind[0];
				k = ind[1] -1;
				
				returns[0] =i;
				returns[1] =k;
				
				if(ind[0] == -7  || ind[1] == -7) {
					System.out.println("fertig die beste Tour bestimmen");
					//return true;
					returns[2] =1;
				}							
		}
		else if ( adj[v1][v2] == 0 ){ //keine tsp tour, verbindung nicht möglich. 2 reihen hoch. reset visited reihe k-2?1?
			int ind[] = resetLetzterSchritt(indexTour, visitedB, visitedN, i); //vorher auch k
			i = ind[0];
			k = ind[1] -1;
			
			returns[0] =i;
			returns[1] =k;
			
			if(ind[0] == -7  || ind[1] == -7) {
				System.out.println("fertig die beste Tour bestimmen");
				//return true;
				returns[2] =1;
			}	
		}

		return returns;
	}

		private int[] reset(int[][] indexTour, boolean[][] visitedB, int[] visitedN, int i, int k, double[][] adj) {
			// TODO Auto-generated method stub
			System.out.println("in the normal reset");
			//suche das nächste false, nimm das letzte Element aus der Tour.
			
			int count =0;
			int[] returns = {-3,-3, 0};
			int m = visitedB.length;
			int v1 =m+3; int v2=m+3;
			
			System.out.println("%%%%%%%%");
			System.out.println(visitedN[0]);
			System.out.println(visitedN[1]);
			System.out.println(visitedN[2]);
			System.out.println(visitedN[3]);
			printIndexTour(indexTour);		
			System.out.println("%%%%%%%%");
			
			
			//letztes element != 0,0 suchen, entfernen
			
			for(int p=i ;p< m; p++) {
				if(indexTour[p][0] == 0 && indexTour[p][1] ==0) {
					indexTour[p-1][0] = 0; 
					indexTour[p-1][1] = 0;
					break;
				}
			}
//			indexTour[][0] =0;
//			indexTour[][1] =0;
			
			visitedN[i]--;
			visitedN[k]--;
		//	visitedB[i][k]=false;
			
			
			printAdj(visitedB);
			
			//das erste false, das nachdem zuletzt hinzugefügten element auftritt.
			for ( int o=i ; o < m ; o++) {	
				if( i == o) {
					for (int h=k ; h < m  ; h++) { //TODO hier passt i nicht. der muss, wenn er in der selben reihe ist, bei k anfangen.
						//also sowas wie 
						if(visitedB[o][h] == false) { 
							returns[0] =o;
							returns[1] =h;
							visitedB[i][k]=false;
							
							System.out.println(o);
							System.out.println(h);
							
							
							return returns;
						}
					}
				}
				else {
					for (int h=1 ; h < m  ; h++) { //TODO hier passt i nicht. der muss, wenn er in der selben reihe ist, bei k anfangen.
											//also sowas wie 
						if(visitedB[o][h] == false) { 
							returns[0] =o;
							returns[1] =h;
						//	visitedB[i][k]=false;
							
							System.out.println(o);
							System.out.println(h);
							
							
							return returns;
						}
					}
				}
			}
			//sollte es keins geben, teste ob bereits alles besucht wurde.
					//erstmal in der Reihe von i!!!!!!!!!! 
			for ( int o=0 ; o < m ; o++) {	
				for (int h=o+1 ; h < m  ; h++) {
					if(visitedB[o][h] == false) { 
						
						
						returns[0] =o;
						returns[1] =h;
						
						System.out.println("hier müssen jetzt noch alle 3 Strukturen verändert werden.");
						
						System.out.println(o);
						System.out.println(h);
						
						
						return returns;
					}
				}
			}	
			
			
			System.out.println(i);
			System.out.println(k);
			printAdj(visitedB);
			System.out.println("shouldnt be here");

			return returns;
		
	}

	private int[] resetLetzterSchritt(int[][] indexTour, boolean[][] visitedB, int[] visitedN, int i) {
		// TODO Auto-generated method stub
		//ungültige Tour gefunden
		i--;
		int m = visitedB.length;
		int indexReset[] = {-7, -7};
		
		System.out.println("@@@@ Before");
		printAdj(visitedB);
		
		for ( int o=i ; o >= 0 ; o--) {	//int o=i ; o >= 0 ; o--
			for (int h=o+1 ; h < m  ; h++) {
			//	if (o != h) {
					if( (ganzeReiheBesucht(visitedB, o, indexTour, visitedN)) == true) {
						for(int p=o+1; p< m; p++) {
							visitedB[o][p] = false;
							}
							//2. die Anzahl, mit der ein Punkt in der Tour vorkommt 
							//visitedN[o]--;
							//visitedN[m-1]--;
							
//							System.out.println(visitedN[0]);
//							System.out.println(visitedN[1]);
//							System.out.println(visitedN[2]);
//							System.out.println(visitedN[3]);
						
							
							
							//3. die Kante aus der IndexTour herrausnehmen
							for( int w=0 ; w < m; w++) {
								//TODO Indexe richtig?
								if (indexTour[w][0] == o && indexTour[w][1] == m-1 ||
										indexTour[w][0] == m-1 && indexTour[w][1] == o) {
									indexTour[w][0] =0;
									indexTour[w][1] =0;
									//2. die Anzahl, mit der ein Punkt in der Tour vorkommt 
									visitedN[o]--;
									visitedN[m-1]--;
									
									for(int u=w; u< m; u++) {
										visitedN[indexTour[u][0]]--;
										visitedN[indexTour[u][1]]--;								
										indexTour[u][0] =0;
										indexTour[u][1] =0;
									}
									//und alle dahinter auch
								}							
							}
							break;
					}
					
					
					
					else { // das erste false in der Reihe finden
						if(visitedB[o][h] == false) {
						//das true vor dem ersten false ist derzeit in der index tour, rausnhemen um neue Tour zu starten.
						//visitedN[o]--;
						//visitedN[h-1]--;	
						
//						System.out.println(visitedN[0]);
//						System.out.println(visitedN[1]);
//						System.out.println(visitedN[2]);
//						System.out.println(visitedN[3]);
						
				//		printIndexTour(indexTour);
						
						for( int w=0 ; w < m; w++) {
							//TODO Indexe richtig?
							if (indexTour[w][0] == o && indexTour[w][1] == m-1 ||
									indexTour[w][0] == m-1 && indexTour[w][1] == o) {
								indexTour[w][0] =0;
								indexTour[w][1] =0;
								//2. die Anzahl, mit der ein Punkt in der Tour vorkommt 
								for(int u=w; u< m; u++) {							
									indexTour[u][0] =0;
									indexTour[u][1] =0;
								}
								//und alle dahinter auch
							}							
						}
						
						for(int ind =0;ind < m ; ind++) {
							visitedN[ind]=0;
						}
						
						for (int ind =0; ind < m ; ind++) {
							if ( indexTour[ind][0] == 0 && indexTour[ind][1] == 0) {
								break;
							}
							else {
								visitedN[indexTour[ind][0]]++;
								visitedN[indexTour[ind][1]]++;
							}
						}
						
						
					//	printIndexTour(indexTour);
							
						System.out.println("reset: Index des ersten false   i:"+ o+"k: "+ h);
						indexReset[0] = o;
						indexReset[1] = h;
						
						System.out.println("@@@@@ After:");
						printAdj(visitedB);
						System.out.println(visitedN[0]);
						System.out.println(visitedN[1]);
						System.out.println(visitedN[2]);
						System.out.println(visitedN[3]);
						System.out.println(visitedN[4]);
						System.out.println("@@@@@");
						printIndexTour(indexTour);
						 				
						return indexReset;	
					}	
				}
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
		for(int i=0; i< m ; i++) {
			if (visitedB[o][i] == false) return false;
		}
		return true;
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
	
	private void printAdj(boolean[][] AdjMatrix ) {
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

	private void bestTour(List<Punkt> cluster) {
		// TODO test für n>4		
		double newLaenge, laengeCurrentBest =999999;
		int indexBesteTour =0;	
		ArrayList<LinienSegment> tspSegmente = new ArrayList<LinienSegment>();

		for (int i=0 ; i < allTsp.size() ; i++) {	
			
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
					System.out.println(laengeCurrentBest);
					System.out.println(newLaenge);
				}
				else {
					if( newLaenge < laengeCurrentBest) {
						System.out.println("neue Tour besser");
						laengeCurrentBest = newLaenge;
						indexBesteTour = i;
					}
					else {
						System.out.println("neue Tour nicht besser"); //debugging only
					}
				}
				
			for (int h =0 ; h < tspSegmente.size() +3 ; h++) { //wieso +3? weiß ich auch nicht :)
				//TODO hier stimmt was nicht.....
				tspSegmente.remove(0);
			}
		//	tspSegmente.remove(0);
		//	tspSegmente.remove(0);
		}
		
		LsAusAdjMatrix(allTsp.get(indexBesteTour), cluster);
	}
	
	private void LsAusAdjMatrix(double[][] currentBest, List<Punkt> clusters) {
		// TODO Auto-generated method stub
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
	
	public ArrayList<LinienSegment> finalEdges(double[][] currentBest){
		
		return FinalTspK;
	}

}
