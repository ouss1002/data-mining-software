package algorithms;

import datamining.DataSet;
import datamining.Instance;

import java.util.ArrayList;
import java.util.HashMap;

public class KMeans {

    public HashMap<Integer, ArrayList<Integer>> getKMeans(DataSet dataset, Integer numClusters) {
        ArrayList<Instance> instances = dataset.getInstances();

        HashMap<Integer, ArrayList<Integer>> ret = new HashMap<>();

        for(int i = 0; i < numClusters; i++) {
            ret.put(i, new ArrayList<>());
        }

        return null;

    }

}
