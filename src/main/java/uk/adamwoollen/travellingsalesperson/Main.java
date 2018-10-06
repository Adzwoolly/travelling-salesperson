package uk.adamwoollen.travellingsalesperson;

import uk.adamwoollen.travellingsalesperson.solutions.Random;

import java.util.StringJoiner;

public class Main {
	public static void main(String[] args)
	{
		System.out.println("Hello, world!");
		new Main();
	}

	public static double getRouteCost(double[][] graph, int[] route)
	{
		double cost = 0;
		for (int i = 0; i < route.length - 1; i++) {
			cost += graph[route[i]][route[i + 1]];
		}
		return cost;
	}
	
	private Main()
	{
		long startTime = System.nanoTime();

		GraphBuilder gb = new GraphBuilder();
		double[][] graph = gb.buildGraphFromCsv("ulysses16.csv");
		
		System.out.println("Calculating, please wait...");

		int[] bestRouteFound = Random.solveUsingRandom(graph, new LineChart(), 10);

		System.out.println("===================");
		System.out.println("CHEAPEST ROUTE");

		Main.printRoute(bestRouteFound);
		System.out.println(getRouteCost(graph, bestRouteFound));

		System.out.println("Total time to execute program: " + ((System.nanoTime() - startTime) / 1000000) + " milliseconds");
	}
	
	public static void printRoute(int[] route)
	{
		StringJoiner sj = new StringJoiner(" -> ");
		for (int node : route) {
			sj.add(String.valueOf(node));
		}
		System.out.println(sj.toString());
	}
}
