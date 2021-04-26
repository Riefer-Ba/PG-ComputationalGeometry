# PG
PG Computational Geometry 
Nick Riefer &amp; Simon Schlingmann

SETUP:

-Disclaimer-  Dieser Setup Guide ist darauf ausgelegt, die Applikation in der Eclipse IDE (https://www.eclipse.org/) laufen zu lassen. 
              Für andere Entwicklungsumgebungen können die notwendigen Schritte variieren.
              
           


1. Installation der aktuellen eclips IDE (https://www.eclipse.org/)

2. E(fx)CLIPSE Plugin für Eclipse: in Ecplise auf Help -> Eclipse Marketplace -> per Suchzeile "E(fx)CLIPSE Plugin" auswählen und installieren

3. Download und Configuration JavaFX: Unter https://gluonhq.com/products/javafx/ die aktuelle long term support version (11.0.2) von Java Fx herunterladen und speichern. 
   Anschließend in Eclipse unter Window -> Preferences, und dort zu Java > Build Path > User Libraries navigieren. Dort unter "new" eine neue User Library anlegen, und
   dieser über "add JARs" die JAR Dateien der eben heruntergeladenen JavaFX Version, befindlich in "JavaFX.../lib" hinzufügen. Zuletzt mit "Apply and Close" bestätigen.

4. Hinzufügen der JavaFX Library zum Project Folder: Das Projekt in Eclipse laden. anschließend per Rechtsklick auf den Projektordner, und zu Build Path > configure
   Buildpath navigieren. Dort unter Libraries -> Classpath in der Leiste rechts "Add Library.." auswählen, und unter "User Library" die eben erzeugte JavaFX Library
   auswählen.
   
5. Run-Argumente: Per Rechtsklick auf die Main.java im Package application.gui unter "Run as" > "Run Configurations.." auswählen. Im öffnenden Fenster den Reiter   
   "Arguments" auswählen und im Feld "VM-arguments" folgende Zeile einfügen und YOUR\PATH\ durch den Speicherort von JavaFX aus Schritt 3 ersetzen: 
   
                                  --module-path "YOUR\PATH\lib" --add-modules javafx.controls,javafx.fxml
                                  


HINWEIS: Das Programm ist modular gebaut und muss auch so ausgeführt werden. Für ein volles Durchspielen aller Optionen auf einer Punktmenge müssen Clustering-Algorithmus, tsp Heuristik und Kantenbasis gewählt werden. Bei jeder neuen Punktmenge muss zuerst ein CLustering erzeugt werden. Vor den Tsp der Cluster muss einmal die Delaunay Triangulation erzeugt werden, und vor dem Merge Schritt "Final Algo" müssen die Tsp der Cluster generiert werden. Danach kann beliebig zwischen den verschiedenen Anzeigen umhergesprungen werden!
