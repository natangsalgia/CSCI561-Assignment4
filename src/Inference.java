import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public abstract class Inference {
	private File kb;
	private File q;
	private File oe;
	private File ol;

	protected String log = "";
	protected ArrayList<String> query;
	protected ArrayList<String> facts;
	protected ArrayList<Rule> rules;
	protected ArrayList<String> entailed;

	public Inference(File kb, File q, File oe, File ol) {
		this.kb = kb;
		this.q = q;
		this.oe = oe;
		this.ol = ol;
		query = new ArrayList<String>();
		facts = new ArrayList<String>();
		rules = new ArrayList<Rule>();
		entailed = new ArrayList<String>();
	}

	public void run() {
		String op = "";
		for (String q : query) {
			if (impl(q)) {
				op += "YES";
			} else {
				op += "NO";
			}
			op += "\n";
		}
		printOp(op);
	}

	protected abstract boolean impl(String q);

	protected abstract String createLog(List<String> facts, Rule r);

	protected void printLog() {
		FileWriter olFr = null;
		BufferedWriter olBr = null;
		try {
			olFr = new FileWriter(ol);
			olBr = new BufferedWriter(olFr);
			olBr.append(log);
			olBr.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Helper.closeQuitely(olBr);
			Helper.closeQuitely(olFr);
		}
	}

	private void printOp(String op) {
		FileWriter olFr = null;
		BufferedWriter olBr = null;
		try {
			olBr = new BufferedWriter(olFr = new FileWriter(oe));
			olBr.append(op);
			olBr.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Helper.closeQuitely(olBr);
			Helper.closeQuitely(olFr);
		}
	}

	protected void loadData() {
		FileReader kbFr = null;
		BufferedReader kbBr = null;

		FileReader qFr = null;
		BufferedReader qBr = null;

		try {
			qFr = new FileReader(q);
			qBr = new BufferedReader(qFr);
			String line = "";
			while ((line = qBr.readLine()) != null) {
				query.add(line);
			}
			// System.out.println(query);

			kbFr = new FileReader(kb);
			kbBr = new BufferedReader(kbFr);
			line = "";
			while ((line = kbBr.readLine()) != null) {
				if (!line.contains(" :- ")) {
					facts.add(line);
				} else {
					ArrayList<String> lhs = new ArrayList<>();
					String[] split = line.split(" :- ");
					String[] lhsSplit = split[1].split(",");
					Helper.addFromArray(lhs, lhsSplit);
					Rule rule = new Rule(lhs, split[0], lhsSplit.length);
					rules.add(rule);
				}
			}
			// System.out.println(rules);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			Helper.closeQuitely(qFr);
			Helper.closeQuitely(qBr);
			Helper.closeQuitely(kbFr);
			Helper.closeQuitely(kbBr);
		}

	}

}
