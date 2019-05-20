package org.insa.algo.utils;

import static org.junit.Assert.assertEquals;
import org.insa.graph.GraphStatistics;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;


import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.BellmanForTests;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.io.BinaryGraphReader;
//import org.junit.BeforeClass;
import org.junit.Test;

public class ShortestPathTest {
	

		private String mapName = ""
				+ "C:\\Users\\ejigu\\Downloads\\insa.mapgr";
		
		final private double epsilon = 0.001;
		
		public static Graph LireGraphe(String filePath) {
  //  JFileChooser chooser = FileUtils.createFileChooser(FolderType.Map);
    
        //graphFilePath = chooser.getSelectedFile().getAbsolutePath();
	        DataInputStream stream;
	        try {
	            stream = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
	        }
	        catch (IOException e1) {
	            System.out.println("Cannot open the selected file.");
	            return null;
	        }
	        
	        try {
				return new BinaryGraphReader(stream).read();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return null;
			}
        
		}
		
		public static Node pickNode(Graph graph) {
			return graph.get((int)(Math.random()*(graph.size())));
		}
	
		
		public void samenodetest(Graph graph,ShortestPathSolution bSolution, BellmanFordAlgorithm bellman,DijkstraAlgorithm dij, ShortestPathSolution dSolution, ShortestPathData d,int mode)
		{
			
			Node Unique=pickNode(graph);
			d=new ShortestPathData(graph, Unique, Unique, ArcInspectorFactory.getAllFilters().get(mode));
			bellman=new BellmanFordAlgorithm(d);
			bSolution=bellman.run();
			dij=new DijkstraAlgorithm(d);
			dSolution=dij.run();
			System.out.println(dSolution.toString());
			System.out.println(bSolution.toString());
			assertEquals(dSolution.toString().substring(0,3),bSolution.toString().substring(0,3));
			
		}
			
		
		
		
		
		@Test
	public void testshortestpath() {
		Graph graph=LireGraphe(mapName);
	
		Node origin=ShortestPathTest.pickNode(graph);
		
		
		
		
		ShortestPathData d=null;

		ShortestPathData bellmanInput=new ShortestPathData(graph,origin,origin,ArcInspectorFactory.getAllFilters().get(0));

		BellmanForTests bellRun=new BellmanForTests(bellmanInput);
		System.out.println("id "+Integer.toString( origin.getId()));

		double [] bellmanData=bellRun.runMyTests();
		
		/*
		for (int i=0;i<graph.size();i++)
		{
			
			System.out.print("id "+Integer.toString( i)+"=");

			System.out.println(Double.toString(bellmanData[i]));
			
			
		}
		*/
			Node testNode=null;
			DijkstraAlgorithm dij=null;
			ShortestPathSolution dSolution = null; 

			for (int i=0; i<50;i++)
			{
				testNode=pickNode(graph); 
				if (bellmanData[testNode.getId()]!=Double.POSITIVE_INFINITY & bellmanData[testNode.getId()]!=0.0)
				{
					d=new ShortestPathData(graph,origin,testNode,ArcInspectorFactory.getAllFilters().get(0));
					dij= new DijkstraAlgorithm(d);
					dSolution=dij.run();
					assertEquals(dSolution.getPath().getLength(),bellmanData[testNode.getId()],epsilon);


					
				}
				
				
				
			}
			
		
			
			
		
	
		}
		@Test
		public void testfastesttime() {
			
			Graph graph=LireGraphe(mapName);
			
			Node origin=ShortestPathTest.pickNode(graph);
			
			
			
			
			ShortestPathData d=null;

			ShortestPathData bellmanInput=new ShortestPathData(graph,origin,origin,ArcInspectorFactory.getAllFilters().get(2));

			BellmanForTests bellRun=new BellmanForTests(bellmanInput);
			System.out.println("id "+Integer.toString( origin.getId()));

			double [] bellmanData=bellRun.runMyTests();
			
			/*
			for (int i=0;i<graph.size();i++)
			{
				
				System.out.print("id "+Integer.toString( i)+"=");

				System.out.println(Double.toString(bellmanData[i]));
				
				
			}
			*/
				Node testNode=null;
				DijkstraAlgorithm dij=null;
				ShortestPathSolution dSolution = null; 

				for (int i=0; i<50;i++)
				{
					testNode=pickNode(graph); 
					if (bellmanData[testNode.getId()]!=Double.POSITIVE_INFINITY & bellmanData[testNode.getId()]!=0.0)
					{
						d=new ShortestPathData(graph,origin,testNode,ArcInspectorFactory.getAllFilters().get(2));
						dij= new DijkstraAlgorithm(d);
						dSolution=dij.run();
						assertEquals(dSolution.getPath().getMinimumTravelTime(),bellmanData[testNode.getId()],epsilon);


						
					}
					
					
					
				}
		}
}
				
	   
	   

