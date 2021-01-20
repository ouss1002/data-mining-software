package charts;

import datamining.DataSet;
import javafx.embed.swing.SwingNode;
import javafx.scene.layout.AnchorPane;
import org.knowm.xchart.*;
import org.knowm.xchart.internal.chartpart.Chart;
import org.knowm.xchart.style.BoxStyler;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public final class Charts {

    private Charts() {

    }

    public static BoxChart BoxPlot(DataSet dataset, String name) {
        ArrayList<Double> column = dataset.getColumn(name);
        BoxChart chart = new BoxChartBuilder().title("Box Plot").build();
        chart.getStyler().setBoxplotCalCulationMethod(BoxStyler.BoxplotCalCulationMethod.N_LESS_1_PLUS_1);
        chart.getStyler().setToolTipsEnabled(true);
        chart.addSeries(name, column);
        return chart;
    }

    public static CategoryChart Histogram(DataSet dataset, String name) {
        ArrayList<Double> column = dataset.getColumn(name);
        HashMap<Double, Integer> counting = new HashMap<>();

        for (Double c: column) {
            if (!counting.containsKey(c)) {
                counting.put(c, 0);
            }
            counting.put(c, counting.get(c) + 1);
        }
        CategoryChart chart = new CategoryChartBuilder().title("Histogram").build();
        chart.getStyler().setToolTipsEnabled(false);
        chart.getStyler().setLegendVisible(false);
        chart.addSeries(name, new ArrayList<>(counting.keySet()), new ArrayList<>(counting.values()));
        return chart;
    }

    public static XYChart ScatterPlot(DataSet dataset, String name1, String name2) {
        ArrayList<Double> column1 = dataset.getColumn(name1);
        ArrayList<Double> column2 = dataset.getColumn(name2);
        XYChart chart = new XYChartBuilder().width(600).height(500).title("Scatter Plot").xAxisTitle("X").yAxisTitle("Y").build();

        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setMarkerSize(13);

        chart.addSeries(name1+ " " + name2, column1, column2);
        return chart;
    }

    public static void showChart(AnchorPane ap, Chart chart) {
        JPanel chartPanel = new XChartPanel<>(chart);

        SwingNode swingNode = new SwingNode();
        swingNode.setContent(chartPanel);

        AnchorPane.setLeftAnchor(swingNode, 0.0);
        AnchorPane.setRightAnchor(swingNode, 0.0);
        AnchorPane.setTopAnchor(swingNode, 0.0);
        AnchorPane.setBottomAnchor(swingNode, 0.0);

        ap.getChildren().add(swingNode);
    }
}
