package refactor.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Cell implements Comparable<Cell> {
    private final String name;
    private final int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        name = toName(x, y);
    }

    List<Cell> getNeighbours() {
        final List<Cell> list = new ArrayList<>();

        for (int row = y - 1; row <= y + 1; row++) {
            list.addAll(getRowNeighbours(row));
        }
        return list;
    }

    private List<Cell> getRowNeighbours(int row) {
        final List<Cell> list = new ArrayList<>();

        for (int column = x - 1; column <= x + 1; column++) {
            if (column != x || row != y) {
                list.add(new Cell(column, row));
            }
        }
        return list;
    }

    int getWeight() {
        return x + y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof Cell) {
            Cell that = (Cell)other;
            return x == that.x && y == that.y;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int compareTo(Cell other) {
        return toString().compareTo(other.toString());
    }

    static String toName(int x, int y) {
        return x + "|" + y;
    }
}