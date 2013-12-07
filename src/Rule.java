import java.util.ArrayList;

public class Rule {
    private final ArrayList<String> lhs;
    private final String rhs;
    private int count;

    public Rule(ArrayList<String> lhs, String rhs, int count) {
        this.lhs = lhs;
        this.rhs = rhs;
        this.count = count;
    }

    public int getCount() {
        return count;
    }

    private void setCount(int count) {
        this.count = count;
    }

    public ArrayList<String> getLhs() {
        return lhs;
    }

    public String getRhs() {
        return rhs;
    }

    public boolean lhsContains(String f) {
        if (lhs.contains(f.toUpperCase()) || lhs.contains(f.toLowerCase())) {
            return true;
        }
        return false;
    }

    public void decrementCount() {
        this.setCount(count - 1);
    }

    @Override
    public String toString() {
        String lString = "";
        for (String l : lhs) {
            lString += l + ",";
        }
        return (rhs + " :- " + lString.replaceAll(",$", ""));
    }

    public String printLHS() {
        String op = "";
        for (String string : lhs) {
            op += string +", ";
        }
        return op.replaceAll(",\\s$","");
    }

}
