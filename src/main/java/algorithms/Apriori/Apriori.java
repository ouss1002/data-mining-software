package algorithms.Apriori;

import datamining.Instance;
import datamining.Variable;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

public class Apriori {
    public static LinkedHashMap<Integer, ArrayList<String>> transactionDB = new LinkedHashMap<>();
    static ArrayList<Instance> classInstances = new ArrayList<>();

    public static void generateTransactionDB(ArrayList<Instance> instances) {
        classInstances = instances;
        for (Instance instance : classInstances) {
            ArrayList<String> itemSet = new ArrayList<>();
            ArrayList<Variable> variables = instance.getVariables();
            for (int i = 0; i < variables.size(); i++) {
                itemSet.add(i + "_" + variables.get(i).get());
            }
            transactionDB.put(instance.getInstanceNumber(), itemSet);
        }
    }

    public static void main(String[] args){
        //generateTransactionDB();
    }
}


