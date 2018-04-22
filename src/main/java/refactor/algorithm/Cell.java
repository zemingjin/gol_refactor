package refactor.algorithm;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Cell extends Point implements Comparable<Cell> {
    private final String string;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
        string = Cell.getString(x, y);
    }

    List<Cell> getNeighbours() {
        final List<Cell> list = new ArrayList<>();

        for (int y = super.y - 1; y <= super.y + 1; y++) {
            list.addAll(getRowNeighbours(y));
        }
        return list;
    }

    private List<Cell> getRowNeighbours(int y) {
        final List<Cell> list = new ArrayList<>();

        for (int x = super.x - 1; x <= super.x + 1; x++) {
            if (x != this.x || y != this.y) {
                list.add(new Cell(x, y));
            }
        }
        return list;
    }

    int getWeight() {
        return x + y;
    }

    boolean isNeighbour(Cell that) {
        return !equals(that) && Math.abs(x - that.x) <= 1 && Math.abs(y - that.y) <= 1;
    }

    @Override
    public int hashCode() {
        return string.hashCode();
    }

    @Override
    public String toString() {
        return string;
    }

    @Override
    public int compareTo(Cell that) {
        return toString().compareTo(that.toString());
    }

    static String getString(int x, int y) {
        return String.format("%d|%d", x, y);
    }
}
