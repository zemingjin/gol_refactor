package refactor.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Cell implements Comparable<Cell> {
    private final String string;
    public final int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        string = toString(x, y);
    }

    List<Cell> getNeighbours() {
        final List<Cell> list = new ArrayList<>();

        for (int y = this.y - 1; y <= this.y + 1; y++) {
            list.addAll(getRowNeighbours(y));
        }
        return list;
    }

    private List<Cell> getRowNeighbours(int y) {
        final List<Cell> list = new ArrayList<>();

        for (int x = this.x - 1; x <= this.x + 1; x++) {
            if (x != this.x || y != this.y) {
                list.add(new Cell(x, y));
            }
        }
        return list;
    }

    int getWeight() {
        return x + y;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof Cell) {
            Cell that = (Cell)other;
            return x == that.x && y == that.y;
        }
        return false;
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
    public int compareTo(Cell other) {
        return toString().compareTo(other.toString());
    }

    static String toString(int x, int y) {
        return x + "|" + y;
    }
}
