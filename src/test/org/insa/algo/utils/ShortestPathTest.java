package org.insa.algo.utils;


import static org.junit.Assert.assertEquals;


import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.IOException;


import org.insa.algo.ArcInspectorFactory;
import org.insa.algo.shortestpath.AStarAlgorithm;
import org.insa.algo.shortestpath.BellmanFordAlgorithm;
import org.insa.algo.shortestpath.DijkstraAlgorithm;
import org.insa.algo.shortestpath.ShortestPathData;
import org.insa.algo.shortestpath.ShortestPathSolution;
import org.insa.algo.shortestpath.ShortestPathTestObserver;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentsAlgorithm;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentsData;
import org.insa.algo.weakconnectivity.WeaklyConnectedComponentsSolution;
import org.insa.graph.Graph;
import org.insa.graph.Node;
import org.insa.graph.io.BinaryGraphReader;
import org.junit.Assume;
//import org.junit.BeforeClass;
import org.junit.Test;

public class ShortestPathTest {
	

		private String pathMapsFolder = "maps/";
		
		
		final private double epsilon = 10^-5;
		
		public static Graph LireGraphe(String filePath) {
  //  JFileChooser chooser = FileUtils.createFileChooser(FolderType.Map);
    
        //graphFilePath = chooser.getSelectedFile().getAbsolutePath();
	        DataInputStream stream;
	        try {
	            stream = new DataInputStream(new BufferedInputStream(new FileInputStream(filePath)));
	        }
	        catch (IOException e1) {
	            System.out.println("No file found at " + filePath);
	            return null;
	        }
	        
	        try {
				return new BinaryGraphReader(stream).read();
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
        
		}
		
		
		// nature=1 <=> pseudorandom; nature=0 <=> random
		public static Node pickNode(Graph graph, int nature) {
			if(nature == 1)
				return graph.get(PseudoRandom.generate(graph.size()));
			else
				return graph.get((int)(Math.random()*(graph.size())));

		}
		
		
		public static Node pickNode(Graph graph) {
			return graph.get((int)(Math.random()*(graph.size())));
		}
	
		// nature=1 <=> pseudorandom; nature=0 <=> random
		//mode = 0 => Length; mode = 2 => Time
		public void sameNodeTestDijstra(String mapName, int mode, int nature) 
		{
			Graph graph = LireGraphe(pathMapsFolder + mapName);
			ShortestPathData data;
			ShortestPathTestObserver observer = new ShortestPathTestObserver();
			
			BellmanFordAlgorithm bellmanAlgo;
			ShortestPathSolution bellmanSolution;
			
			DijkstraAlgorithm dijkstraAlgo;
			ShortestPathSolution dijkstraSolution;
			
			
			
			Node origin_destination=pickNode(graph, nature);
			data = new ShortestPathData(graph, origin_destination, origin_destination, ArcInspectorFactory.getAllFilters().get(mode));
			
			bellmanAlgo = new BellmanFordAlgorithm(data);
			bellmanSolution=bellmanAlgo.run();
			
			dijkstraAlgo = new DijkstraAlgorithm(data);
			dijkstraAlgo.addObserver(observer);
			dijkstraSolution = dijkstraAlgo.run();
			
			assertEquals(observer.isGrowingCost(), true);
			assertEquals(dijkstraSolution.getPath().isValid(), true);
			
			assertEquals(dijkstraSolution.getCost(), bellmanSolution.getCost(), epsilon);
			
		}
			
		
		public void exhaustiveTestDijkstra(String mapName) {
			Graph graph=LireGraphe(pathMapsFolder + mapName);
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
					double [] bellmanData=bellRun.getArraySolution();
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
		
		
		// nature=1 <=> pseudorandom; nature=0 <=> random
		//mode = 0 => Length; mode = 2 => Time
		public void randomTestDijkstra (String mapName, int nbTest, int mode, int nature) {
			
			Graph graph = LireGraphe(pathMapsFolder + mapName);
			ShortestPathData data;
			
			BellmanFordAlgorithm bellmanAlgo;
			ShortestPathSolution bellmanSolution;
			
			DijkstraAlgorithm dijkstraAlgo;
			ShortestPathSolution dijkstraSolution;
			
			ShortestPathTestObserver observer = new ShortestPathTestObserver();
			
			Node origin;
			Node destination;
			
			if(nature == 1){
				PseudoRandom.initSequence();
			}
			
			origin = pickNode(graph, nature);
			destination = pickNode(graph, nature);
			while(origin.getId() == destination.getId()){
				destination = pickNode(graph, nature);
			}
			
			data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(mode));
			
			bellmanAlgo = new BellmanFordAlgorithm(data);
			bellmanSolution = bellmanAlgo.run();
			
			double [] bellmanCosts = bellmanAlgo.getArraySolution();
			
			double djikstraResult;
			for(int i=0; i<nbTest; i++){
			
				dijkstraAlgo = new DijkstraAlgorithm(data);
				dijkstraAlgo.addObserver(observer);
				dijkstraSolution = dijkstraAlgo.run();
				
				
				if(dijkstraSolution.getPath() == null) { // No path found ! The cost is infinity
					djikstraResult = Double.POSITIVE_INFINITY;
				}
				else {
					if(mode == 0)
						djikstraResult = dijkstraSolution.getPath().getLength();
					else
						djikstraResult = dijkstraSolution.getPath().getMinimumTravelTime();
					
					assertEquals(dijkstraSolution.getPath().isValid(), true);
				}
				
				try {
					assertEquals(observer.isGrowingCost(), true);
					assertEquals(djikstraResult, bellmanCosts[destination.getId()], epsilon);
				}
				catch(java.lang.AssertionError e) {
					System.out.println("[ Error on randomTestDjikstra ] mapName( " + mapName + " ) originId(" + origin.getId() + ")" + " destinationId(" + destination.getId() + ") mode( " + mode + " )");
					throw e;
				}
	            
				destination = pickNode(graph, nature); //Take a new node
				
				while(origin.getId() == destination.getId()){
					destination = pickNode(graph, nature);
				}
				
				data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(mode));	
				
			}
		}

		
		//mode=0 => Length; mode=2 => Time
		// nature=0 => Random; nature=1 => Pseudorandom
		public void randomTestAStar (String mapName, int nbTest, int mode, int nature) {

			Graph graph = LireGraphe(pathMapsFolder + mapName);
			ShortestPathData data;
			
			BellmanFordAlgorithm bellmanAlgo;
			ShortestPathSolution bellmanSolution;
			
			AStarAlgorithm aStarAlgo;
			ShortestPathSolution aStarSolution;
			
			ShortestPathTestObserver observer = new ShortestPathTestObserver();
			
			Node origin;
			Node destination;
			
			if(nature == 1){
				PseudoRandom.initSequence();
			}
			
			origin = pickNode(graph, nature);
			destination = pickNode(graph, nature);
			while(origin.getId() == destination.getId()){
				destination = pickNode(graph, nature);
			}
			
			data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(mode));
			
			bellmanAlgo = new BellmanFordAlgorithm(data);
			bellmanSolution = bellmanAlgo.run();
			
			double [] bellmanCosts = bellmanAlgo.getArraySolution();
			
			double aStarResult;
			for(int i=0; i<nbTest; i++){
			
				aStarAlgo = new AStarAlgorithm(data);
				aStarAlgo.addObserver(observer);
				aStarSolution = aStarAlgo.run();
				
				
				if(aStarSolution.getPath() == null) { // No path found ! The cost is infinity
					aStarResult = Double.POSITIVE_INFINITY;
				}
				else {
					if(mode == 0)
						aStarResult = aStarSolution.getPath().getLength();
					else
						aStarResult = aStarSolution.getPath().getMinimumTravelTime();
					
					assertEquals(aStarSolution.getPath().isValid(), true);
				}
				
				try {					
					//assertEquals(observer.isGrowingCost(), true);
					assertEquals(aStarResult, bellmanCosts[destination.getId()], epsilon);
				}
				catch(java.lang.AssertionError e) {
					System.out.println("[ Error on randomTestAStar ] mapName( " + mapName + " ) originId(" + origin.getId() + ")" + " destinationId(" + destination.getId() + ") mode( " + mode + " )");
					throw e;
				}
	            
				destination = pickNode(graph, nature); //Take a new node
				
				while(origin.getId() == destination.getId()){
					destination = pickNode(graph, nature);
				}
				
				data = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(mode));	
				
			}
		}
		
		
		
		
		public void noPathTestAStar(String mapName) {
			Graph graph = LireGraphe(pathMapsFolder + mapName);
			WeaklyConnectedComponentsData dataWeakConn;
			ShortestPathData dataShortest;
			
			BellmanFordAlgorithm bellmanAlgo;
			ShortestPathSolution bellmanSolution;
			WeaklyConnectedComponentsSolution weakConnSolution;
			
			AStarAlgorithm aStarAlgo;
			ShortestPathSolution aStarSolution;
			
			ShortestPathTestObserver observer = new ShortestPathTestObserver();
			
			int nbComponent;
			
			Node origin;
			Node destination;
			
			dataWeakConn = new WeaklyConnectedComponentsData(graph);
			
			WeaklyConnectedComponentsAlgorithm weakConnAlgo = new WeaklyConnectedComponentsAlgorithm(dataWeakConn);
			
			weakConnSolution = weakConnAlgo.run(2);
			
			nbComponent = weakConnSolution.getComponents().size();
			
			if(nbComponent == 1)
			{
				System.out.println("< " + mapName + " > is a connected graph !");
			}
			/*else
			{
				System.out.println(nbComponent + " = nbComponent found !");
			}*/
			
			
			Assume.assumeTrue(nbComponent == 2);
			
			PseudoRandom.initSequence();
		
			int randomId = PseudoRandom.generate(weakConnSolution.getComponents().get(0).size());
			origin = weakConnSolution.getComponents().get(0).get(randomId);
			randomId = PseudoRandom.generate(weakConnSolution.getComponents().get(1).size());
			destination = weakConnSolution.getComponents().get(1).get(randomId);
			
			dataShortest = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(0));
		
			bellmanAlgo = new BellmanFordAlgorithm(dataShortest);
			bellmanSolution = bellmanAlgo.run();
			
			double [] bellmanCosts = bellmanAlgo.getArraySolution();
			
			Assume.assumeTrue("A path is found, weaklyComponentClass does not work !", bellmanCosts[destination.getId()] == Double.POSITIVE_INFINITY);
					
			
			aStarAlgo = new AStarAlgorithm(dataShortest);
			aStarAlgo.addObserver(observer);
			aStarSolution = aStarAlgo.run();
			
			//assertEquals(observer.isGrowingCost(), true);
			assertEquals(aStarSolution.getPath(), null);		
		
		}
		
		
		public void noPathTestDijkstra(String mapName) {
			
			Graph graph = LireGraphe(pathMapsFolder + mapName);
			WeaklyConnectedComponentsData dataWeakConn;
			ShortestPathData dataShortest;
			
			BellmanFordAlgorithm bellmanAlgo;
			ShortestPathSolution bellmanSolution;
			WeaklyConnectedComponentsSolution weakConnSolution;
			
			DijkstraAlgorithm dijkstraAlgo;
			ShortestPathSolution dijkstraSolution;
			
			ShortestPathTestObserver observer = new ShortestPathTestObserver();
			
			int nbComponent;
			
			Node origin;
			Node destination;
			
			dataWeakConn = new WeaklyConnectedComponentsData(graph);
			
			WeaklyConnectedComponentsAlgorithm weakConnAlgo = new WeaklyConnectedComponentsAlgorithm(dataWeakConn);
			
			weakConnSolution = weakConnAlgo.run(2);
			
			nbComponent = weakConnSolution.getComponents().size();
			
			if(nbComponent == 1)
			{
				System.out.println("< " + mapName + " > is a connected graph !");
			}
			
			
			Assume.assumeTrue(nbComponent == 2);
		
			PseudoRandom.initSequence();
			int randomId = PseudoRandom.generate(weakConnSolution.getComponents().get(0).size());
			origin = weakConnSolution.getComponents().get(0).get(randomId);
			randomId = PseudoRandom.generate(weakConnSolution.getComponents().get(1).size());
			destination = weakConnSolution.getComponents().get(1).get(randomId);
			
		
			dataShortest = new ShortestPathData(graph, origin, destination, ArcInspectorFactory.getAllFilters().get(0));
		
			bellmanAlgo = new BellmanFordAlgorithm(dataShortest);
			bellmanSolution = bellmanAlgo.run();
			
			double [] bellmanCosts = bellmanAlgo.getArraySolution();
			
			Assume.assumeTrue("A path has been found, weaklyComponentClass does not work !", bellmanCosts[destination.getId()] == Double.POSITIVE_INFINITY);
					
			
			dijkstraAlgo = new DijkstraAlgorithm(dataShortest);
			dijkstraAlgo.addObserver(observer);
			dijkstraSolution = dijkstraAlgo.run();
			
			assertEquals(observer.isGrowingCost(), true);
			assertEquals(dijkstraSolution.getPath(), null);		
		
		}
		
		
		
		
		// Campangne AStar
		
		@Test
		public void test_AStar_BENIN_Length_PseudoRandom(){
			randomTestAStar("benin.mapgr", 1, 0, 1); // Length, PseudoRandom
		}
		
		@Test
		public void test_AStar_BENIN_Length_Random(){
			randomTestAStar("benin.mapgr", 1, 0, 0); // Length, Random
		}
		
		@Test
		public void test_AStar_BENIN_Time_PseudoRandom(){
			randomTestAStar("benin.mapgr", 1, 2, 1); // Time, PseudoRandom
		}
		
		@Test
		public void test_AStar_BENIN_Time_Random(){
			randomTestAStar("benin.mapgr", 1, 2, 0); // Time, Random
		}
		
		
		@Test
		public void test_AStar_FRACTAL_noPath(){
			noPathTestAStar("fractal.mapgr");
		}
		
		@Test
		public void test_AStar_INSA_noPath(){
			noPathTestAStar("insa.mapgr");
		}
		
		
		// Campangne DIJKSTRA
		
		@Test
		public void test_Dijkstra_BENIN_Length_PseudoRandom(){
			randomTestDijkstra("benin.mapgr", 1, 0, 1); // Length, PseudoRandom
		}
		
		@Test
		public void test_Dijkstra_BENIN_Length_Random(){
			randomTestDijkstra("benin.mapgr", 1, 0, 0); // Length, Random
		}
		
		@Test
		public void test_Dijkstra_BENIN_Time_PseudoRandom(){
			randomTestDijkstra("benin.mapgr", 1, 2, 1); // Time, PseudoRandom
		}
		
		@Test
		public void test_Dijkstra_BENIN_Time_Random(){
			randomTestDijkstra("benin.mapgr", 1, 2, 0); // Time, Random
		}
		
		@Test
		public void test_Dijkstra_FRACTAL_noPath(){
			noPathTestDijkstra("fractal.mapgr");
		}
		
		@Test
		public void test_Dijkstra_INSA_noPath(){
			noPathTestDijkstra("insa.mapgr");
		}
		
		
		
		
}
				
	   
	   

