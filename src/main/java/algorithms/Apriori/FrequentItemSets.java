package algorithms.Apriori;

import java.util.ArrayList;
import java.util.HashMap;

public class FrequentItemSets {
    private HashMap<ItemSet, Integer> itemsetsMap = new HashMap<>();
    private ArrayList<ItemSet> itemsets = new ArrayList<>();



    public void setItemSets(FrequentItemSets otherFrequentItemSets) {
        for(ItemSet itemset : otherFrequentItemSets.getItemSets()) {
            itemsetsMap.put(itemset, otherFrequentItemSets.getItemSetsMap().get(itemset));
            if (! itemsets.contains(itemset))
                itemsets.add(itemset);
        }
    }

    public void setItemSets(CandidateItemSets candidateItemSets) {
        itemsets.clear();
        for(ItemSet itemset : candidateItemSets.getCandidateItemSets()) {
            itemsetsMap.put(itemset, itemset.getSupportCount());
            if (! itemsets.contains(itemset))
                itemsets.add(itemset);
        }
    }

    public Integer getSize() {
        return itemsets.size();
    }

    public Integer getValue(ItemSet key) {
        return itemsetsMap.get(key);
    }

    public void addItemSet(ItemSet itemSet,Integer supportCount) {
        itemsetsMap.put(itemSet, supportCount);
        if (! itemsets.contains(itemSet)) {
            itemsets.add(itemSet);
        }
    }

    public boolean containsKey(ItemSet key) {
        if (itemsets.contains(key)) {
            return true;
        }
        return false;
    }
    public ArrayList<ItemSet> getItemSets() {
        return itemsets;
    }

    public ItemSet getItemSet(int index) {
        return itemsets.get(index);
    }

    public HashMap<ItemSet, Integer> getItemSetsMap() {
        return itemsetsMap;
    }
}
