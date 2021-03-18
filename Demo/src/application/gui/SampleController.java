 package application.gui;
 
 import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;

import application.graphmodel.Builder;
import application.graphmodel.DelaunayNaiv2;
import application.graphmodel.Dreieck;
import application.graphmodel.Kmeans;
import application.graphmodel.LinienSegment;
import application.graphmodel.Mst_all;
import application.graphmodel.Point;
import application.graphmodel.Position;
import application.graphmodel.Punkt;
import application.graphmodel.clusterCentroid;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.Tooltip;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.transform.Translate;
import javafx.stage.FileChooser;
//import test.Punkt;


public class SampleController {
	

	
	@FXML
	Pane world;
	
	int k = 4;
	Builder graphs;
	Group lines = new Group();
	Group ccenters = new Group();
	ArrayList<Line> edges = new ArrayList<Line>();
	Group delEdges = new Group();
	Group points = new Group();
	Group mstEdges = new Group();
	Group globalMst = new Group();
		
	@FXML
	public void initialize() {																	//initialisiert Das Feld
		
		
		world.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		world.getChildren().clear();															//löscht vorher eventuell vorhandene Punktmenge
		//world.setScaleX(1000);
		//world.setScaleY(1000);
		delEdges.setScaleX(1);
		delEdges.setScaleX(1);
		graphs = new Builder(world, 0);
		
	}
	
	
	@FXML
	public void generate() { 																	//generiert punkte zuffällig, 
		world.getChildren().clear();
		lines.getChildren().clear();
		points.getChildren().clear();
		delEdges.getChildren().clear();
		graphs.punkte.clear(); 																	//löscht vorher eventuell vorhandene Punktmenge
		graphs = new Builder(world, 10);
		for (int i=0; i< graphs.punkte.size(); i++) {
			Circle c = graphs.punkte.get(i).getC();
			points.getChildren().add(c);
		}
		world.getChildren().add(points);
		graphs.draw();
	}
	
	@FXML
	public void mstGlobal() {
		world.getChildren().remove(mstEdges);
		world.getChildren().remove(delEdges);
		world.getChildren().remove(globalMst);

		delEdges.getChildren().clear();
		mstEdges.getChildren().clear();
		globalMst.getChildren().clear();
		ArrayList<Punkt> punkte = new ArrayList<Punkt>();
		
		for (Point p : graphs.punkte) { 
			
			
			Punkt t = new Punkt(p.getX(), p.getY());
			punkte.add(t);
		}
		
		Mst_all tester = new Mst_all();
		
		tester.execute(punkte);
		List <LinienSegment> mstGlobals = tester.getFinalKanten();
		
		for (LinienSegment l : mstGlobals) {
			
			Punkt x = l.getEndpkt1();
			Punkt y = l.getEndpkt2();
			
			Line g = new Line(x.getX(),x.getY(), y.getX(), y.getY());
			globalMst.getChildren().add(g);
			
		}
		
		world.getChildren().add(globalMst);
	}
	
	
	@FXML
	public void undo() {																		// zuletzt hinzugefügten punkt entfernen
		
	
		if (graphs.punkte.isEmpty()==false ) {
			world.getChildren().remove(graphs.punkte.size()-1);
			graphs.punkte.remove(graphs.punkte.size()-1);
			lines.getChildren().clear();
			edges.clear();
			ccenters.getChildren().clear();
			graphs.draw();
			
		}
		
		
		else {return;}
	}
	
	@FXML
	public List<List<LinienSegment>> mst() {
		world.getChildren().remove(mstEdges);
		world.getChildren().remove(delEdges);
		delEdges.getChildren().clear();
		mstEdges.getChildren().clear();
		List<List<Point>> clusters = clustering();
		List<List<LinienSegment>> triKanten = triangulation();
		delEdges.getChildren().clear();
		
		List<List<LinienSegment>> mstKanten = new ArrayList<List<LinienSegment>>();

		for (int i = 0; i<clusters.size(); i++) {
			
			Collections.sort(triKanten.get(i));
			List <LinienSegment> clusterMst = new ArrayList<LinienSegment>();
			mstKanten.add(clusterMst);
			List<Punkt> pkt = new ArrayList<Punkt>();
			
			

			
			
			for (int j = 0; j<triKanten.get(i).size();j++) {
				
				Punkt a = triKanten.get(i).get(j).getEndpkt1();
				Punkt b = triKanten.get(i).get(j).getEndpkt2();
				if (!(pkt.contains(a))) {pkt.add(a);}
				if (!(pkt.contains(b))) {pkt.add(b);}
				
				
				
			}
			

				
			for (Punkt p : pkt) {
				pkt.get(pkt.indexOf(p)).addReachable(p);
				System.out.println("-----" + p.getX() + ", " + p.getY());
			}
		
			
			for (int j = 0; j<triKanten.get(i).size();j++) {
				
				Punkt x = triKanten.get(i).get(j).getEndpkt1();
				Punkt y = triKanten.get(i).get(j).getEndpkt2();

				if (!pkt.get(pkt.indexOf(x)).getReachable().contains(y) && !pkt.get(pkt.indexOf(y)).getReachable().contains(x))  {
					
					
					Line l = new Line(x.getX(), x.getY(), y.getX(), y.getY());
					mstEdges.getChildren().add(l);
					mstKanten.get(i).add(triKanten.get(i).get(j));
					
					for (Punkt p : pkt.get(pkt.indexOf(y)).getReachable()) {
						if (!pkt.get(pkt.indexOf(x)).getReachable().contains(p)) {pkt.get(pkt.indexOf(x)).addReachable(p);}
						
					}
					
					for (Punkt p : pkt.get(pkt.indexOf(x)).getReachable()) {
						if (!pkt.get(pkt.indexOf(y)).getReachable().contains(p)) {pkt.get(pkt.indexOf(y)).addReachable(p);}
						
					}
					
					pkt.get(pkt.indexOf(x)).addReachable(y);
					

					pkt.get(pkt.indexOf(y)).addReachable(x);
					

					for (Punkt t : pkt) {
						
						if (t.getReachable().contains(x)) {
							
							for (Punkt c : x.getReachable()) {
								
								if (!t.getReachable().contains(c)) {t.addReachable(c);}
								
							}
							
							
						}
					}
					
					for (Punkt t : pkt) {
						
						if (t.getReachable().contains(y)) {
							
							for (Punkt c : y.getReachable()) {
								
								if (!t.getReachable().contains(c)) {t.addReachable(c);}
								
							}
						}
					}
				}
			}
		}
		world.getChildren().add(mstEdges);
		return mstKanten;
	}
				
	
	
	
	@FXML
	public void load() {
		graphs.punkte.clear();
		FileChooser fileChooser = new FileChooser();
		FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("TXT files (*.txt)", "*.txt");
		fileChooser.getExtensionFilters().add(extFilter);
		fileChooser.setTitle("Open Resource File");
		
		File selectedFile = fileChooser.showOpenDialog(null); 
		if (selectedFile != null) {
			
			clear(); 
			Scanner scanner;
			try {
				
				scanner = new Scanner((selectedFile), "UTF-8");
				scanner.useLocale(Locale.GERMANY);
				double x,y;
				String s;
				
				while(scanner.hasNext()) {
					
					s = scanner.next();
					if ( scanner.hasNext()) {
						
						
						x = Double.parseDouble(scanner.next());
						if ( scanner.hasNext()) {
							
							y = Double.parseDouble(scanner.next());
						}
						
						else {
							
							throw(new FileNotFoundException());
						}
					}
						
					
				
					else {
						
						throw(new FileNotFoundException());
						
					}
				Point p = new Point(world);
				p.setPos(x, y);
				graphs.punkte.add(p);
				
				}
				
				scanner.close();
				double xMax = 0;
				double yMax = 0;
				
				while (xMax > 2000 || yMax > 2000 || xMax < 800 || yMax < 800) {
					xMax = 0;
					yMax = 0;
					for (int i =0; i<graphs.punkte.size();i++) {
					
						if (graphs.punkte.get(i).getX() > xMax) {xMax = graphs.punkte.get(i).getX();}
						if (graphs.punkte.get(i).getY() > yMax) {yMax = graphs.punkte.get(i).getY();}

						System.out.println("("+graphs.punkte.get(i).getX() +", "+ graphs.punkte.get(i).getY()+")");
						System.out.println("------");
					}
				
					if(xMax > 2000 || yMax > 2000) {
						for (int i = 0; i<graphs.punkte.size(); i++) {
						
							graphs.punkte.get(i).setPos((0.5)*graphs.punkte.get(i).getX(), (0.5)*graphs.punkte.get(i).getY()); 
						}
					}
					if (xMax < 800 || yMax < 800) {
						for (int i = 0; i<graphs.punkte.size(); i++) {
							
							graphs.punkte.get(i).setPos((2)*graphs.punkte.get(i).getX(), (2)*graphs.punkte.get(i).getY()); 
						}
					}
				}
				
				for (int i=0; i< graphs.punkte.size(); i++) {
					Circle c = graphs.punkte.get(i).getC();
					points.getChildren().add(c);
				}
				world.getChildren().addAll(points);
				graphs.draw();
				
			}
			catch (FileNotFoundException e) {
				
				e.printStackTrace();
				System.out.println("Datei nicht gefunden oder Koordinaten ungültig.");
			}
			
		}
		
		
	}
	
	@FXML
	public void handleOnMouseClicked(MouseEvent event) {										//einzelnen Punkt manuell einfügen am Mauscursor

		if (!event.isControlDown() && (!event.isAltDown())) {
			Point p = new Point(world);
			p.setPos(event.getX(), event.getY());
			graphs.punkte.add(p);
			graphs.draw();
			System.out.println(p.getX() +", "+ p.getY());
			
		}

		
		
	} 
	
		     
	@FXML
	public void clear() {																		//Alle Punkte löschen, liste leeren
		
		world.getChildren().clear();
		lines.getChildren().clear();
		ccenters.getChildren().clear();
		graphs.punkte.clear();
		delEdges.getChildren().clear();
		globalMst.getChildren().clear();

	}
	
	
	
	
	
	public List<List<Point>> clustering() {
		world.getChildren().remove(ccenters);
		ccenters.getChildren().clear();
		
		// K-Means: sinnvoll, liefert sogar voronoi regionen. Frage: wie ist k zu wählen
		ArrayList<clusterCentroid> centers = new ArrayList<clusterCentroid>();	
		ArrayList<clusterCentroid> centersTemp = new ArrayList<clusterCentroid>();
		List<List<Point>> clustersFinal = new ArrayList<List<Point>>();
		boolean flag = true;
		double maxX = 0;																			
		double minX = graphs.punkte.get(0).getX();													
		double maxY = 0;																			
		double minY = graphs.punkte.get(0).getY();													
																									
		for (Point p : graphs.punkte) {																
																									
			if (maxX < p.getX() ) {maxX = p.getX();}
			if (minX > p.getX() ) {minX = p.getX();}
			if (maxY < p.getY() ) {maxY = p.getY();}
			if (minY > p.getY() ) {minY = p.getY();}
			
		}																							// Initialisierung der Clusterzentren
		
		for (int i = 0; i<k;i++) {
			
			List<Point> clusterList = new ArrayList<>();
			clustersFinal.add(clusterList);
			
			double xCord = (Math.random() * (maxX-minX)) + minX;
			double yCord = (Math.random() * (maxY-minY)) + minY;
			
			centers.add( new clusterCentroid(xCord, yCord));

			
		}

		
		do{
		int trigger = 0;
		clustersFinal.clear();
		centersTemp.clear();
		
		for (int i = 0; i<k;i++) {
			
			List<Point> clusterList = new ArrayList<>();
			clustersFinal.add(clusterList);
		}
			
			for (Point p : graphs.punkte) {										//für alle punkte die minimale Distanz zum aktuellen Zentrum berechnen
			
				int cl = 0;
				double minDist = centers.get(0).distance(p); 
			
				for (clusterCentroid c : centers) {
				
					double dist = c.distance(p);
					if (dist < minDist) {
					
						cl = centers.indexOf(c);
						minDist = dist;
					}
				}
			
			clustersFinal.get(cl).add(p);
		}
		for (clusterCentroid c : centers) {										// cluster zentren updaten
			int index = centers.indexOf(c);
			int count = 0;
			double newX = 0;
			double newY = 0;
			double valueX = 0;
			double valueY = 0;
				
			for (Point t : clustersFinal.get(index)) {
				count += 1;
				newX += t.getX();
				newY += t.getY();
			}
				
			valueX = Math.round(newX / count);
			valueY = Math.round(newY / count);
			
			if (Math.round(c.getX()) == valueX && Math.round(c.getY()) == valueY) {		//abrruchbedingung (keine zentren-updates mehr)
				
				trigger +=1;
			}
			
			c.setX(valueX);
			c.setY(valueY);
			
		}
		
		if (trigger == 4) flag = false;
			

	}while(flag == true);
		
		   
	
		   for (Point t : clustersFinal.get(0)) {
			   
			   t.c.setFill(Color.RED);
			   
			   Circle z = new Circle(10, Color.WHITE);
			   z.setStroke(Color.RED);
			   z.setTranslateX(centers.get(0).getX());
			   z.setTranslateY(centers.get(0).getY());
			   z.setOpacity(0.3);
			   ccenters.getChildren().add(z);
			   
		   }
		   
		   for (Point t : clustersFinal.get(1)) {
			   
			   t.c.setFill(Color.GREEN);
			   
			   Circle z = new Circle(10, Color.WHITE);
			   z.setStroke(Color.GREEN);
			   z.setTranslateX(centers.get(1).getX());
			   z.setTranslateY(centers.get(1).getY());
			   z.setOpacity(0.3);
			   ccenters.getChildren().add(z);
		   }
		   
		   for (Point t : clustersFinal.get(2)) {
			   
			   t.c.setFill(Color.YELLOW);
			   
			   Circle z = new Circle(10, Color.WHITE);
			   z.setStroke(Color.YELLOW);
			   z.setTranslateX(centers.get(2).getX());
			   z.setTranslateY(centers.get(2).getY());
			   z.setOpacity(0.3);
			   ccenters.getChildren().add(z);
		   }
		   
		   for (Point t : clustersFinal.get(3)) {
			   
			   t.c.setFill(Color.PURPLE);
			   
			   Circle z = new Circle(10, Color.WHITE);
			   z.setStroke(Color.PURPLE);
			   z.setTranslateX(centers.get(3).getX());
			   z.setTranslateY(centers.get(3).getY());
			   z.setOpacity(0.3);
			   ccenters.getChildren().add(z);
		   }
		   
		   world.getChildren().addAll(ccenters);
			
		   return clustersFinal;
		} 
	
//	@FXML
	//public void slidingUp() {

	//	world.setScaleX(world.getScaleX()+10);
	//	world.setScaleY(world.getScaleY()+10);

	//}
	
	//@FXML
	//public void slindingDown() {
		
	//	world.setScaleX(world.getScaleX()-10);
	//	world.setScaleY(world.getScaleY()-10);

	//}
		
	
	public List<List<LinienSegment>> triangulation() {
		delEdges.getChildren().clear();
		world.getChildren().remove(delEdges);
		List<List<LinienSegment>> triKanten = new ArrayList<List<LinienSegment>>();
		
		List<List<Point>> cl = clustering();
		for (int z = 0; z<cl.size(); z++) {
			
			
			DelaunayNaiv2 del = new DelaunayNaiv2();
			ArrayList<Punkt> pts = new ArrayList<Punkt>();
		
			for(int i = 0; i<cl.get(z).size();i++) {	
				pts.add(new Punkt(cl.get(z).get(i).getX(), cl.get(z).get(i).getY()));
			}
		
			del.execute(pts);
			ArrayList<LinienSegment> edges = del.finalEdges();
			triKanten.add(edges);

			for (int i = 0; i<edges.size(); i++) {
				
				Punkt x = edges.get(i).getEndpkt1();
				Punkt y = edges.get(i).getEndpkt2();
				
				Line l = new Line(x.getX(), x.getY(), y.getX(), y.getY());
				
				delEdges.getChildren().add(l);
			
			}
			
		
		}
		world.getChildren().addAll(delEdges);
		return triKanten;
	}
	
	
	@FXML
	public void handleOnMousePressed(MouseEvent event){
		
		
		
			for (Point p : graphs.punkte) {
				

					p.move();
					
			}
			lines.getChildren().clear();
			delEdges.getChildren().clear();
			edges.clear();
			
	}

	
	@FXML public void zoom(MouseEvent event) {
		final double SCALE_DELTA = 1.1;
		
		Group g = new Group();
		
		for (Point p : graphs.punkte) {
			
			g.getChildren().add(p.c);
		}
		
		world.getChildren().add(g);
		world.setOnScroll(new EventHandler<ScrollEvent>() {
			
		  @Override public void handle(ScrollEvent event) {
		    event.consume();

		    if (event.getDeltaY() == 0) {
		      return;
		    }

		    double scaleFactor =
		      (event.getDeltaY() > 0)
		        ? SCALE_DELTA
		        : 1/SCALE_DELTA;

		    g.setScaleX(g.getScaleX() * scaleFactor);
		    g.setScaleY(g.getScaleY() * scaleFactor);
		  }
		});
		
	}
		
}
	

	
	
	

