import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ClusterMerge {

	public void execute(List<List<Punkt>> clusters, ArrayList<LinienSegment> mst, List<LinkedList<Punkt>> tsp) {
		mst.get(0).SortMstLaenge(mst);
		
		LinienSegment mstEdge =  findClosestCluster(clusters, mst, tsp);
		//verbindet Cluster, behaltet Tsp Eigenschaften bei.
	}


	public LinienSegment findClosestCluster(List<List<Punkt>> clusters, ArrayList<LinienSegment> mst, List<LinkedList<Punkt>> tsp) {
		LinienSegment ConnectingMstEdge = null;
		int a[]= {0,0,0};
		//findet immer connect Kante, solange mind 2 cluster
		for (int i=0 ; i <  mst.size(); i++) {
			a = ConnectsTwoClusters(clusters, mst.get(i));
			if( a[0] == 1 ) {
				ConnectingMstEdge = mst.get(i);
				break;
			}
		}
		
		ConnectTsp(ConnectingMstEdge,a[1] , a[2], tsp);
		ClusterCombine(a[1] , a[2] , clusters);
//		LinienSegment bestEdge = ConnectEdge(ConnectingMstEdge, a[1], a[2], tsp);	
//		
//		TspCombine(a[1], a[2], ConnectingMstEdge, bestEdge);
		return ConnectingMstEdge;
		
	}


	private int[] ConnectsTwoClusters(List<List<Punkt>> clusters, LinienSegment ls) {
		//Test ob die beiden Endpkte NICHT dem selben Cluster gehören
		//1.Cluster finden
		int a[]= {0,0,0}; 
		for( int i =0 ; i < clusters.size(); i++) {
			if (clusters.get(i).contains(ls.endpkt1) == true) {
				if (clusters.get(i).contains(ls.endpkt2) == true) {
					return a;
				}
				else {
					//2. Cluster finden
					for( int j =0 ; j < clusters.size(); j++) {
						if (clusters.get(j).contains(ls.endpkt2) == true) {
							a[0] =1; a[1] = i; a[2] =j;
							return a;
						}
					}
				}
			}
		}
		return null;
	}
	
	private LinienSegment ConnectTsp(LinienSegment mstEdge, int i, int j, List<LinkedList<Punkt>> tsp) {
		LinienSegment bestEdge;
		ArrayList<LinienSegment> candidate = new ArrayList<LinienSegment>();
		int minLaenge =0;
		boolean next;
		LinkedList<Punkt> temp= new LinkedList<Punkt>();
		
		int a = tsp.get(i).indexOf(mstEdge.endpkt1);
		int b = tsp.get(j).indexOf(mstEdge.endpkt2);
		
//		Punkt aNext = tsp.get(i).listIterator(a).next();
//		aNext.printPunkt();
//		System.out.println( tsp.get(i).listIterator(a).hasNext());
		
		mstEdge.printLs();
		
		
		//reicht das.? nicht kreuzen mit mst!
		LinienSegment c11 = new LinienSegment(tsp.get(i).listIterator(a).next() , tsp.get(j).listIterator(b).next());
	//	LinienSegment c11 = new LinienSegment(tsp.get(i).listIterator(a).previous() , tsp.get(j).listIterator(b).next());
	//	LinienSegment c22 = new LinienSegment(tsp.get(i).listIterator(a).next() ,  tsp.get(j).listIterator(b).previous());
		LinienSegment c22 = new LinienSegment(tsp.get(i).listIterator(a).previous(), tsp.get(j).listIterator(b).previous());
		

		System.out.println("Die 2 sind meine Kandidaten fürs verbinden, sollten keine mst Knoten haben");
		System.out.println("@@@@@@@");
		c11.printLs();
		c22.printLs();
		System.out.println("@@@@@@@");
		
		
		LinienSegment m11 = new LinienSegment(mstEdge.endpkt1 , tsp.get(i).listIterator(a).next() );
		LinienSegment m12 = new LinienSegment(mstEdge.endpkt1 , tsp.get(i).listIterator(a).previous());
		LinienSegment m21 = new LinienSegment(mstEdge.endpkt2 , tsp.get(j).listIterator(a).next());
		LinienSegment m22 = new LinienSegment(mstEdge.endpkt2 , tsp.get(j).listIterator(a).previous());
		
		double g0= Math.pow(c11.gewicht - m11.gewicht - m21.gewicht, 2);
		double g1= Math.pow(c22.gewicht - m12.gewicht - m22.gewicht,2);
		
		
		
		if( g0 < g1) {
			bestEdge = c11;
			next = true;
			temp.add(mstEdge.endpkt1);
			for (int p=0; p< tsp.get(j).size() -1 ; p++) { // -1, den der letzte Punkt soll mit vorherigem mstedge.next
				temp.add(tsp.get(j).listIterator(b - p).previous());
			//funktioniert nur mit connected linked list
			}
			
			for (int p=0; p< tsp.get(i).size() -1 ; p++) { // -1, den der letzte Punkt soll mit vorherigem mstedge.next
				temp.add(tsp.get(i).listIterator(a + p).next());
			}
		}
		else {
			bestEdge = c22;
			next = false;
		}
		tsp.remove(i);
		tsp.remove(j-1);
		
		tsp.add(temp);
		System.out.println(" size:"+ tsp.get(0).size() );
		return bestEdge;
	}

	public void ClusterCombine(int i, int j, List<List<Punkt>> clusters) {
		clusters.get(i).addAll(clusters.get(j));
		clusters.remove(j);
	}


}
