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
        
        //Remplissage du tableau des labels
        for(Node n:graph.getNodes()) {
        	map[n.getId()] =new LabelStar(n,Point.distance(n.getPoint(), data.getDestination().getPoint()));
        }
	}


}
