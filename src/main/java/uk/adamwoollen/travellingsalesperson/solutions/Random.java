package uk.adamwoollen.travellingsalesperson.solutions;

import uk.adamwoollen.travellingsalesperson.Main;
import uk.adamwoollen.travellingsalesperson.SolutionLog;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class Random
{
    public static int[] getRandomRoute(int routeLength)
    {
        ArrayList<Integer> nodes = new ArrayList<>();
        for(int i = 0; i < routeLength; i++)
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

    public static int[] solveUsingRandom(double[][] graph, SolutionLog solutionLog)
    {
        long startTime = System.nanoTime();

        int[] cheapestRoute = null;
        double cheapestRouteCost = Double.MAX_VALUE;
        for(int i = 0; i < 100000; i++)
        {
            int[] route = getRandomRoute(graph.length);
            double routeCost = Main.getRouteCost(graph, route);

            if(routeCost < cheapestRouteCost)
            {
                cheapestRouteCost = routeCost;
                cheapestRoute = route;
                long timeElapsed = ((System.nanoTime() - startTime) / 1000000);
                solutionLog.logSolution("random", timeElapsed, routeCost);
            }
        }
        return cheapestRoute;
    }
}
