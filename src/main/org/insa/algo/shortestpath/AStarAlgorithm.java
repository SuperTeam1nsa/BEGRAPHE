package org.insa.algo.shortestpath;

import org.insa.algo.AbstractInputData.Mode;
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
	    double maxSpeed;
	     
	    if(graph.getGraphInformation().hasMaximumSpeed())
	    	maxSpeed = graph.getGraphInformation().getMaximumSpeed();
	    else
	    	maxSpeed = 50; // 50 km/h (vitesse max moyenne)
     
     
	    //Remplissage du tableau des labels
	    for(Node n:graph.getNodes()) {
	    	distanceFromDest = Point.distance(n.getPoint(), data.getDestination().getPoint());
     	
	     	if(data.getMode() == Mode.LENGTH){
	     		map[n.getId()] = new LabelStar(n, distanceFromDest);
	     	}
	     	else
	     	{
	     		timeFromDest = (distanceFromDest * 3.6) / maxSpeed;
	     		map[n.getId()] =new LabelStar(n, timeFromDest);
	     	}
	    }
    }

}
