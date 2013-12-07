import java.io.File;

public class pl {

    /**
     * @param args
     */
    public static void main(String[] args) {

        final int task = Integer.parseInt(args[1]);
        final String kb = args[3];
        final String q = args[5];
        final String oe = args[7];
        final String ol = args[9];

        File kbFile = Helper.strToFile(kb);
        File qFile = Helper.strToFile(q);
        File oeFile = Helper.strToFile(oe);
        File olFile = Helper.strToFile(ol);

        Inference f = null;
        if (task == 1) {
            f = new ForwardChaining(kbFile, qFile, oeFile, olFile);
        } else if (task == 2) {
            f = new BackwardChaining(kbFile, qFile, oeFile, olFile);
        } else if (task == 3) {
            f = new Resolution(kbFile, qFile, oeFile, olFile);
        }

        f.loadData();
        f.run();
    }

}
