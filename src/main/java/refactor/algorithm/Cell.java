package refactor.algorithm;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Cell implements Comparable<Cell> {
    final String name;
    private final int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        name = toName(getX(), getY());
    }

    List<Cell> getNeighbours() {
        return IntStream.rangeClosed(y - 1, y + 1)
                .mapToObj(this::getRowNeighbours)
                .flatMap(it -> it)
                .collect(Collectors.toList());
    }

    private Stream<Cell> getRowNeighbours(int row) {
        return IntStream.rangeClosed(x - 1, x + 1)
                .filter(column -> column != x || row != y)
                .mapToObj(column -> new Cell(column, row));
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    @Override
    public boolean equals(Object other) {
        return Optional.ofNullable(other)
                .filter(Cell.class::isInstance)
                .map(Cell.class::cast)
                .map(that -> x == that.getX() && y == that.getY())
                .orElse(false);
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