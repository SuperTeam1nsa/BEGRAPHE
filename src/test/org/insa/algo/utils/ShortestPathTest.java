package org.insa.algo.utils;

import static org.junit.Assert.assertEquals;
import org.insa.graph.GraphStatistics;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;


import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.AStarAlgorithm;
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
	

		private String pathMapsFolder = "./";
		
		
		final private double epsilon = 10^-5;
		
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
	
		
		public void sameNodeTestDijstra(String mapName, int mode) //mode = 0 => Length; mode = 2 => Time
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
		
		
		public void randomTestDijkstra (String mapName, int nbTest, int mode) {//mode = 0 => Length; mode = 2 => Time

			Graph graph = LireGraphe(pathMapsFolder + mapName);
			ShortestPathData data;
			
			BellmanFordAlgorithm bellmanAlgo;
			ShortestPathSolution bellmanSolution;
			
			DijkstraAlgorithm dijkstraAlgo;
			ShortestPathSolution dijkstraSolution;
			
			
			
			Node origin;
			Node destination;
			
			
			
			origin = pickNode(graph);
			destination = pickNode(graph);
			while(origin.getId() == destination.getId()){
				destination = pickNode(graph);
			}
			
			data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(mode));
			
			bellmanAlgo = new BellmanFordAlgorithm(data);
			bellmanSolution = bellmanAlgo.run();
			
			double [] bellmanCosts = bellmanAlgo.runMyTests();
			
			double djikstraResult;
			for(int i=0; i<nbTest; i++){
			
				dijkstraAlgo = new DijkstraAlgorithm(data);
				dijkstraSolution = dijkstraAlgo.run();
				
				
				if(dijkstraSolution.getPath() == null) { // No path found ! The cost is infinity
					djikstraResult = Double.POSITIVE_INFINITY;
				}
				else {
					if(mode == 0)
						djikstraResult = dijkstraSolution.getPath().getLength();
					else
						djikstraResult = dijkstraSolution.getPath().getMinimumTravelTime();
				}
				
				try {
					assertEquals(djikstraResult, bellmanCosts[destination.getId()], epsilon);
				}
				catch(java.lang.AssertionError e) {
					System.out.println("[ Error on randomTestDjikstra ] mapName( " + mapName + " ) originId(" + origin.getId() + ")" + " destinationId(" + destination.getId() + ") mode( " + mode + " )");
					throw e;
				}
	            
				destination = pickNode(graph); //Take a new node
				
				while(origin.getId() == destination.getId()){
					destination = pickNode(graph);
				}
				
				data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(mode));	
				
			}
		}
		
		
		
		public void randomTestAStar (String mapName, int nbTest, int mode) {//mode = 0 => Length; mode = 2 => Time

			Graph graph = LireGraphe(pathMapsFolder + mapName);
			ShortestPathData data;
			
			BellmanFordAlgorithm bellmanAlgo;
			ShortestPathSolution bellmanSolution;
			
			AStarAlgorithm aStarAlgo;
			ShortestPathSolution aStarSolution;
			
			
			
			Node origin;
			Node destination;
			
			
			
			origin = pickNode(graph);
			destination = pickNode(graph);
			while(origin.getId() == destination.getId()){
				destination = pickNode(graph);
			}
			
			data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(mode));
			
			bellmanAlgo = new BellmanFordAlgorithm(data);
			bellmanSolution = bellmanAlgo.run();
			
			double [] bellmanCosts = bellmanAlgo.runMyTests();
			
			double djikstraResult;
			for(int i=0; i<nbTest; i++){
			
				aStarAlgo = new AStarAlgorithm(data);
				aStarSolution = aStarAlgo.run();
				
				
				if(aStarSolution.getPath() == null) { // No path found ! The cost is infinity
					djikstraResult = Double.POSITIVE_INFINITY;
				}
				else {
					if(mode == 0)
						djikstraResult = aStarSolution.getPath().getLength();
					else
						djikstraResult = aStarSolution.getPath().getMinimumTravelTime();
				}
				
				try {
					assertEquals(djikstraResult, bellmanCosts[destination.getId()], epsilon);
				}
				catch(java.lang.AssertionError e) {
					System.out.println("[ Error on randomTestAStar ] mapName( " + mapName + " ) originId(" + origin.getId() + ")" + " destinationId(" + destination.getId() + ") mode( " + mode + " )");
					throw e;
				}
	            
				destination = pickNode(graph); //Take a new node
				
				while(origin.getId() == destination.getId()){
					destination = pickNode(graph);
				}
				
				data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(mode));	
				
			}
		}
		
		
		
		
		public void randomSubpathTestDijkstra(String mapName, int nbTest, int mode) {//mode = 0 => Length; mode = 2 => Time
			Graph graph = LireGraphe(pathMapsFolder + mapName);
			ShortestPathData data;
			
			BellmanFordAlgorithm bellmanAlgo;
			ShortestPathSolution bellmanSolution;
			
			DijkstraAlgorithm dijkstraAlgo;
			ShortestPathSolution dijkstraSolution;
			
			
			
			Node origin;
			Node destination;
			
			
			
			origin = pickNode(graph);
			destination = pickNode(graph);
			while(origin.getId() == destination.getId()){
				destination = pickNode(graph);
			}
			
			data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(mode));
			
			bellmanAlgo = new BellmanFordAlgorithm(data);
			bellmanSolution = bellmanAlgo.run();
			
			int id;
			
			for(int i=0; i<nbTest; i++){
			
				dijkstraAlgo = new DijkstraAlgorithm(data);
				dijkstraSolution = dijkstraAlgo.run();
				
				Label [] mapDijkstra = dijkstraAlgo.getMapLabel();
				double [] bellmanCosts = bellmanAlgo.runMyTests();
				
				Assume.assumeTrue(mapDijkstra.length == graph.size()); //DijkstraAlgo correspond to the right graph
				
				
				Arc arc = mapDijkstra[data.getDestination().getId()].getFather();
				
				while ( arc != null ) {
					id = arc.getDestination().getId();
					assertEquals(mapDijkstra[id].getCost(), bellmanCosts[id], epsilon);
	            	arc=mapDijkstra[arc.getOrigin().getId()].getFather();
	            }
				
				destination = pickNode(graph); //Take a new node
				
				while(origin.getId() == destination.getId()){
					destination = pickNode(graph);
				}
				
				data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(mode));	
				
			}
		}
		
		

		public void randomSubpathTestAStar(String mapName, int nbTest, int mode) {//mode = 0 => Length; mode = 2 => Time
			Graph graph = LireGraphe(pathMapsFolder + mapName);
			ShortestPathData data;
			
			BellmanFordAlgorithm bellmanAlgo;
			ShortestPathSolution bellmanSolution;
			
			AStarAlgorithm aStarAlgo;
			ShortestPathSolution aStarSolution;
			
			
			
			Node origin;
			Node destination;
			
			
			
			origin = pickNode(graph);
			destination = pickNode(graph);
			while(origin.getId() == destination.getId()){
				destination = pickNode(graph);
			}
			
			data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(mode));
			
			bellmanAlgo = new BellmanFordAlgorithm(data);
			bellmanSolution = bellmanAlgo.run();
			
			int id;
			
			for(int i=0; i<nbTest; i++){
			
				aStarAlgo = new AStarAlgorithm(data);
				aStarSolution = aStarAlgo.run();
				
				Label [] mapAStar = aStarAlgo.getMapLabel();
				double [] bellmanCosts = bellmanAlgo.runMyTests();
				
				Assume.assumeTrue(mapAStar.length == graph.size()); //AStarAlgo correspond to the right graph
				
				
				Arc arc = mapAStar[data.getDestination().getId()].getFather();
				
				while ( arc != null ) {
					id = arc.getDestination().getId();
					assertEquals(mapAStar[id].getCost(), bellmanCosts[id], epsilon);
					
	            	arc=mapAStar[arc.getOrigin().getId()].getFather();
	            }
				
				destination = pickNode(graph); //Take a new node
				
				while(origin.getId() == destination.getId()){
					destination = pickNode(graph);
				}
				
				data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(mode));	
				
			}
		}
		
		@Test
		public void randomTestsAStar() {
			randomTestAStar("toulouse.mapgr", 10, 0); // Length
			randomTestAStar("toulouse.mapgr", 10, 2); // Time
			
			randomSubpathTestAStar("toulouse.mapgr", 10, 2); // Time
			randomSubpathTestAStar("toulouse.mapgr", 10, 0); // Length
		}
		
		
		@Test
		public void randomTestsDijkstra() {
			randomTestDijkstra("toulouse.mapgr", 10, 0); // Length
			randomTestDijkstra("toulouse.mapgr", 10, 2); // Time
			
			randomSubpathTestDijkstra("toulouse.mapgr", 10, 2); // Time
			randomSubpathTestDijkstra("toulouse.mapgr", 10, 0); // Length
		}
}
				
	   
	   

