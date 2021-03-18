package algorithms;

import datamining.DataSet;
import datamining.Instance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class Clarans {

    public static HashMap<Integer, ArrayList<Integer>> getClarans(DataSet dataset, int numMedoids, int numIterations, int numRepetitions) {
        HashMap<Integer, ArrayList<Integer>> medoids;
        ArrayList<HashMap<Integer, ArrayList<Integer>>> history = new ArrayList<>();

        for(int i = 0; i < numRepetitions; i++) {
            medoids = Clarans.initMedoids(dataset, numMedoids);
            System.out.println(medoids.keySet());
            medoids = Clarans.buildClusters(dataset, medoids);
            for(int j = 0; j < numIterations; j++) {
                int rand = Clarans.getRandomMedoid(numMedoids);
                int chosenKey = (int)medoids.keySet().toArray()[rand];
                ArrayList<Integer> forbidden = new ArrayList<>();
                for(int key : medoids.keySet()) {
                    forbidden.add(key);
                }
                Medoid md = new Medoid(dataset, chosenKey);
                double score = 0;
                int perm = chosenKey;
                ArrayList<Integer> targetInstances = medoids.get(chosenKey);
                for(int instanceNumber : targetInstances) {
                    score += md.getDistanceWith(dataset.getSingleInstance(instanceNumber));
                }

                double otherScore = 0;
                for(Instance instance : dataset.getInstances()) {
                    if(forbidden.contains(instance.getInstanceNumber())) {
                        continue;
                    }
                    Medoid tempMD = new Medoid(dataset, instance.getInstanceNumber());
                    for(int instanceNumber : targetInstances) {
                        otherScore += tempMD.getDistanceWith(dataset.getSingleInstance(instanceNumber));
                    }
                    if(otherScore < score) {
                        score = otherScore;
                        perm = instance.getInstanceNumber();
                    }
                }
                if(perm != chosenKey) {
                    medoids.put(perm, new ArrayList<>());
                    medoids.remove(chosenKey);
                    medoids = Clarans.buildClusters(dataset, medoids);
                }
            }
            history.add(medoids);
        }

        // calculating scores
        double theScore = -1;
        HashMap<Integer, ArrayList<Integer>> result = null;
        for(HashMap<Integer, ArrayList<Integer>> mds : history) {
            double tempScore = Clarans.getScore(dataset, mds);
            if(tempScore < theScore || theScore < 0) {
                theScore = tempScore;
                result = mds;
            }
        }

        return result;
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
        HashMap<Integer, ArrayList<Integer>> kmeans = Clarans.getClarans(ds, 3, 50, 50);
        for(int i : kmeans.keySet()) {
            System.out.println(i + ": " + kmeans.get(i).size());
        }
    }

}
