package datamining;

import javafx.beans.property.StringProperty;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
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

    public DataSet(DataSet ds) {
        this.filePath = ds.filePath;
        this.instances = new ArrayList<>();
        this.variablesNames = ds.variablesNames;
        this.variablesNumber = ds.variablesNumber;
        this.instancesNumber = ds.instancesNumber;
        this.staticNames = ds.staticNames;
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
        HashMap<Double, Integer> counting = new HashMap<>();
        HashMap<Double, Integer> countingMax = new HashMap<>();

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

    public Instance getSingleInstance(int i) {
        return this.instances.get(i - 1);
    }

    public DataSet normalize() {
        DataSet ret = new DataSet(this);
        ArrayList<Double> col01 = (ArrayList<Double>) getColumn("#").clone();
        ArrayList<Double> col02 = (ArrayList<Double>) getColumn("class").clone();
        ArrayList<Double> col1 = (ArrayList<Double>) getColumn("t3_resin").clone();
        ArrayList<Double> col2 = (ArrayList<Double>) getColumn("total_thyroxin").clone();
        ArrayList<Double> col3 = (ArrayList<Double>) getColumn("total_triio").clone();
        ArrayList<Double> col4 = (ArrayList<Double>) getColumn("tsh").clone();
        ArrayList<Double> col5 = (ArrayList<Double>) getColumn("max_diff_tsh").clone();

        double numberOfElements = instances.size();

        double sumCol1 = 0;
        double sumCol2 = 0;
        double sumCol3 = 0;
        double sumCol4 = 0;
        double sumCol5 = 0;

        double meanCol1;
        double meanCol2;
        double meanCol3;
        double meanCol4;
        double meanCol5;

        double stdv1 = 0;
        double stdv2 = 0;
        double stdv3 = 0;
        double stdv4 = 0;
        double stdv5 = 0;

        for(double ele : col1) {
            sumCol1 += ele;
        }
        for(double ele : col2) {
            sumCol2 += ele;
        }
        for(double ele : col3) {
            sumCol3 += ele;
        }
        for(double ele : col4) {
            sumCol4 += ele;
        }
        for(double ele : col5) {
            sumCol5 += ele;
        }

        meanCol1 = sumCol1 / numberOfElements;
        meanCol2 = sumCol2 / numberOfElements;
        meanCol3 = sumCol3 / numberOfElements;
        meanCol4 = sumCol4 / numberOfElements;
        meanCol5 = sumCol5 / numberOfElements;

        for(double ele : col1) {
            stdv1 += (ele - meanCol1) * (ele - meanCol1);
        }
        stdv1 /= numberOfElements;
        stdv1 = Math.sqrt(stdv1);


        for(double ele : col2) {
            stdv2 += (ele - meanCol2) * (ele - meanCol2);
        }
        stdv2 /= numberOfElements;
        stdv2 = Math.sqrt(stdv2);


        for(double ele : col3) {
            stdv3 += (ele - meanCol3) * (ele - meanCol3);
        }
        stdv3 /= numberOfElements;
        stdv3 = Math.sqrt(stdv3);


        for(double ele : col4) {
            stdv4 += (ele - meanCol4) * (ele - meanCol4);
        }
        stdv4 /= numberOfElements;
        stdv4 = Math.sqrt(stdv4);

        for(double ele : col5) {
            stdv5 += (ele - meanCol5) * (ele - meanCol5);
        }
        stdv5 /= numberOfElements;
        stdv5 = Math.sqrt(stdv5);

        for(int i = 0; i < numberOfElements; i++) {
            Variable var01 = new Variable(String.valueOf(col01.get(i)));
            Variable var02 = new Variable(String.valueOf(col02.get(i)));
            Variable var1 = new Variable(String.valueOf((col1.get(i) - meanCol1) / stdv1));
            Variable var2 = new Variable(String.valueOf((col2.get(i) - meanCol2) / stdv2));
            Variable var3 = new Variable(String.valueOf((col3.get(i) - meanCol3) / stdv3));
            Variable var4 = new Variable(String.valueOf((col4.get(i) - meanCol4) / stdv4));
            Variable var5 = new Variable(String.valueOf((col5.get(i) - meanCol5) / stdv5));

            ArrayList<Variable> vars = new ArrayList<>();
            vars.add(var01);
            vars.add(var02);
            vars.add(var1);
            vars.add(var2);
            vars.add(var3);
            vars.add(var4);
            vars.add(var5);

            ret.getInstances().add(new Instance(vars, i + 1));
        }

        return ret;
    }
}
