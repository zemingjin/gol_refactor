package refactor.algorithm;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Cell implements Comparable<Cell> {
    private final String string;
    public int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        string = toString(x, y);
    }

    List<Cell> getNeighbours() {
        return IntStream.rangeClosed(y - 1, y + 1)
                .mapToObj(this::getRowNeighbours)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Cell> getRowNeighbours(int row) {
        return IntStream.rangeClosed(x - 1, x + 1)
                .filter(x1 -> x1 != x || row != this.y)
                .mapToObj(x1 -> new Cell(x1, row))
                .collect(Collectors.toList());
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
