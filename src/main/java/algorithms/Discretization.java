package algorithms;

import datamining.DataSet;
import datamining.Instance;
import datamining.Variable;
import javafx.beans.property.StringProperty;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Discretization {
    public static ArrayList<Integer> discretize(ArrayList<Double> list, int bins) {
        ArrayList<Integer> ret = new ArrayList<>();
        ArrayList<Double> copy = (ArrayList<Double>) list.clone();
        Collections.sort(copy);
        Double limdown = copy.get(0);
        Double limup = copy.get(copy.size() - 1);
        Double decal = (limup - limdown) / bins;
        //.println("decal: " + decal);
        ArrayList<Double> boundaries = new ArrayList<>();
        for(int i = 0; i < bins; i++) {
            boundaries.add(limdown + decal * i);
        }
        for(Double ele : list) {
            for(int i = boundaries.size() - 1; i >= 0; i--) {
                if(ele >= boundaries.get(i)) {
                    ret.add(i);
                    break;
                }
            }
        }
        return ret;
    }

    public static ArrayList<Instance> discretizeDataset(DataSet ds, int bins) {
        ArrayList<Instance> ret = new ArrayList<>();

        ArrayList<Double> colDiese = ds.getColumn("#");
        ArrayList<Double> colClass = ds.getColumn("class");
        ArrayList<Double> colT3Resin = ds.getColumn("t3_resin");
        ArrayList<Double> colTotalThyroxin = ds.getColumn("total_thyroxin");
        ArrayList<Double> colTotalTriio = ds.getColumn("total_triio");
        ArrayList<Double> colTSH = ds.getColumn("tsh");
        ArrayList<Double> colMaxDiffTSH = ds.getColumn("max_diff_tsh");

        ArrayList<Integer> discColDiese = Discretization.castDoubleToInteger(colDiese);
        ArrayList<Integer> discColClass = Discretization.castDoubleToInteger(colClass);
        ArrayList<Integer> discColT3Resin = Discretization.discretize(colT3Resin, bins);
        ArrayList<Integer> discColTotalThyroxin = Discretization.discretize(colTotalThyroxin, bins);
        ArrayList<Integer> discColTotalTriio = Discretization.discretize(colTotalTriio, bins);
        ArrayList<Integer> discColTSH = Discretization.discretize(colTSH, bins);
        ArrayList<Integer> discColMaxDiffTSH = Discretization.discretize(colMaxDiffTSH, bins);

        for(int i = 0; i < discColDiese.size(); i++) {
            ArrayList<Variable> vars = new ArrayList<>();
            vars.add(new Variable(String.valueOf(discColClass.get(i))));
            vars.add(new Variable(String.valueOf(discColT3Resin.get(i))));
            vars.add(new Variable(String.valueOf(discColTotalThyroxin.get(i))));
            vars.add(new Variable(String.valueOf(discColTotalTriio.get(i))));
            vars.add(new Variable(String.valueOf(discColTSH.get(i))));
            vars.add(new Variable(String.valueOf(discColMaxDiffTSH.get(i))));
            Instance instance = new Instance(vars, discColDiese.get(i));
            ret.add(instance);
        }

        return ret;
    }

    public static ArrayList<Integer> castDoubleToInteger(ArrayList<Double> doubleArr) {
        ArrayList<Integer> ret = new ArrayList<>();

        for(double val : doubleArr) {
            ret.add((int) val);
        }

        return ret;
    }

    public static void main(String[] args) throws IOException {
//        ArrayList<Double> ar = new ArrayList<>();
//        ar.add((double) 0);
//        ar.add((double) 1);
//        ar.add((double) 2);
//        ar.add((double) 3);
//        ar.add((double) 4);
//        ar.add((double) 5);
//        ar.add((double) 6);
//        ar.add((double) 7);
//        ar.add((double) 8);
//        ar.add((double) 9);
//        ar.add((double) 10);
//        System.out.println(Discretization.discretize(ar, 5));

        DataSet ds = new DataSet("C:\\Users\\a\\Desktop\\Source Code\\GIT repositories\\Data science\\data-mining-software\\src\\main\\resources\\Thyroid_Dataset.txt");
        ArrayList<Instance> test = Discretization.discretizeDataset(ds, 3);
        System.out.println(test);
        System.out.println(ds.getMin("max_diff_tsh"));
        System.out.println(ds.getMax("max_diff_tsh"));
    }

}
