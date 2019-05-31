package org.insa.algo.utils;

import org.junit.Test;

public class PseudoRandomTest {

	@Test
	public void main(){
		for(int i=0; i<10; i++){
			System.out.println(i+"> " + PseudoRandom.generate(10));
		}
		System.out.println("initOfSequence");
		PseudoRandom.initSequence();
		
		for(int i=0; i<30; i++){
			System.out.println(i+"> " + PseudoRandom.generate(10));
		}
		
	}
}
