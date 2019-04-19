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
        Graph graph = data.getGraph();

        final int nbNodes = graph.size();

        // Initialisation du tableau des Labels
        Label[] map = new Label[nbNodes];
        
        //Remplissage du tableau des labels
        for(Node n:graph.getNodes()) {
        	map[n.getId()]=new Label(n);
        }
        
        //Initialisation du coût du noeud initial à 0
        map[data.getOrigin().getId()].setCost(0);
        
        // Notify observers about the first event (origin processed).
        notifyOriginProcessed(data.getOrigin());

        // Initialisation du tableau des arcs contenant les predecesseurs
        Arc[] predecessorArcs = new Arc[nbNodes];

        BinaryHeap<Label> tas=new BinaryHeap<Label>();
        
        //on insère le 1er élément
        tas.insert(map[data.getOrigin().getId()]);
        
        System.out.println("Il ya " + nbNodes + " nodes dans le graph");
        
        int i=-1;
        Label labelCurrentNode; // Le label du noeud qu'on evalue à un instant de la boucle while
        Label labelNextNode; // L'un des successeur de currentNode
        //boolean stillContinue = true;
        while( ! tas.isEmpty() ) {
        	i++;
        	
        	//Extract the current Label 
        	labelCurrentNode=tas.deleteMin();
        	System.out.println("\ncurrentEvaluatingNode("+ labelCurrentNode.getNode().getId() + "), Cost("+ labelCurrentNode.getCost()+")\niteration=(" + i +")");
        	
        	labelCurrentNode.setMarque(true);
        	for(Arc a : labelCurrentNode.getNode().getSuccessors()) {
        		// Small test to check allowed roads...
                if (data.isAllowed(a)) {
                	//labelNextNode.set = a.getDestination();
	        		labelNextNode=map[a.getDestination().getId()];
	        		//labelNextNode.setCost( data.getCost(a));
	        		
	        		if(!labelNextNode.getMarque()) {
	        			/*oldCost=suiv.getCost();
	        			suiv.setCost(Double.min(suiv.getCost(), eval.getCost()+a.getLength()));
	        			if(oldCost !=suiv.getCost()) {
	        				tas.insert(suiv);
	        				
	        				suiv.setFather(eval.getOrigin());
	        			}
	        			*/
	                    // Retrieve weight of the arc.
	                    //double w = labelNextNode.getCost();
	        			double currentCost = data.getCost(a);
	        			double oldDistance = labelNextNode.getCost(); //ATTENTION
	                    double newDistance = labelCurrentNode.getCost() + currentCost;
	                    
	                    //System.out.println("Node num " + a.getDestination().getId() + " touché !");
        				//System.out.println("from Node num " + a.getOrigin().getId()  + " touché !");
	                    System.out.println("--> Node(" + a.getDestination().getId() + ") : <AnciantCost(" + oldDistance + ") --> NewCost(" + newDistance +")>"  );
	                    if (Double.isInfinite(oldDistance) && Double.isFinite(newDistance)) {
	                        notifyNodeReached(a.getDestination());
	                    }
	                    
	        			if(oldDistance>newDistance) {
	        				labelNextNode.setCost(newDistance);
	        				labelNextNode.setFather(a.getOrigin());
	        				
	        				try {
								tas.remove(labelNextNode);
							} 
	        				catch (ElementNotFoundException e) {
								//e.printStackTrace();
							}
	        				finally {
	        					tas.insert(labelNextNode);
	        				}
	        				
	        				predecessorArcs[a.getDestination().getId()] = a;
	        				//predecessorArcs[a.getOrigin().getId()] = a;
	        				
	        			}
	        		}
        		
                }
        		
        	}
        }
        ShortestPathSolution solution = null;
        
        /*for(int i1=0; i1<nbNodes; i1++)
        {
        	double idNodeOrigin = predecessorArcs[i1].getOrigin().getId();
            double idNodeDest = predecessorArcs[i1].getDestination().getId();
            double coutArc = data.getCost(predecessorArcs[i1]);
            
        	System.out.println("Node " + idNodeOrigin + " < Node(" + idNodeDest + "), Cost(" + coutArc + ")> iteration("  + i1 + ")");
        }*/

        // Destination has no predecessor, the solution is infeasible...
        if (predecessorArcs[data.getDestination().getId()] == null) {
            solution = new ShortestPathSolution(data, Status.INFEASIBLE);
        }
        else {
        	System.out.println("I found the solution ");
            // The destination has been found, notify the observers.
            notifyDestinationReached(data.getDestination());

            // Create the path from the array of predecessors...
            ArrayList<Arc> arcs = new ArrayList<>();
            Arc arc = predecessorArcs[data.getDestination().getId()];
            while (arc != null && arc.getDestination() != data.getOrigin()) {
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


