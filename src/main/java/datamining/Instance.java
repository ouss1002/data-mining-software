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

    public ArrayList<Double> getImportantValues() {
        ArrayList<Double> ret = new ArrayList<>();

        ret.add(this.variables.get(2).getDouble());
        ret.add(this.variables.get(3).getDouble());
        ret.add(this.variables.get(4).getDouble());
        ret.add(this.variables.get(5).getDouble());
        ret.add(this.variables.get(6).getDouble());

        return ret;
    }

    public Integer getInstanceNumber() {
        return instanceNumber;
    }

    @Override
    public String toString() {
        return "Instance{" + "\n" +
                "\tvariables=" + variables + "\n" +
                "\tinstanceNumber=" + instanceNumber + "\n" +
                "}\n";
    }
}
