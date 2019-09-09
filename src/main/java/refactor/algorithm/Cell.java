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
        return IntStream.rangeClosed(this.y - 1, this.y + 1)
                .mapToObj(this::getRowNeighbours)
                .flatMap(s -> s)
                .collect(Collectors.toList());
    }

    private Stream<Cell> getRowNeighbours(int y) {
        return IntStream.rangeClosed(this.x - 1, this.x + 1)
                .filter(x1 -> x1 != this.x || y != this.y)
                .mapToObj(x1 -> new Cell(x1, y));
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
        return string.compareTo(that.string);
    }

    static String toString(int x, int y) {
        return x + "|" + y;
    }
}
