package org.insa.algo.shortestpath;

import org.insa.graph.Arc;
import org.insa.graph.Node;



public class LabelStar implements Comparable<LabelStar>{

	private 
	Node sommetCourant ;
	boolean marque; 
	Arc pere;
	double costFromOrigin;
	double costFromDestination;
public LabelStar(Node current)
{
	marque=false;
	costFromOrigin=Double.POSITIVE_INFINITY;
	sommetCourant=current;
	pere=null;
	
}
public boolean getMarque() {return marque;}
public void setMarque(boolean value) {marque=value;}

public Node getNode() { return sommetCourant; }
public void setNode(Node n) { sommetCourant = n;}

public String toString() {
	   return "Noeud sommet : " +this.sommetCourant.getId() + "marque :" + Boolean.toString(this.marque)+
			   " Cout :" +Double.toString(this.costFromOrigin+this.costFromDestination)+"Noeud pere : " +this.pere.getOrigin().getId() ;
	}
@Override
public int compareTo(LabelStar o) {
	double other_cost=o.getTotalCost();
	double current_cost=getTotalCost();
	return current_cost-other_cost>0?1:current_cost==other_cost?0:-1;
}
public void setFather(Arc a) {
	pere=a;
	
}
public Arc getFather() {
	return pere;
}
	public double getTotalCost() {
		return costFromDestination+costFromOrigin;
	}
	public double getCostFromDest() {
		return costFromDestination;
	}
	
	public void setCostFromDest(double cost) {
		costFromDestination=cost;
	}
	
	public void setCostFromOrig(double cost) {
		costFromOrigin=cost;
	}
	
	public double getCostFromOrigin() {
		return costFromOrigin;
	}

	
	
	
}
