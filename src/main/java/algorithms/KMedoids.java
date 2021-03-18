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
        System.out.println(theScore);

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

    public static void main(String[] args) throws IOException {
        DataSet ds = new DataSet("C:\\Users\\MSI\\Desktop\\Thyroid_Dataset.txt");
        ds = ds.normalize();
        HashMap<Integer, ArrayList<Integer>> kmeans = KMedoids.getKMedoids(ds, 5);
        for(int i : kmeans.keySet()) {
            System.out.println(i + ": " + kmeans.get(i).size());
        }
    }

}
