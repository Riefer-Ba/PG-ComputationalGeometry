package application.graphmodel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Mst_all {

	
ArrayList<LinienSegment> mstKanten = new ArrayList<LinienSegment>();
	
	
	
	public void execute(ArrayList <Punkt> p) {
		
		DelaunayNaiv2 del = new DelaunayNaiv2();

		del.execute(p);
		List<LinienSegment> delKanten = del.DelaunayK;
		
		
		
		Collections.sort(delKanten);
		
		
		for(Punkt x : p) {
			
			p.get(p.indexOf(x)).addReachable(x);
		}
		
		for (LinienSegment l : delKanten) {
			
			Punkt x = l.getEndpkt1();
			Punkt y = l.getEndpkt2();
			
			if (!p.get(p.indexOf(x)).getReachable().contains(y) && !p.get(p.indexOf(y)).getReachable().contains(x)) {
				
				mstKanten.add(l);
				
			}
			
			for(Punkt t : p.get(p.indexOf(y)).getReachable()) {
				
				if(!p.get(p.indexOf(x)).getReachable().contains(t)) {p.get(p.indexOf(x)).addReachable(t);}
				
			}
			
			for(Punkt t : p.get(p.indexOf(x)).getReachable()) {
				
				if(!p.get(p.indexOf(y)).getReachable().contains(t)) {p.get(p.indexOf(y)).addReachable(t);}
				
			}
			
			p.get(p.indexOf(x)).addReachable(y);
			p.get(p.indexOf(y)).addReachable(x);
			
			for (Punkt t : p) {
				
				if (t.getReachable().contains(x)) {
					
					for (Punkt c : x.getReachable()) {
						
						if (!t.getReachable().contains(c)) {t.addReachable(c);}
						
					}
					
					
				}
			}
			
			for (Punkt t : p) {
				
				if (t.getReachable().contains(y)) {
					
					for (Punkt c : y.getReachable()) {
						
						if (!t.getReachable().contains(c)) {t.addReachable(c);}
						
					}
				}
			}
			
		}
		
		
		
	}
	
	
	
	
	public ArrayList <LinienSegment> getFinalKanten(){
		
		return mstKanten;
	}
	
	
	
	


}
