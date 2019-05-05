package org.insa.algo.utils;

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
import org.insa.algo.utils.ShortestPathTest;

public class Brouillon {
	   

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		String mapNamee = "/Users/ejigu/Downloads/fractal.mapgr";
		 Graph graph=LireGraphe(mapNamee);
			ShortestPathSolution bSolution = null;
			BellmanFordAlgorithm bellman = null;
			DijkstraAlgorithm dij = null;
			ShortestPathSolution dSolution = null; 
			ShortestPathData d = null;
			Node U =pickNode(graph);
			
			d=new ShortestPathData(graph,U,U, ArcInspectorFactory.getAllFilters().get(0));
			bellman=new BellmanFordAlgorithm(d);
			dij=new DijkstraAlgorithm(d);
			bSolution=bellman.run();
			dSolution=dij.run(); 
			
			System.out.println(bSolution.getPath().getLength());
			System.out.println(dSolution.getPath().getLength());
		
		

	}

	public static Graph LireGraphe(String filePath) {
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
	private static Node pickNode(Graph graph) {
		return graph.get((int)(Math.random()*(graph.size())));
	}
	
}
