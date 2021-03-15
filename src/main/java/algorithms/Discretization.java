package algorithms;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Discretization {

    public static ArrayList<Integer> discretize(ArrayList<Float> list, int bins) {
        int length = list.size() / bins;

        ArrayList<Integer> ret = new ArrayList<>();
        ArrayList<Float> copy = (ArrayList<Float>) list.clone();
        Collections.sort(copy);

        float limdown = copy.get(0);
        float limup = copy.get(copy.size() - 1);
        float decal = (limup - limdown) / bins;

        ArrayList<Float> boundaries = new ArrayList<>();
        for(int i = 0; i < bins; i++) {
            boundaries.add(limdown + decal * i);
        }

        for(float ele : list) {
            for(int i = boundaries.size() - 1; i >= 0; i--) {
                if(ele >= boundaries.get(i)) {
                    ret.add(i);
                    break;
                }
            }
        }

        return ret;
    }

    public static void main(String[] args) {
        ArrayList<Float> ar = new ArrayList<>();
        ar.add((float) 0);
        ar.add((float) 1);
        ar.add((float) 2);
        ar.add((float) 3);
        ar.add((float) 4);
        ar.add((float) 5);
        ar.add((float) 6);
        ar.add((float) 7);
        ar.add((float) 8);
        ar.add((float) 9);
        ar.add((float) 10);
        System.out.println(Discretization.discretize(ar, 5));
    }

}
