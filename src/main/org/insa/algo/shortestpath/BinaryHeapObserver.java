package org.insa.algo.shortestpath;

import org.insa.graph.Node;

public class BinaryHeapObserver implements ShortestPathObserver{
	
	
	private int nb_explores;
	private int nb_marques; 
	private int max_tas; 
	
	public BinaryHeapObserver()
	{
		nb_explores=0; 
		nb_marques=0; 
		max_tas=0;
	}
	
	
	
	public int getNb_explores() {
		return nb_explores;
	}



	public void setNb_explores(int nb_explores) {
		this.nb_explores = nb_explores;
	}



	public int getNb_marques() {
		return nb_marques;
	}



	public void setNb_marques(int nb_marques) {
		this.nb_marques = nb_marques;
	}



	public int getMax_tas() {
		return max_tas;
	}



	public void setMax_tas(int max_tas) {
		this.max_tas = max_tas;
	}



	@Override
	public void notifyOriginProcessed(Node node) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void notifyNodeReached(Node node) {
		nb_explores ++;
		
	}

	@Override
	public void notifyNodeMarked(Node node) {
		nb_marques ++;
		
	}

	@Override
	public void notifyDestinationReached(Node node) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public void notifyHeapInsertion(int new_size)
	{
		if (new_size>max_tas)
		{
			max_tas=new_size; 
		}
	}



	@Override
	public void notifyLabelMarked(Label label) {
		// TODO Auto-generated method stub
		
	}

	

}
