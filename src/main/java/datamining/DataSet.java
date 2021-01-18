package datamining;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class DataSet {
    String filePath;
    ArrayList<Instance> instances = new ArrayList<Instance>();
    ArrayList<String> variablesNames;
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
        variablesNames = new ArrayList<String>(Arrays.asList(names));

        variablesNumber = variablesNames.size();

    }

}
