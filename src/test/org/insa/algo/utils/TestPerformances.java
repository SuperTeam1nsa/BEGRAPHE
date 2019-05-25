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
	
	String mapsPath="./"; 
	String testResultPath=".//test";
	Graph graph=null;
	public void createTestFiles(String carte,int mode, int nbPairs) throws IOException
	{
		 graph=ShortestPathTest.LireGraphe(mapsPath+carte+".mapgr");
	    
			ShortestPathData data;

			AStarAlgorithm aStarAlgo;
			ShortestPathSolution aStarSolution;
		 	
		 	
		 	
	        //file name only
	        String modetext=""; 
	        if (mode==0)
	        {modetext="distance";}
	        else if(mode==1)
	        {
	        modetext="temps";	
	        }
	        String fileName=carte+"_"+modetext+"_"+Integer.toString(nbPairs)+".txt";
	        File Folder=new File("test"); 
	        if (Folder.mkdir())
	        {
	        	System.out.println("Folder created");
	        }
	        else
	        {System.out.println("Folder not created");}
	        File subFolder=new File("./test/"+carte); 
	        if (subFolder.mkdir())
	        {
	        	System.out.println("Folder created");
	        }
	        else
	        {System.out.println("Folder not created");}
	       File file = new File(testResultPath+"//"+carte+"//"+fileName);
	        try {
				if(file.createNewFile()){
				    System.out.println(" File Created in Project root directory");
				}else System.out.println("File already exists in the project root directory");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        BufferedWriter output;
	        output = new BufferedWriter(new FileWriter(testResultPath+"//"+carte+"//"+fileName));  
	        
	        output.append(carte); 
	        output.newLine();
	        output.append(Integer.toString(mode)); 
	        output.newLine();
	        output.append(Integer.toString(nbPairs));
	        output.newLine();
	        Node origin;
	        Node destination; 
	        for (int i=0;i<nbPairs;i++)
	        {

				origin = ShortestPathTest.pickNode(graph);
				destination = ShortestPathTest.pickNode(graph);
				while(origin.getId() == destination.getId()){
					destination = ShortestPathTest.pickNode(graph);
				}
				
				data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get((mode==0)?0:2));
	        	
				aStarAlgo = new AStarAlgorithm(data);
				aStarSolution = aStarAlgo.run();
				
				if (aStarSolution.isFeasible()){
				output.append(Integer.toString(origin.getId()));
	        	output.append(" ");
	        	output.append(Integer.toString(destination.getId()));
	        	output.newLine();
				}
				else
				{
					i--;
				}
	        }
	        output.close();
		
	}
	void readTestFiles(String fileName,int algo) throws IOException
	{
		 File file = null;
		 String resultFileName="";
		 
		 switch(algo)
		    {
		    case (0): 
		    	 resultFileName=fileName.substring(0, fileName.length()-4)+"bellman"+"results.txt";
		    	break; 
		    case (1): 
		    	 resultFileName=fileName.substring(0, fileName.length()-4)+"dijkstra"+"results.txt";		
		    break; 
		    case (2): 
		    	 resultFileName=fileName.substring(0, fileName.length()-4)+"astar"+"results.txt";		    
		    	break; 
		    	            
		    
		    }
		 

		 
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
    output.append(Integer.toString(graph.size()));
    output.newLine();
    output.append(Integer.toString(nbPairs));
    output.newLine();

    
    while (sc2.hasNextLine()) {
        s2 = new Scanner(sc2.nextLine());
        ShortestPathAlgorithm algorithm=null;
        ShortestPathSolution solution=null;
        ShortestPathData data=null; 
        
        
        while (s2.hasNext()) {
            String originid = s2.next();
            String destinationid=s2.next();
            output.append(originid+" "+destinationid+" ");
 
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

            	String zerosFilling=""; 
            	for  (int z=0;z<9-(Long.toString(solution.getSolvingTime().getNano())).length();z++)
            	{
            		zerosFilling+="0";
            	}
            	output.append(" " +Long.toString(solution.getSolvingTime().getSeconds())+"."+zerosFilling+ Long.toString(solution.getSolvingTime().getNano()));
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
            	
            	String zerosFilling=""; 
            	for  (int z=0;z<9-(Long.toString(solution.getSolvingTime().getNano())).length();z++)
            	{
            		zerosFilling+="0";
            	}
            	output.append(" " +Long.toString(solution.getSolvingTime().getSeconds())+"."+zerosFilling+ Long.toString(solution.getSolvingTime().getNano()));
            	output.append(" "+ Integer.toString(myObserver.getNb_explores()) + " "+ Integer.toString((myObserver.getNb_explores()))+" "+Integer.toString(myObserver.getMax_tas()));
            	
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
            	String zerosFilling=""; 
            	for  (int z=0;z<9-(Long.toString(solution.getSolvingTime().getNano())).length();z++)
            	{
            		zerosFilling+="0";
            	}
            	output.append(" " +Long.toString(solution.getSolvingTime().getSeconds())+"."+zerosFilling+ Long.toString(solution.getSolvingTime().getNano()));
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
		String carte="toulouse";
		int modeInspector=0;
		int nbPair=30;
		
		
		
		String modeInspectorString;
		if (modeInspector==0)
			modeInspectorString="distance"; 
		else 
			modeInspectorString="temps"; 
		
		
		createTestFiles(carte,modeInspector,nbPair);
		
		
		//Dernier parametre: 0 = bellman , 1= dijkstra, 2=astar
		readTestFiles(testResultPath+"//"+carte+"//"+carte+"_"+modeInspectorString+"_"+Integer.toString(nbPair)+".txt",0);
		readTestFiles(testResultPath+"//"+carte+"//"+carte+"_"+modeInspectorString+"_"+Integer.toString(nbPair)+".txt",1);
		readTestFiles(testResultPath+"//"+carte+"//"+carte+"_"+modeInspectorString+"_"+Integer.toString(nbPair)+".txt",2);
		
	}
	
	
	
	

}
