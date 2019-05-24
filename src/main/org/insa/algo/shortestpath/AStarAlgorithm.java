package org.insa.algo.shortestpath;

import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Point;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    @Override
public void initLabel(Graph graph) {
     // Initialisation du tableau des Labels
       map = new LabelStar[graph.size()];
        double timeFromDest;
        double distanceFromDest;
        
        
        //Remplissage du tableau des labels
        for(Node n:graph.getNodes()) {
        	distanceFromDest = Point.distance(n.getPoint(), data.getDestination().getPoint());
        	if(graph.getGraphInformation().hasMaximumSpeed())
        		timeFromDest = (distanceFromDest * 3.6) / graph.getGraphInformation().getMaximumSpeed();
        	else
        		timeFromDest = (distanceFromDest * 3.6) / 50; // 50 km/h (vitesse moyenne)
        	
        	map[n.getId()] =new LabelStar(n, timeFromDest);
        }
	}


}
