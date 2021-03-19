package algorithms.Apriori;

import java.util.ArrayList;

public class CandidateItemSets {
    ArrayList<ItemSet> candidates = new ArrayList<>();

    public CandidateItemSets() {}

    public ArrayList<ItemSet> getCandidateItemSets() {
        return candidates;
    }

    public void removeCandidate(int index) {
        candidates.remove(index);
    }

    public ItemSet getCandidate(int index) {
        return candidates.get(index);
    }

    public boolean containsItemSet(ItemSet candidate) {
        for (ItemSet candidateTMP: candidates) {
            if (candidateTMP.equals(candidate.getItemSet()))
                return true;
        }
        return false;
    }

    public void addCandidate(ItemSet candidate) {
        if(! containsItemSet(candidate))
            candidates.add(candidate);
    }
}
