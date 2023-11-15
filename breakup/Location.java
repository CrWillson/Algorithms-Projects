package breakup;

public class Location {
    public int num;
    public Location parent;
    public int rank;

    public Location(int n) {
        num = n;
        parent = this;
        rank = 0;
    }
}
