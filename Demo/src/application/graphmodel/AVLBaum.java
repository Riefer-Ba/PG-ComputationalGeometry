package application.graphmodel;

import java.io.*;
import java.util.ArrayList;
public class AVLBaum {
  static class AVLKnoten{
	  private AVLKnoten links;
	  private AVLKnoten rechts;
	  private int balance;
	  private Pnkt pkt;
	  public AVLKnoten(Pnkt punkt) {
		  pkt=punkt;
	  }
  }
  private AVLKnoten wurzel;
  
  public AVLBaum() {
    // Konstruktor erzeugt leeren Baum
    wurzel = null;
  }
  
  public int hoehe() {
    // gibt Höhe des Baums zurück
    return hoehe(wurzel);
  }
  
  public int hoehe(AVLKnoten spitze) {
    // gibt Höhe des Baums unter spitze zurück
    if (spitze == null) {
      return 0;
    } else {
      int h = Math.max(hoehe(spitze.links), hoehe(spitze.rechts));
      return h + 1;
    }
  }
  public ArrayList<Pnkt> uebertragen(){
	  ArrayList<Pnkt> pkt=new ArrayList<Pnkt>();
	  ueb(wurzel,pkt);
	  return pkt;
  }
  private void ueb(AVLKnoten akt, ArrayList<Pnkt> pkts) {
	  if(akt!=null) {
		  ueb(akt.links, pkts);
		  pkts.add(akt.pkt);
		  ueb(akt.rechts, pkts);
	  }
  }
  
  private AVLKnoten rotiereLinks(AVLKnoten b) {
    // vollführt einfaches Rotieren mit b, Fall links - links
    // gibt die geänderte Wurzel zurück
    // Vorbedingung: b ist ein nichtleerer Baum mit den richtigen Teilbäumen
    //    zum entsprechenden Rotieren
    
    AVLKnoten a = b.links;
    b.links = a.rechts;
    a.rechts = b;
    
    b.balance = 0;
    a.balance = 0;
    
    return a;
  }
  
  
  private AVLKnoten rotiereRechts(AVLKnoten b) {
    // vollführt einfaches Rotieren mit b, Fall rechts - rechts
    // gibt die geänderte Wurzel zurück
    // Vorbedingung: b ist ein nichtleerer Baum mit den richtigen Teilbäumen
    //    zum entsprechenden Rotieren
    
    AVLKnoten a = b.rechts;
    b.rechts = a.links;
    a.links = b;
    
    b.balance = 0;
    a.balance = 0;
    
    return a;
  }
  
  private AVLKnoten rotiereDoppeltLinksRechts(AVLKnoten c) {
    // vollführt doppeltes Rotieren mit c, Fall links - rechts
    // gibt die geänderte Wurzel zurück
    // Vorbedingung: c ist ein nichtleerer Baum mit den richtigen Teilbäumen
    //    zum entsprechenden Rotieren
    
    AVLKnoten a = c.links;
    AVLKnoten b = a.rechts;
    a.rechts = b.links;
    b.links = a;
    c.links = b.rechts;
    b.rechts = c;
    
    c.balance = (b.balance == -1)? 1 : 0;
    a.balance = (b.balance ==  1)? -1 : 0;
    b.balance = 0;
    
    return b;
  }
  
  private AVLKnoten rotiereDoppeltRechtsLinks(AVLKnoten c) {
    // vollführt doppeltes Rotieren mit c, Fall rechts - links
    // gibt die geänderte Wurzel zurück
    // Vorbedingung: c ist ein nichtleerer Baum mit den richtigen Teilbäumen
    //    zum entsprechenden Rotieren
    
    AVLKnoten a = c.rechts;
    AVLKnoten b = a.links;
    a.links = b.rechts;
    b.rechts = a;
    c.rechts = b.links;
    b.links = c;
    
    c.balance = (b.balance == 1)? -1 : 0;
    a.balance = (b.balance ==  -1)? 1 : 0;
    b.balance = 0;
    
    return b;
  }
  
  public void einfuegen(Pnkt neuerWert, Pnkt start) {
    // fügt Knoten neu an die richtige Stelle ein
    
    AVLKnoten neu = new AVLKnoten(neuerWert);
    
    // 1. Fall: Baum ist noch leer
    if (wurzel == null) {
      wurzel = neu;
    } else {
      // sonst rekursiv durch
      wurzel = einfuegen(wurzel, neu, start);
    }
  }
  
  boolean rebalance;   // muss der Baum umsortiert werden?
  
  private AVLKnoten einfuegen(AVLKnoten spitze, AVLKnoten neu, Pnkt start) {
    // fügt Knoten neu an die richtige Stelle unter Teilbaum spitze ein
    // gibt Zeiger auf neuen Wert für spitze zurück
    
    AVLKnoten temp;
    
    if (neu.pkt.orient(start, spitze.pkt)== -1) {                              //Orientationtest
      // rechten Teilbaum betrachten
      if (spitze.rechts == null) {
        // neuen Knoten einfach rechts anhängen
        spitze.rechts = neu;
        spitze.balance++;
        rebalance = (spitze.balance >= 1);
        return spitze;
      } else {
        // im rechten Teilbaum einfügen
        spitze.rechts = einfuegen(spitze.rechts, neu,start);  // ggf. spitze ändern
        if (rebalance) {
          // Ausgleichen
          switch (spitze.balance) {
            case -1:
              // war links-lastig, jetzt ausgewogen
              spitze.balance = 0;
              rebalance = false;
              return spitze;
            case 0:
              // war ausgeglichen, jetzt hier rechtslastig. Weiter oben?
              spitze.balance = 1;
              return spitze;
            case 1:
              // ok, ich bin rechts schief
              rebalance = false;  // wird hier gleich bereinigt
              if (spitze.rechts.balance == 1) {
                // Fall rechts - rechts
                return rotiereRechts(spitze);
              } else {
                // Fall rechts - links
                return rotiereDoppeltRechtsLinks(spitze);
              }
          }
        } else {
          return spitze;   // alles ok, einfach hochreichen
        }
      }
    } else {
      // linken Teilbaum betrachten, genau wie rechts
      if (spitze.links == null) {
        // neuen Knoten einfach links anhängen
        spitze.links = neu;
        spitze.balance--;
        rebalance = (spitze.balance <= -1);
        return spitze;
      } else {
        // im linken Teilbaum einfügen
        spitze.links = einfuegen(spitze.links, neu, start);  // ggf. spitze ändern
        if (rebalance) {
          // Ausgleichen
          switch (spitze.balance) {
            case 1:
              // war rechts-lastig, jetzt ausgewogen
              spitze.balance = 0;
              rebalance = false;
              return spitze;
            case 0:
              // war ausgeglichen, jetzt hier linkslastig. Weiter oben?
              spitze.balance = -1;
              return spitze;
            case -1:
              // ok, ich bin links schief
              rebalance = false;  // wird hier gleich bereinigt
              if (spitze.links.balance == -1) {
                // Fall links - links
                return rotiereLinks(spitze);
              } else {
                // Fall links - rechts
                return rotiereDoppeltLinksRechts(spitze);
              }
          }
        } else {
          return spitze;   // alles ok, einfach hochreichen
        }
      }
    }
    
    return null;  // der Form halber - sollte hier nie ankommen!
  }
  
  public String toString() {
    if (wurzel != null) {
      return toString(wurzel, 0);
    } else {
      return "<leerer Baum>";
    }
  }
  
  private static int indentPrint = 4;  // Einrückung beim Ausdrucken
  
  private String toString(AVLKnoten b, int pos) {
    // Ausdrucken des Teilbaums ab b, Reihenfolge inorder
    String s = "";
    
    // pos Leerzeichen für die weitere Ausgabe
    String margin = "";
    for (int i=0; i<pos; i++) {
      margin += " ";
    }
    
    if (b == null) {
      s += margin + "<null>"+ "\n";
    } else {
      s += toString(b.rechts, pos + indentPrint);
      s += margin +" Knoten " + "\n";
      s += toString(b.links, pos + indentPrint);
    }
    
    return s;
  }
  
}

