package roadscholar;

import java.util.SortedMap;
import java.util.TreeMap;

public class Sign {
    public int i1;
    public int i2;
    public float dist;
    public SortedMap<Integer, String> labels;

    public Sign(int i1, int i2, float d) {
        this.i1 = i1;
        this.i2 = i2;
        dist = d;
        labels = new TreeMap<>();
    }
}
