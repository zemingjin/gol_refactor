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
        return IntStream.rangeClosed(this.y - 1, this.y + 1)
                .mapToObj(this::getRowNeighbours)
                .flatMap(List::stream)
                .distinct()
                .collect(Collectors.toList());
    }

    private List<Cell> getRowNeighbours(int y) {
        return IntStream.rangeClosed(this.x - 1, this.x + 1)
                .filter(x1 -> x1 != this.x || y != this.y)
                .mapToObj(x1 -> new Cell(x1, y))
                .collect(Collectors.toList());
    }

    int getWeight() {
        return x + y;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (obj instanceof Cell) {
            Cell that = (Cell)obj;
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
    public int compareTo(Cell that) {
        return toString().compareTo(that.toString());
    }

    static String toString(int x, int y) {
        return x + "|" + y;
    }
}
