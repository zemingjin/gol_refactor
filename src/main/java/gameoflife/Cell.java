package gameoflife;

import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Cell implements Comparable<Cell> {
    private int x, y;
    private Cell boundary;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Cell(int x, int y, Cell boundary) {
        this(x, y);
        this.boundary = boundary;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public Cell max(Cell that) {
        return new Cell(Math.max(getX(), that.getX()), Math.max(getY(), that.getY()));
    }

    boolean isNeighbour(Cell that) {
        return !equals(that) && isSameOrAdjacent(x, that.x) && isSameOrAdjacent(y, that.y);
    }

    List<Cell> getNeighbours() {
        return IntStream.rangeClosed(decrementIndex(x), incrementIndex(x, boundary::getX))
                .mapToObj(this::getNeighboursByColumn)
                .flatMap(s -> s)
                .filter(this::isNotThis)
                .collect(Collectors.toList());
    }

    private Stream<Cell> getNeighboursByColumn(int row) {
        return IntStream.rangeClosed(decrementIndex(y), incrementIndex(y, boundary::getY))
                .mapToObj(i -> new Cell(row, i, boundary));
    }

    private boolean isSameOrAdjacent(int a, int b) {
        return Math.abs(a - b) <= 1;
    }

    private int decrementIndex(int i) {
        return Math.max(i - 1, 0);
    }

    private int incrementIndex(int i, Supplier<Integer> getter) {
        return Math.min(i + 1, getter.get() - 1);
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
        return toString().hashCode();
    }

    @Override
    public String toString() {
        return String.format("%d|%d", x, y);
    }

    @Override
    public int compareTo(Cell that) {
        return toString().compareTo(that.toString());
    }
}
