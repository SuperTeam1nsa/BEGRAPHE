package org.insa.algo.shortestpath;
import org.insa.graph.*;




public class Label implements Comparable<Label> {

	private 
	Node sommetCourant ;
	boolean marque; 
	double cout; 
	Node pere; 
	
	
public Label(Node current)
{
	marque=false;
	cout=Double.POSITIVE_INFINITY;
	pere=null;
	sommetCourant=current;
	
}
public boolean getMarque() {return marque;}
public void setMarque(boolean value) {marque=value;}

public void setCost(double cost) {cout=cost;}

public void setFather(Node p) { pere=p;}
public Node getFather() {return pere;}

public Node getNode() {return sommetCourant;}
double getCost()
{
	return this.cout;
}
/* fake good idea : multiple arc from same node
@Override
public int hashCode() {
	return sommetCourant.getId();
}*/

public String toString() {
	   return "Noeud sommet : " +this.sommetCourant.getId() + "marque :" + Boolean.toString(this.marque)+
			   " Cout :" +Double.toString(this.cout)+"Noeud pere : " +this.pere.getId() ;
	}
@Override
public int compareTo(Label o) {
	return this.cout-o.getCost()>0?1:this.cout==o.getCost()?0:-1;
}

}



