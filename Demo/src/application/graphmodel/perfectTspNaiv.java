import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class perfectTspNaiv {
	ArrayList<double[][]> allTsp = new ArrayList<double[][]>();
	
	public void execute(List<List<Punkt>> clusters, ArrayList<LinienSegment> DelaunayK) {
		for (int i=0 ; i< clusters.size() ; i++) { // 1 vs clusters.size()
			setupAdjMatrix(clusters.get(i), DelaunayK); //clusters.get(i)
			getTspTour(clusters.get(i), i);
		}
		
	}

	private void setupAdjMatrix(List<Punkt> clusters, ArrayList<LinienSegment> DelaunayK) {
		int m = clusters.size();
		boolean added = false;
		double[][] AdjMatrix = new double[m][m];
		
		for (int i=0 ; i < m ; i++) { 
			for (int j=0 ; j < m ; j++) {
				if (i == j) AdjMatrix[i][j] = 0;
				else {
					for (int k = 0 ; k < DelaunayK.size(); k++) {
						if ( (DelaunayK.get(k).sameEdge(new LinienSegment(clusters.get(i),clusters.get(j))) == true)) {
							AdjMatrix[i][j] = 1;
							added = true;
							break;
						}
						if (added == false) AdjMatrix[i][j] = 0;
					}
				}
			}
		}
		allTsp.add(AdjMatrix);
		printAdj(AdjMatrix);
	}
	

	private void getTspTour(List<Punkt> clusters, int l) {
		int m = clusters.size();
		int[][] indexTour = new int[m][1];
		int[] visitedN = new int[m];
		Arrays.fill(visitedN, 0);

		
		
		boolean[][] visitedB = new boolean[m][m];
		//visited[i][k] == false
		
		for (int i=0 ; i < m ; i++) {	//reicht es hier nur für die erste zeile den algo? sind damit alle fälle drin?
			for (int k=i+1 ; k < m ; k++) {
				if (allTsp.get(l)[i][k]  == 1 && visitedB[i][k] == false) {
						indexTour[i][0] = i; 
						indexTour[i][1] = k;
						visitedB[i][k] = true;
						
						visitedN[i]++;
						visitedN[k]++;
						
						
						
						if(visitedN[i] >2 || visitedN[k] > 2) {
							//ein punkt hat mehr als 2 Nachbarn in dieser Tour, abbruch!
							visitedN[i]--;
							visitedN[k]--;
						}
						
						break;
				}
				if(k == m-1) {
			// suche die 2 visited N == 1(x,y), wenn  allTsp.get(l)[x][y]  == 1 -> tour gefunden
				}
			}
			//reset all visited bool für alle i > i auf false bei == 1
			
			//reset all visited N auf 0? ALLE!?
			
			//switch i und k loop? reihen vs spalten weise
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
		
}
