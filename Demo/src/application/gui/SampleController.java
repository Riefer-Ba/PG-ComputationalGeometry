 package application.gui;
 
 import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.Scanner;


import application.graphmodel.Builder;
import application.graphmodel.ClusterMerge;
import application.graphmodel.ClusteringRandomKmeans;
import application.graphmodel.DelaunayNaiv2;
import application.graphmodel.Dreieck;
import application.graphmodel.Kmeans;
import application.graphmodel.LinienSegment;
import application.graphmodel.Mst_all;
import application.graphmodel.Pnkt;
import application.graphmodel.Point;
import application.graphmodel.Position;
import application.graphmodel.Punkt;
import application.graphmodel.TspFinder;
import application.graphmodel.clusterCentroid;
import application.graphmodel.perfectTspNaiv;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
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
	ObservableList<String> choiceBoxAlgo = FXCollections.observableArrayList("K++ means", "random k means");
	
	Builder graphs;
	Group lines = new Group();
	Group ccenters = new Group();
	ArrayList<Line> edges = new ArrayList<Line>();
	Group delEdges = new Group();
	Group points = new Group();
	Group mstEdges = new Group();
	Group globalMst = new Group();
	Group tspEdges = new Group();
	Group finalTsp = new Group();
	public int test = 10;
	List<List<Point>> globalCluster = new ArrayList<List<Point>>();
	@FXML
	public void initialize() {																	//initialisiert Das Feld
		
		
		world.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		world.getChildren().clear();															//löscht vorher eventuell vorhandene Punktmenge
		//world.setScaleX(1000);
		//world.setScaleY(1000);
		delEdges.setScaleX(1);
		delEdges.setScaleX(1);
		graphs = new Builder(world, 0);
		choicebox.setValue("Clustering:");
		choicebox.setItems(choiceBoxAlgo);
		//clustering();
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
		
		if(choicebox.getValue() == "K++ means") {
			
			test = 500;
			System.out.println(test);
			
		}
		
		else if(choicebox.getValue() == "random k means") {
			
			test = 2;
			System.out.println(test);
		}
	}
	
	@FXML
	public void kOrderTest() {
		
		ArrayList<Punkt> punkte = new ArrayList<Punkt>();
		
		for (Point p : graphs.punkte) { 
			
			
			Punkt t = new Punkt(p.getX(), p.getY());
			punkte.add(t);
		}
		ArrayList<Pnkt> pnkts = new ArrayList<Pnkt>();
		
		for (int i = 0; i< punkte.size();i++) {
			
			punkte.get(i).setLabel(i+1);
			Pnkt pt = new Pnkt(punkte.get(i).getX(),punkte.get(i).getY(),punkte.get(i).getLabel());
			pnkts.add(pt);
		}
		
		for(Pnkt p : pnkts) {
			
			System.out.println(p.getN());
		}
		
		ArrayList<LinienSegment> finalTour = finalAlgo();
		for (LinienSegment l : finalTour) {
			System.out.println("checkpoint loop");


			Pnkt p1 = new Pnkt(l.getEndpkt1().getX(),l.getEndpkt1().getY(),l.getEndpkt1().getLabel());
			Pnkt p2 = new Pnkt(l.getEndpkt2().getX(),l.getEndpkt2().getY(),l.getEndpkt2().getLabel());
			
			for (Pnkt p : pnkts) {
				
				if ( p == p1) {
					
					for (Pnkt q : pnkts) {
						
						if (q == p2) {
							
							int order1 = Pnkt.delaunayOrd(pnkts, p.getN(), q.getN());
							if(order1 >= 1) {
								
								System.out.println("higher order Kantee!!!!!!!!!!!!!!!!!");
						}
						
					} 
				}
			}
			//int order = Pnkt.delaunayOrd(pnkts, p1.getN(), p2.getN());
			//int order = Pnkt.delaunayOrd(pnkts, test1.getN(), test2.getN());
		//	if(order >= 1) {
				
			//	System.out.println("higher order Kantee!!!!!!!!!!!!!!!!!");
			}
			
		}
	}
	
	@FXML
	public ArrayList<LinienSegment> finalAlgo() {
		world.getChildren().remove(tspEdges);
		tspEdges.getChildren().clear();
		world.getChildren().remove(finalTsp);
		finalTsp.getChildren().clear();
		ArrayList<LinienSegment> mst = mstGlobal();
		ArrayList<double[][]> tsps = tsp_perfect();
		List<List<Point>> cl = globalCluster;
		List<List<Punkt>> clusters = new ArrayList<List<Punkt>>();
		
		//world.getChildren().remove(tspEdges);
		//tspEdges.getChildren().clear();
		//world.getChildren().remove(finalTsp);
		//finalTsp.getChildren().clear();
		world.getChildren().remove(tspEdges);
		tspEdges.getChildren().clear();
		world.getChildren().remove(globalMst);
		globalMst.getChildren().clear();
		for (List<Point> cluster : cl) {								// PROVISORISCH: Pointliste -> Punktliste
			ArrayList<Punkt> cls = new ArrayList<Punkt>();
			for (Point p : cluster) {
				
				Punkt x = new Punkt(p.getX(),p.getY());
				cls.add(x);
			}
		
			clusters.add(cls);
		}
		
		
		ClusterMerge finalStep = new ClusterMerge();
		
		finalStep.execute(clusters, mst, tsps);
		
		for (LinienSegment line : finalStep.getTsp()) {
			
			Line l = new Line(line.getEndpkt1().getX(), line.getEndpkt1().getY(), line.getEndpkt2().getX(), line.getEndpkt2().getY());
			finalTsp.getChildren().add(l);
			
		}
		
		world.getChildren().add(finalTsp);
		return finalStep.getTsp();
	}
	
	@FXML
	public ArrayList<LinienSegment> mstGlobal() {
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
		ArrayList <LinienSegment> mstGlobals = tester.getFinalKanten();
		
		for (LinienSegment l : mstGlobals) {
			
			Punkt x = l.getEndpkt1();
			Punkt y = l.getEndpkt2();
			
			Line g = new Line(x.getX(),x.getY(), y.getX(), y.getY());
			g.setStroke(Color.RED);
			globalMst.getChildren().add(g);
			
		}
		
		world.getChildren().add(globalMst);
		return mstGlobals;
	}
	
	
	@FXML
	public void undo() {																		// zuletzt hinzugefügten punkt entfernen
		
	
		if (graphs.punkte.isEmpty()==false ) {
			world.getChildren().remove(graphs.punkte.size()-1);
			graphs.punkte.remove(graphs.punkte.size()-1);
			//ccenters.getChildren().clear();
			

			world.getChildren().remove(mstEdges);
			world.getChildren().remove(globalMst);
			world.getChildren().remove(tspEdges);
			world.getChildren().remove(finalTsp);
			world.getChildren().remove(delEdges);

			mstEdges.getChildren().clear();
			globalMst.getChildren().clear();
			tspEdges.getChildren().clear();
			finalTsp.getChildren().clear();
			delEdges.getChildren().clear();
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
		List<List<Point>> clusters = globalCluster;
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
	private ChoiceBox choicebox;

	
	
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
	
	
	@FXML
	public List<LinkedList<Punkt>> tsp_mst() {
		
		world.getChildren().remove(tspEdges);
		world.getChildren().remove(mstEdges);
		mstEdges.getChildren().clear();
		world.getChildren().remove(finalTsp);
		finalTsp.getChildren().clear();
		tspEdges.getChildren().clear();
		delEdges.getChildren().clear();
		world.getChildren().remove(delEdges);
		List<List<Point>> cll = globalCluster;
		ArrayList<ArrayList<Punkt>> cl = new ArrayList<ArrayList<Punkt>>();
		List<LinkedList<Punkt>> tspClusters = new ArrayList<LinkedList<Punkt>>();
		
		for (List<Point> cluster : cll) {								// PROVISORISCH: Pointliste -> Punktliste
			ArrayList<Punkt> cls = new ArrayList<Punkt>();
			for (Point p : cluster) {
				
				Punkt x = new Punkt(p.getX(),p.getY());
				cls.add(x);
			}
		
			cl.add(cls);
		}

		for(ArrayList<Punkt> cluster : cl) {							//für jedes cluster einzelne tsp berechnung
			
			
			
				Mst_all cluster_mst = new Mst_all();
				cluster_mst.execute(cluster);
				ArrayList<LinienSegment> mstKanten = cluster_mst.getFinalKanten();
				if(mstKanten.size()>0) {
					
					TspFinder tsp = new TspFinder(cluster, mstKanten);					

					tsp.execute();												//tsp algo wird aufgerufen
					tspClusters.add(tsp.getLL());
					
					for(LinienSegment lin : tsp.getTspKanten()) {				//linien der tsp kanten zeichnen
			
						Line l = new Line(lin.getEndpkt1().getX(),lin.getEndpkt1().getY(), lin.getEndpkt2().getX(), lin.getEndpkt2().getY());
						tspEdges.getChildren().add(l);
						
				
					}
					
				}
				else if (mstKanten.size() == 0 ) {
					
						LinkedList<Punkt> einzelPunkt = new LinkedList<Punkt>();
						einzelPunkt.add(cluster.get(0));
						tspClusters.add(einzelPunkt);
					
					
				}
				System.out.println(mstKanten);									//nur zum debugging, kann weg
		}
		
		world.getChildren().add(tspEdges);
		return tspClusters;
	}
	
	
	@FXML
	public ArrayList<double[][]> tsp_perfect() {
		
		world.getChildren().remove(tspEdges);
		world.getChildren().remove(mstEdges);
		mstEdges.getChildren().clear();
		world.getChildren().remove(finalTsp);
		finalTsp.getChildren().clear();
		tspEdges.getChildren().clear();
		delEdges.getChildren().clear();
		world.getChildren().remove(delEdges);
		List<List<Point>> cll = globalCluster;
		List<List<Punkt>> cl = new ArrayList<List<Punkt>>();
		ArrayList<double[][]> tsps = new ArrayList<double[][]>();
		//List<LinkedList<Punkt>> tspClusters = new ArrayList<LinkedList<Punkt>>();
		
		for (List<Point> cluster : cll) {								// PROVISORISCH: Pointliste -> Punktliste
			ArrayList<Punkt> cls = new ArrayList<Punkt>();
			for (Point p : cluster) {
				
				Punkt x = new Punkt(p.getX(),p.getY());
				cls.add(x);
			}
		
			cl.add(cls);
		}
		
		
		List<List<LinienSegment>> delK = triangulation();

		delEdges.getChildren().clear();
		for(int i = 0; i < cl.size();i++) {							//für jedes cluster einzelne tsp berechnung
			
			perfectTspNaiv pt = new perfectTspNaiv();
			
			pt.execute(cl.get(i), delK.get(i));
			tsps.add(pt.getFinalAdj());
			for (LinienSegment lin : pt.getFinal()) {
				
				
				Line l = new Line(lin.getEndpkt1().getX(),lin.getEndpkt1().getY(), lin.getEndpkt2().getX(), lin.getEndpkt2().getY());
				tspEdges.getChildren().add(l);
			}
			
		}
		
		world.getChildren().add(tspEdges);
		//return tspClusters;
		return tsps;
	}
			

	
	
	
	
	
		public List<List<Point>> clustering() {
		world.getChildren().remove(ccenters);
		ccenters.getChildren().clear();
		globalCluster = null;
		boolean bed = false;
		/*
		// K-Means: sinnvoll, liefert sogar voronoi regionen. Frage: wie ist k zu wählen
		int k = 4;
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
		
		for (int i = 0; i<k;i++) {	// Zuweisungen und k ändern 
			
			
			if (k == 0) { //fürs erste, wenn random gewollt einfach Auskommentieren, ist eine gui erweiterung 
				//möglich? als switch case?
				
				//der erste ist immer noch zufällig!
			
			List<Point> clusterList = new ArrayList<>();
			clustersFinal.add(clusterList);
			
			double xCord = (Math.random() * (maxX-minX)) + minX;
			double yCord = (Math.random() * (maxY-minY)) + minY;
			
			centers.add( new clusterCentroid(xCord, yCord));
			}
			
			else {
				double[] cord  = neuerCentroid(graphs.punkte, centers);
				
				centers.add(new clusterCentroid(cord[0], cord[1]));
			}
			
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
		
		if (trigger == k) flag = false;
			

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
	
			Iterator <List<Point>> iterator = clustersFinal.iterator();
			
			while (iterator.hasNext()) {
				
				List<Point> vgl = iterator.next();
				if (iterator.next().isEmpty()) {
					iterator.remove();;
				}
				
			}
		   
		//	if(clustersFinal.get(0).isEmpty()==true) {clustersFinal.remove(clustersFinal.get(0));}
		//	if(clustersFinal.get(1).isEmpty()==true) {clustersFinal.remove(clustersFinal.get(1));}
		//	if(clustersFinal.get(2).isEmpty()==true) {clustersFinal.remove(clustersFinal.get(2));}
		//	if(clustersFinal.get(3).isEmpty()==true) {clustersFinal.remove(clustersFinal.get(3));}
		*/
		if(choicebox.getValue() == "K++ means") {
			
			bed = true;
			
			
		}
		
		else if(choicebox.getValue() == "random k means") {
			
			bed = false;
			
		}
		
		ClusteringRandomKmeans cl = new ClusteringRandomKmeans(graphs.punkte, bed);
		cl.execute();
		globalCluster = cl.getClusters();
		  // world.getChildren().addAll(ccenters);
			
		   return globalCluster;
		} 
	
	//used for k-means++ finden vom 2+-ten Cluster, Wahrscheinlichkeitsbasiert.
	private double[] neuerCentroid(ArrayList<Point> punkte, ArrayList<clusterCentroid> centers) {
		double[] cord = {0,0};
		double minDist = 10000;
		ArrayList<Double> DistArray = new ArrayList<Double>();
		ArrayList<Double> DuplikatedDist = new ArrayList<Double>();
		
		for(int i=0; i< punkte.size(); i++) {
			for(int k=0; k< centers.size(); k++) {
				double temp = Math.pow(centers.get(k).distance(punkte.get(i)), 2);
				if(temp < minDist) minDist=temp; 
			}
			DistArray.add(minDist);
			
			double repeat = DistArray.get(i)/50.0;	//D^2 für alle 50 Einheiten einmal einfügen
			for(int k=0; k < repeat ; k++) {			//auf größe anpassen!!!!!
				DuplikatedDist.add(DistArray.get(i));
			}
		}
		
		Random rnd = new Random();
		double r = DuplikatedDist.get(rnd.nextInt(DuplikatedDist.size()));
		
		cord[0]=punkte.get(DistArray.indexOf(r)).getX();
		cord[1]=punkte.get(DistArray.indexOf(r)).getX();
		
		return cord;
	}
		
	@FXML
	public List<List<LinienSegment>> triangulation() {
		world.getChildren().remove(tspEdges);
		world.getChildren().remove(mstEdges);
		mstEdges.getChildren().clear();
		world.getChildren().remove(finalTsp);
		finalTsp.getChildren().clear();
		tspEdges.getChildren().clear();
		delEdges.getChildren().clear();
		world.getChildren().remove(delEdges);
		List<List<LinienSegment>> triKanten = new ArrayList<List<LinienSegment>>();
		
		List<List<Point>> cl = globalCluster;
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

	
	
		
	
		
}
	

	
	
	

