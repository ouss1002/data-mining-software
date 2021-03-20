package algorithms.Apriori;

import algorithms.Discretization;
import datamining.DataSet;
import datamining.Instance;
import datamining.Variable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Apriori {
    public static LinkedHashMap<Integer, ArrayList<String>> transactionDB = new LinkedHashMap<>();
    public static ArrayList<Instance> classInstances = new ArrayList<>();
    public static ArrayList<Instance> discretizedInstances;
    public static ArrayList<FrequentItemSets> frequentItemSetsList;
    public static ArrayList<Rule> rules;

    public static long timeDiscretization;
    public static long timeFreqPat;
    public static long timeAssocRules;
    public static long timeFull;

    public static ArrayList<FrequentItemSets> apriori(Integer minSupport) {
        ArrayList<FrequentItemSets> frequentItemSetsList = new ArrayList<>();
        FrequentItemSets l = generateFirstFrequentItemSet(minSupport);
        frequentItemSetsList.add(new FrequentItemSets(l));
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
            frequentItemSetsList.add(new FrequentItemSets(l));
        }
        return frequentItemSetsList;
    }

    public static CandidateItemSets aprioriGen(FrequentItemSets l) {
        CandidateItemSets candidates = new CandidateItemSets();
        for (int i = 0; i < l.getSize() - 1; i++) {
            ItemSet firstItemSet = l.getItemSet(i);
            for (int j = i + 1; j < l.getSize(); j++) {
                ItemSet secondItemSet = l.getItemSet(j);
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
        int size = l.getItemSets().get(0).getSize() + 1;

        if (itemSet.getSize() < size)
            return true;
        ItemSet subItemSet = new ItemSet(size - 1);
        return ! getAllSubsets(itemSet, size - 1, 0, subItemSet, l);
    }

    private static boolean getAllSubsets(ItemSet itemset, int length, int startPosition, ItemSet subItemSet, FrequentItemSets l){
        if (length == 0) {
            return l.containsItemSet(subItemSet);
        }
        for (int i=startPosition; i <= itemset.getSize()-length; i++){
            subItemSet.replaceItem(itemset.getItem(i), subItemSet.getSize() - length);
            boolean frequent = getAllSubsets(itemset, length-1, i+1, subItemSet, l);
            if(! frequent)
                return false;
        }
        return true;
    }
    public static FrequentItemSets generateFirstFrequentItemSet(Integer minSupport) {
        FrequentItemSets l1 = new FrequentItemSets();
        for (Integer key: transactionDB.keySet()) {
            for (String item: transactionDB.get(key)) {
                ItemSet keyItem = new ItemSet();
                keyItem.addItem(item);
                keyItem.setSupportCount(1);
                Integer index = l1.containsItem(item);
                if (index == -1) {
                    l1.addItemSet(keyItem, 1);
                } else {
                    l1.updateSupportCount(index, l1.getItemSet(index).getSupportCount() + 1);
                }
            }
        }
        int i = 0;
        while (i < l1.getSize()) {
            if (l1.getItemSet(i).getSupportCount() < minSupport)
                l1.removeItemSet(l1.getItemSet(i));
            else
                i++;
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

    public static void main(String[] args)  throws IOException {
        DataSet ds = new DataSet("C:\\Users\\MSI\\Desktop\\Thyroid_Dataset.txt");
        long start;
        long finish;

        start = System.nanoTime();
        Apriori.discretizedInstances = Discretization.discretizeDataset(ds, 5);
        finish = System.nanoTime();
        Apriori.timeDiscretization = (finish - start) / 1000000;

        start = System.nanoTime();
        Apriori.generateTransactionDB(Apriori.discretizedInstances);
        Apriori.frequentItemSetsList = Apriori.apriori(129);
        finish = System.nanoTime();
        Apriori.timeFreqPat = (finish - start) / 1000000;

        start = System.nanoTime();
        AssociationRules.generateRules(frequentItemSetsList, 90);
        Apriori.rules = AssociationRules.getRules();
        finish = System.nanoTime();
        Apriori.timeAssocRules = (finish - start) / 1000000;

        for (FrequentItemSets frequentItemSets: frequentItemSetsList) {
            for (ItemSet itemSet: frequentItemSets.getItemSets()) {
                System.out.println(itemSet.getItemSet());
            }
        }
        System.out.println();
        for (Rule rule: rules) {
            System.out.println(rule.getConditions().getItemSet() + " ==> " + rule.getConsequences().getItemSet() + " ::= " + rule.getConfidence());
        }
    }
}


