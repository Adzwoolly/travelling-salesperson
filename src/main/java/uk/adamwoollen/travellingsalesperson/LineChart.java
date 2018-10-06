package uk.adamwoollen.travellingsalesperson;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import javax.swing.*;
import java.util.*;
import java.util.Timer;

public class LineChart implements SolutionLog
{
    private Map<String, GraphSeriesData> graphData = new HashMap<>();
    private JFrame frame;

    public LineChart()
    {
        frame = new JFrame();
        frame.setSize(600, 400);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Timer chartUpdateTimer = new Timer();
        chartUpdateTimer.schedule(new UpdateChart(), 0, 1000);
    }

    public void logSolution(String solutionName, long time, double bestSolutionDistance)
    {
        if (!graphData.containsKey(solutionName))
        {
            graphData.put(solutionName, new GraphSeriesData(solutionName));
        }
        GraphSeriesData graphSeriesData = graphData.get(solutionName);
        graphSeriesData.times.add(time);
        graphSeriesData.bestSolutions.add(bestSolutionDistance);
    }

    private class UpdateChart extends TimerTask
    {
        @Override
        public void run()
        {
            XYSeriesCollection dataset = new XYSeriesCollection();
            for (GraphSeriesData graphSeriesData : graphData.values())
            {
                dataset.addSeries(graphSeriesData.getDataset());
            }

            JFreeChart lineChart = ChartFactory.createXYLineChart("Best Solution Over Time", "Time / ms", "Best Solution", dataset, PlotOrientation.VERTICAL, true, true, false);

            ChartPanel chartPanel = new ChartPanel(lineChart);
            frame.add(chartPanel);
            frame.revalidate();
        }
    }

    private class GraphSeriesData
    {
        public String solutionName;
        public List<Long> times = new ArrayList<>();
        public List<Double> bestSolutions = new ArrayList<>();

        public GraphSeriesData(String solutionName)
        {
            this.solutionName = solutionName;
        }

        public XYSeries getDataset()
        {
            XYSeries series = new XYSeries(solutionName);

            for (int i = 0; i < times.size(); i++)
            {
                series.add(times.get(i), bestSolutions.get(i));
            }

            return series;
        }
    }
}
