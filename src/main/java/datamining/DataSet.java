package datamining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class DataSet {
    String filePath;
    ArrayList<Instance> instances = new ArrayList<>();
    HashMap<String, Integer> variablesNames = new HashMap<>();
    Integer variablesNumber;
    Integer instancesNumber;
    String[] staticNames = {"#", "class", "t3_resin", "total_thyroxin", "total_triio", "tsh", "max_diff_tsh"};

    public ArrayList<String> getStaticNames() {

        ArrayList<String> arr = new ArrayList<>();

        arr.addAll(Arrays.asList(staticNames));

        return arr;
    }

    public DataSet(String filePath) throws IOException {
        this.filePath = filePath;

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int j = 1;
        while ((line = reader.readLine()) != null) {
            String[] observation = line.split(",");
            ArrayList<Variable> variables = new ArrayList<>();
            variables.add(new Variable(String.valueOf(j)));
            for (String var: observation) {
                variables.add(new Variable(var));
            }
            instances.add(new Instance(variables, j));
            //System.out.println(Arrays.toString(observation));
            j++;
        }
        // because we have only one dataset for now, columns are static
        String[] names = {"#", "class", "t3_resin", "total_thyroxin", "total_triio", "tsh", "max_diff_tsh"};
        for (int i = 0; i < names.length; i++) {
            variablesNames.put(names[i], i);
        }

        variablesNumber = variablesNames.size();
        instancesNumber = instances.size();

    }

    public boolean isSymmetrical(String name) {
        Double mean = this.getMean(name);
        Double median = this.getMedian(name);
        HashMap<Double, Integer> mode = this.getModeDictionary(name);
        return mode.size() == 1 && mean.equals(median) && median.equals(mode.keySet().toArray()[0]);
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

    public HashMap<Double, Integer> getModeDictionary(String name) {
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

    public String getModeString(String name) {
        HashMap<Double, Integer> mode = this.getModeDictionary(name);
        String modeString = "";
        if (mode.keySet().size() > 0) {
            return mode.keySet().toString();
        } else {
            return "Null";
        }
    }

    public Double getMin(String name) {
        ArrayList<Double> column = this.getColumn(name);
        Double minValue = column.get(0);
        for (Double value: column) {
            if (value < minValue) {
                minValue = value;
            }
        }
        return minValue;
    }

    public Double getMax(String name) {
        ArrayList<Double> column = this.getColumn(name);
        Double maxValue = column.get(0);
        for (Double value: column) {
            if (value > maxValue) {
                maxValue = value;
            }
        }
        return maxValue;
    }

    public Double getQ1(String name) {
        ArrayList<Double> column = this.getColumn(name);
        Collections.sort(column);
        return column.get(this.instancesNumber / 4);
    }

    public Double getQ3(String name) {
        ArrayList<Double> column = this.getColumn(name);
        Collections.sort(column);
        return column.get((this.instancesNumber / 4)*3);
    }

    public ArrayList<Instance> getInstances() {
        return this.instances;
    }

    public ArrayList<Double> getOutliers(String name) {
        Double Q1 = this.getQ1(name);
        Double Q3 = this.getQ3(name);
        Double lowerBoundry = Q1 - (Q3 - Q1) * 1.5;
        Double upperBoundry = Q3 + (Q3 - Q1) * 1.5;

        ArrayList<Double> outliers = new ArrayList<>();
        ArrayList<Double> column = this.getColumn(name);

        for (Double value: column) {
            if ((value < lowerBoundry || value > upperBoundry) && !outliers.contains(value)) {
                outliers.add(value);
            }
        }
        return outliers;
    }
}
