package algorithms.Apriori;

import java.util.ArrayList;
import java.util.Arrays;

public class ItemSet {
    private ArrayList<String> items = new ArrayList<>();
    private Integer supportCount = 0;

    public ItemSet() {}
    public ItemSet(ItemSet otherItemSet) {
        items = (ArrayList<String>) otherItemSet.getItemSet().clone();
    }
    public Integer getSize() {
        return items.size();
    }
    public ItemSet(int length) {
        items = new ArrayList<>(Arrays.asList(new String[length]));
    }
    public ItemSet(ItemSet otherItemSet, Integer sc) {
        items = (ArrayList<String>) otherItemSet.getItemSet().clone();
        supportCount = sc;
    }

    public Integer getSupportCount() {
        return supportCount;
    }

    public void setSupportCount(Integer sc) {
        supportCount = sc;
    }

    public void replaceItem(String item, int index) {
        items.set(index, item);
    }

    public ArrayList<String> getItemSet() {
        return items;
    }

    public boolean containsItem(String item) {
        return items.contains(item);
    }

    public boolean equals(ArrayList<String> itemsTMP) {
        return items.containsAll(itemsTMP);
    }

    public void addItem(String item) {
        if (!items.contains(item)) {
            items.add(item);
        }
    }

    public String getItem(Integer index) {
        return items.get(index);
    }

    public void removeItem(String item) {
        items.remove(item);
    }

    public ArrayList<ItemSet> getAllSubItemSets(){
        ArrayList<ItemSet> allSubsets = new ArrayList<>();
        int n = items.size();
        for (int i = 0; i < (1<<n); i++){
            ItemSet currSubset = new ItemSet();
            for (int j = 0; j < n; j++)
                if ((i & (1 << j)) > 0)
                    currSubset.addItem(getItem(j));
            if(currSubset.getSize() > 0 && currSubset.getSize() < n)
                allSubsets.add(currSubset);
        }
        return allSubsets;
    }

    public ItemSet substract(ItemSet otherItemSet) {
        ItemSet currItemSet = new ItemSet(this);
        for(String item : otherItemSet.getItemSet())
            currItemSet.removeItem(item);
        return currItemSet;
    }

}
