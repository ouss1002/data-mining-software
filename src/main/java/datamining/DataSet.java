package datamining;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class DataSet {
    String filePath;
    ArrayList<Instance> instances = new ArrayList<Instance>();
    HashMap<String, Integer> variablesNames = new HashMap<String, Integer>();
    Integer variablesNumber;
    Integer instancesNumber;

    public DataSet(String filePath) throws IOException {
        this.filePath = filePath;

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] observation = line.split(",");
            ArrayList<Variable> variables = new ArrayList<Variable>();
            for (String var: observation) {
                variables.add(new Variable(var));
            }
            instances.add(new Instance(variables));
            //System.out.println(Arrays.toString(observation));
        }
        // because we have only one dataset for now, columns are static
        String[] names = {"class", "t3_resin", "total_thyroxin", "total_triio", "tsh", "max_diff_tsh"};
        for (int i = 0; i < names.length; i++) {
            variablesNames.put(names[i], i);
        }

        variablesNumber = variablesNames.size();
        instancesNumber = instances.size();

    }

    public ArrayList<String> getVariablesNames() {
        return new ArrayList<String>(variablesNames.keySet());
    }

    public ArrayList<Double> getColumn(String name) {
        ArrayList<Double> column = new ArrayList<Double>();
        for (Instance instance: this.instances) {
            column.add(instance.variables.get(this.variablesNames.get(name)).getDouble());
        }
        return column;
    }

    public Double getMean(String name) {
        ArrayList<Double> column = this.getColumn(name);
        Double mean = 0.0;
        for (Double value: column) {
            mean += value;
        }
        return mean / instancesNumber;
    }


    public Double getMedian(String name) {
        ArrayList<Double> column = this.getColumn(name);
        Collections.sort(column);

        if (this.instancesNumber % 2 == 0) {
            return (column.get(this.instancesNumber/2) + column.get(this.instancesNumber/2-1)) / 2;
        } else {
            return column.get(this.instancesNumber/2);
        }
    }

    public HashMap<Double, Integer> getMode(String name) {
        ArrayList<Double> column = this.getColumn(name);
        HashMap<Double, Integer> counting = new HashMap<Double, Integer>();
        HashMap<Double, Integer> countingMax = new HashMap<Double, Integer>();

        for (Double c: column) {
            if (!counting.containsKey(c)) {
                counting.put(c, 0);
            }
            counting.put(c, counting.get(c) + 1);
        }

        int maxAppearance = 1;
        for (Double key: counting.keySet()) {
            if (counting.get(key) > maxAppearance) {
                maxAppearance = counting.get(key);
                countingMax.clear();
                countingMax.put(key, counting.get(key));
            } else if (counting.get(key) == maxAppearance) {
                countingMax.put(key, counting.get(key));
            }
        }
        return countingMax;
    }



}
