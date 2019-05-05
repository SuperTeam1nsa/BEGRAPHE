package org.insa.algo.utils;

import static org.junit.Assert.assertEquals;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
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

public class ShortestPathTest {
	

		private String mapName = "/Users/ejigu/Downloads/fractal.mapgr";
		
		
		
		public Graph LireGraphe(String filePath) {
  //  JFileChooser chooser = FileUtils.createFileChooser(FolderType.Map);
    
        //graphFilePath = chooser.getSelectedFile().getAbsolutePath();
        DataInputStream stream;
        try {
            stream = new DataInputStream(new BufferedInputStream(
                    new FileInputStream(filePath)));
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
		private Node pickNode(Graph graph) {
			return graph.get((int)(Math.random()*(graph.size())));
		}
		
		
		public void randomtest(Graph graph,ShortestPathSolution bSolution, BellmanFordAlgorithm bellman,DijkstraAlgorithm dij, ShortestPathSolution dSolution, ShortestPathData d)
		{
			d=new ShortestPathData(graph, pickNode(graph), pickNode(graph), ArcInspectorFactory.getAllFilters().get(0));
			bellman=new BellmanFordAlgorithm(d);
			bSolution=bellman.run();
			dij=new DijkstraAlgorithm(d);
			dSolution=dij.run();
			assertEquals(dSolution.getPath().getLength(),bSolution.getPath().getLength(),10^(-6));
		}
		
		public void samenodetest(Graph graph,ShortestPathSolution bSolution, BellmanFordAlgorithm bellman,BellmanFordAlgorithm dij, ShortestPathSolution dSolution, ShortestPathData d)
		{
			
			Node Unique=pickNode(graph);
			d=new ShortestPathData(graph, Unique, Unique, ArcInspectorFactory.getAllFilters().get(0));
			bellman=new BellmanFordAlgorithm(d);
			bSolution=bellman.run();
			dij=new BellmanFordAlgorithm(d);
			dSolution=dij.run();
			assertEquals(dSolution.getPath().getLength(),bSolution.getPath().getLength(),10^(-6));
		}
			
		
		
		
		
		@Test
	public void test() {
		Graph graph=LireGraphe(mapName);
		ShortestPathSolution bSolution = null;
		BellmanFordAlgorithm bellman = null;
		BellmanFordAlgorithm dij = null;
		ShortestPathSolution dSolution = null; 
		ShortestPathData d = null;
		
		
		
		for (int i=0; i<5;i++)
		{
			//randomtest(graph,bSolution,bellman,dij,dSolution,d);
		}
		
		samenodetest(graph,bSolution,bellman,bellman,bSolution,d);
		
		
		
		
		
		}
		
		
		
		
	   
	   
	   
	   
	   
	   
}
