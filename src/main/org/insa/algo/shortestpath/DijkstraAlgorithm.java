package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.insa.algo.AbstractSolution.Status;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

	private Map<Node,Label> map=new HashMap<>();
	private List<Node> nodesDone=new ArrayList<Node>();
	private List<Node> nodesUndone=new ArrayList<Node>();
	private ShortestPathData data;
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    //find the node with the lowest distance in nodesUndone and return it
    private Node getNodeWithLowestDistance(){
    	double lowest_cost=Double.MAX_VALUE;
    	double aux;
    	Node lowest = null;
    	for(Node i:nodesUndone) {
    		if((aux=map.get(i).getCost())<lowest_cost) {
    			lowest=i;
    			lowest_cost=aux;
    		}
    	}
		return lowest;    
    }
    //ajoute les voisins à la liste des noeuds à évaluer + met à jour les étiquettes
    private void evaluatedNeighbors(Node evaluationNode){
    	double distance,newDistance;
    	for(Arc i:evaluationNode.getSuccessors()) {
    		if(!nodesDone.contains(i.getDestination())) {
    			distance=i.getLength();
    			newDistance=map.get(evaluationNode).getCost()+distance;
    			if(map.get(i.getDestination()).getCost() > newDistance) {
    				map.get(i.getDestination()).setCost(newDistance);
    				nodesUndone.add(i.getDestination());
    			}
    		}
    	}
       /*
        * 
        *  Foreach destinationNode which can be reached via an edge from evaluationNode AND which is not in SettledNodes {
            edgeDistance = getDistance(edge(evaluationNode, destinationNode))
            newDistance = distance[evaluationNode] + edgeDistance
            if (distance[destinationNode]  > newDistance ) {
                distance[destinationNode]  = newDistance
                add destinationNode to UnSettledNodes
            }
        }*/
    }
    private void init_etiquette(Node n) {
    	if(n != data.getDestination())
              for(Arc i: n.getSuccessors()) {
           	map.put(i.getDestination(),new Label(i.getDestination(),false,Double.MAX_VALUE,i.getOrigin()));
           	init_etiquette(i.getDestination());
           }
    }
    
    @Override
    protected ShortestPathSolution doRun() {
       data = getInputData();
        List<Arc> arcsSolution=new ArrayList<Arc>();
        Graph graph=data.getGraph();
        Node evaluation;
      //initialisation 1ère etiquette
        map.put(data.getOrigin(),new Label(data.getOrigin(),false,Double.MAX_VALUE,data.getOrigin()));
        
        // init_etiquette: récursif et fait la boucle pour tous les nodes
       for(Arc i: data.getOrigin().getSuccessors()) {
       init_etiquette(i.getDestination());
       }
        
        nodesUndone.add(data.getOrigin());
        //le node origine a un cout nul
        map.get(data.getOrigin()).setCost(0);
        //labels.get(labels.indexOf(nodesUndone)).setCost(0);
        while(!nodesUndone.isEmpty()) {
        	evaluation =getNodeWithLowestDistance();
        	nodesUndone.remove(evaluation);
        	nodesDone.add(evaluation);
        	evaluatedNeighbors(evaluation);
        }
        /*
         * 
         * 
         * 
         * 
         * Foreach node set distance[node] = HIGH
SettledNodes = empty
UnSettledNodes = empty

Add sourceNode to UnSettledNodes
distance[sourceNode]= 0

while (UnSettledNodes is not empty) {
    evaluationNode = getNodeWithLowestDistance(UnSettledNodes)
    remove evaluationNode from UnSettledNodes
    add evaluationNode to SettledNodes
    evaluatedNeighbors(evaluationNode)
}



evaluatedNeighbors(evaluationNode){
    Foreach destinationNode which can be reached via an edge from evaluationNode AND which is not in SettledNodes {
        edgeDistance = getDistance(edge(evaluationNode, destinationNode))
        newDistance = distance[evaluationNode] + edgeDistance
        if (distance[destinationNode]  > newDistance ) {
            distance[destinationNode]  = newDistance
            add destinationNode to UnSettledNodes
        }
    }
}
         */
        //création du chemin solution
        //à partir des étiquettes placées (à rebours)
        Node fils = data.getDestination();
        Node pere;
        double total_distance=0;//pour nous
        while(fils!=data.getOrigin()) {
        	total_distance+=map.get(fils).getCost();
        	pere=map.get(fils).getFather();
        	//recuperer l'arc et l'ajouter à la liste des arcs solutions
        	for(Arc i:pere.getSuccessors()) {
        		if(i.getDestination()==fils) {
        			arcsSolution.add(i);
        			break;
        		}
        	}
        	fils=pere;
        }
        //on remet les arcs dans le bon sens:
        Collections.reverse(arcsSolution);
        
        System.out.print("distance totale :"+total_distance);
        
        
		// TODO: modifier le status selon le résultat
        return new ShortestPathSolution(data, Status.FEASIBLE, new Path(graph,arcsSolution));
    }

}

