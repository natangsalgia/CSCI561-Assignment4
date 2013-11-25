import java.io.File;
import java.util.List;

public class ForwardChaining extends Inference {

	public ForwardChaining(File kb, File q, File oe, File ol) {
		super(kb, q, oe, ol);
	}

	@Override
	protected boolean impl(String q) {
		System.out.println("Query: " + q);
		List<String> facts = Helper.cloneList(this.facts);
		List<String> entailed = Helper.cloneList(this.entailed);
		List<Rule> rules = Helper.cloneList(this.rules);
		for (int i = 0; i < rules.size(); i++) {
			Rule rule = rules.get(i);
			rules.set(i,
					new Rule(rule.getLhs(), rule.getRhs(), rule.getCount()));
		}
		System.out.println(facts);
		System.out.println(entailed);
		System.out.println(rules);
		while (!facts.isEmpty()) {
			String fact = facts.remove(0);
			System.out.println("fact: " + fact);
			entailed.add(fact);
			for (Rule r : rules) {
				System.out.println("Rule: " + r);
				if (r.lhsContains(fact)) {
					System.out.println("selected");
					r.decrementCount();
					if (r.getCount() == 0) {
						System.out.println("count 0");
						log += createLog(entailed, r) + "\n";
						printLog();
						if (r.getRhs().equalsIgnoreCase(q)) {
							log += createLog(entailed, r) + "\n";
							printLog();
							return true;
						}
						facts.add(r.getRhs());
					}
				}
			}
		}
		return false;
	}

	@Override
	protected String createLog(List<String> facts, Rule r) {
		return (facts.toString() + "\t#" + r.toString() + "\t#" + r.getRhs());
	}

}
