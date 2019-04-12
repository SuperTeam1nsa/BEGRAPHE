package org.insa.algo.shortestpath;
import org.insa.graph.*;




public class Label implements Comparable<Label> {

	private 
	Node sommet_courant ;
	boolean marque; 
	double cout; 
	Node pere; 
	
	
public Label(Node current)
{
	marque=false;
	cout=Double.POSITIVE_INFINITY;
	pere=null;
	sommet_courant=current;
	
}
public boolean getMarque() {return marque;}
public void setMarque(boolean value) {marque=value;}
public void setCost(double cost) {cout=cost;}
public void setFather(Node p) { pere=p;}
public Node getFather() {return pere;}
public Node getOrigin() {return sommet_courant;}
double getCost()
{
	return this.cout;
}


public String toString() {
	   return "Noeud sommet : " +this.sommet_courant.getId() + "marque :" + Boolean.toString(this.marque)+
			   " Cout :" +Double.toString(this.cout)+"Noeud pere : " +this.pere.getId() ;
	}
@Override
public int compareTo(Label o) {
	return (int) (this.cout-o.getCost());
}

}



