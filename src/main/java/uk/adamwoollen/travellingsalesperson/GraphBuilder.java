package uk.adamwoollen.travellingsalesperson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class GraphBuilder {

	public double[][] buildGraphFromCsv(String inputFilePath)
	{	
		List<Node> nodes = new ArrayList<>();
		
		try{
			File inputF = new File(inputFilePath);
			System.out.println("Looking at " + inputF.getAbsolutePath());
			InputStream inputFS = new FileInputStream(inputF);
			BufferedReader br = new BufferedReader(new InputStreamReader(inputFS));
			
			// Skip header lines (three in this case)
			nodes = br.lines().skip(1).skip(1).skip(1).map(mapToItem).collect(Collectors.toList());
			
			br.close();
		} catch (IOException e) {
			System.err.println("Error reading file: " + e);
		}
		
		double[][] graph = new double[nodes.size()][nodes.size()];
		
		// Set name maps
		Map<Integer, String> positionToName = new HashMap<>();
		for(int i = 0; i < nodes.size(); i++)
		{
			positionToName.put(i, nodes.get(i).name);
		}
		
		for (int i = 0; i < nodes.size(); i++)
		{
			Node startNode = nodes.get(i);
			for (int j = 0; j < nodes.size(); j++)
			{
				Node endNode = nodes.get(j);
				double distance = Math.sqrt(Math.pow(startNode.x - endNode.x, 2) + Math.pow(startNode.y - endNode.y, 2));
				graph[i][j] = distance;
				//System.out.println("graph[" + i + "][" + j + "] = " + distance);
			}
		}
		
		
		return graph;
	}
	
	private Function<String, Node> mapToItem = (line) -> {
	
		String[] column = line.split(",");// a CSV has comma separated lines
		
		String name = column[0];
		double x = Double.parseDouble(column[1]);
		double y = Double.parseDouble(column[2]);
		
		Node node = new Node(name, x, y);
		
		return node;
	};
	
	private class Node
	{
		public String name;
		public double x;
		public double y;
		
		public Node(String name, double x, double y)
		{
			this.name = name;
			this.x = x;
			this.y = y;
		}
	}
	
	private static final int A = 0;
	private static final int B = 1;
	private static final int C = 2;
	private static final int D = 3;
	
	public double[][] buildExampleGraph()
	{
		int numberOfNodes = 4;
		double[][] graph = new double[numberOfNodes][numberOfNodes];
//		route = new int[numberOfNodes];
		
		graph[A][B] = 20;
		graph[A][C] = 42;
		graph[A][D] = 35;
		graph[B][C] = 30;
		graph[B][D] = 34;
		graph[C][D] = 12;
		
		// Matrix is reflected
		for(int i = 0; i < graph.length; i++)
		{
			for (int j = 0; j < graph.length; j++) {
				if(graph[i][j] == 0)
				{
					graph[i][j] = graph[j][i];
				}
			}
		}
		
//		route[0] = A;
//		route[1] = B;
//		route[2] = C;
//		route[3] = D;
		
		return graph;
	}
}
