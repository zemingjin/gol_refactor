package refactor.algorithm;

import java.util.ArrayList;
import java.util.List;

public class Cell implements Comparable<Cell> {
    private final int x, y;
    private String string;

    Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    List<Cell> getNeighbours() {
        final List<Cell> list = new ArrayList<>();

        for (int y = getY() - 1; y <= getY() + 1; y++) {
            for (int x = getX() - 1; x <= getX() + 1; x++) {
                if (x != getX() || y != getY()) {
                    list.add(new Cell(x, y));
                }
            }
        }
        return list;
    }

    boolean isNeighbour(Cell that) {
        return !equals(that) && Math.abs(x - that.getX()) <= 1 && Math.abs(y - that.getY()) <= 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Cell) {
            final Cell that = (Cell)obj;
            return getX() == that.getX() && getY() == that.getY();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return getString().hashCode();
    }

    @Override
    public String toString() {
        return getString();
    }

    @Override
    public int compareTo(Cell that) {
        return toString().compareTo(that.toString());
    }

    private String getString() {
        return string == null ? string = getString(x, y) : string;
    }

    static String getString(int x, int y) {
        return String.format("%d|%d", x, y);
    }
}
