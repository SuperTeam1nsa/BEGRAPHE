package org.insa.algo.utils;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;


import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;

import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.io.BinaryGraphReader;
//import org.junit.BeforeClass;
import org.junit.Test;
import org.insa.algo.utils.ShortestPathTest;
import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Scanner;

public class TestPerformances {
	
	
	
	
	public void createTestFiles(String carte,int mode, int nbPairs) throws IOException
	{
		Graph graph=ShortestPathTest.LireGraphe("./"+carte+".mapgr");
	    
	        
	        //file name only
	        String modetext=""; 
	        if (mode==0)
	        {modetext="distance";}
	        else if(mode==1)
	        {
	        modetext="temps";	
	        }
	        String fileName=carte+"_"+modetext+"_"+Integer.toString(nbPairs)+".txt";
	       File file = new File(fileName);
	        try {
				if(file.createNewFile()){
				    System.out.println(" File Created in Project root directory");
				}else System.out.println("File already exists in the project root directory");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        BufferedWriter output;
	        output = new BufferedWriter(new FileWriter(fileName));  
	        
	        output.append(carte); 
	        output.newLine();
	        output.append(Integer.toString(mode)); 
	        output.newLine();
	        output.append(Integer.toString(nbPairs));
	        output.newLine();
	        
	        for (int i=0;i<nbPairs;i++)
	        {
	        	output.append(Integer.toString(ShortestPathTest.pickNode(graph).getId()));
	        	output.append(" ");
	        	output.append(Integer.toString(ShortestPathTest.pickNode(graph).getId()));
	        	output.newLine();
	        	
	        }
	        output.close();
		
	}
	void readTestFiles(String fileName) throws IOException
	{
		 File file = new File("./");

		 String resultFileName="results_"+fileName;
	        file = new File(resultFileName);
	        try {
				if(file.createNewFile()){
				    System.out.println(" File Created in Project root directory");
				}else System.out.println("File already exists in the project root directory");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        BufferedWriter output;
	        output = new BufferedWriter(new FileWriter(resultFileName));  
		
		Scanner sc2 = null;
		 
		
    try {
        sc2 = new Scanner(new File(fileName));
    } catch (FileNotFoundException e) {
        e.printStackTrace();  
    }
    Scanner s2 = new Scanner(sc2.nextLine());
    String carte=s2.next();
    System.out.println(carte);

    s2.close();
    s2 = new Scanner(sc2.nextLine());
    int nbPairs=Integer.parseInt(s2.next());
    System.out.println(nbPairs);
    s2.close();
    s2 = new Scanner(sc2.nextLine());
    int mode=Integer.parseInt(s2.next());
    System.out.println(mode);
    while (sc2.hasNextLine()) {
        s2 = new Scanner(sc2.nextLine());

        while (s2.hasNext()) {
            String s = s2.next();
            output.append(s);
            output.append(" ");
        }
        output.newLine();
    }
    output.close();
    s2.close();
    sc2.close();
    
    }
	
	
	
	
	
	@Test
	public void createFiles() throws IOException
	{
	
		createTestFiles("insa",0,50);
		readTestFiles("insa_distance_50.txt");
	}
	
	
	
	

}
