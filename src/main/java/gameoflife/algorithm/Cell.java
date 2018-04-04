package gameoflife.algorithm;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Cell implements Comparable<Cell> {
    private final int x, y;
    private String string;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    boolean isNeighbour(Cell that) {
        return !equals(that) && isSameOrAdjacent(x, that.x) && isSameOrAdjacent(y, that.y);
    }

    private boolean isSameOrAdjacent(int a, int b) {
        return Math.abs(a - b) <= 1;
    }

    List<Cell> getNeighbours() {
        return IntStream.rangeClosed(y - 1, y + 1)
                .mapToObj(this::getNeighboursByRow)
                .flatMap(s -> s)
                .collect(Collectors.toList());
    }

    private Stream<Cell> getNeighboursByRow(int y) {
        return IntStream.rangeClosed(getX() - 1, getX() + 1)
                .mapToObj(x -> new Cell(x, y))
                .filter(this::isNotThis);
    }

    private boolean isNotThis(Cell that) {
        return !this.equals(that);
    }

    @Override
    public boolean equals(Object obj) {
        return Optional.ofNullable(obj)
                .filter(Cell.class::isInstance)
                .map(that -> (Cell)that)
                .filter(that -> x == that.x && y == that.y)
                .isPresent();
    }

    @Override
    public int hashCode() {
        return getString().hashCode();
    }

    public String toString() {
        return getString();
    }

    @Override
    public int compareTo(Cell that) {
        return toString().compareTo(that.toString());
    }

    private String getString() {
        return Optional.ofNullable(string)
                .orElseGet(() -> string = String.format("%d|%d", x, y));
    }
}
