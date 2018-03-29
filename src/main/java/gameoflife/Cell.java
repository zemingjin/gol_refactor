package gameoflife;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class Cell {
    private int x, y;

    public Cell(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public boolean isNeighbour(Cell that) {
        return !equals(that) && Math.abs(x - that.x) <= 1 && Math.abs(y - that.y) <= 1;
    }

    public List<Cell> getNeighbours() {
        return IntStream.rangeClosed(x > 0 ? x - 1 : 0, x + 1)
                .mapToObj(this::getNeighboursByRow)
                .flatMap(s -> s)
                .filter(cell -> !equals(cell))
                .collect(Collectors.toList());
    }

    private Stream<Cell> getNeighboursByRow(int row) {
        return IntStream.rangeClosed(y > 0 ? y - 1 : 0, y + 1)
                .mapToObj(i -> new Cell(row, i));
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
}
