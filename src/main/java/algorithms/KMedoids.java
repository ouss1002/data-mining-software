package algorithms;

import datamining.DataSet;
import datamining.Instance;

import javax.xml.crypto.Data;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class KMedoids {

    public static HashMap<Integer, ArrayList<Integer>> getKMedoids(DataSet dataset, int numMedoids) {
        HashMap<Integer, ArrayList<Integer>> medoids;
        medoids = KMedoids.initMedoids(dataset, numMedoids);
        medoids = KMedoids.buildClusters(dataset, medoids);
        Set<Integer> oldKeySet = medoids.keySet();

        int count = 0;

        while(true) {
            count++;
            boolean redo = false;
            for(int key : medoids.keySet()) {
                redo = false;
                Medoid orgMD = new Medoid(dataset, key);
                double orgScore = orgMD.getErrorWithAllInstances(dataset, medoids.get(key));
                for(Instance instance : dataset.getInstances()) {
                    double otherScore = 0;
                    if(medoids.containsKey(instance.getInstanceNumber())) {
                        continue;
                    }
                    Medoid otherMD = new Medoid(dataset, instance.getInstanceNumber());
                    otherScore = otherMD.getErrorWithAllInstances(dataset, medoids.get(key));
                    if(otherScore < orgScore) {
                        medoids.remove(key);
                        medoids.put(otherMD.getInstance().getInstanceNumber(), new ArrayList<>());
                        redo = true;
                        break;
                    }
                }
                if(redo) {
                    medoids = KMedoids.buildClusters(dataset, medoids);
                    break;
                }
            }
            if(!redo) {
                System.out.println(count);
                break;
            }
        }

        // calculating scores
//        ArrayList<Double> scores = new ArrayList<>();
//        for(int key : medoids.keySet()) {
//            Medoid md = new Medoid(dataset, key);
//            scores.add(md.getErrorWithAllInstances(dataset, medoids.get(key)));
//        }
        double theScore = getScore(dataset, medoids);

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
        HashMap<Integer, Integer> last = new HashMap<>();
        HashMap<Integer, Double> last2 = new HashMap<>();
        double[] maxes = new double[lines];
        int[] classes = new int[lines];
        double ret;

        for(int key : kmedoids.keySet()) {
            keys.add(key);
        }

        for(int l = 0; l < lines; l++) {
            int key = keys.get(l);
            double max = 0;
            int classSet = 0;
            for(int c = 0; c < cols; c++) {
                double precision = KMedoids.getPrecision(ds, kmedoids, key, c);
                double recall = KMedoids.getRecall(ds, kmedoids, key, c);
                double fmeasure = (2 * precision * recall) / (precision + recall);
                if(Double.isNaN(fmeasure)) {
                    fmeasure = 0;
                }
                mat[l][c] = fmeasure;
                if(fmeasure > max) {
                    max = fmeasure;
                    classSet = c + 1;
                }
            }
            maxes[l] = max;
            classes[l] = classSet;
            last.put(key, classSet);
            last2.put(key, max);
        }
        ret = 0;
        for(int key : last.keySet()) {
            ret += (last2.get(key) * KMedoids.getArrayFromClass(ds, last.get(key)).size()) / ds.getInstances().size();
        }

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

            double[][] fm = KMedoids.getMatrixFMeasure(ds, kmedoids);
//            for(int i = 0; i < 3; i++) {
//                for(int j = 0; j < 3; j++){
//                    System.out.print("" + fm[i][j] + ", ");
//                }
//                System.out.print("\n");
//            }
            System.out.print("finally: " + KMedoids.getTotalFMeasure(ds, kmedoids));
        }
    }

}
