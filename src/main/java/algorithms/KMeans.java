package algorithms;

import datamining.DataSet;
import datamining.Instance;
import datamining.Variable;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

public class KMeans {
    public static double fmeasure = 0;
    public static double[][] fmeasureMatrix = null;
    public static int mat_lines = 0;
    public static int mat_cols = 3;
    public static double estimation = 0;
    public static long time = 0;
    public static int numClusters = 0;
    public static HashMap<Integer, ArrayList<Integer>> clusters = null;


    public static HashMap<Integer, ArrayList<Integer>> getKMeans(DataSet dataset, Integer numClusters) {
        long start;
        long finish;

        KMeans.numClusters = numClusters;
        KMeans.mat_lines = numClusters;
        KMeans.mat_cols = 3;

        start = System.nanoTime();

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
        double score = 0;
        while(true) {

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
//        System.out.println("iters: " + iters);
//        System.out.println("score: " + score);

        finish = System.nanoTime();
        KMeans.clusters = newClusters;
        KMeans.time = (finish - start) / 1000000;
        KMeans.estimation = score;

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

    public static double[][] getMatrixFMeasure(DataSet ds, HashMap<Integer, ArrayList<Integer>> kmeans) {
        int lines =  kmeans.keySet().size();
        int cols = 3;
        double[][] ret = new double[lines][cols];

        for(int l = 0; l < lines; l++) {
            for(int c = 0; c < cols; c++) {
                double precision = KMeans.getPrecision(ds, kmeans, l, c);
                double recall = KMeans.getRecall(ds, kmeans, l, c);
                double fmeasure = (2 * precision * recall) / (precision + recall);
                if(Double.isNaN(fmeasure)) {
                    fmeasure = 0;
                }
                ret[l][c] = fmeasure;
            }
        }
        return ret;
    }

    public static double getTotalFMeasure(DataSet ds, HashMap<Integer, ArrayList<Integer>> kmeans) {
        int lines =  kmeans.keySet().size();
        int cols = 3;
        double[][] mat = new double[lines][cols];
        int[] classes = new int[lines];
        double[] maxes = new double[cols];
        double ret;

        for(int l = 0; l < lines; l++) {
            for(int c = 0; c < cols; c++) {
                double precision = KMeans.getPrecision(ds, kmeans, l, c);
                double recall = KMeans.getRecall(ds, kmeans, l, c);
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

        System.out.println(Arrays.toString(maxes));
        KMeans.fmeasureMatrix = mat;
        KMeans.fmeasure = ret;

        return ret;
    }

    public static double getPrecision(DataSet ds, HashMap<Integer, ArrayList<Integer>> kmeans, int line, int colon) {
        ArrayList<Integer> elements = KMeans.getArrayFromClass(ds, colon + 1);
        ArrayList<Integer> clusteredElements = kmeans.get(line);

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

    public static double getRecall(DataSet ds, HashMap<Integer, ArrayList<Integer>> kmeans, int line, int colon) {
        ArrayList<Integer> elements = KMeans.getArrayFromClass(ds, colon + 1);
        ArrayList<Integer> clusteredElements = kmeans.get(line);

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
        HashMap<Integer, ArrayList<Integer>> kmeans;
        kmeans = KMeans.getKMeans(ds, 3);
        System.out.println("finally: " + KMeans.getTotalFMeasure(ds, kmeans));
        System.out.println(Arrays.deepToString(KMeans.fmeasureMatrix));
    }
}
