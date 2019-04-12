package org.insa.algo.shortestpath;

import java.util.ArrayList;
import java.util.Collections;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.utils.BinaryHeap;
import org.insa.algo.utils.ElementNotFoundException;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

	/*
	private Map<Node,Label> map=new HashMap<>();
	private List<Node> nodesDone=new ArrayList<Node>();
	private List<Node> nodesUndone=new ArrayList<Node>();
	private ShortestPathData data;*/
    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    /*
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
    //ajoute les voisins � la liste des noeuds � �valuer + met � jour les �tiquettes
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
   
        * 
        *  Foreach destinationNode which can be reached via an edge from evaluationNode AND which is not in SettledNodes {
            edgeDistance = getDistance(edge(evaluationNode, destinationNode))
            newDistance = distance[evaluationNode] + edgeDistance
            if (distance[destinationNode]  > newDistance ) {
                distance[destinationNode]  = newDistance
                add destinationNode to UnSettledNodes
            }
        }
    }
    private void init_etiquette(Node n) {
    	if(n != data.getDestination())
              for(Arc i: n.getSuccessors()) {
           	map.put(i.getDestination(),new Label(i.getDestination(),false,Double.MAX_VALUE,i.getOrigin()));
           	init_etiquette(i.getDestination());
           }
    }*/
    
    @Override
    protected ShortestPathSolution doRun() {
    	
    	// Retrieve the graph.
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();

        final int nbNodes = graph.size();

        // Initialize array of distances.
        Label[] map = new Label[nbNodes];
        int j=0;
        for(Node n:graph.getNodes()) {
        	map[j]=new Label(n);
        	j++;
        }
        map[data.getOrigin().getId()].setCost(0);
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        // Initialize array of predecessors.
        Arc[] predecessorArcs = new Arc[nbNodes];

        BinaryHeap<Label> tas=new BinaryHeap<Label>();
        //on insère le 1er élément
        tas.insert(map[0]);
        int i=-1;
        //tant qu'on n'a pas marqué tous les nodes
        Label eval;
        Label suiv;
        while(i<nbNodes) {
        	i++;
        	eval=tas.findMin();
        	//check si eval est le même dans le tas et map
        	eval.setMarque(true);
        	for(Arc a:eval.getOrigin().getSuccessors()) {
        		// Small test to check allowed roads...
                if (!data.isAllowed(a)) {
                    continue;
                }
        		suiv=map[a.getDestination().getId()];
        		
        		if(!suiv.getMarque()) {
        			/*oldCost=suiv.getCost();
        			suiv.setCost(Double.min(suiv.getCost(), eval.getCost()+a.getLength()));
        			if(oldCost !=suiv.getCost()) {
        				tas.insert(suiv);
        				
        				suiv.setFather(eval.getOrigin());
        			}
        			*/
                    // Retrieve weight of the arc.
                    double w = data.getCost(a);
                    double oldDistance =suiv.getCost();
                    double newDistance = eval.getCost() + w;

                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
                        notifyNodeReached(a.getDestination());
                    }
        			if(oldDistance>newDistance) {
        				suiv.setCost(newDistance);
        				try {
							tas.remove(suiv);
						} catch (ElementNotFoundException e) {
							//e.printStackTrace();
						}
        				tas.insert(suiv);
        				predecessorArcs[a.getDestination().getId()] = a;
        			}
        		}
        		
        	}
        }
        ShortestPathSolution solution = null;
        
        

        // Destination has no predecessor, the solution is infeasible...
        if (predecessorArcs[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {

            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            while (arc != null) {
                arcs.add(arc);
                arc = predecessorArcs[arc.getOrigin().getId()];
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }

        return solution;
    }

}
           
        /*while(!nodesUndone.isEmpty()) {
        	evaluation =getNodeWithLowestDistance();
        	nodesUndone.remove(evaluation);
        	nodesDone.add(evaluation);
        	evaluatedNeighbors(evaluation);
        }
        
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
        //cr�ation du chemin solution
        //� partir des �tiquettes plac�es (� rebours)
        /*
        Node fils = data.getDestination();
        Node pere;
        double total_distance=0;//pour nous
        while(fils!=data.getOrigin()) {
        	total_distance+=map.get(fils).getCost();
        	pere=map.get(fils).getFather();
        	//recuperer l'arc et l'ajouter � la liste des arcs solutions
        	//utiliser fastest
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
        
        
		//  modifier le status selon le r�sultat
        return new ShortestPathSolution(data, Status.FEASIBLE, new Path(graph,arcsSolution));
        */


