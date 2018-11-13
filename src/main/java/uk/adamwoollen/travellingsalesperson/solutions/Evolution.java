package uk.adamwoollen.travellingsalesperson.solutions;

import uk.adamwoollen.travellingsalesperson.LineChart;
import uk.adamwoollen.travellingsalesperson.Main;
import uk.adamwoollen.travellingsalesperson.SolutionLog;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.DoubleStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static uk.adamwoollen.travellingsalesperson.solutions.Random.getRandomRoute;

public class Evolution
{
    public static int[] solveUsingEvolution(double[][] graph, SolutionLog solutionLog, int secondsToRunFor)
    {
        long startTime = System.nanoTime();
        long endTime = startTime + (secondsToRunFor * 1000000000L);

        int populationSize = 1000;

        int[][] population = Stream.generate(() -> getRandomRoute(graph.length)).limit(populationSize).toArray(int[][]::new);//size -> new int[size][]
        int[][] childPopulation = new int[population.length][];

//        testRoulette(graph, populationSize, solutionLog);

        while(System.nanoTime() < endTime)
        {


            // Select parents and produce children
            for (int i = 0; i < population.length; i++)
            {
                int[] parent1 = selectUsingRoulette(graph, population);
                int[] parent2 = selectUsingRoulette(graph, population);
                int[] child = recombine(parent1, parent2);

                childPopulation[i] = child;
            }

            // Mutate children
            for (int[] child : childPopulation)
            {
                if (ThreadLocalRandom.current().nextDouble() < 0.7)
                {
                    Main.switchNodes(child, ThreadLocalRandom.current().nextInt(child.length), ThreadLocalRandom.current().nextInt(child.length));
                }
            }


            Set<int[]> totalPopulation = Stream.concat(Arrays.stream(population), Arrays.stream(childPopulation))
                    .collect(Collectors.toSet());

            // Choose best individuals to keep in new population generation
            for (int i = 0; i < population.length; i++)
            {
                int[] individual = selectUsingRoulette(graph, totalPopulation.toArray(new int[totalPopulation.size()][]));
                population[i] = individual;
                totalPopulation.remove(individual);
            }

            if (solutionLog != null)
            {
                int[] bestIndividualInPopulation = getBestIndividualInPopulation(graph, population);
                long timeElapsed = ((System.nanoTime() - startTime) / 1000000);
                solutionLog.logSolution("Evolution", timeElapsed, Main.getRouteCost(graph, bestIndividualInPopulation));
            }

        }

        int[] fittestIndividual = null;
        double fittestIndividualScore = Double.MAX_VALUE;
        for (int[] individual : population)
        {
            double fitness = Main.getRouteCost(graph, individual);
            if (fitness < fittestIndividualScore)
            {
                fittestIndividual = individual;
                fittestIndividualScore = fitness;
            }
        }
        return fittestIndividual;
    }

    private static int[] selectUsingRoulette(double[][] graph, int[][] population)
    {
        double minFitness = Arrays.stream(population).mapToDouble(x -> Main.getRouteCost(graph, x)).min().getAsDouble();
        double fitnessSum = Arrays.stream(population).mapToDouble(x -> Main.getRouteCost(graph, x)).sum() - minFitness + 1;
        double correctedFitnessSum = Arrays.stream(population).mapToDouble(x -> 1 / (Main.getRouteCost(graph, x) / fitnessSum)).sum();
        double selection = ThreadLocalRandom.current().nextDouble();

        for (int[] individual : population)
        {
            double normalisedFitness = (Main.getRouteCost(graph, individual) - minFitness + 1) / fitnessSum;
            double correctedFitness = (1 / normalisedFitness) / correctedFitnessSum;
            selection -= correctedFitness;
            if (selection <= 0)
            {
                return individual;
            }
        }
        throw new RuntimeException("Failed to select using roulette.  Something went wrong.");
    }

    private static int[] recombine(int[] parent1, int[] parent2)
    {
        // Copy randomly selected set from parent
        int copyFromOffset = ThreadLocalRandom.current().nextInt(parent1.length);
        int[] child = new int[parent1.length];
        int currentGene = 0;
        Set<Integer> copiedGenes = new HashSet<>();
        while (currentGene < (parent1.length / 2) + 1)
        {
            int copyGene = parent1[(currentGene + copyFromOffset) % parent1.length];
            child[currentGene] = copyGene;
            copiedGenes.add(copyGene);
            currentGene++;
        }
        // Copy rest from other parent in order
        int alreadyCopiedOffset = 0;
        while (currentGene < child.length)
        {
            int potentialCopyGene = parent2[(currentGene + copyFromOffset + alreadyCopiedOffset) % parent1.length];
            if (!copiedGenes.contains(potentialCopyGene))
            {
                child[currentGene] = potentialCopyGene;
                currentGene++;
            }
            else
            {
                alreadyCopiedOffset++;
            }
        }
        return child;
    }

    private static int[] getBestIndividualInPopulation(double[][] graph, int[][] population)
    {
        int[] bestIndividual = null;
        double bestIndividualScore = Double.MAX_VALUE;

        for (int[] individual : population)
        {
            double individualScore = Main.getRouteCost(graph, individual);
            if (individualScore < bestIndividualScore)
            {
                bestIndividual= individual;
                bestIndividualScore = individualScore;
            }
        }
        return bestIndividual;
    }





    private static void printPopulation(double[][] graph, int[][] population)
    {
        for (int[] individual : population)
        {
            System.out.println(Arrays.toString(individual) + " -> " + Main.getRouteCost(graph, individual));
        }
    }

    private static void testRoulette(double[][] graph, int populationSize, SolutionLog solutionLog)
    {
        System.out.println("=======================");
        Map<int[], Integer> timesChosen = new HashMap<>();
        final int numberOfRuns = 10000;

        for (int plotTimes = 0; plotTimes < 10; plotTimes++)
        {
            int[][] population = Stream.generate(() -> getRandomRoute(graph.length)).limit(populationSize).toArray(int[][]::new);
            for (int i = 0; i < numberOfRuns; i++) {
                int[] rouletteBoi = selectUsingRoulette(graph, population);
                //System.out.println(Arrays.toString(rouletteBoi) + " -> " + Main.getRouteCost(graph, rouletteBoi));

                timesChosen.put(rouletteBoi, timesChosen.getOrDefault(rouletteBoi, 0) + 1);
            }

            // Printing stuff
            List<int[]> sortedPopulation = new ArrayList<>(timesChosen.keySet());
            sortedPopulation.sort(Comparator.comparingDouble(individual -> Main.getRouteCost(graph, individual)));

            final int finalPlotTimes = plotTimes;
            sortedPopulation.forEach(individual -> {
                System.out.println(Arrays.toString(individual) + " : " + Main.getRouteCost(graph, individual) + " : " + (timesChosen.get(individual) / (double) numberOfRuns) * 100.0);
                solutionLog.logSolution("Testing Roulette " + finalPlotTimes, (long) Main.getRouteCost(graph, individual), (timesChosen.get(individual) / (double) numberOfRuns) * 100.0);
            });
            System.out.println("----------------------------");
        }
    }

    private static double getAveragePopulationFitness(double[][] graph, int[][] population)
    {
        return Arrays.stream(population).mapToDouble(x-> Main.getRouteCost(graph, x)).average().getAsDouble();
    }
}
