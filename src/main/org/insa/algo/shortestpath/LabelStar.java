package org.insa.algo.shortestpath;

import org.insa.graph.Node;



public class LabelStar extends Label{
private double volOiseauDest;
public LabelStar(Node current,double coutDest)
{
	super(current);
	volOiseauDest=coutDest;
}
@Override
public double getTotalCost() {
	return cout+volOiseauDest;
	
}

@Override
public int compareTo(Label o) {
	double other_cost=o.getTotalCost();
	double current_cost=getTotalCost();
	return current_cost-other_cost>0?1:current_cost==other_cost?0:-1;
}
}
