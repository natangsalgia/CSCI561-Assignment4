import java.io.File;
import java.util.List;

public class ForwardChaining extends Inference {

    public ForwardChaining(File kb, File q, File oe, File ol) {
        super(kb, q, oe, ol);
    }

    @Override
    protected boolean impl(String q) {
        List<String> facts = Helper.cloneList(this.facts);
        List<String> entailed = Helper.cloneList(this.entailed);
        List<Rule> rules = Helper.cloneList(this.rules);
        for (int i = 0; i < rules.size(); i++) {
            Rule rule = rules.get(i);
            rules.set(i, new Rule(rule.getLhs(), rule.getRhs(), rule.getCount()));
        }
        while (!facts.isEmpty()) {
            String fact = facts.remove(0);
            entailed.add(fact);
            for (Rule r : rules) {
                if (r.lhsContains(fact)) {
                    r.decrementCount();
                    if (r.getCount() == 0) {
                        log += createLog(entailed, r) + "\n";
                        printLog();
                        if (r.getRhs().equalsIgnoreCase(q)) {
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
    protected void addHeader() {
        log += "<Known/Deducted facts>#Rules Fires#NewlyEntailedFacts" + "\n";
    }

    protected String createLog(List<String> facts, Rule r) {
        return (printList(facts) + "#" + r.toString() + " # " + r.getRhs());
    }

    private String printList(List<String> facts) {
        String print = "";
        for (String string : facts) {
            print += string+", ";
        }
        return (print.replaceAll(", $", ""));
    }

}
