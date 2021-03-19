package algorithms.Apriori;

import java.util.ArrayList;
import java.util.HashMap;

public class FrequentItemSets {
    private HashMap<ItemSet, Integer> itemsetsMap = new HashMap<>();
    private ArrayList<ItemSet> itemsets = new ArrayList<>();

    public  FrequentItemSets() {}

    public FrequentItemSets(FrequentItemSets otherFrequentItemSets) {
        for(ItemSet itemset : otherFrequentItemSets.getItemSets()) {
            itemsetsMap.put(itemset, otherFrequentItemSets.getItemSetsMap().get(itemset));
            if (! itemsets.contains(itemset))
                itemsets.add(itemset);
        }
    }

    public void setItemSets(CandidateItemSets candidateItemSets) {
        itemsets.clear();
        itemsetsMap.clear();
        for(ItemSet itemset : candidateItemSets.getCandidateItemSets()) {
            itemsetsMap.put(itemset, itemset.getSupportCount());
            if (! itemsets.contains(itemset))
                itemsets.add(itemset);
        }
    }

    public void updateSupportCount(Integer index, Integer sc) {
        ItemSet itemSet = itemsets.get(index);
        itemSet.setSupportCount(sc);
        itemsetsMap.put(itemSet, sc);
    }

    public Integer containsItem(String item) {
        for (ItemSet itemSet: itemsets) {
            if (itemSet.containsItem(item))
                return itemsets.indexOf(itemSet);
        }
        return -1;
    }

    public boolean containsItemSet(ItemSet itemSet) {
        for (ItemSet itemSetTMP: itemsets) {
            if (itemSetTMP.equals(itemSet.getItemSet()))
                return true;
        }
        return false;
    }


    public Integer getSize() {
        return itemsets.size();
    }

    public Integer getValue(ItemSet key) {
        return itemsetsMap.get(key);
    }

    public Integer getValueByString(ItemSet key) {
        for (ItemSet itemSet: itemsets) {
            if (itemSet.equals(key.getItemSet())) {
                return getValue(itemSet);
            }
        }
        return -1;
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

    public void removeItemSet(ItemSet itemSet) {
        itemsetsMap.remove(itemSet);
        itemsets.remove(itemSet);
    }

    public HashMap<ItemSet, Integer> getItemSetsMap() {
        return itemsetsMap;
    }
}
