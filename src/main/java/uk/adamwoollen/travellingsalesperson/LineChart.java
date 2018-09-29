package uk.adamwoollen.travellingsalesperson;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.DefaultXYDataset;
import org.jfree.data.xy.XYDataset;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class LineChart
{
    private List<Long> times = new ArrayList<>();
    private List<Double> bestSolutions = new ArrayList<>();

    public void addData(long time, double bestSolution)
    {
        times.add(time);
        bestSolutions.add(bestSolution);
    }

    public void showChart()
    {
        XYDataset dataset = createDataset();
        JFreeChart lineChart = ChartFactory.createXYLineChart("Best Solution Over Time", "Time / ms", "Best Solution", dataset, PlotOrientation.VERTICAL, true, true, false);

        SwingUtilities.invokeLater(() -> {
            JFrame frame = new JFrame("Charts");

            frame.setSize(600, 400);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            ChartPanel chartPanel = new ChartPanel(lineChart);

            frame.getContentPane().add(chartPanel);
        });
    }

    private XYDataset createDataset()
    {
        DefaultXYDataset dataset = new DefaultXYDataset();
//        double[][] data = { {0.1, 0.2, 0.3}, {1, 2, 3} };

        double[][] data = new double[2][times.size()];
        for (int i = 0; i < times.size(); i++)
        {
            data[0][i] = times.get(i);
        }
        for (int i = 0; i < bestSolutions.size(); i++)
        {
            data[1][i] = bestSolutions.get(i);
        }

        dataset.addSeries("series1", data);

        return dataset;
    }
}
