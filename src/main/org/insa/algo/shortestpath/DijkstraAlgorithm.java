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


    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }
    
    
    @Override
    protected ShortestPathSolution doRun() {
    	
    	// Retrieve the graph.
        ShortestPathData data = getInputData();
        
        if (data.getOrigin().getId() == data.getDestination().getId()){
        	return new ShortestPathSolution(data, Status.TRIVIAL);
        }
        
        Graph graph = data.getGraph();

        final int nbNodes = graph.size();

        // Initialisation du tableau des Labels
        Label[] map = new Label[nbNodes];
        
        //Remplissage du tableau des labels
        for(Node n:graph.getNodes()) {
        	map[n.getId()]=new Label(n);
        }
        
        //Initialisation du co√ªt du noeud initial √† 0
        map[data.getOrigin().getId()].setCost(0);
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        // Initialisation du tableau des arcs contenant les predecesseurs
        Arc[] predecessorArcs = new Arc[nbNodes];

        BinaryHeap<Label> tas=new BinaryHeap<Label>();
        
        //on insere le premier element
        tas.insert(map[data.getOrigin().getId()]);
     
        
        Label labelCurrentNode; // Le label du noeud qu'on evalue a† un instant de la boucle while
        Label labelNextNode; // L'un des successeur de currentNode
        //boolean stillContinue = true;
       do {

        	//Extract the current Label 
        	labelCurrentNode=tas.deleteMin();        	
        	labelCurrentNode.setMarque(true);
        	
        	for(Arc a : labelCurrentNode.getNode().getSuccessors()) {
        		// Small test to check allowed roads...
                if (data.isAllowed(a)) {
                	//labelNextNode.set = a.getDestination(); //Remplir la destination ou bien virer de la class car ne sert a† rien
	        		//labelNextNode=map[a.getDestination().getId()];
                	labelNextNode=map[a.getDestination().getId()];
                	
	        		if(!labelNextNode.getMarque()) {
	        			
	        			double currentCost = data.getCost(a); //need
	        			//double currentCost = a.getLength();
	        			double oldDistance = labelNextNode.getCost(); //ATTENTION
	                    double newDistance = labelCurrentNode.getCost() + currentCost;
	                    
	                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
	                        notifyNodeReached(a.getDestination());
	                    }
	                    
	        			if(oldDistance>newDistance) {
	        				//getters/setters out to enhance performance
	        				labelNextNode.setCost(newDistance);
	        				//labelNextNode.setFather(a.getOrigin());
	        				
	        				/* 	*************************************************
	        				try {												*
								tas.remove(labelNextNode);						*
							} 													*
	        				catch (ElementNotFoundException e) {				*
								//e.printStackTrace();							*
							}													*
	        				finally {											*
	        					tas.insert(labelNextNode);						*
	        				}													*
	        				***************************************************** */
	        				
	        				if(Double.isInfinite(oldDistance)) {
	        					tas.insert(labelNextNode);
	        				}
	        				else
	        				{	
	        					try {
									tas.remove(labelNextNode);
								} 
		        				catch(ElementNotFoundException e) {}
	        					tas.insert(labelNextNode);
	        				}
	        				
	        				predecessorArcs[a.getDestination().getId()] = a;
	        				
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
            Arc arc = predecessorArcs[data.getDestination().getId()];
            //while (arc != null && arc.getOrigin() != data.getOrigin()) {
            while ( arc != null ) {
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
      