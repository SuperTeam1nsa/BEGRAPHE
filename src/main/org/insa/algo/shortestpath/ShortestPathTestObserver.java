package org.insa.algo.shortestpath;

import org.insa.graph.Node;

public class ShortestPathTestObserver implements ShortestPathObserver {
	
	private Label labelMarked;

	private double prevCostLabel;
	// This boolean allows to know if the function labelMarkedOnce has been called at least once
	private boolean labelMarkedOnce;
	
	private boolean growingCost;
	
	public ShortestPathTestObserver(){
		prevCostLabel = 0;
		labelMarkedOnce = false;
		growingCost = true;
		
	}
	
	public Label getNodeMarked(){
		return labelMarked;
	}
	
	@Override
	public void notifyOriginProcessed(Node node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyNodeReached(Node node) {
		
	}

	@Override
	public void notifyLabelMarked(Label label) {
		labelMarked = label;
		double gc = label.getCost();
		//System.out.println("prevCost " + prevCostLabel + " currentCost " + gc );
		
		if(!labelMarkedOnce){
			labelMarkedOnce = true;
			prevCostLabel = gc;
		}
		else
		{
			if(prevCostLabel > gc){
				growingCost = false;
			}
			prevCostLabel = gc;
		}
		
	}
	
	public boolean isGrowingCost(){
		return growingCost;
	}
	

	@Override
	public void notifyDestinationReached(Node node) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void notifyHeapInsertion(int new_size)
	{
	}

	@Override
	public void notifyNodeMarked(Node node) {
		// TODO Auto-generated method stub
		
	}


}
