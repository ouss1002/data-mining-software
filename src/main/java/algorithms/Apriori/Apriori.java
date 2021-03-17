package algorithms.Apriori;

import datamining.Instance;
import datamining.Variable;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedHashMap;

public class Apriori {
    public static LinkedHashMap<Integer, ArrayList<String>> transactionDB = new LinkedHashMap<>();
    static ArrayList<Instance> classInstances = new ArrayList<>();

    public static FrequentItemSets apriori(Integer minSupport, Double minConfidence) {
        FrequentItemSets l = generateFirstFrequentItemSet();
        while (l.getSize() > 0) {
            CandidateItemSets candidates = aprioriGen(l);
            for (Integer key: transactionDB.keySet()) {
                ArrayList<String> t = transactionDB.get(key);
                for (ItemSet candidate: candidates.getCandidateItemSets()) {
                    boolean exist = true;
                    for (String item: candidate.getItemSet()) {
                        if (!t.contains(item)) {
                            exist = false;
                        }
                    }
                    if (exist)
                        candidate.setSupportCount(candidate.getSupportCount() + 1);
                }
            }
            int i = 0;
            while (i < candidates.getCandidateItemSets().size()) {
                if (candidates.getCandidate(i).getSupportCount() < minSupport)
                    candidates.removeCandidate(i);
                else
                    i++;
            }
            l.setItemSets(candidates);
        }
        return l;
    }

    public static CandidateItemSets aprioriGen(FrequentItemSets l) {
        CandidateItemSets candidates = new CandidateItemSets();
        for (int i = 0; i < l.getSize() - 1; i++) {
            ItemSet firstItemSet = l.getItemSet(i);
            for (int j = i + 1; j < l.getSize(); j++) {
                ItemSet secondItemSet = l.getItemSet(i);
                for (int k = 0; k < secondItemSet.getSize(); k++) {
                    ItemSet currentItemSet = new ItemSet(firstItemSet);
                    currentItemSet.addItem(secondItemSet.getItem(k));

                    if(! hasInfrequentSubSet(currentItemSet, l))
                        candidates.addCandidate(new ItemSet(currentItemSet));
                }
            }
        }
        return candidates;
    }

    private static boolean hasInfrequentSubSet(ItemSet itemSet, FrequentItemSets l) {
        int size = itemSet.getSize();

        ItemSet subItemSet = new ItemSet(size - 1);

        return ! getAllSubsets(itemSet, size - 1, 0, subItemSet, l);
    }

    private static boolean getAllSubsets(ItemSet itemset, int length, int startPosition, ItemSet subItemSet, FrequentItemSets l){
        if (length == 0)
            return l.containsKey(subItemSet);

        boolean frequent = true;

        for (int i=startPosition; i <= itemset.getSize()-length; i++){
            subItemSet.replaceItem(itemset.getItem(i), subItemSet.getSize() - length);
            frequent = getAllSubsets(itemset, length-1, i+1, subItemSet, l);
            if(! frequent)
                return false;
        }
        return frequent;
    }
    public static FrequentItemSets generateFirstFrequentItemSet() {
        FrequentItemSets l1 = new FrequentItemSets();
        for (Integer key: transactionDB.keySet()) {
            for (String item: transactionDB.get(key)) {
                ItemSet keyItem = new ItemSet();
                keyItem.addItem(item);
                if (! l1.containsKey(keyItem)) {
                    l1.addItemSet(keyItem, 1);
                } else {
                    l1.addItemSet(keyItem, l1.getValue(keyItem) + 1);
                }
            }
        }
        return l1;
    }

    public static void generateTransactionDB(ArrayList<Instance> instances) {
        classInstances = instances;
        for (Instance instance : classInstances) {
            ArrayList<String> itemSet = new ArrayList<>();
            ArrayList<Variable> variables = instance.getVariables();
            for (int i = 0; i < variables.size(); i++) {
                itemSet.add(i + "_" + variables.get(i).get());
            }
            transactionDB.put(instance.getInstanceNumber(), itemSet);
        }
    }

    public static void main(String[] args){
        //generateTransactionDB();
    }
}


