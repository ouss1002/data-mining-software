package datamining;

import java.util.ArrayList;

public class Instance {
    ArrayList<Variable> variables;
    Integer instanceNumber;

    private String number, classs, t3Resin, totalThyroxin, totalTriio, tsh, maxDiffTsh;

    public Instance(ArrayList<Variable> variables, Integer instanceNumber) {
        this.variables = variables;
        this.instanceNumber = instanceNumber;
        this.number = this.variables.get(0).toString();
        this.classs = this.variables.get(1).toString();
        this.t3Resin = this.variables.get(2).toString();
        this.totalThyroxin = this.variables.get(3).toString();
        this.totalTriio = this.variables.get(4).toString();
        this.tsh = this.variables.get(5).toString();
        this.maxDiffTsh = this.variables.get(6).toString();
    }

    public ArrayList<Variable> getVariables() {
        return this.variables;
    }

    // FOR STATIC TABLE
    public String getNumber() {
        return number;
    }

    public String getClasss() {
        return classs;
    }

    public String gett3Resin() {
        return t3Resin;
    }

    public String getTotalTriio() {
        return totalTriio;
    }

    public String getTotalThyroxin() {
        return totalThyroxin;
    }

    public String getTsh() {
        return tsh;
    }

    public String getMaxDiffTsh() {
        return maxDiffTsh;
    }
}
