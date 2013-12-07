import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;

public class BackwardChaining extends Inference {

    public BackwardChaining(File kb, File q, File oe, File ol) {
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
        List<String> tasks = new ArrayList<String>();
        tasks.add(q);
        LinkedHashMap<String, LinkedList<Rule>> branchMap = new LinkedHashMap<String, LinkedList<Rule>>();

        while (!tasks.isEmpty()) {
            String task = tasks.remove(0);
            entailed.add(task);
            if (!facts.contains(task)) {
                LinkedList<Rule> ruleList = new LinkedList<Rule>();
                int index = entailed.size() - 1;
                Rule selectedRule = null;
                boolean cycle = false;
                if (task.equalsIgnoreCase(q) && branchMap.containsKey(task)) {
                    cycle = true;
                    log += task + " # " + "CYCLE DETECTED" + " # " + "N/A\n";
                    entailed.remove(index--);
                    if (index < 0) {
                        return false;
                    }
                } else {
                    for (Rule rule : rules) {
                        if (rule.getRhs().equalsIgnoreCase(task)) {
                            ruleList.add(rule);
                        }
                    }
                    branchMap.put(task, ruleList);
                    task = entailed.get(index);
                }
                
                boolean printOnce = false;
                while (index >= 0 && (task = entailed.get(index)) != null
                        && ((ruleList = branchMap.get(task)) == null || ruleList.isEmpty())) {
                    if (!printOnce && !cycle) {
                        log += task + " # " + "N/A" + " # " + "N/A\n";
                        printOnce = true;
                    }
                    tasks.clear();
                    index--;
                }

                if (ruleList == null || ruleList.isEmpty()) {
                    return false;
                }

                selectedRule = ruleList.removeFirst();
                log += task + " # " + selectedRule.toString() + " # " + selectedRule.printLHS() + "\n";
                ArrayList<String> lhs = selectedRule.getLhs();
                for (int i = lhs.size() - 1; i >= 0; i--) {
                    String string = lhs.get(i);
                    boolean contains = facts.contains(string);
                    if ((contains) || (!contains && !entailed.contains(string))
                            || (string.equals(q))) {
                        tasks.add(0, string);
                    }
                }
            } else {
                log += task + " # " + task + " # " + "N/A\n";
            }
        }
        return true;
    }

    protected String createLog(List<String> facts, Rule r) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void addHeader() {
        log += "<Queue of Goals>#Relevant Rules/Fact#New Goal Introduced\n";
    }
}
