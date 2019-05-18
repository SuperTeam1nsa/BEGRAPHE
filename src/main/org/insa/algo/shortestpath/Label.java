package org.insa.algo.shortestpath;
import org.insa.graph.*;




public class Label implements Comparable<Label> {

	protected 
	Node sommetCourant ;
	boolean marque; 
	double cout; 
	Arc pere;
		
public Label(Node current)
{
	marque=false;
	cout=Double.POSITIVE_INFINITY;
	sommetCourant=current;
	pere=null;
	
}
public boolean getMarque() {return marque;}
public void setMarque(boolean value) {marque=value;}

public void setCost(double cost) {cout=cost;}
public Node getNode() {return sommetCourant;}
/* 
@Override
public int hashCode() {
	return sommetCourant.getId();
}*/

public String toString() {
	   return "Noeud sommet : " +this.sommetCourant.getId() + "marque :" + Boolean.toString(this.marque)+
			   " Cout :" +Double.toString(this.cout)+"Noeud pere : " +this.pere.getOrigin().getId() ;
	}

public double getTotalCost() {return cout;}

@Override
public int compareTo(Label o) {
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

}



