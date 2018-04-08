package gameoflife.algorithm;

import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

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

    Stream<Cell> getNeighbours() {
        return IntStream.rangeClosed(getY() - 1, getY() + 1)
                .mapToObj(this::getNeighboursByRow)
                .flatMap(s -> s);
    }

    private Stream<Cell> getNeighboursByRow(int y) {
        return IntStream.rangeClosed(getX() - 1, getX() + 1)
                .mapToObj(x -> new Cell(x, y))
                .filter(this::isNotThis);
    }

    boolean isNeighbour(Cell that) {
        return !equals(that) && Math.abs(x - that.x) <= 1 && Math.abs(y - that.y) <= 1;
    }

    private boolean isNotThis(Cell that) {
        return !equals(that);
    }

    @Override
    public boolean equals(Object obj) {
        return Optional.ofNullable(obj)
                .filter(Cell.class::isInstance)
                .map(that -> (Cell)that)
                .filter(that -> getX() == that.getX() && getY() == that.getY())
                .isPresent();
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
        return Optional.ofNullable(string)
               .orElseGet(() -> string = getString(x, y));
    }

    static String getString(int x, int y) {
        return String.format("%d|%d", x, y);
    }
}