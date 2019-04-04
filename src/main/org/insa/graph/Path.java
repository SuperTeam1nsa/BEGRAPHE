package org.insa.graph;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;

/**
 * <p>
 * Class representing a path between nodes in a graph.
 * </p>
 * 
 * <p>
 * A path is represented as a list of {@link Arc} with an origin and not a list
 * of {@link Node} due to the multi-graph nature (multiple arcs between two
 * nodes) of the considered graphs.
 * </p>
 *
 */
public class Path {
    // Graph containing this path.
    private final Graph graph;

    // Origin of the path
    private final Node origin;

    // List of arcs in this path.
    private final List<Arc> arcs;


    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the fastest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
       *
     */
    
    public static Path createFastestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
        ListIterator<Node> nodeit=nodes.listIterator();
    	List<Arc> arcs = new ArrayList<Arc>();
        List<Arc> successors= new ArrayList<Arc>();
        Arc fastest = null;
        double fastest_time ;
        boolean linked;
        if (nodeit.hasNext()){
            Node current=nodeit.next(); 
            Node next;
            while (nodeit.hasNext()) {   
                next=nodeit.next();
                successors=current.getSuccessors();
                fastest_time=Double.MAX_VALUE;
                linked=false;
                for (Arc a:successors) {   
                    if (a.getDestination()==next ) {  
                        linked=true;
                        //on cherche le plus rapide
                         if(a.getMinimumTravelTime()<fastest_time){
                            fastest=a;
                            fastest_time=fastest.getMinimumTravelTime();
                        }
                     }  
                }
                //s'il y a une connexion on ajoute la plus rapide
                if (linked){
                    arcs.add(fastest); 
                }
                else{
                    throw new IllegalArgumentException();
                }
                current=next; 
            }
        }
        else  {
        return new Path(graph);
        }
         if(nodes.size()==1)
        return new Path(graph, nodes.get(0));
        else
        return new Path(graph, arcs);   	 
    }

    /**
     * Create a new path that goes through the given list of nodes (in order),
     * choosing the shortest route if multiple are available.
     * 
     * @param graph Graph containing the nodes in the list.
     * @param nodes List of nodes to build the path.
     * 
     * @return A path that goes through the given list of nodes.
     * 
     * @throws IllegalArgumentException If the list of nodes is not valid, i.e. two
     *         consecutive nodes in the list are not connected in the graph.
     * 
     */
  //lin�aire mais avec plusieurs arcs entre 2 nodes => pas dijkstra (cf prof vendredi) 
    /*
     * graph = ensemble des noeuds
     * nodes =les noeuds qu'on veut connecter pour cr�er le path
     */
    public static Path createShortestPathFromNodes(Graph graph, List<Node> nodes)
            throws IllegalArgumentException {
    	List<Arc> arcs = new ArrayList<Arc>();
        List<Arc> successors= new ArrayList<Arc>();
		Arc shortestpath=null;
        ListIterator<Node> it = nodes.listIterator();
        if(it.hasNext()) {
	        Node previous=it.next();
	        Node current;
	        boolean linked;
	        double shortest;
	        while(it.hasNext()) {
	        	current=it.next();
	        	successors=previous.getSuccessors();	   
	        	linked=false;
	        	shortest=Double.MAX_VALUE;
	        	if(successors.size()==0)//le dernier noeud n'a pas de successeur mais le path est valide
	            	linked=true;
	        	for(Arc a:successors) {
	        		if(a.getDestination()==current){
	        			linked=true;
						if(a.getLength()<shortest) {
	        			shortestpath=a;
						shortest=a.getLength();
	        			}
	        		}
	        	}
	        	if(!linked)
	        		throw new IllegalArgumentException();
	        	//chemins_possibles.sort(chemins_possibles.get(0));
	        	arcs.add(shortestpath);
	        	previous=current;
	        }
        } 
        else{ 
	        	return new Path(graph);
	        }
        if(nodes.size()==1)
        return new Path(graph, nodes.get(0));
        else
        return new Path(graph, arcs);
    }

    /**
     * Concatenate the given paths.
     * 
     * @param paths Array of paths to concatenate.
     * 
     * @return Concatenated path.
     * 
     * @throws IllegalArgumentException if the paths cannot be concatenated (IDs of
     *         map do not match, or the end of a path is not the beginning of the
     *         next).
     */
    public static Path concatenate(Path... paths) throws IllegalArgumentException {
        if (paths.length == 0) {
            throw new IllegalArgumentException("Cannot concatenate an empty list of paths.");
        }
        final String mapId = paths[0].getGraph().getMapId();
        for (int i = 1; i < paths.length; ++i) {
            if (!paths[i].getGraph().getMapId().equals(mapId)) {
                throw new IllegalArgumentException(
                        "Cannot concatenate paths from different graphs.");
            }
        }
        ArrayList<Arc> arcs = new ArrayList<>();
        for (Path path: paths) {
            arcs.addAll(path.getArcs());
        }
        Path path = new Path(paths[0].getGraph(), arcs);
        if (!path.isValid()) {
            throw new IllegalArgumentException(
                    "Cannot concatenate paths that do not form a single path.");
        }
        return path;
    }


    /**
     * Create an empty path corresponding to the given graph.
     * 
     * @param graph Graph containing the path.
     */
    public Path(Graph graph) {
        this.graph = graph;
        this.origin = null;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path containing a single node.
     * 
     * @param graph Graph containing the path.
     * @param node Single node of the path.
     */
    public Path(Graph graph, Node node) {
        this.graph = graph;
        this.origin = node;
        this.arcs = new ArrayList<>();
    }

    /**
     * Create a new path with the given list of arcs.
     * 
     * @param graph Graph containing the path.
     * @param arcs Arcs to construct the path.
     */
    public Path(Graph graph, List<Arc> arcs) {
        this.graph = graph;
        this.arcs = arcs;
        this.origin = arcs.size() > 0 ? arcs.get(0).getOrigin() : null;
    }


	/**
     * @return Graph containing the path.
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * @return First node of the path.
     */
    public Node getOrigin() {
        return origin;
    }

    /**
     * @return Last node of the path.
     */
    public Node getDestination() {
        return arcs.get(arcs.size() - 1).getDestination();
    }

    /**
     * @return List of arcs in the path.
     */
    public List<Arc> getArcs() {
        return Collections.unmodifiableList(arcs);
    }

    /**
     * Check if this path is empty (it does not contain any node).
     * 
     * @return true if this path is empty, false otherwise.
     */
    public boolean isEmpty() {
        return this.origin == null;
    }

    /**
     * Get the number of <b>nodes</b> in this path.
     * 
     * @return Number of nodes in this path.
     */
    public int size() {
        return isEmpty() ? 0 : 1 + this.arcs.size();
    }

    /**
     * Check if this path is valid.
     * 
     * A path is valid if any of the following is true:
     * <ul>
     * <li>it is empty;</li>
     * <li>it contains a single node (without arcs);</li>
     * <li>the first arc has for origin the origin of the path and, for two
     * consecutive arcs, the destination of the first one is the origin of the
     * second one.</li>
     * </ul>
     * 
     * @return true if the path is valid, false otherwise.
     * 
     *  Need to be implemented.
     */
    public boolean isValid() {
        boolean valid=true;
        if(isEmpty())
        	return true;
        else if(arcs.size()==0)
        	return true;
        else if(arcs.get(0).getOrigin() != getOrigin())
        	return false;
        else {
        ListIterator<Arc> it = arcs.listIterator();
        Arc previous=it.next();
        Arc current;
        while(it.hasNext()) {
        	current=it.next();
        	if(previous.getDestination()!=current.getOrigin())
        		{valid=false;
        		break;
        		}
        	previous=current;
        }
        }
    	
    	
        return valid;
    }

    /**
     * Compute the length of this path (in meters).
     * 
     * @return Total length of the path (in meters).
     * 
     *  Need to be implemented.
     */
    //un path is oneway != graph
    public float getLength() {
        // TODO
    	float distance=0;
    	for(Arc a:arcs) {
    		distance+=a.getLength();
    	}
    	/*while(n.hasSuccessors())
    	{
    		distance += n.getSuccessors().get(0).getLength();
    		n=n.getSuccessors().get(0).getDestination();
    	}*/
        return distance;
    }

    /**
     * Compute the time required to travel this path if moving at the given speed.
     * 
     * @param speed Speed to compute the travel time.
     * 
     * @return Time (in seconds) required to travel this path at the given speed (in
     *         kilometers-per-hour).
     * 
     *  Need to be implemented.
     */
    public double getTravelTime(double speed) {   	
        return ((double)(this.getLength()/1000))/(speed/3600);
    }

    /**
     * Compute the time to travel this path if moving at the maximum allowed speed
     * on every arc.
     * 
     * @return Minimum travel time to travel this path (in seconds).
     * 
     *  Need to be implemented.
     */
    public double getMinimumTravelTime() {
        // TODO:
    	double r=0;
    	for(Arc a:this.arcs)
    	{
    		r += a.getMinimumTravelTime();
    	}
        return r;
    }

}
