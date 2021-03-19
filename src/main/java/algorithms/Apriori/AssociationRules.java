package algorithms.Apriori;

import java.util.ArrayList;


public class AssociationRules {
	private static ArrayList<Rule> rules = new ArrayList<>();

	public static ArrayList<Rule> getRules() { return rules; }

	public static int numRules() {
		return rules.size();
	}

	public static Rule getRule(int position) {
		if(position >= numRules() || position < 0)
			return null;
		return rules.get(position);
	}

	public static void addRule(Rule rule) {
		if(! rules.contains(rule))
			rules.add(rule);
	}

	public static void generateAssociationRules(ArrayList<FrequentItemSets> frequentItemSetsList, int minConfidenceThreshold) {
		for(FrequentItemSets frequentItemSets : frequentItemSetsList) {
			for (int i = 0; i < frequentItemSets.getSize(); i++) {
				ItemSet currItemSet = frequentItemSets.getItemSet(i);
				int suppNominator = frequentItemSets.getValue(currItemSet);
				ArrayList<ItemSet> allSubItemSets = currItemSet.getAllSubItemSets();
				for (ItemSet subItemSet : allSubItemSets) {
					ItemSet consequent = currItemSet.substract(subItemSet);
					Integer suppDenominator = frequentItemSetsList.get(subItemSet.getSize() - 1).getValueByString(subItemSet);
					if (suppDenominator == null)
						continue;
					int confidence = (int) ((1.0 * suppNominator / suppDenominator) * 100.0);
					if (confidence >= minConfidenceThreshold) {
						ItemSet subItemSetTrans = new ItemSet(), consequentTrans = new ItemSet();
						for (String item : subItemSet.getItemSet())
							subItemSetTrans.addItem(item);
						for (String item : consequent.getItemSet())
							consequentTrans.addItem(item);
						addRule(new Rule(subItemSetTrans, consequentTrans, confidence));
					}
				}
			}
		}
	}
	@Override
	public String toString() {
		return "AssociationRules [rules=" + rules + "]";
	}
}
