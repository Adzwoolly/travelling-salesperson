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
        int[] route = new int[nodes.size()];
        for(int i = 0; i < route.length; i++)
        {
            int randomNode = ThreadLocalRandom.current().nextInt(0, nodes.size());
            route[i] = nodes.remove(randomNode);
        }
        return route;
    }

    public static int[] solveUsingRandom(double[][] graph, SolutionLog solutionLog, int secondsToRunFor)
    {
        long startTime = System.nanoTime();
        long endTime = startTime + (secondsToRunFor * 1000000000L);

        int[] cheapestRoute = null;
        double cheapestRouteCost = Double.MAX_VALUE;
        while(System.nanoTime() < endTime)
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
