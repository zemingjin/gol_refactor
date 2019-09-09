package refactor.algorithm;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Cell implements Comparable<Cell> {
    final String string;
    public final int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        string = toString(x, y);
    }

    List<Cell> getNeighbours() {
        return IntStream.rangeClosed(y - 1, y + 1)
                .mapToObj(this::getRowNeighbours)
                .flatMap(s -> s)
                .collect(Collectors.toList());
    }

    private Stream<Cell> getRowNeighbours(int row) {
        return IntStream.rangeClosed(x - 1, x + 1)
                .filter(x1 -> !equals(x1, row))
                .mapToObj(x1 -> new Cell(x1, row));
    }

    int getWeight() {
        return x + y;
    }

    private boolean equals(int column, int row) {
        return x == column && y == row;
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        } else if (other instanceof Cell) {
            Cell that = (Cell)other;
            return equals(that.x, that.y);
        } else
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
        return string.compareTo(other.string);
    }

    static String toString(int x, int y) {
        return x + "|" + y;
    }
}
