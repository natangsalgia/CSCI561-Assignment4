import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BackwardChaining extends Inference {

	public BackwardChaining(File kb, File q, File oe, File ol) {
		super(kb, q, oe, ol);
	}

	private String getAND(ArrayList<String> ip) {
		StringBuilder sb = new StringBuilder();
		for (String string : ip) {
			sb.append((string + "&"));
		}
		String string = sb.toString();
		return (string.replaceAll("&$", ""));
	}

	@Override
	protected boolean impl(String q) {
		Map<String, Boolean> entailValue = new HashMap<String, Boolean>();
		String clauseStr = "";

		List<String> facts = Helper.cloneList(this.facts);
		List<String> entailed = Helper.cloneList(this.entailed);
		List<Rule> rules = Helper.cloneList(this.rules);
		for (int i = 0; i < rules.size(); i++) {
			Rule rule = rules.get(i);
			rules.set(i,
					new Rule(rule.getLhs(), rule.getRhs(), rule.getCount()));
		}
		List<String> tasks = new ArrayList<String>();
		tasks.add(q);
		while (!tasks.isEmpty()) {
			String task = tasks.remove(tasks.size() - 1);
			entailed.add(task);
			if (!facts.contains(task)) {
				boolean ruleFound = false;
				for (Rule rule : rules) {
					if (rule.getRhs().equalsIgnoreCase(task)) {
						ruleFound = true;
						ArrayList<String> lhs = rule.getLhs();
						String andLHS = getAND(lhs);
						clauseStr += (andLHS + "/");
						for (String string : lhs) {
							System.out.println("adding "+string);
							if (!entailed.contains(string)) {
								tasks.add(string);
							}
						}
					}
				}
				if (tasks.isEmpty() && !ruleFound) {
					return false;
				}
				entailValue.put(task, ruleFound);
				clauseStr = clauseStr.replaceAll("/$", "&");
			} else {
				entailValue.put(task, true);
			}
		}
		clauseStr = clauseStr.replaceAll("&$", "");
		System.out.println(clauseStr);
		int length = clauseStr.length();
		boolean op = entailValue.get(clauseStr.charAt(0) + "");
		for (int i = 0; i < length - 2; i++) {
			char c1 = clauseStr.charAt(i);
			if (c1 == '&' || c1 == '/') {
				continue;
			}
			char c2 = clauseStr.charAt(i + 2);
			boolean op1 = entailValue.get(c1 + "");
			boolean op2 = entailValue.get(c2 + "");
			boolean temp = getBooleanValue(op1, op2, clauseStr, i + 1);
			op = getBooleanValue(op, temp, clauseStr, i - 1);
		}
		return op;
	}

	private boolean getBooleanValue(boolean op1, boolean op2, String clauseStr,
			int i) {
		if (i < 0) {
			return op2;
		}
		if (clauseStr.charAt(i) == '&') {
			return op1 && op2;
		}
		return op1 || op2;
	}

	@Override
	protected String createLog(List<String> facts, Rule r) {
		// TODO Auto-generated method stub
		return null;
	}
}
