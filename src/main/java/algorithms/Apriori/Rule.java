package algorithms.Apriori;

public class Rule {
	private ItemSet conditions, consequences;
	private int confidence;

	public Rule(ItemSet conditions, ItemSet consequences, int confidence) {
		this.conditions = conditions;
		this.consequences = consequences;
		this.confidence = confidence;
	}

	public ItemSet getConditions() { return conditions; }
	public ItemSet getConsequences() { return consequences; }
	public int getConfidence() { return confidence; }

	@Override
	public boolean equals(Object otherRule) {
		if (this == otherRule)
			return true;
		if (otherRule == null || getClass() != otherRule.getClass())
			return false;
		Rule other = (Rule) otherRule;
		return conditions.equals(other.getConditions()) && consequences.equals(other.getConsequences());
	}
}
