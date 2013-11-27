import java.io.File;
import java.util.ArrayList;
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
                Rule selectedRule = null;
                for (Rule rule : rules) {
                    if (rule.getRhs().equalsIgnoreCase(task)) {
                        ruleList.add(rule);
                    }
                }
                branchMap.put(task, ruleList);
                int index = entailed.size() - 1;
                task = entailed.get(index);
                while (index >= 0 && (task = entailed.get(index)) != null
                        && ((ruleList = branchMap.get(task)) == null || ruleList.isEmpty())) {
                    index--;
                }

                if (ruleList == null || ruleList.isEmpty()) {
                    return false;
                }

                selectedRule = ruleList.removeFirst();
                ArrayList<String> lhs = selectedRule.getLhs();
                for (String string : lhs) {
                    if (!entailed.contains(string)) {
                        tasks.add(string);
                    }
                }
            } else {
                // print log for fact
            }
        }
        return true;
    }

    @Override
    protected String createLog(List<String> facts, Rule r) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void addHeader() {
        // TODO Auto-generated method stub
        
    }
}
