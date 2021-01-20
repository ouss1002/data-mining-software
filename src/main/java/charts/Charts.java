package charts;

import datamining.DataSet;
import org.knowm.xchart.BoxChart;
import org.knowm.xchart.BoxChartBuilder;
import org.knowm.xchart.CategoryChart;
import org.knowm.xchart.CategoryChartBuilder;
import org.knowm.xchart.style.BoxStyler;

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
        chart.addSeries(name, new ArrayList<>(counting.keySet()), new ArrayList<>(counting.values()));
        return chart;
    }
}
