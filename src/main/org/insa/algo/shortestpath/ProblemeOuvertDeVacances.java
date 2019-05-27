package org.insa.algo.shortestpath;

import static org.junit.Assert.assertEquals;

import java.awt.Color;
import java.awt.Container;
import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Stack;

import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.AbstractSolution.Status;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Point;
import org.insa.graph.io.BinaryGraphReader;
import org.insa.graphics.MainWindow;
import org.insa.graphics.drawing.components.BasicDrawing;
import org.junit.Assume;
import org.junit.Test;

public class ProblemeOuvertDeVacances extends ShortestPathAlgorithm{

	protected ProblemeOuvertDeVacances(ShortestPathData data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

	private final double tolerance_distance=0.3;
	private final double tolerance_time = 0.15;
	
	/*SUJET :
	    Deux vacanciers V1 et V2 habitent en O1 et O2, respectivement. Ils cherchent à déterminer un lieu de vacances à mi-chemin entre O1 et O2.
    Déterminer l'ensemble des points de rencontre M qui satisfont les critères suivants :
        Chacun a sensiblement la même durée de trajet : les coûts des trajets MO1 et MO2 doivent être égaux avec une tolérance de 15% (paramétrable).
        Le point de rencontre doit être sensiblement au milieu entre O1 et O2 : le coût du trajet MO1 doit être égal à la moitié du coût du trajet O1O2 avec une tolérance de 30% (paramétrable). Idem pour le trajet MO2.
    Une solution est donc un ensemble de noeuds du graphe.
    Il faudrait idéalement trouver une manière de les afficher sur la carte (de préférence en utilisant un observateur).
	
	*/
	public ShortestPathSolution doRun() 
	{
		//Graph graph = LireGraphe(pathMapsFolder + mapName);
		// Retrieve the graph.
		ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
		Node O1=data.getOrigin();//pickNode(graph);
		Node O2=data.getDestination();//pickNode(graph);
		Stack<Node> nodesEnCours =new Stack<Node>();
		ArrayList<Node> nodesDone =new ArrayList<Node>();
		ArrayList<Node> nodes01 =new ArrayList<Node>();
		ArrayList<Node> nodes02 =new ArrayList<Node>();
		double currentDistance,currentdistanceBis;
		Node current=O1;
		notifyOriginProcessed(data.getOrigin());
		nodesEnCours.push(O1);
		Node good;
		AStarAlgorithm aStarAlgo;
		ShortestPathSolution aStarSolution;
		
		double distance = Point.distance(O1.getPoint(), O2.getPoint()); //version 0102 à vol d'oiseau, sinon A star pour trouver la distance réel
		//selon compréhension des données du problème
		//version A star
		/*data=new ShortestPathData(graph, O1, O2, ArcInspectorFactory.getAllFilters().get(0));//0=distance
		aStarAlgo = new AStarAlgorithm(data);
		aStarSolution = aStarAlgo.run();
		double distance = aStarSolution.getCost();*/
		
		while(!nodesEnCours.isEmpty()) {
			current=nodesEnCours.pop();
			nodesDone.add(current);
			// notifyNodeReached(current);
			//notifyNodeMarked(current);
			for(Arc i : current.getSuccessors()) {
				currentDistance=Point.distance(i.getDestination().getPoint(), O1.getPoint());
				currentdistanceBis=Point.distance(i.getDestination().getPoint(), O2.getPoint());
				if(currentDistance< distance && currentdistanceBis <distance && !nodesDone.contains(i.getDestination()))
				{
					good=i.getDestination();
					if(currentDistance<(distance/2)*(1+tolerance_distance) && currentdistanceBis <(distance/2)*(1+tolerance_distance)) {
						nodes01.add(good);
						notifyNodeReached(good);
					}
					
						nodesEnCours.push(good);
				}
			
		}
		}
		
		System.out.println(" Il y a "+nodes01.size()+" neuds ok pour la distance");

		double tempsO1M,tempsO2M;
		Node goodone;
		for(Node i:nodes01) {
			data=new ShortestPathData(graph, i, O1, ArcInspectorFactory.getAllFilters().get(2));//2=time and car
			aStarAlgo = new AStarAlgorithm(data);
			aStarSolution = aStarAlgo.run();
			tempsO1M = aStarSolution.getCost();
			data=new ShortestPathData(graph, i, O2, ArcInspectorFactory.getAllFilters().get(2));//2=time and car
			aStarAlgo = new AStarAlgorithm(data);
			aStarSolution = aStarAlgo.run();
			tempsO2M = aStarSolution.getCost();
			if(Math.abs((tempsO2M-tempsO1M))<tolerance_time*Math.min(tempsO2M, tempsO1M)) {
				goodone=i;
				nodes02.add(goodone);
				notifyNodeMarked(goodone);
			}
			//System.out.println(" diff :Math.abs((tempsO2M-tempsO1M)): "+Math.abs((tempsO2M-tempsO1M))+"tolerance_time*Math.min(tempsO2M, tempsO1M): "+tolerance_time*Math.min(tempsO2M, tempsO1M));
				
		}
		System.out.println(" Il y a "+nodes02.size()+" neuds ok en tout");
		return new ShortestPathSolution(data, Status.TRIVIAL);
		
	}
		
	

}
