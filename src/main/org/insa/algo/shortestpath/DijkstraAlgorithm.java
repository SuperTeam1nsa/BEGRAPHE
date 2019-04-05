package org.insa.algo.shortestpath;

public class DijkstraAlgorithm extends ShortestPathAlgorithm {

    public DijkstraAlgorithm(ShortestPathData data) {
        super(data);
    }

    @Override
    protected ShortestPathSolution doRun() {
        ShortestPathData data = getInputData();
        ShortestPathSolution solution = null;
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

getNodeWithLowestDistance(UnSettledNodes){
    find the node with the lowest distance in UnSettledNodes and return it
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
        
        
        
        
        // TODO:
        return solution;
    }

}

