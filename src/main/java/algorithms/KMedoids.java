package algorithms;

import datamining.DataSet;
import datamining.Instance;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class KMedoids {
    public static double fmeasure = 0;
    public static double[][] fmeasureMatrix = null;
    public static int mat_lines = 0;
    public static int mat_cols = 3;
    public static double estimation = 0;
    public static long time = 0;
    public static int numClusters = 0;
    public static HashMap<Integer, ArrayList<Integer>> clusters = null;

    public static HashMap<Integer, ArrayList<Integer>> getKMedoids(DataSet dataset, int numMedoids) {
        long start;
        long finish;

        KMedoids.numClusters = numMedoids;
        KMedoids.mat_lines = numMedoids;
        KMedoids.mat_cols = 3;

        start = System.nanoTime();

        HashMap<Integer, ArrayList<Integer>> medoids;
        medoids = KMedoids.initMedoids(dataset, numMedoids);
        medoids = KMedoids.buildClusters(dataset, medoids);

        int count = 0;

        while(true) {
            count++;
            ArrayList<Integer> newMedoids = new ArrayList<>();
            for(int key : medoids.keySet()) {
                int replacement = key;
                Medoid orgMD = new Medoid(dataset, key);
                double score = orgMD.getErrorWithAllInstances(dataset, medoids.get(key));
                for(Instance inst : dataset.getInstances()) {
                    if(medoids.containsKey(inst.getInstanceNumber()) || newMedoids.contains(inst.getInstanceNumber())) {
                        continue;
                    }
                    Medoid tempMD = new Medoid(dataset, inst.getInstanceNumber());
                    double tempScore = tempMD.getErrorWithAllInstances(dataset, medoids.get(key));
                    if(tempScore < score) {
                        replacement = tempMD.getInstance().getInstanceNumber();
                        score = tempScore;
                    }
                }
                newMedoids.add(replacement);
            }

            if(medoids.keySet().containsAll(newMedoids)) {
                break;
            }
            medoids.clear();
            System.out.println(newMedoids);
            for(int k : newMedoids) {
                medoids.put(k, new ArrayList<>());
            }
            medoids = KMedoids.buildClusters(dataset, medoids);
        }

        double theScore = KMedoids.getScore(dataset, medoids);

        finish = System.nanoTime();
        KMedoids.time = (finish - start) / 1000000;
        KMedoids.estimation = theScore;
        KMedoids.clusters = medoids;
        System.out.println("iterations: " + count);
        return medoids;
    }

    public static double getScore(DataSet ds, HashMap<Integer, ArrayList<Integer>> medoids) {
        double score = 0;
        for(int key : medoids.keySet()) {
            ArrayList<Integer> values = medoids.get(key);
            Medoid md = new Medoid(ds, key);
            score += md.getErrorWithAllInstances(ds, values);
        }
        return score;
    }

    public static int getRandomMedoid(int numMedoids) {
        int ret;
        do {
            ret = (int)(Math.random() * (numMedoids + 1));
        } while(ret < 0 || ret >= numMedoids);
        return ret;
    }

    public static HashMap<Integer, ArrayList<Integer>> initMedoids(DataSet ds, int numMedoids) {
        HashMap<Integer, ArrayList<Integer>> medoids = new HashMap<>();
        double random;
        int chosen;
        ArrayList<Integer> meds = new ArrayList<>();
        for(int i = 0; i < numMedoids; i++) {
            while(true) {
                random = (Math.random() * (ds.getInstances().size()) - 1) + 1;
                chosen = (int) random;
                if(chosen >= ds.getInstances().size()) {
                    chosen = chosen - 1;
                }
                if(chosen <= 1) {
                    chosen = 1;
                }
                if(!meds.contains(chosen)) {
                    meds.add(chosen);
                    break;
                }
            }
            for(int j : meds) {
                medoids.put(j, new ArrayList<>());
            }
        }
        return medoids;
    }

    public static HashMap<Integer, ArrayList<Integer>> buildClusters(DataSet ds, HashMap<Integer, ArrayList<Integer>> medoids) {
        HashMap<Integer, ArrayList<Integer>> ret = new HashMap<>();
        for(int key : medoids.keySet()) {
            ret.put(key, new ArrayList<>());
        }
        for(Instance instance : ds.getInstances()) {
            double score = -1;
            int chosenOne = 0;
            for(int key : medoids.keySet()) {
                Medoid md = new Medoid(ds, key);
                double temp = md.getDistanceWith(instance);
                if(temp < score || score < 0) {
                    chosenOne = key;
                    score = temp;
                }
            }
            ret.get(chosenOne).add(instance.getInstanceNumber());
        }
        return ret;
    }

//    private static HashMap<Integer, ArrayList<Integer>> updateClusters(DataSet ds, HashMap<Integer, ArrayList<Integer>> medoids) {
//
//    }

    public static double[][] getMatrixFMeasure(DataSet ds, HashMap<Integer, ArrayList<Integer>> kmedoids) {
        int lines =  kmedoids.keySet().size();
        int cols = 3;
        double[][] ret = new double[lines][cols];
        ArrayList<Integer> keys = new ArrayList<>();

        for(int key : kmedoids.keySet()) {
            keys.add(key);
        }

        for(int l = 0; l < lines; l++) {
            int key = keys.get(l);
            for(int c = 0; c < cols; c++) {
                double precision = KMedoids.getPrecision(ds, kmedoids, key, c);
                double recall = KMedoids.getRecall(ds, kmedoids, key, c);
                double fmeasure = (2 * precision * recall) / (precision + recall);
                if(Double.isNaN(fmeasure)) {
                    fmeasure = 0;
                }
                ret[l][c] = fmeasure;
            }
        }
        return ret;
    }
    
    public static double getTotalFMeasure(DataSet ds, HashMap<Integer, ArrayList<Integer>> kmedoids) {
        int lines =  kmedoids.keySet().size();
        int cols = 3;
        double[][] mat = new double[lines][cols];
        ArrayList<Integer> keys = new ArrayList<>();
        double[] maxes = new double[cols];
        double ret;

        for(int key : kmedoids.keySet()) {
            keys.add(key);
        }

        for(int l = 0; l < lines; l++) {
            int key = keys.get(l);
            for(int c = 0; c < cols; c++) {
                double precision = KMedoids.getPrecision(ds, kmedoids, key, c);
                double recall = KMedoids.getRecall(ds, kmedoids, key, c);
                double fmeasure = (2 * precision * recall) / (precision + recall);
                if(Double.isNaN(fmeasure)) {
                    fmeasure = 0;
                }
                mat[l][c] = fmeasure;
            }
        }
        ret = 0;

        for(int j = 0; j < cols; j++) {
            double max = 0;
            for(int i = 0; i < lines; i++) {
                if(mat[i][j] > max) {
                    max = mat[i][j];
                }
            }
            maxes[j] = max;
        }

        for(int j = 0; j < cols; j++) {
            ret += (maxes[j] * KMeans.getArrayFromClass(ds, j + 1).size()) / ds.getInstances().size();
        }

        KMedoids.fmeasureMatrix = mat;
        KMedoids.fmeasure = ret;
        return ret;
    }

    public static double getPrecision(DataSet ds, HashMap<Integer, ArrayList<Integer>> kmedoids, int key, int colon) {
        ArrayList<Integer> elements = KMedoids.getArrayFromClass(ds, colon + 1);
        ArrayList<Integer> clusteredElements = kmedoids.get(key);

        int nij = 0;
        int nj = clusteredElements.size();
        for(int ele : clusteredElements) {
            if(elements.contains(ele)) {
                nij++;
            }
        }
        double precision = ((double)nij) / nj;
        return precision;
    }

    public static double getRecall(DataSet ds, HashMap<Integer, ArrayList<Integer>> kmedoids, int key, int colon) {
        ArrayList<Integer> elements = KMedoids.getArrayFromClass(ds, colon + 1);
        ArrayList<Integer> clusteredElements = kmedoids.get(key);

        int nij = 0;
        int ni = elements.size();
        for(int ele : clusteredElements) {
            if(elements.contains(ele)) {
                nij++;
            }
        }
        double recall = ((double)nij) / ni;
        return recall;
    }

    public static ArrayList<Integer> getArrayFromClass(DataSet ds, int classe) {
        ArrayList<Integer> ret = new ArrayList<>();

        for(Instance instance : ds.getInstances()) {
            if(instance.getVariables().get(1).getDouble() == classe) {
                ret.add(instance.getInstanceNumber());
            }
        }

        return ret;
    }

    public static void main(String[] args) throws IOException {
        DataSet ds = new DataSet("C:\\Users\\MSI\\Desktop\\Thyroid_Dataset.txt");
        ds = ds.normalize();
        for(int bla = 0; bla < 10; bla++) {
            HashMap<Integer, ArrayList<Integer>> kmedoids = KMedoids.getKMedoids(ds, 3);
//            for(int i : kmedoids.keySet()) {
//                System.out.println(i + ": " + kmedoids.get(i).size());
//            }

//            double[][] fm = KMedoids.getMatrixFMeasure(ds, kmedoids);
//            for(int i = 0; i < 3; i++) {
//                for(int j = 0; j < 3; j++){
//                    System.out.print("" + fm[i][j] + ", ");
//                }
//                System.out.print("\n");
//            }
            System.out.println("finally: " + KMedoids.getTotalFMeasure(ds, kmedoids));
        }
    }
}
