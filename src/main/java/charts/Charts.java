package charts;

import datamining.DataSet;
import org.knowm.xchart.BoxChart;
import org.knowm.xchart.BoxChartBuilder;
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

}
