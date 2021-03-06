package org.insa.algo.shortestpath;

import java.io.PrintStream;

import org.insa.graph.Node;

public class ShortestPathTextObserver implements ShortestPathObserver {

    private final PrintStream stream;

    public ShortestPathTextObserver(PrintStream stream) {
        this.stream = stream;
    }

    @Override
    public void notifyOriginProcessed(Node node) {
        // TODO Auto-generated method stub

    }

    @Override
    public void notifyNodeReached(Node node) {
        stream.println("Node " + node.getId() + " reached.");
    }

    @Override
    public void notifyNodeMarked(Node node) {
        stream.println("Node " + node.getId() + " marked.");
    }

    @Override
    public void notifyDestinationReached(Node node) {
        // TODO Auto-generated method stub

    }

	@Override
	public void notifyHeapInsertion(int size) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyLabelMarked(Label label) {
		// TODO Auto-generated method stub
		
	}

}
