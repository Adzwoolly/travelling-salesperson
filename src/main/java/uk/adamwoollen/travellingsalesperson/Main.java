package uk.adamwoollen.travellingsalesperson;

import java.util.ArrayList;
import java.util.StringJoiner;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
	public static void main(String[] args)
	{
		System.out.println("Hello, world!");
		new Main();
	}
	
	private double[][] graph;
	private int[] route;
	
	public Main()
	{
		long startTime = System.nanoTime();
		LineChart lineChart = new LineChart();
		GraphBuilder gb = new GraphBuilder();
		graph = gb.buildGraphFromCsv("ulysses16.csv");
//		graph = gb.buildExampleGraph();
		
		route = new int[graph.length];
		
		System.out.println("Calculating, please wait...");
		
		int[] cheapestRoute = null;
		double cheapestRouteCost = Double.MAX_VALUE;
		for(int i = 0; i < 10000000; i++)
		{
			route = getRandomRoute();
			double routeCost = getRouteCost();
			// Uncommenting the two lines below will dramatically increase
			// program run time for large data sets and number of iterations.
//			printRoute(route);
//			System.out.println(routeCost);
			
			if(routeCost < cheapestRouteCost)
			{
				cheapestRouteCost = routeCost;
				cheapestRoute = route;
				long timeElapsed = ((System.nanoTime() - startTime) / 1000000);
				System.out.println(timeElapsed + ", " + routeCost);
				lineChart.addData(timeElapsed, routeCost);
			}
		}
		
		System.out.println("===================");
		System.out.println("CHEAPEST ROUTE");
		printRoute(cheapestRoute);
		System.out.println(cheapestRouteCost);

		lineChart.showChart();
		
		System.out.println("Total time to execute program: " + ((System.nanoTime() - startTime) / 1000000) + " milliseconds");
	}
	
	public double getRouteCost()
	{
		double cost = 0;
		for (int i = 0; i < route.length - 1; i++) {
			cost += graph[route[i]][route[i + 1]];
		}
		return cost;
	}
	
	public int[] getRandomRoute()
	{
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		for(int i = 0; i < graph.length; i++)
		{
			nodes.add(i);
		}
		int[] route = new int[nodes.size() + 1];
		route[0] = nodes.remove(0);
		// First and last nodes both set to 0 for a complete loop
		for(int i = 1; i < route.length - 1; i++)
		{
			int randomNode = ThreadLocalRandom.current().nextInt(0, nodes.size());
			route[i] = nodes.remove(randomNode);
		}
		route[route.length - 1] = route[0];
		return route;
	}
	
	public void printRoute(int[] route)
	{
		StringJoiner sj = new StringJoiner(" -> ");
		for(int i = 0; i < route.length; i++)
		{
			sj.add(String.valueOf(route[i]));
		}
		System.out.println(sj.toString());
	}
}
