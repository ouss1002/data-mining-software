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

    public static Centroid getRandomCentroid(DataSet dataset) {
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

    public static boolean isSame(HashMap<Integer, ArrayList<Integer>> a, HashMap<Integer, ArrayList<Integer>> b) {
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

    public static ArrayList<Object> constructClusters(DataSet ds, HashMap<Integer, Centroid> centroids) {
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

    public static HashMap<Integer, Centroid> updateCentroids(DataSet ds, HashMap<Integer, Centroid> centroids, HashMap<Integer, ArrayList<Integer>> clusters) {

        for(int i = 0; i < centroids.size(); i++) {
            ArrayList<Integer> elements = clusters.get(i);
            Centroid c = KMeans.getMeanCentroidFromElements(ds, elements);
            centroids.put(i, c);
        }

        return centroids;
    }

    public static Centroid getMeanCentroidFromElements(DataSet ds, ArrayList<Integer> elements) {
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

    public static HashMap<Integer, ArrayList<Integer>> initializeClusters(int num) {
        HashMap<Integer, ArrayList<Integer>> clusters = new HashMap<>();

        for(int i = 0; i < num; i++) {
            clusters.put(i, new ArrayList<>());
        }

        return clusters;
    }

    public static ArrayList<ArrayList<Double>> getMatrixFMeasure(DataSet dataset, HashMap<Integer, ArrayList<Integer>> clusters) {
        ArrayList<ArrayList<Double>> ret = new ArrayList<>();
        for(int i = 0; i < clusters.size(); i++) {
            ArrayList<Double> line = new ArrayList<>();
            for(int j = 1; j < 4; j++) {
                double precision = KMeans.getPrecision(dataset, clusters, i, j);
                double recall = KMeans.getRecall(dataset, clusters, i, j);
                double fmeasure = (2 * precision * recall) / (precision + recall);
                line.add(fmeasure);
                System.out.println(fmeasure);
            }
            ret.add(line);
        }
        return ret;
    }

    public static double getTotalFMeasure(DataSet dataset, HashMap<Integer, ArrayList<Integer>> clusters) {
        int numClass1 = 150;
        int numClass2 = 35;
        int numClass3 = 30;
        ArrayList<ArrayList<Double>> mat = KMeans.getMatrixFMeasure(dataset, clusters);
        ArrayList<Double> maxes = new ArrayList<>();

        for(int j = 0; j < mat.get(0).size(); j++) {
            double max = -1;
            for(ArrayList<Double> arr : mat) {
                double ele = arr.get(j);
                if(ele > max) {
                    max = ele;
                }
            }
            maxes.add(max);
        }
        double fm1 = maxes.get(0);
        double fm2 = maxes.get(1);
        double fm3 = maxes.get(2);

        fm1 *= numClass1;
        fm2 *= numClass2;
        fm3 *= numClass3;

        fm1 /= dataset.getInstances().size();
        fm2 /= dataset.getInstances().size();
        fm3 /= dataset.getInstances().size();

        double ret = fm1 + fm2 + fm3;
        return ret;
    }

    public static double getPrecision(DataSet ds, HashMap<Integer, ArrayList<Integer>> clusters, int cluster, int classe) {
        ArrayList<Integer> elements = KMeans.getArrayFromClass(ds, classe);
        ArrayList<Integer> clusteredElements = clusters.get(cluster);

        int nj = clusteredElements.size();
        int nij = 0;

        for(int i : clusteredElements) {
            if(!elements.contains(i)) {
                nij++;
            }
        }
        return nij / nj;
    }

    public static double getRecall(DataSet ds, HashMap<Integer, ArrayList<Integer>> clusters, int cluster, int classe) {
        ArrayList<Integer> elements = KMeans.getArrayFromClass(ds, classe);
        ArrayList<Integer> clusteredElements = clusters.get(cluster);

        int ni = elements.size();
        int nij = 0;

        for(int i : clusteredElements) {
            if(!elements.contains(i)) {
                nij++;
            }
        }
        return nij / ni;
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
        HashMap<Integer, ArrayList<Integer>> kmeans = getKMeans(ds, 1);
        for(int i : kmeans.keySet()) {
            System.out.println(i + ": " + kmeans.get(i).size());
        }
        double fm = KMeans.getTotalFMeasure(ds, kmeans);
        System.out.println(fm);
    }
}
