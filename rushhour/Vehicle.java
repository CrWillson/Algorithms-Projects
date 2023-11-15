package rushhour;

public class Vehicle {
    String type;
    char orientation;
    String color;
    int row;
    int col;
    int id;

    public Vehicle(int i, String t, String c, char o, int r, int co) {
        type = t;
        orientation = o;
        color = c;
        row = r;
        col = co;
        id = i;
    }

    public Vehicle clone() {
        return new Vehicle(id, type, color, orientation, row, col);
    }

    public Vehicle clone(int rowAdjustment, int colAdjustment) {
        return new Vehicle(id, type, color, orientation, row + rowAdjustment, col + colAdjustment);
    }
}
