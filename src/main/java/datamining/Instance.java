package datamining;

import java.util.ArrayList;

public class Instance {
    ArrayList<Variable> variables;
    Integer instanceNumber;

    public Instance(ArrayList<Variable> variables, Integer instanceNumber) {
        this.variables = variables;
        this.instanceNumber = instanceNumber;
    }

    public ArrayList<Variable> getVariables() {
        return this.variables;
    }

    @Override
    public String toString() {
        return "Instance{" + "\n" +
                "\tvariables=" + variables + "\n" +
                "\tinstanceNumber=" + instanceNumber + "\n" +
                '}';
    }
}
