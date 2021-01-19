package datamining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class DataSet {
    String filePath;
    ArrayList<Instance> instances = new ArrayList<Instance>();
    HashMap<String, Integer> variablesNames = new HashMap<String, Integer>();
    Integer variablesNumber;

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

    }

    public ArrayList<Double> getColumn(String name) {
        ArrayList<Double> column = new ArrayList<Double>();
        for (Instance instance: this.instances) {
            column.add(instance.variables.get(this.variablesNames.get(name)).getDouble());
        }
        return column;
    }

}
