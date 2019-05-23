package org.insa.algo.utils;

import static org.junit.Assert.assertEquals;
import org.insa.graph.GraphStatistics;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;


import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.BinaryHeapObserver;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.Label;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.graph.Arc;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.io.BinaryGraphReader;
import org.junit.Assume;
//import org.junit.BeforeClass;
import org.junit.Test;

public class ShortestPathTest {
	

		private String pathMapsFolder = "maps/";
		
		
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
	
		
		public void sameNodeTest(String mapName, int mode) //mode = 0 => Length; mode = 2 => Time
		{
			Graph graph = LireGraphe(pathMapsFolder + mapName);
			ShortestPathData data;
			
			BellmanFordAlgorithm bellmanAlgo;
			ShortestPathSolution bellmanSolution;
			
			DijkstraAlgorithm dijkstraAlgo;
			ShortestPathSolution dijkstraSolution;
			
			Node origin_destination=pickNode(graph);
			data = new ShortestPathData(graph, origin_destination, origin_destination, ArcInspectorFactory.getAllFilters().get(mode));
			
			bellmanAlgo = new BellmanFordAlgorithm(data);
			bellmanSolution=bellmanAlgo.run();
			
			dijkstraAlgo = new DijkstraAlgorithm(data);
			dijkstraSolution = dijkstraAlgo.run();
			
			//To remove
			/*System.out.println(dijkstraSolution.toString());
			System.out.println(bellmanSolution.toString());*/
			
			assertEquals(dijkstraSolution.getCost(), bellmanSolution.getCost(), epsilon);
			
		}
			
		
		//@Test
		public void testexhaustif(String mapName) {
			Graph graph=LireGraphe(pathMapsFolder + mapName);
			int nbnodes=graph.size();
			int []modes= {0,2};
			
			
			
			
			ShortestPathSolution bSolution=null;
			ShortestPathData d=null;

			ShortestPathData bellmanInput=null;
			BellmanFordAlgorithm bellRun=null;
			for (int m=0;m<2;m++)
			{
				for (int originid=0;originid<graph.size();originid++)
				{
					bellmanInput=new ShortestPathData(graph,graph.getNodes().get(originid),graph.getNodes().get((originid==0)?1:0),ArcInspectorFactory.getAllFilters().get(modes[m]));
					bellRun=new BellmanFordAlgorithm(bellmanInput);
					bSolution=bellRun.run();
					double [] bellmanData=bellRun.runMyTests();
					DijkstraAlgorithm dij=null;
					ShortestPathSolution dSolution = null; 

					for (int destinationid=0;destinationid<graph.size();destinationid++)
					{
						 
						if (bellmanData[destinationid]!=Double.POSITIVE_INFINITY & bellmanData[destinationid]!=0.0)
						{
							d=new ShortestPathData(graph,graph.getNodes().get(originid),graph.getNodes().get(destinationid),ArcInspectorFactory.getAllFilters().get(modes[m]));
							dij= new DijkstraAlgorithm(d);
							dSolution=dij.run();
							if(modes[m]==0)
							assertEquals(dSolution.getPath().getLength(),bellmanData[destinationid],epsilon);
							else
								assertEquals(dSolution.getPath().getMinimumTravelTime(),bellmanData[destinationid],epsilon);

							
						}
						
					
					}
				}
			}
		
			}
		
		
	
		public void testShortestPath50NoeudsRandom(String mapName) {
			
		Graph graph=LireGraphe(pathMapsFolder + mapName);
	
		Node origin=ShortestPathTest.pickNode(graph);
		
		
		
		ShortestPathSolution bSolution=null;
		ShortestPathData d=null;

		ShortestPathData bellmanInput=new ShortestPathData(graph,origin,ShortestPathTest.pickNode(graph),ArcInspectorFactory.getAllFilters().get(0));
		BellmanFordAlgorithm bellRun=new BellmanFordAlgorithm(bellmanInput);
		System.out.println("id "+Integer.toString( origin.getId())); //???
		bSolution=bellRun.run();
		System.out.println(bSolution.toString());

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
				if (bellmanData[testNode.getId()]!=Double.POSITIVE_INFINITY && bellmanData[testNode.getId()]!=0.0)
				{
					d=new ShortestPathData(graph,origin,testNode,ArcInspectorFactory.getAllFilters().get(0));
					dij= new DijkstraAlgorithm(d);
					dSolution=dij.run();
					assertEquals(dSolution.getPath().getLength(),bellmanData[testNode.getId()],epsilon);


					
				}
				
				
				
			}
			
		
			
			
		
	
		}
		
		
		private void samePathManyNodeTest(String mapName, int mode) //mode = 0 => Length; mode = 2 => Time
		{
			Graph graph = LireGraphe(pathMapsFolder + mapName);
			ShortestPathData data;
			
			BellmanFordAlgorithm bellmanAlgo;
			ShortestPathSolution bellmanSolution;
			
			DijkstraAlgorithm dijkstraAlgo;
			ShortestPathSolution dijkstraSolution;
			
			Node origin=pickNode(graph);
			Node destination = pickNode(graph);
			
			
			
			
			data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(mode));
			
			bellmanAlgo = new BellmanFordAlgorithm(data);
			bellmanSolution = bellmanAlgo.run();
			dijkstraAlgo = new DijkstraAlgorithm(data);
			dijkstraSolution = dijkstraAlgo.run();
			
			Label [] mapDijkstra = dijkstraAlgo.getMapLabel();
			double [] bellmanCosts = bellmanAlgo.runMyTests();
			
			Assume.assumeTrue(mapDijkstra.length == graph.size()); //DijkstraAlgo correspond to the right graph
			int id;
			Arc arc = mapDijkstra[data.getDestination().getId()].getFather();
			
			while ( arc != null ) {
				id = arc.getDestination().getId();
				assertEquals(mapDijkstra[id].getCost(), bellmanCosts[id], epsilon);
            	arc=mapDijkstra[arc.getOrigin().getId()].getFather();
            	
            }
			
		}
		public void testFastestTime50NoeudsRandom(String mapName) {

			
			
			Graph graph=LireGraphe(pathMapsFolder + mapName);
			
			Node origin=ShortestPathTest.pickNode(graph);
			
			
			
			
			ShortestPathData d=null;
			ShortestPathSolution bSolution=null;

			ShortestPathData bellmanInput=new ShortestPathData(graph,origin,ShortestPathTest.pickNode(graph),ArcInspectorFactory.getAllFilters().get(2));

			BellmanFordAlgorithm bellRun=new BellmanFordAlgorithm(bellmanInput);
			System.out.println("id "+Integer.toString( origin.getId()));
			bSolution=bellRun.run();
			double [] bellmanData=bellRun.runMyTests();
			BinaryHeapObserver myObserver=new BinaryHeapObserver(); 
			
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

				for (int i=0; i<1;i++)
				{
					testNode=pickNode(graph); 
					if (bellmanData[testNode.getId()]!=Double.POSITIVE_INFINITY & bellmanData[testNode.getId()]!=0.0)
					{
						d=new ShortestPathData(graph,origin,testNode,ArcInspectorFactory.getAllFilters().get(2));
						dij= new DijkstraAlgorithm(d);
						//dij.addObserver(myObserver);

						dSolution=dij.run();
						assertEquals(dSolution.getPath().getMinimumTravelTime(),bellmanData[testNode.getId()],epsilon);
						System.out.println("nb explores= "+Integer.toString(myObserver.getNb_explores())+"nb marques= "+Integer.toString(myObserver.getNb_marques()) +"nb max tas= "+ Integer.toString(myObserver.getMax_tas()));

						
					}
					
					
					
				}
		}
		
		public void randomTest(String mapName, int nbTest, int mode) {
			Graph graph = LireGraphe(pathMapsFolder + mapName);
			ShortestPathData data;
			
			BellmanFordAlgorithm bellmanAlgo;
			ShortestPathSolution bellmanSolution;
			
			DijkstraAlgorithm dijkstraAlgo;
			ShortestPathSolution dijkstraSolution;
			
			
			
			Node origin;
			Node destination;
			
			for(int i=0; i<nbTest; i++){
				origin = pickNode(graph);
				destination = pickNode(graph);
				while(origin.getId() == destination.getId()){
					destination = pickNode(graph);
				}
					
				data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(mode));
				
				bellmanAlgo = new BellmanFordAlgorithm(data);
				bellmanSolution = bellmanAlgo.run();
				dijkstraAlgo = new DijkstraAlgorithm(data);
				dijkstraSolution = dijkstraAlgo.run();
				
				Label [] mapDijkstra = dijkstraAlgo.getMapLabel();
				double [] bellmanCosts = bellmanAlgo.runMyTests();
				
				Assume.assumeTrue(mapDijkstra.length == graph.size()); //DijkstraAlgo correspond to the right graph
				int id;
				Arc arc = mapDijkstra[data.getDestination().getId()].getFather();
				
				while ( arc != null ) {
					id = arc.getDestination().getId();		
					assertEquals(mapDijkstra[id].getCost(), bellmanCosts[id], epsilon);
	            	arc=mapDijkstra[arc.getOrigin().getId()].getFather();
	            	
	            }
			}
		}
		
		@Test
		public void run(){
			sameNodeTest("insa.mapgr", 0);//LenghtMode
			sameNodeTest("insa.mapgr", 2);//TimeMode
			
			samePathManyNodeTest("insa.mapgr", 0);//LenghtMode
			samePathManyNodeTest("insa.mapgr", 2);//TimetMode
			
			randomTest("insa.mapgr", 10, 0);//LenghtMode
			randomTest("insa.mapgr", 10, 2);//LenghtMode
			
			
		}
}
				
	   
	   

