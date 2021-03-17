package algorithms;

import datamining.Instance;
import datamining.Variable;

import java.util.ArrayList;

public class Centroid {

    ArrayList<Double> values;

    public Centroid(ArrayList<Double> values) {
        this.values = values;
    }

    public ArrayList<Double> getValues() {
        return values;
    }

    public double getDistanceWith(Instance instance) {
        ArrayList<Double> importantVals = instance.getImportantValues();

        double sum = 0;
        for(int i = 0; i < importantVals.size(); i++) {
            double val = importantVals.get(i) - this.values.get(i);
            sum += val * val;
        }

        return sum;
    }
}
