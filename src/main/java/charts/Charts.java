package charts;

import datamining.DataSet;
import org.knowm.xchart.*;
import org.knowm.xchart.style.BoxStyler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public final class Charts {

    private Charts() {

    }

    public static BoxChart BoxPlot(DataSet dataset, String name) throws IOException {
        ArrayList<Double> column = dataset.getColumn(name);
        BoxChart chart = new BoxChartBuilder().title("Box Plot").build();
        chart.getStyler().setBoxplotCalCulationMethod(BoxStyler.BoxplotCalCulationMethod.N_LESS_1_PLUS_1);
        chart.getStyler().setToolTipsEnabled(true);
        chart.addSeries(name, column);
        BitmapEncoder.saveBitmapWithDPI(chart, "./BoxPlot_300_DPI", BitmapEncoder.BitmapFormat.PNG, 300);
        return chart;
    }

    public static CategoryChart Histogram(DataSet dataset, String name) throws IOException {
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
        BitmapEncoder.saveBitmapWithDPI(chart, "./Histogram_300_DPI", BitmapEncoder.BitmapFormat.PNG, 300);
        return chart;
    }

    public static XYChart ScatterPlot(DataSet dataset, String name1, String name2) throws IOException {
        ArrayList<Double> column1 = dataset.getColumn(name1);
        ArrayList<Double> column2 = dataset.getColumn(name2);
        XYChart chart = new XYChartBuilder().width(600).height(500).title("Scatter Plot").xAxisTitle("X").yAxisTitle("Y").build();

        chart.getStyler().setDefaultSeriesRenderStyle(XYSeries.XYSeriesRenderStyle.Scatter);
        chart.getStyler().setLegendVisible(false);
        chart.getStyler().setMarkerSize(13);

        chart.addSeries(name1+ " " + name2, column1, column2);
        BitmapEncoder.saveBitmapWithDPI(chart, "./ScatterPlot_300_DPI", BitmapEncoder.BitmapFormat.PNG, 300);
        return chart;
    }
}
