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
import org.insa.graph.Point;

public class AStarAlgorithm extends DijkstraAlgorithm {

    public AStarAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    protected ShortestPathSolution doRun() {
    	// Retrieve the graph.
        ShortestPathData data = getInputData();
        Graph graph = data.getGraph();
        
        if (data.getOrigin().getId() == data.getDestination().getId()){
        	return new ShortestPathSolution(data, Status.TRIVIAL);
        }

        final int nbNodes = graph.size();

        // Initialisation du tableau des Labels
        LabelStar[] map = new LabelStar[nbNodes];
        
        //Remplissage du tableau des labels
        for(Node n:graph.getNodes()) {
        	LabelStar l = new LabelStar(n);
        	l.setNode(n);
        	l.setCostFromDest(Point.distance(n.getPoint(), data.getDestination().getPoint()));
        	map[n.getId()] = l;
        }
        
        //Initialisation du coût du noeud initial à 0
        map[data.getOrigin().getId()].setCostFromOrig(0);
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        // Initialisation du tableau des arcs contenant les predecesseurs
        Arc[] predecessorArcs = new Arc[nbNodes];

        BinaryHeap<LabelStar> tas=new BinaryHeap<LabelStar>();
        
        //on insère le 1er élément
        tas.insert(map[data.getOrigin().getId()]);
     
        
        LabelStar labelCurrentNode; // Le LabelStar du noeud qu'on evalue à un instant de la boucle while
        LabelStar labelNextNode; // L'un des successeur de currentNode
        //boolean stillContinue = true;
       do {

        	//Extract the current Label 
        	labelCurrentNode=tas.deleteMin();        	
        	labelCurrentNode.setMarque(true);
        	for(Arc a : labelCurrentNode.getNode().getSuccessors()) {
        		// Small test to check allowed roads...
                if (data.isAllowed(a)) {
                	//labelNextNode.set = a.getDestination(); //Remplir la destination ou bien virer de la Claass car ne sert à rien
	        		//labelNextNode=map[a.getDestination().getId()];
                	labelNextNode=map[a.getDestination().getId()];
                	
                	
	        		if(!labelNextNode.getMarque()) {
	        			
	        			double currentCost = data.getCost(a); //need
	        			//double currentCost = a.getLength();
	        			double oldDistance = labelNextNode.getTotalCost(); 
	                    double newDistance = labelCurrentNode.getCostFromOrigin() + currentCost + labelCurrentNode.getCostFromDest();
	                    
	                    if(Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
	                        notifyNodeReached(a.getDestination());
	                    }
	                    
	        			if(oldDistance>newDistance) {
	        				//getters/setters out to enhance performance
	        				labelNextNode.setCostFromOrig(labelCurrentNode.getCostFromOrigin() + currentCost );
	        				labelNextNode.setFather(a);
	        				map[a.getDestination().getId()]=labelNextNode;
	        				//labelNextNode.setFather(a.getOrigin());
	        				
	        				/*try {
								tas.remove(labelNextNode);
							} 
	        				catch (ElementNotFoundException e) {
								//e.printStackTrace();
							}
	        				finally {
	        					tas.insert(labelNextNode);
	        				}*/
	        				
	        				if(Double.isInfinite(oldDistance)) {
	        					tas.insert(labelNextNode);
	        				}
	        				else
	        				{	
	        					try {
									tas.remove(labelNextNode);
								} 
		        				catch (ElementNotFoundException e) {
		        					
								}
	        					tas.insert(labelNextNode);
	        				}
	        				
	        				//predecessorArcs[a.getDestination().getId()] = a;
	        				
	        			}
	        		}
        		
                }
        		
        	}
        } while( !tas.isEmpty() && labelCurrentNode.getNode().getId()!=data.getDestination().getId());
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
            Arc arc = map[data.getDestination().getId()].getFather();//predecessorArcs[data.getDestination().getId()];
            //while (arc != null && arc.getDestination() != data.getOrigin()) {
            while ( arc != null ) {
            	arcs.add(arc);
                //arc = predecessorArcs[arc.getOrigin().getId()];
            	arc=map[arc.getOrigin().getId()].getFather();
            }

            // Reverse the path...
            Collections.reverse(arcs);

            // Create the final solution.
            solution = new ShortestPathSolution(data, Status.OPTIMAL, new Path(graph, arcs));
        }

        return solution;
    }


}
