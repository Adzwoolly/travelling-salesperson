package uk.adamwoollen.travellingsalesperson.solutions;

import uk.adamwoollen.travellingsalesperson.Main;
import uk.adamwoollen.travellingsalesperson.SolutionLog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LocalSearch
{
    public static int[] solveUsingLocalSearch(double[][] graph, SolutionLog solutionLog, int secondsToRunFor)
    {
        long startTime = System.nanoTime();
        long endTime = startTime + (secondsToRunFor * 1000000000L);

        int[] cheapestRoute = null;
        double cheapestRouteCost = Double.MAX_VALUE;
        while(System.nanoTime() < endTime)
        {
            int[] cheapestNeighbourhoodRoute = Random.getRandomRoute(graph.length);
            int[] neighbouringRoute = cheapestNeighbourhoodRoute;
            // Keep searching the neighbourhood until the cheapest route in the neighbourhood is found
            while(!Arrays.equals(neighbouringRoute, cheapestNeighbourhoodRoute))
            {
                neighbouringRoute = getShortestRouteInTwoOptNetwork(graph, cheapestNeighbourhoodRoute);
            }

            double cheapestNeighbourhoodRouteCost = Main.getRouteCost(graph, cheapestNeighbourhoodRoute);

            if (cheapestNeighbourhoodRouteCost < cheapestRouteCost)
            {
                cheapestRoute = cheapestNeighbourhoodRoute;
                cheapestRouteCost = cheapestNeighbourhoodRouteCost;

                long timeElapsed = ((System.nanoTime() - startTime) / 1000000);
                solutionLog.logSolution("local search", timeElapsed, cheapestNeighbourhoodRouteCost);
            }
        }
        return cheapestRoute;
    }

    private static int[] getShortestRouteInTwoOptNetwork(double[][] graph, int[] route)
    {
        List<int[]> twoOptNetwork = getTwoOptNetwork(route);

        int[] cheapestRoute = null;
        double cheapestRouteCost = Double.MAX_VALUE;

        for (int[] neighbouringRoute : twoOptNetwork)
        {
            double routeCost = Main.getRouteCost(graph, neighbouringRoute);
            if(routeCost < cheapestRouteCost)
            {
                cheapestRouteCost = routeCost;
                cheapestRoute = route;
            }
        }
        return cheapestRoute;
    }

    private static List<int[]> getTwoOptNetwork(int[] route)
    {
        List<int[]> twoOptNetwork = new ArrayList<>();
        for (int i = 0; i < route.length; i++)
        {
            for (int j = 0; j < route.length; j++)
            {
                twoOptNetwork.add(Main.switchNodes(route, i, j));
            }
        }
        return twoOptNetwork;
    }

}
