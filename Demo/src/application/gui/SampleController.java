 package application.gui;
 
 import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
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
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
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
	ObservableList<String> choiceBoxTsp = FXCollections.observableArrayList("optimale Tsp", "MST-Heuristik Tsp");
	ObservableList<String> choiceBoxKanten = FXCollections.observableArrayList("Delaunay Kanten", "vollst. Graph");
	
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
	Group higherOrderEdges = new Group();
	public int test = 10;
	boolean tourmod = false;
	boolean kantenmod = false;
	ArrayList<double[][]> globalTspPerfect = new ArrayList<double[][]>();
	List<LinkedList<Punkt>> globalTspMst = new ArrayList<LinkedList<Punkt>>();
	ArrayList<LinienSegment> globalerMst = new ArrayList<LinienSegment>();
	List<List<Point>> globalCluster = new ArrayList<List<Point>>();
	ArrayList <Punkt> trav = new ArrayList<Punkt>();
	double toursize;
	@FXML
	public void initialize() {																	//initialisiert Das Feld
		
		
		world.setBackground(new Background(new BackgroundFill(Color.WHITE, null, null)));
		world.getChildren().clear();															//l√∂scht vorher eventuell vorhandene Punktmenge
		delEdges.setScaleX(1);
		delEdges.setScaleX(1);
		graphs = new Builder(world, 0);
		choicebox.setValue("Clustering Algorithmus:");
		choicebox.setItems(choiceBoxAlgo);
		cbtsp.setValue("Cluster-Tsp Algorithmus:");
		cbtsp.setItems(choiceBoxTsp);
		kanten.setValue("Kantenbasis: ");
		kanten.setItems(choiceBoxKanten);
		
		
	}
	
	
	@FXML
	public void generate() { 
		world.getChildren().remove(higherOrderEdges);
		higherOrderEdges.getChildren().clear();
		world.getChildren().clear();
		lines.getChildren().clear();
		points.getChildren().clear();
		delEdges.getChildren().clear();
		graphs.punkte.clear(); 																	//l√∂scht vorher eventuell vorhandene Punktmenge
		graphs = new Builder(world, 10);
		for (int i=0; i< graphs.punkte.size(); i++) {
			Circle c = graphs.punkte.get(i).getC();
			points.getChildren().add(c);
		}
		world.getChildren().add(points);
		graphs.draw();
		
		
	}
	
	@FXML
	public void kOrderTest() {
		world.getChildren().remove(higherOrderEdges);
		higherOrderEdges.getChildren().clear();
		boolean testing = false;
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
		
	
		ArrayList<LinienSegment> finalTour = finalAlgo();
		for (LinienSegment l : finalTour) {


			Pnkt p1 = new Pnkt(l.getEndpkt1().getX(),l.getEndpkt1().getY(),l.getEndpkt1().getLabel());
			Pnkt p2 = new Pnkt(l.getEndpkt2().getX(),l.getEndpkt2().getY(),l.getEndpkt2().getLabel());
			
			for (Pnkt p : pnkts) {
				
				if ( p == p1) {
					
					for (Pnkt q : pnkts) {
						
						if (q == p2) {
							
							int order1 = Pnkt.delaunayOrd(pnkts, p.getN(), q.getN());
							if(order1 >= 1) {
								
								testing = true;
								System.out.println("higher Order Kante:"+" ["+l.getEndpkt1().getX()+", "+l.getEndpkt1().getY()+"] "+ "["+l.getEndpkt2().getX()+", "+l.getEndpkt2().getY()+"]"+" | Ordnung: "+order1);
								Line lin = new Line(l.getEndpkt1().getX(),l.getEndpkt1().getY(), l.getEndpkt2().getX(), l.getEndpkt2().getY());
								higherOrderEdges.getChildren().add(lin);
						}
						
					} 
				}
			
			}

			}
			
		}
		if (testing == false) {System.out.println("Keine Higher Order Kanten genutzt.");}
		world.getChildren().add(higherOrderEdges);
	}
	
	@FXML
	public ArrayList<LinienSegment> finalAlgo() {
		world.getChildren().remove(higherOrderEdges);
		higherOrderEdges.getChildren().clear();
		trav.clear();
		world.getChildren().remove(globalMst);
		globalMst.getChildren().clear();
		world.getChildren().remove(tspEdges);
		tspEdges.getChildren().clear();
		world.getChildren().remove(finalTsp);
		finalTsp.getChildren().clear();
		ArrayList<LinienSegment> mst = new ArrayList<LinienSegment>();
		if(!globalerMst.isEmpty()) {
			
			mst = globalerMst;
		}
		
		else if(globalerMst.isEmpty()) {
			
			mst = mstGlobal();
		}
		
		//ArrayList<double[][]> tsps = tsp_perfect();
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
		
		if (tourmod == false) {
			if(!globalTspPerfect.isEmpty()) {
				finalStep.execute(clusters, mst, globalTspPerfect);
			}
		}
		
		else if(tourmod == true) {
			if(!globalTspMst.isEmpty()) {
				finalStep.execute(clusters, mst, globalTspMst);
				
			}
			
		}
		
		
		
		trav = finalStep.traverse;
		toursize = finalStep.size;
		for (LinienSegment line : finalStep.getTsp()) {
			
			Line l = new Line(line.getEndpkt1().getX(), line.getEndpkt1().getY(), line.getEndpkt2().getX(), line.getEndpkt2().getY());
			finalTsp.getChildren().add(l);
			
		}
		
		world.getChildren().add(finalTsp);
		return finalStep.getTsp();
	}
	
	@FXML
	public ArrayList<LinienSegment> mstGlobal() {
		world.getChildren().remove(higherOrderEdges);
		higherOrderEdges.getChildren().clear();
		world.getChildren().remove(mstEdges);
		world.getChildren().remove(delEdges);
		world.getChildren().remove(globalMst);
		world.getChildren().remove(tspEdges);

		delEdges.getChildren().clear();
		mstEdges.getChildren().clear();
		globalMst.getChildren().clear();
		tspEdges.getChildren().clear();
		if (!globalerMst.isEmpty()) {
			for(LinienSegment l : globalerMst) {
				
				Punkt x = l.getEndpkt1();
				Punkt y = l.getEndpkt2();
			
				Line g = new Line(x.getX(),x.getY(), y.getX(), y.getY());
				g.setStroke(Color.RED);
				globalMst.getChildren().add(g);
				
			}
			
			world.getChildren().add(globalMst);
			return globalerMst;
			
		}
		
		else {
			ArrayList<Punkt> punkte = new ArrayList<Punkt>();
		
			for (Point p : graphs.punkte) { 
			
			
				Punkt t = new Punkt(p.getX(), p.getY());
				punkte.add(t);
			}
			
			Mst_all tester = new Mst_all();
		
			tester.execute(punkte);
			ArrayList <LinienSegment> mstGlobals = tester.getFinalKanten();
			globalerMst = mstGlobals;
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
	}
	
	
	@FXML
	public void undo() {																		// zuletzt hinzugef√ºgten punkt entfernen
		
	
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
	
	@FXML public void speichern() {
		
		try {
		      File saveTour = new File("Sample "+graphs.punkte.size());
		      if (saveTour.createNewFile()) {
		        System.out.println("File created: " + saveTour.getName() + " in " + saveTour.getAbsolutePath());
		      } else {
		        System.out.println("File already exists.");
		      }
		    } catch (IOException e) {
		      System.out.println("An error occurred.");
		      e.printStackTrace();
		    }
		
	 try {
	  	        FileWriter myWriter = new FileWriter("Sample "+graphs.punkte.size());
	  	       
	  	        	if (!trav.isEmpty()) {
	  	       
	  	        		for (int i = 0; i<trav.size(); i++) {
	  	        			
	  	        			double x = trav.get(i).getX();
	  	        			double y = trav.get(i).getY();

	  	        			myWriter.write((i+1)+" "+x+" "+y+"\n");
	  	        			
	  	        		}
	  	        		
	  	        		myWriter.write("\n \n \n \n");
	  	        		myWriter.write("Tourl‰nge: "+toursize);
	  	        		
	  	        	}
	  	        
	  	        myWriter.close();
	  	        System.out.println("Successfully wrote to the file.");
	  	        
	  	        
	 		} catch (IOException e) {
	 			System.out.println("An error occurred.");
	 			e.printStackTrace();
	 			}
		
	
	}
	
	@FXML
	public List<List<LinienSegment>> mst() {
		

		world.getChildren().remove(higherOrderEdges);
		higherOrderEdges.getChildren().clear();
		world.getChildren().remove(mstEdges);
		world.getChildren().remove(delEdges);
		world.getChildren().remove(globalMst);
		globalMst.getChildren().clear();
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
	private ChoiceBox cbtsp;
	
	@FXML 
	private ChoiceBox kanten;

	
	
	@FXML
	public void load() {
		world.getChildren().remove(higherOrderEdges);
		higherOrderEdges.getChildren().clear();
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
			//	double xMax = 0;
			//	double yMax = 0;
				
			/**	while (xMax > 2000 || yMax > 2000 || xMax < 800 || yMax < 800) {
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
				**/
				world.getChildren().addAll(points);
				graphs.draw();
				
			}
			catch (FileNotFoundException e) {
				
				e.printStackTrace();
				System.out.println("Datei nicht gefunden oder Koordinaten ung¸ltig.");
			}
			
		}
		
		
	}
	
	@FXML
	public void handleOnMouseClicked(MouseEvent event) {										//einzelnen Punkt manuell einf√ºgen am Mauscursor

		if (!event.isControlDown() && (!event.isAltDown())) {
			Point p = new Point(world);
			p.setPos(event.getX(), event.getY());
			graphs.punkte.add(p);
			graphs.draw();
			System.out.println(p.getX() +", "+ p.getY());
			globalerMst.clear();
			trav.clear();
		}

		
		
	} 
	
		     
	@FXML
	public void clear() {	//Alle Punkte l√∂schen, liste leeren
		world.getChildren().remove(higherOrderEdges);
		higherOrderEdges.getChildren().clear();
		world.getChildren().clear();
		lines.getChildren().clear();
		ccenters.getChildren().clear();
		graphs.punkte.clear();
		delEdges.getChildren().clear();
		globalMst.getChildren().clear();

	}
	
	
	@FXML
	public List<LinkedList<Punkt>> tsp_mst() {
		
		world.getChildren().remove(higherOrderEdges);
		higherOrderEdges.getChildren().clear();
		world.getChildren().remove(globalMst);
		globalMst.getChildren().clear();
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

		for(ArrayList<Punkt> cluster : cl) {							//f√ºr jedes cluster einzelne tsp berechnung
			
			
			
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
				//System.out.println(mstKanten);									//nur zum debugging, kann weg
		}
		
		world.getChildren().add(tspEdges);
		return tspClusters;
	}
	
	
	@FXML
	public ArrayList<double[][]> tsp_perfect() {
		
		
		if (kanten.getValue() == "Delaunay Kanten") {
			
			kantenmod = false;
		}
		
		else if (kanten.getValue() == "vollst. Graph") {
			
			kantenmod = true;
		}
		
		world.getChildren().remove(higherOrderEdges);
		higherOrderEdges.getChildren().clear();
		world.getChildren().remove(globalMst);
		globalMst.getChildren().clear();
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
		List<List<LinienSegment>> delK = new ArrayList<List<LinienSegment>>();
		if (kantenmod == false) {
			
			delK = triangulation();
		}
		
		else if(kantenmod == true) {
			
			delK = new ArrayList<List<LinienSegment>>();
			
			for (List<Punkt> ps : cl) {
				DelaunayNaiv2 dv = new DelaunayNaiv2();
				dv.fullGraph(ps);
				List<LinienSegment> edg = dv.finalEdges();
				delK.add(edg);
				
			}
			
		}
		

		delEdges.getChildren().clear();
		for(int i = 0; i < cl.size();i++) {							//f√ºr jedes cluster einzelne tsp berechnung
			
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
			

	
	
		public void clusterTsp() { 	
			
			
			if (cbtsp.getValue() == "optimale Tsp") {
				
				tourmod = false;
				globalTspPerfect = tsp_perfect();
				
				
			}
			
			else if(cbtsp.getValue() == "MST-Heuristik Tsp") {
				
				tourmod = true;
				globalTspMst = tsp_mst();
				
			}
			
		}
		
		
		
	
	
	
	
	public List<List<Point>> clustering() {
		
		world.getChildren().remove(higherOrderEdges);
		higherOrderEdges.getChildren().clear();
		world.getChildren().remove(globalMst);
		globalMst.getChildren().clear();
		world.getChildren().remove(ccenters);
		ccenters.getChildren().clear();
		globalCluster = null;
		boolean bed = false;
		int size = 8;
		
		boolean first = true;
		
		while (first == true) {
			
			TextInputDialog tid = new TextInputDialog("");
			tid.setTitle("Clustergrˆﬂe w‰hlen");
			tid.setContentText("Wie groﬂ sollen die Cluster maximal sein?: (<=10 empfohlen)");
			Optional<String> result = tid.showAndWait();
			result.ifPresent(name -> System.out.println("Clustegrˆﬂe: " + name));
			String res = tid.getResult();
			
			size = Integer.parseInt(res);
			
			first = false;
		}
		
				
		if(choicebox.getValue() == "K++ means") {
			
			bed = true;
			
			
		}
		
		else if(choicebox.getValue() == "random k means") {
			
			bed = false;
			
		}
		
		ClusteringRandomKmeans cl = new ClusteringRandomKmeans(graphs.punkte, bed, size);
		cl.execute();
		
		
		globalCluster = cl.getClusters();
		return globalCluster;
} 
	

		
	@FXML
	public List<List<LinienSegment>> triangulation() {
		
		world.getChildren().remove(higherOrderEdges);
		higherOrderEdges.getChildren().clear();
		world.getChildren().remove(globalMst);
		globalMst.getChildren().clear();
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
	public List<List<LinienSegment>> completeGraph() {
		
		world.getChildren().remove(higherOrderEdges);
		higherOrderEdges.getChildren().clear();
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
		
			del.fullGraph(pts);
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
			globalerMst.clear();
			trav.clear();
			
	}

	
	
		
	
		
}
	

	
	
	


