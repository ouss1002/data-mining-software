package algorithms;

import datamining.DataSet;
import datamining.Instance;
import datamining.Variable;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class KMeans {

    public static HashMap<Integer, ArrayList<Integer>> getKMeans(DataSet dataset, Integer numClusters) {

        HashMap<Integer, Centroid> newCentroids = new HashMap<>();
        HashMap<Integer, Centroid> oldCentroids = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> newClusters = new HashMap<>();
        HashMap<Integer, ArrayList<Integer>> oldClusters = new HashMap<>();

        for(int i = 0; i < numClusters; i++) {
            Centroid centroid = KMeans.getRandomCentroid(dataset);
            newCentroids.put(i, centroid);
            oldCentroids.put(i, centroid);

            newClusters.put(i, new ArrayList<>());
            oldClusters.put(i, new ArrayList<>());
        }
        int iters = 0;
        while(true) {
            double score = 0;

            ArrayList<Object> retFromFunction = KMeans.constructClusters(dataset, newCentroids);
            newClusters = (HashMap<Integer, ArrayList<Integer>>) retFromFunction.get(0);
            score = (double) retFromFunction.get(1);

            newCentroids = KMeans.updateCentroids(dataset, newCentroids, newClusters);

            iters++;

            if(KMeans.isSame(newClusters, oldClusters)) {
                break;
            }
            oldCentroids = (HashMap<Integer, Centroid>) newCentroids.clone();
            oldClusters = (HashMap<Integer, ArrayList<Integer>>) newClusters.clone();
        }
        System.out.println("iters: " + iters);
        return newClusters;
    }

    private static Centroid getRandomCentroid(DataSet dataset) {
        ArrayList<Double> vars = new ArrayList<>();
        double random;
        double min, max;
        String currVar;

        currVar = "t3_resin";
        min = dataset.getQ1(currVar);
        max = dataset.getQ3(currVar);
        random = min + Math.random() * (max - min);
        vars.add(random);

        currVar = "total_thyroxin";
        min = dataset.getQ1(currVar);
        max = dataset.getQ3(currVar);
        random = min + Math.random() * (max - min);
        vars.add(random);

        currVar = "total_triio";
        min = dataset.getQ1(currVar);
        max = dataset.getQ3(currVar);
        random = min + Math.random() * (max - min);
        vars.add(random);

        currVar = "tsh";
        min = dataset.getQ1(currVar);
        max = dataset.getQ3(currVar);
        random = min + Math.random() * (max - min);
        vars.add(random);

        currVar = "max_diff_tsh";
        min = dataset.getQ1(currVar);
        max = dataset.getQ3(currVar);
        random = min + Math.random() * (max - min);
        vars.add(random);

        Centroid ret = new Centroid(vars);

        return ret;
    }

    private static boolean isSame(HashMap<Integer, ArrayList<Integer>> a, HashMap<Integer, ArrayList<Integer>> b) {
        for(int i = 0; i < a.size(); i++) {
            ArrayList<Integer> as,bs;
            as = a.get(i);
            bs = b.get(i);

            if(as.size() != bs.size()) {
                return false;
            }

            HashSet s = new HashSet();
            s.addAll(as);
            s.addAll(bs);

            if(s.size() != as.size()) {
                return false;
            }
        }
        return true;
    }

    private static ArrayList<Object> constructClusters(DataSet ds, HashMap<Integer, Centroid> centroids) {
        HashMap<Integer, ArrayList<Integer>> retClusters = initializeClusters(centroids.size());
        double score = 0;

        ArrayList<Object> ret = new ArrayList<>();

        for(Instance instance : ds.getInstances()) {
            int favorable = 0;
            double dist = -1;
            double temp;
            for(int i = 0; i < centroids.size(); i++) {
                if(i == 0) {
                    dist = centroids.get(0).getDistanceWith(instance);
                }
                else {
                    temp = centroids.get(i).getDistanceWith(instance);
                    if(temp < dist) {
                        dist = temp;
                        favorable = i;
                    }
                }
            }
            score += dist;
            retClusters.get(favorable).add(instance.getInstanceNumber());
        }

        ret.add(retClusters);
        ret.add(score);
        return ret;
    }

    private static HashMap<Integer, Centroid> updateCentroids(DataSet ds, HashMap<Integer, Centroid> centroids, HashMap<Integer, ArrayList<Integer>> clusters) {

        for(int i = 0; i < centroids.size(); i++) {
            ArrayList<Integer> elements = clusters.get(i);
            Centroid c = KMeans.getMeanCentroidFromElements(ds, elements);
            centroids.put(i, c);
        }

        return centroids;
    }

    private static Centroid getMeanCentroidFromElements(DataSet ds, ArrayList<Integer> elements) {
        Centroid ret;
        ArrayList<Double> values = new ArrayList<>();
        double[] sum = new double[]{0, 0, 0, 0, 0};
        int count = 0;

        for(int i : elements) {
            Instance instance = ds.getSingleInstance(i);
            if(instance.getInstanceNumber() != i) {
                System.out.println("OK WHAT THE HELL INSTANCE NUMBER");
                System.exit(0);
            }
            ArrayList<Double> var = instance.getImportantValues();
            for(int j = 0; j < var.size(); j++) {
                sum[j] += var.get(j);
            }
            count++;
        }
        for(int j = 0; j < 5; j++) {
            if(count != 0) {
                sum[j] /= count;
            }
            values.add(sum[j]);
        }
        return new Centroid(values);
    }

    private static HashMap<Integer, ArrayList<Integer>> initializeClusters(int num) {
        HashMap<Integer, ArrayList<Integer>> clusters = new HashMap<>();

        for(int i = 0; i < num; i++) {
            clusters.put(i, new ArrayList<>());
        }

        return clusters;
    }

    public static void main(String[] args) throws IOException {
        DataSet ds = new DataSet("C:\\Users\\MSI\\Desktop\\Thyroid_Dataset.txt");
        HashMap<Integer, ArrayList<Integer>> kmeans = getKMeans(ds, 3);
        for(int i : kmeans.keySet()) {
            System.out.println(i + ": " + kmeans.get(i).size());
        }
    }
}
