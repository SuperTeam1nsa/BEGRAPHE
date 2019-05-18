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
}
