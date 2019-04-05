package org.insa.algo.shortestpath;
import org.insa.graph.*;




public class Label {

	Node sommet_courant ;
	boolean marque; 
	double cout; 
	Node pere; 
	
	
public Label(Node sommet, boolean mark, double cost,Node father)
{
	 	this.sommet_courant=sommet; 
	 	this.marque=mark; 
	 	this.cout=cost; 
	 	this.pere=father; 
	 	
	
	
}



double getCost()
{
	return this.cout;
}


public String toString() {
	   return "Noeud sommet : " +this.sommet_courant.getId() + "marque :" + Boolean.toString(this.marque)+
			   " Cout :" +Double.toString(this.cout)+"Noeud pere : " +this.pere.getId() ;
	}
}



