package algorithms;

import datamining.DataSet;
import datamining.Instance;

import java.util.ArrayList;

public class Medoid {

    int instanceNumber;
    Instance instance;

    public Medoid(DataSet ds, int instanceNumber) {
        this.instanceNumber = instanceNumber;
        this.instance = ds.getSingleInstance(instanceNumber);

        if(this.instanceNumber != this.instance.getInstanceNumber()) {
            System.out.println("WOW WOW WOW");
        }
    }

    public Instance getInstance() {
        return this.instance;
    }

    public double getErrorWithAllInstances(DataSet ds, ArrayList<Integer> elements) {
        double error = 0;

        for(int i : elements) {
            error += this.getDistanceWith(ds.getSingleInstance(i));
        }
        return error;
    }

    public double getDistanceWith(Instance instance) {
        ArrayList<Double> importantVals = instance.getImportantValues();
        ArrayList<Double> thisImportantVals = this.instance.getImportantValues();

        double sum = 0;
        for(int i = 0; i < importantVals.size(); i++) {
            double val = importantVals.get(i) - thisImportantVals.get(i);
            sum += val * val; // >= 0 ? val : -val;
        }

        return sum;
    }
}
