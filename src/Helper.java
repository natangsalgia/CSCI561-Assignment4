import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Helper {

    private Helper() {
    }

    public static File strToFile(String fileStr) throws NullPointerException {
        return new File(fileStr);
    }

    public static void closeQuitely(Reader r) {
        try {
            r.close();
        } catch (IOException e) {
            // Ignore exception
        }
    }

    public static void closeQuitely(Writer r) {
        try {
            r.close();
        } catch (IOException e) {
            // Ignore exception
        }
    }

    private static double square(double a) {
        return Math.pow(a, 2);
    }

    public static CharSequence getLogOp(String path, double cost, double d, double cost2) {
        return path + "," + cost + "," + d + "," + cost2;
    }

    public static FileWriter getOPBuffer(String name) throws IOException {
        File file = Helper.strToFile(name);
        FileWriter fw = new FileWriter(file);
        return fw;
    }

    public static <T> List<T> subList(List<T> list, int start, int end) {
        List<T> newList = new ArrayList<T>();
        Iterator<T> iterator = list.subList(start, end).iterator();
        for (; iterator.hasNext();) {
            newList.add(iterator.next());
        }
        return newList;
    }

    public static <T> List<T> cloneList(List<T> list) {
        List<T> newList = new ArrayList<T>();
        for (T object : list) {
            newList.add(object);
        }
        return newList;
    }

    public static <T> void addFromArray(List<T> list, Object array[]) {
        T[] arr = (T[]) array;
        for (int i = 0; i < arr.length; i++) {
            list.add(arr[i]);
        }
    }

}
