package org.insa.algo.utils;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.insa.algo.AbstractSolution.Status;
import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.AStarAlgorithm;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.BinaryHeapObserver;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;

import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.io.BinaryGraphReader;
//import org.junit.BeforeClass;
import org.junit.Test;
import org.insa.algo.utils.ShortestPathTest;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentsAlgorithm;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentsData;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentsSolution;

import java.io.File;
import java.io.IOException;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.Writer;
import java.util.Scanner;

public class TestPerformances {
	
	Graph graph=null;
	public void createTestFiles(String carte,int mode, int nbPairs) throws IOException
	{
		 graph=ShortestPathTest.LireGraphe("./"+carte+".mapgr");
	    
		 	 
		 	WeaklyConnectedComponentsData WCCD=new WeaklyConnectedComponentsData(graph); 
		 	WeaklyConnectedComponentsAlgorithm WCCA=new WeaklyConnectedComponentsAlgorithm(WCCD);
		 	WeaklyConnectedComponentsSolution WCCS=WCCA.run();
		 	
		 	
		 	
	        //file name only
	        String modetext=""; 
	        if (mode==0)
	        {modetext="distance";}
	        else if(mode==1)
	        {
	        modetext="temps";	
	        }
	        String fileName=carte+"_"+modetext+"_"+Integer.toString(nbPairs)+".txt";
	        File Folder=new File(carte); 
	        if (Folder.mkdir())
	        {
	        	System.out.println("Folder created");
	        }
	        else
	        {System.out.println("Folder not created");}
	       File file = new File(".//"+carte+"//"+fileName);
	        try {
				if(file.createNewFile()){
				    System.out.println(" File Created in Project root directory");
				}else System.out.println("File already exists in the project root directory");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        BufferedWriter output;
	        output = new BufferedWriter(new FileWriter("./"+carte+"//"+fileName));  
	        
	        output.append(carte); 
	        output.newLine();
	        output.append(Integer.toString(mode)); 
	        output.newLine();
	        output.append(Integer.toString(nbPairs));
	        output.newLine();
	        int node1;
	        int node2; 
	        for (int i=0;i<nbPairs;i++)
	        {
	        	 int component =(int)(Math.random()*WCCS.getComponents().size());
	 	         if (WCCS.getComponents().get(component).size()==1)
	 	         {
	 	        	 while (WCCS.getComponents().get(component).size()<=1)
	 	         
	 	        	 	{ component =(int)(Math.random()*WCCS.getComponents().size());}
	 	      	         }
	 	         
	 	         
	        	 node1=(int)(Math.random()*WCCS.getComponents().get(component).size());
	 	         while((node2=(int)(Math.random()*WCCS.getComponents().get(component).size()))==node1)
	 	         {node2=(int)(Math.random()*WCCS.getComponents().get(component).size());}
	 	         
	        	output.append(Integer.toString(WCCS.getComponents().get(component).get(node1).getId()));
	        	output.append(" ");
	        	output.append(Integer.toString(WCCS.getComponents().get(component).get(node2).getId()));
	        	output.newLine();
	        	
	        }
	        output.close();
		
	}
	void readTestFiles(String fileName,int algo) throws IOException
	{
		 File file = null;

		 String resultFileName=fileName.substring(0, fileName.length()-4)+"results.txt";
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
    System.out.println("Carte " +carte);

    s2.close();
  
   
    s2 = new Scanner(sc2.nextLine());
    int mode=Integer.parseInt(s2.next());
    s2.close();
    s2 = new Scanner(sc2.nextLine());
    int nbPairs=Integer.parseInt(s2.next());
    System.out.println("nb Pairs " +nbPairs);
    s2.close();
    System.out.println("ID Mode"+ mode);
    while (sc2.hasNextLine()) {
        s2 = new Scanner(sc2.nextLine());
        ShortestPathAlgorithm algorithm=null;
        ShortestPathSolution solution=null;
        ShortestPathData data=null; 
        
        while (s2.hasNext()) {
            String originid = s2.next();
            String destinationid=s2.next();
            output.append(originid+" "+destinationid+" ");
            switch(algo)
            {
            case (0): 
            	output.append("BellmanFord");
            	break; 
            case (1): 
            	output.append("Dijkstra");
            	break; 
            case (2): 
            	output.append("Astar");
            	break; 
            	            
            
            }
            output.newLine();
            Node origin=graph.getNodes().get(Integer.parseInt(originid)); 
            Node destination=graph.getNodes().get(Integer.parseInt(destinationid));
            data=new ShortestPathData(graph,origin,destination,ArcInspectorFactory.getAllFilters().get(mode==0?0:2));
			BinaryHeapObserver myObserver=new BinaryHeapObserver(); 

            
            
            if (algo==0)
            {
            	algorithm=new BellmanFordAlgorithm (data);
            	solution=algorithm.run();
            	if (solution.getStatus()==Status.OPTIMAL) 
            	{if (mode==0)
            	{
            	output.append(Double.toString(solution.getPath().getLength()));
            	}
            	else 
            	{
                	output.append(Double.toString(solution.getPath().getMinimumTravelTime()));

            	}
            	output.append(" " +Long.toString(solution.getSolvingTime().getSeconds())+"."+ Long.toString(solution.getSolvingTime().getNano()));

            	}
            	}
            	
            	
            
            else if (algo==1)
            {
            	algorithm=new DijkstraAlgorithm (data);
            	algorithm.addObserver(myObserver);
            	
            	solution=algorithm.run();
            	if (solution.getStatus()==Status.OPTIMAL) 
            	{if (mode==0)
            	{
            	output.append(Double.toString(solution.getPath().getLength()));
            	}
            	else 
            	{
                	output.append(Double.toString(solution.getPath().getMinimumTravelTime()));

            	}
            	output.append(" " +Long.toString(solution.getSolvingTime().getSeconds())+"."+ Long.toString(solution.getSolvingTime().getNano()));

            	}
            	
            	
            	
            	

            }
            else if (algo==2)
            {
            	
            	algorithm=new AStarAlgorithm (data);
            	algorithm.addObserver(myObserver);
            	solution=algorithm.run();
            	if (solution.getStatus()==Status.OPTIMAL) 
            	{if (mode==0)
            	{
            	output.append(Double.toString(solution.getPath().getLength()));
            	}
            	else 
            	{
                	output.append(Double.toString(solution.getPath().getMinimumTravelTime()));

            	}
            	output.append(" " +Long.toString(solution.getSolvingTime().getSeconds())+"."+ Long.toString(solution.getSolvingTime().getNano()));
            	output.append(" "+ Integer.toString(myObserver.getNb_explores()) + " "+ Integer.toString((myObserver.getNb_explores()))+" "+Integer.toString(myObserver.getMax_tas()));
            	}
            	
            	
            		

            }
            	
            
            
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
		String carte="insa";
		createTestFiles(carte,0,30);
		readTestFiles(".//"+carte+"//"+carte+"_distance_30.txt",1);
	}
	
	
	
	

}
