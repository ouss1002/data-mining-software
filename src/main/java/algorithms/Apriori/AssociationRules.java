package algorithms.Apriori;
import java.util.ArrayList;

public class AssociationRules {
	private static ArrayList<Rule> rules = new ArrayList<>();

	public static ArrayList<Rule> getRules() {
		return rules;
	}

	public static void addRule(Rule rule) {
		if(! rules.contains(rule))
			rules.add(rule);
	}

	public static void generateRules(ArrayList<FrequentItemSets> frequentItemSetsList, int minConfidenceThreshold) {
		rules = new ArrayList<>();
		for(FrequentItemSets frequentItemSets : frequentItemSetsList) {
			for (int i = 0; i < frequentItemSets.getSize(); i++) {
				int suppNominator = frequentItemSets.getValue(frequentItemSets.getItemSet(i));
				ArrayList<ItemSet> allSubItemSets = frequentItemSets.getItemSet(i).getAllSubItemSets();
				for (ItemSet subItemSet : allSubItemSets) {
					ItemSet consequent = frequentItemSets.getItemSet(i).substract(subItemSet);
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
}
