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
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.Path;
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
	
	String mapsPath="./Maps/"; 
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
       // ShortestPathAlgorithm algorithm=null;
        ShortestPathSolution solution=null;
        ShortestPathData data=null; 
        
        
        while (s2.hasNext()) {
            String originid = s2.next();
            String destinationid=s2.next();
            output.append(originid+" "+destinationid+" ");
 
            Node origin=graph.getNodes().get(Integer.parseInt(originid)); 
            Node destination=graph.getNodes().get(Integer.parseInt(destinationid));
            data=new ShortestPathData(graph,origin,destination,ArcInspectorFactory.getAllFilters().get(mode==0?0:2));

            
            
            if (algo==0)
            {	
            	
    			BinaryHeapObserver myObserver=new BinaryHeapObserver(); 

    			BellmanFordAlgorithm algorithm=new BellmanFordAlgorithm (data);
            	solution=algorithm.run();
          
            	if (solution.getStatus()==Status.OPTIMAL) 
            	{if (mode==0)
            	{
            	output.append(Double.toString(solution.getPath().getLength())); //resultat en distance
            	}
            	else 
            	{
                	output.append(Double.toString(solution.getPath().getMinimumTravelTime())); //résultat en temps

            	}
            
            	output.append(" " +Long.toString(solution.getSolvingTime().toNanos())); //temps chnometré par les méthodes fournies
            	output.append(" "+Integer.toString(solution.getPath().getArcs().size())); // nb noeuds-1 
            	algorithm=null; // Précaution pour que le Garbage Collector puisse supprimer les données par la suite
            	
            	}
            	}
            	
        
            
            else if (algo==1)
            {	
            	
    			BinaryHeapObserver myObserver=new BinaryHeapObserver(); 

    			DijkstraAlgorithm  algorithm=new DijkstraAlgorithm (data);
            	algorithm.addObserver(myObserver);
            	solution=algorithm.run();
         
            	if (solution.getStatus()==Status.OPTIMAL) 
            	{if (mode==0)
            	{
            	output.append(Double.toString(solution.getPath().getLength())); //resultat en distance
            	}
            	else 
            	{
                	output.append(Double.toString(solution.getPath().getMinimumTravelTime())); //résultat en temps

            	}
            	
            	
            	output.append(" " +Long.toString(solution.getSolvingTime().toNanos())); //temps chnometré par les méthodes fournies
            	
            	
            	/* 
            	  utilisation de l'observer pour récuperer les informations qui'il a récupéré (nb explorés, marqués et nb d'éléments max du tas) 
            	 */
            	output.append(" "+ Integer.toString(myObserver.getNb_explores()) + " "+ Integer.toString((myObserver.getNb_explores()))+" "+Integer.toString(myObserver.getMax_tas())); 
            	
            	
            	output.append(" "+Integer.toString(solution.getPath().getArcs().size())); // nb noeuds-1 
            	output.append(" " +Long.toString(algorithm.getInitTime()));  //utilisation de la méthode (implémentée par nous) pour récuperer le temps d'initialisation des labels
            	output.append(" " +Long.toString(algorithm.getCalculationTime()));  //utilisation de la méthode (implémentée par nous) pour récuperer le temps de calcul 
            	algorithm=null; 
            	}

            }
            else if (algo==2)
            {
    			BinaryHeapObserver myObserver=new BinaryHeapObserver(); 

    			AStarAlgorithm algorithm=new AStarAlgorithm (data);
            	algorithm.addObserver(myObserver);
            	solution=algorithm.run();
            	
            	if (solution.getStatus()==Status.OPTIMAL) 
            	{if (mode==0)
            	{
            	output.append(Double.toString(solution.getPath().getLength())); //resultat en distance
            	}
            	else 
            	{
                	output.append(Double.toString(solution.getPath().getMinimumTravelTime())); //résultat en temps

            	}
            
            	output.append(" " +Long.toString(solution.getSolvingTime().toNanos())); //temps chnometré par les méthodes fournies
            	
            	/* 
           	  utilisation de l'observer pour récuperer les informations qui'il a récupéré (nb explorés, marqués et nb d'éléments max du tas) 
           	 */
            	output.append(" "+ Integer.toString(myObserver.getNb_explores()) + " "+ Integer.toString((myObserver.getNb_explores()))+" "+Integer.toString(myObserver.getMax_tas()));
            	output.append(" "+Integer.toString(solution.getPath().getArcs().size())); // nb noeuds-1  
            	output.append(" " +Long.toString(algorithm.getInitTime()));  //utilisation de la méthode (implémentée par nous) pour récuperer le temps d'initialisation des labels
            	output.append(" " +Long.toString(algorithm.getCalculationTime()));  //utilisation de la méthode (implémentée par nous) pour récuperer le temps de calcul 
            	algorithm=null; 
            	}
            }
            	
            
            
        }
        output.newLine();
        
        System.gc();
    }
    output.close();
    s2.close();
    sc2.close();
    
    }
	
	
	
	
	
	@Test
	public void createFiles() throws IOException
	{
		  File folder =new File("./Maps");
		   File[] fileNames = folder.listFiles();
		   
		for (File f:fileNames)
		{String carte=f.getName().substring(0,f.getName().length()-6);
		System.out.println(carte);
		int modeInspector=0;
		int nbPair=50;
		
		
		//Mode distance 
		String modeInspectorString;
		if (modeInspector==0)
			modeInspectorString="distance"; 
		else 
			modeInspectorString="temps"; 
		
	
		
		createTestFiles(carte,modeInspector,nbPair);
		System.out.println(carte+ " " +modeInspectorString +" DONE CREATING FILES");
		
		//Dernier parametre: 0 = bellman , 1= dijkstra, 2=astar
		
		readTestFiles(testResultPath+"//"+carte+"//"+carte+"_"+modeInspectorString+"_"+Integer.toString(nbPair)+".txt",0);
		System.out.println(carte+ " " +modeInspectorString +" Bellman done");


		readTestFiles(testResultPath+"//"+carte+"//"+carte+"_"+modeInspectorString+"_"+Integer.toString(nbPair)+".txt",1);
		System.out.println(carte+ " " +modeInspectorString +" Dijkstra Done");
		

		readTestFiles(testResultPath+"//"+carte+"//"+carte+"_"+modeInspectorString+"_"+Integer.toString(nbPair)+".txt",2);
		System.out.println(carte+ " " +modeInspectorString +" Astar Done");

		
		
		
		//Passage en Mode temps 
		modeInspector=1;
		if (modeInspector==0)
			modeInspectorString="distance"; 
		else 
			modeInspectorString="temps"; 
		
		
		createTestFiles(carte,modeInspector,nbPair);
		System.out.println(carte+ " " +modeInspectorString +" DONE CREATING FILES");

		readTestFiles(testResultPath+"//"+carte+"//"+carte+"_"+modeInspectorString+"_"+Integer.toString(nbPair)+".txt",0);
		System.out.println(carte+ " " +modeInspectorString +"Bellman done");
		
		
		readTestFiles(testResultPath+"//"+carte+"//"+carte+"_"+modeInspectorString+"_"+Integer.toString(nbPair)+".txt",1);
		System.out.println(carte+ " " +modeInspectorString +" Dijkstra Done");

		
		readTestFiles(testResultPath+"//"+carte+"//"+carte+"_"+modeInspectorString+"_"+Integer.toString(nbPair)+".txt",2);
		System.out.println(carte+ " " +modeInspectorString +" Astar Done");
	
		
		 	

	
		}
		
	}
	
	
	
	

}
