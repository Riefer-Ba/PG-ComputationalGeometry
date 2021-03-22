package application.graphmodel;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;

public class TspFinder {

	ArrayList <LinienSegment> mstK = new ArrayList <LinienSegment>();							//mst Kanten
	ArrayList<LinienSegment> tspK = new ArrayList <LinienSegment>();							//tspKanten
	ArrayList<Punkt> pkt = new ArrayList <Punkt>();												//punktmenge
	LinkedList<Punkt> pts = new LinkedList<Punkt>();											//linkedlist vom tsp der punkte
	ArrayList<LinienSegment> euler = new ArrayList <LinienSegment>();							//eulergraph (doppelter mst)
	
	public TspFinder(ArrayList<Punkt> punkte, ArrayList<LinienSegment> kanten) {
		
		pkt = punkte;		
		mstK = kanten;
		
	}
	
	public void execute() {
		
		generateEuler();
		generateLL();
		createTour();
	}
	
	public void generateEuler() {																			//der eulergraph wird erstellt 
		
		for(LinienSegment l : mstK) {																		//jedem Punkt werden seine Nachbarn in eienr ArrayList übergeben
			if(!l.getEndpkt1().getNb().contains(l.getEndpkt2())) {l.getEndpkt1().addNb(l.getEndpkt2());}
			if(!l.getEndpkt2().getNb().contains(l.getEndpkt1())) {l.getEndpkt2().addNb(l.getEndpkt1());}

			euler.add(l);
			LinienSegment rev = new LinienSegment(l.getEndpkt2(),l.getEndpkt1());
			euler.add(rev);
		}
	}
	
	
	public void generateLL() {																				//LinkedList wird erstellt, im Kantenzug/reihenfolge des Tsp
		boolean trigger = true;
		boolean tag;
		pts.add(euler.get(0).getEndpkt1());																	//startpunkt (ziemlich willkürlich), wird sofort zur tsp tour hinzugefügt
		Punkt x = euler.get(0).getEndpkt1();																//
		pkt.get(pkt.indexOf(x)).setvisited();																//
		
		while(trigger == true) {
			
			tag = false;
			
			for(LinienSegment l : euler) {																	// x (startpunkt) ist der aktuell betrachtete Punkt. Durchläuft alle Kanten und sucht diejenige,
																											// die Punkt x als Endpkt 1 hat und deren Endpkt 2 noch nicht besucht wurde auf der Tour.
				if(l.getEndpkt1() == x && pkt.get(pkt.indexOf(l.getEndpkt2())).getvisited() == false) {		// Das passiert insgesamt so lange bis es keinen unbesuchten Knoten mehr im Graphen gibt.
					
					pkt.get(pkt.indexOf(l.getEndpkt2())).setvisited();										// der Punkt Endpkt2 wirdals besucht markiert, 
					pts.addLast(l.getEndpkt2());															// zur tsp tour hinzugefügt und als neuer aktueller punkt für den nächsten durchlauf gewählt.
					tag = true;																				//
					x = l.getEndpkt2();																		//
					break;
				}
			}
			
			if (tag == false) {												
				
				Iterator<Punkt> t = pts.descendingIterator();												// wenn keine Kante mit x als pkt1 und unbesuchtem pkt2 gefunden wird:
				Punkt tp = null;																			// iteriere die linkedlist (die bisherige tsp tour) so lange zurück, bis der betrachtete punkt 
				while (t.hasNext()) {																		// zum ersten mal wieder mind. einen unbesuchte nachbarn hat. 										
					boolean stop = false;																	// dieser wird zur linkedlist hinzugefügt und als neues aktuelles x für den nächsten durchlauf markiert.
					
					Punkt a = pkt.get(pkt.indexOf(t.next()));
					
					for(Punkt c : a.getNb()) {
						
						if(c.getvisited() == false) {
							
							tp = c;
							
							pkt.get(pkt.indexOf(tp)).setvisited();
							x = c;
							stop = true;
							break;
							
						}
					}
					if(stop == true) {break;}
					
				}
				pts.addLast(tp);
				
			}
				
			trigger = false;
			for(Punkt p : pkt) {																			// hier findet nach jedem hinzugefügten punkt die überprüfung statt, ob noch unbesuchte knoten im graph existieren.
				
				if(p.getvisited() == false) {
					
					trigger = true;
					break;
				}
			}
			
	
			
		}
	
	
		
	}
	
	public void createTour() {																				// die punkte der linkedlist (fertige tsp tour) werden einfach ihrer folge nach per kanten verbunden 
		
		for(int i = 0; i<pts.size()-1;i++) {
			
			Punkt x = pts.get(i);
			Punkt y = pts.get(i+1);
			LinienSegment l = new LinienSegment(x,y);
			tspK.add(l);
		}
		
		LinienSegment last = new LinienSegment(pts.getFirst(),pts.getLast());								// der letzte knoten wird mit dem startknoten verbunden
		tspK.add(last);
	}
	
	public ArrayList<LinienSegment> getTspKanten(){
		
		
		return tspK;
	}
}
	
	
	
	

