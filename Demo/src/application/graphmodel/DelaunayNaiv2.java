package application.graphmodel;

import java.util.ArrayList;

public class DelaunayNaiv2 {

	ArrayList<Dreieck> DelaunayD = new ArrayList<Dreieck>();
	ArrayList<LinienSegment> DelaunayK = new ArrayList<LinienSegment>();
	
	public void execute(ArrayList<Punkt> p) {
		init(p);
		//Laufzeit verbessern duplikate löschen aber wöre immer noch zu langsam
		ArrayList<Dreieck> toDelete = testAll(p);
		DeleteNonDelaunay(toDelete);
		UniqueEdges();
		//printK();
	}
	
	public void fullGraph(ArrayList<Punkt> p) {
		for (int i=0 ; i < p.size() ; i++) {
			for (int k= i+1 ; k < p.size(); k++) {
				LinienSegment l = new LinienSegment(p.get(i), p.get(k));
				DelaunayK.add(l);
			}
		}
		
	}
	
	private void init(ArrayList<Punkt> pList) {
		if (pList.size()<3 && pList.size()>1) {
		
			LinienSegment l = new LinienSegment(pList.get(0), pList.get(1));
			DelaunayK.add(l);
			
			
		}
		
		
			for(int i=0; i < pList.size() ; i++ ) {
				for(int j=1; j < pList.size() ; j++ ) {
					for(int k=2; k < pList.size() ; k++ ) {
						if(i != j && i != k && j!= k) {
							Dreieck o = new Dreieck(pList.get(i), pList.get(j), pList.get(k));
								if(DelaunayD.contains(o) == false) {
									DelaunayD.add(o);	

								}				
						}
					}
				}
			}
		
	}	
	
	public ArrayList<Dreieck> testAll(ArrayList<Punkt> p) {
		ArrayList<Dreieck> toDelete = new ArrayList<Dreieck>();
		for (int i=0 ; i< DelaunayD.size(); i++) {
			for (int j=0 ; j< p.size(); j++) {
				boolean hope = imKreis(DelaunayD.get(i),p.get(j));
				if(hope == true) {
					toDelete.add(DelaunayD.get(i));
					break;
				}
			}
		}
		return toDelete;
	}
	
	public boolean imKreis(Dreieck d, Punkt p) {
		Punkt m = d.Mittelpunkt();
		LinienSegment r = new LinienSegment(m, p);	
		LinienSegment r1 = new LinienSegment(m, d.getEndpkt1());
		if( (r1.getGewicht() - r.getGewicht()) > 0.00000000001) { //rounding error...
			return true;
		}
		else return false;
	}
	
	private void DeleteNonDelaunay(ArrayList<Dreieck> toDelete) {
		for(int j=0 ; j < toDelete.size(); j++) {
			DelaunayD.remove(toDelete.get(j));
		}
	}
	
	public void UniqueEdges() {
		for (int i=0 ; i< DelaunayD.size(); i++) {
			LinienSegment l1 = new LinienSegment(DelaunayD.get(i).getEndpkt1(), DelaunayD.get(i).endpkt2);
			LinienSegment l2 = new LinienSegment(DelaunayD.get(i).endpkt2, DelaunayD.get(i).endpkt3);
			LinienSegment l3 = new LinienSegment(DelaunayD.get(i).getEndpkt1(), DelaunayD.get(i).endpkt3);
			if(i == 0) {
				DelaunayK.add(l1);
				DelaunayK.add(l2);
				DelaunayK.add(l3);
			}
			else {
				boolean unique = testEdge(l1);
						if(unique) {
							DelaunayK.add(l1);
						}
				boolean unique2 = testEdge(l2);
						if(unique2) {
							DelaunayK.add(l2);
						}
				boolean unique3 = testEdge(l3);
						if(unique3) {
							DelaunayK.add(l3);
						}
			}
		}
	}
	
	
	private boolean testEdge(LinienSegment li) {
		for (int j=0 ; j< DelaunayK.size(); j++) {
			if ( !(li.sameEdge(DelaunayK.get(j) ))) {
					if(((double)j == DelaunayK.size()-1)) {
					return true;
					}
				}
			else return false;
		}
		System.out.println("never here! error");
		return false;
	}
			
		//platzhaltermethode für später
	public void naivEinfügen(Punkt pi, ArrayList<Punkt> p) {
		for(int i=0; i < p.size(); i++){
			for(int j=1; j < p.size() ; j++ ) {
				Dreieck dNew = new Dreieck(pi, p.get(i),p.get(j));
				DelaunayD.add(dNew);
				
			}
		}
	}

	public void printK() {
		for(int i=0; i < DelaunayK.size(); i++){
          System.out.println("("+DelaunayK.get(i).endpkt1.getX() +", "+ DelaunayK.get(i).endpkt1.getY()+")");
          System.out.println("("+DelaunayK.get(i).endpkt2.getX() +", "+ DelaunayK.get(i).endpkt2.getY()+")");
          System.out.println("------");
      }
		 System.out.println("@@@@@@@");
	}
	
	public ArrayList<LinienSegment> finalEdges(){
		
		return DelaunayK;
	}

}
