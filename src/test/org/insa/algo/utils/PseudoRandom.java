package org.insa.algo.utils;

public class PseudoRandom {
	
	static int counter=0;
	
	/*
	 * Generate an integer between 0 and max-1
	 * return integer
	 */
	
	public static int generate(int max){
		counter++;
		return (counter*counter - 2*counter + 3) % max;
	}
	
	public static void initSequence(){
		counter = 0;
	}
}
