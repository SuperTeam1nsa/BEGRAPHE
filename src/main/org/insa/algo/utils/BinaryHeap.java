//
// ******************PUBLIC OPERATIONS*********************
// void insert( x ) --> Insert x
// Comparable deleteMin( )--> Return and remove smallest item
// Comparable findMin( ) --> Return smallest item
// boolean isEmpty( ) --> Return true if empty; else false
// ******************ERRORS********************************
// Throws RuntimeException for findMin and deleteMin when empty

package org.insa.algo.utils;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Implements a binary heap. Note that all "matching" is based on the compareTo
 * method.
 * 
 * @author Mark Allen Weiss
 * @author DLB
 */
public class BinaryHeap<E extends Comparable<E> > implements PriorityQueue<E> {

    // Number of elements in heap.
    private int currentSize;

    // The heap array.
    private final ArrayList<E> array;
    
    private HashMap<E,Integer> indirection;
    /**
     * Construct a new empty binary heap.
     */
    public BinaryHeap() {
        this.currentSize = 0;
        this.array = new ArrayList<E>();
        indirection=new HashMap<E,Integer>();
    }

    /**
     * Construct a copy of the given heap.
     * 
     * @param heap Binary heap to copy.
     */
    public BinaryHeap(BinaryHeap<E> heap) {
        this.currentSize = heap.currentSize;
        this.array = new ArrayList<E>(heap.array);
        indirection= new HashMap<E,Integer>(heap.indirection);
    }

    /**
     * Set an element at the given index.
     * 
     * @param index Index at which the element should be set.
     * @param value Element to set.
     */
    private void arraySet(int index, E value) {
        indirection.put(value, index);
        if (index == this.array.size()) {
            this.array.add(value);
        }
        else {
            this.array.set(index, value);
        }
    }

    /**
     * @return Index of the parent of the given index.
     */
    private int index_parent(int index) {
        return (index - 1) / 2;
    }

    /**
     * @return Index of the left child of the given index.
     */
    private int index_left(int index) {
        return index * 2 + 1;
    }

    /**
     * Internal method to percolate up in the heap.
     * 
     * @param index Index at which the percolate begins.
     */
    private void percolateUp(int index) {
        E x = this.array.get(index);

        for (; index > 0
                && x.compareTo(this.array.get(index_parent(index))) < 0; index = index_parent(
                        index)) {
            E moving_val = this.array.get(index_parent(index));
            this.arraySet(index, moving_val);
        }

        this.arraySet(index, x);
    }

    /**
     * Internal method to percolate down in the heap.
     * 
     * @param index Index at which the percolate begins.
     */
    private void percolateDown(int index) {
        int ileft = index_left(index);
        int iright = ileft + 1;

        if (ileft < this.currentSize) {
            E current = this.array.get(index);
            E left = this.array.get(ileft);
            boolean hasRight = iright < this.currentSize;
            E right = (hasRight) ? this.array.get(iright) : null;

            if (!hasRight || left.compareTo(right) < 0) {
                // Left is smaller
                if (left.compareTo(current) < 0) {
                    this.arraySet(index, left);
                    this.arraySet(ileft, current);
                    this.percolateDown(ileft);
                }
            }
            else {
                // Right is smaller
                if (right.compareTo(current) < 0) {
                    this.arraySet(index, right);
                    this.arraySet(iright, current);
                    this.percolateDown(iright);
                }
            }
        }
    }

    @Override
    public boolean isEmpty() {
        return this.currentSize == 0;
    }

    @Override
    public int size() {
        return this.currentSize;
    }

    @Override
    public void insert(E x) {
        int index = this.currentSize++;
        this.arraySet(index, x);
        this.percolateUp(index);
    }
   /* 
    private void move_down(int indexElt, BinaryHeap h) {
    	boolean Continue = True;
    	int i = indexElt;
    	
    	while (Continue) {
    		if()
    	}
    }*/

    @Override
    public void remove(E x) throws ElementNotFoundException {
    	int indexElt;
    	//int indexParent;
        
    	
    	if(this.isEmpty()){
    		throw new ElementNotFoundException(x);
    	}
  
    	//indexElt = this.array.indexOf(x);
    	//en O(1) HashMap hors hash collision (avec id qui est != pour chaque node il n'y est pas censé y en avoir
    	indexElt=indirection.get(x);
    	/*Rq pour Abdel :
    	 * Une table de hachage est une implémentation particulière d'un tableau associatif. Elle est aussi la plus courante. Basiquement il s'agit d'un tableau dont les cases contiennent un pointeur vers nil, un élément ou une liste d'élément. On détermine la case à utiliser en appliquant une fonction de hachage à la clé. Idéalement, chaque case ne pointera que vers un unique élément. Dans ce cas les opérations d'insertion, de consultation et de suppression se font en temps constant, noté O(1), c'est à dire qui ne dépend pas du nombre d'éléments présents dans la table de hachage. Cependant si la fonction de hachage retourne deux fois la même valeur pour deux clés différentes, ce que l'on nomme collision, alors les deux valeurs sont généralement stockées comme une liste. C'est à dire que maintenant il va falloir parcourir toute cette liste. Dans le pire cas, la fonction de hachage retourne toujours la même valeur, toutes les valeurs vont donc être stockées dans la même case et l'on va devoir parcourir la liste pour chaque opération. La complexité est alors linéaire par rapport au nombre d'éléments dans la structure, noté O(n), ce qui est très peu performant. Une table de hachage a donc une complexité moyenne d'O(1) mais un pire cas en O(n). Il est donc crucial d'avoir une fonction de hachage performante. Les personnes n'étant pas à l'aise avec l'implémentation d'une table de hachage ou les concepts précédant auront tout intérêt à consulter la page Wikipédia qui est assez complète.
    	 */
    	
        if(indexElt == -1 || indexElt >= currentSize) {
        	throw new ElementNotFoundException(x);
        }
        

        /*
          this.arraySet(indexElt, this.array.get(--this.currentSize));
        	this.percolateDown(indexElt);
        	this.percolateUp(indexElt);
        */
        E min = this.deleteMin();
        this.arraySet(indexElt, min);
        this.percolateUp(indexElt);
         
    }

    @Override
    public E findMin() throws EmptyPriorityQueueException {
        if (isEmpty())
            throw new EmptyPriorityQueueException();
        return this.array.get(0);
    }

    @Override
    public E deleteMin() throws EmptyPriorityQueueException {
    	
        E minItem = findMin();
        E lastItem = this.array.get(--this.currentSize);
        this.arraySet(0, lastItem);
        this.percolateDown(0);
        
        return minItem;
    }

    /**
     * Prints the heap
     */
    public void print() {
        System.out.println();
        System.out.println("========  HEAP  (size = " + this.currentSize + ")  ========");
        System.out.println();

        for (int i = 0; i < this.currentSize; i++) {
            System.out.println(this.array.get(i).toString());
        }

        System.out.println();
        System.out.println("--------  End of heap  --------");
        System.out.println();
    }

    /**
     * Prints the elements of the heap according to their respective order.
     */
    public void printSorted() {

        BinaryHeap<E> copy = new BinaryHeap<E>(this);

        System.out.println();
        System.out.println("========  Sorted HEAP  (size = " + this.currentSize + ")  ========");
        System.out.println();

        while (!copy.isEmpty()) {
            System.out.println(copy.deleteMin());
        }

        System.out.println();
        System.out.println("--------  End of heap  --------");
        System.out.println();
    }

}
