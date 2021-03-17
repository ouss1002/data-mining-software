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

        return ;
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


