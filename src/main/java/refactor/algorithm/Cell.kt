package refactor.algorithm;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Cell {
    private String name;
    private final int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
        name = toName(x, y);
    }

    String getName() {
        return name;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    int getWeight() {
        return x + y;
    }

    List<Cell> getNeighbours() {
        return IntStream.rangeClosed(y - 1, y + 1)
                .mapToObj(this::getRowNeighbours)
                .flatMap(s -> s)
                .collect(Collectors.toList());
    }

    private Stream<Cell> getRowNeighbours(int row) {
        return IntStream.rangeClosed(x - 1, x + 1)
                .filter(col -> !equals(col, row))
                .mapToObj(col -> new Cell(col, row));
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
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }

    static String toName(int x, int y) {
        return x + "|" + y;
    }
}