package refactor.algorithm;

import org.apache.commons.collections4.ListUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Refactor {
    private final Map<String, Cell> livingCells;

    Refactor() {
        livingCells = Collections.emptyMap();
    }

    public Refactor(Map<String, Cell> livingCells) {
        this.livingCells = livingCells;
    }

    Cell findCellWithLeastWeight() {
        return getLivingCells().stream().min(Comparator.comparingInt(Cell::getWeight)).orElse(null);
    }

    public boolean isLivingCell(int x, int y) {
        return isLivingCell(Cell.toName(x, y));
    }

    private boolean isLivingCell(String key) {
        return livingCells.get(key) != null;
    }

    Collection<Cell> getLivingCells() {
        if (livingCells.isEmpty()) {
            throw new RuntimeException("No more living cells");
        }
        return livingCells.values();
    }

    public Refactor tick() {
        return new Refactor(getNextMap());
    }

    @NotNull
    private Map<String, Cell> getNextMap() {
        return Stream.concat(getNextGenerationCells(), getReproducibleCells())
                .collect(Collectors.toMap(Cell::toString, cell -> cell));
    }

    private Stream<Cell> getNextGenerationCells() {
        return getFilteredCells(getLivingCells().stream(), n -> n == 2 || n == 3);
    }

    private Stream<Cell> getReproducibleCells() {
        return getFilteredCells(getNeighbouringDeadCells(), n -> n == 3);
    }

    private Stream<Cell> getFilteredCells(Stream<Cell> list, Predicate<Long> filter) {
        return list.filter(cell -> filter.test(getNumberOfLivingNeighbours(cell)));
    }

    private long getNumberOfLivingNeighbours(Cell that) {
        return that.getNeighbours().stream().filter(cell -> isLivingCell(cell.toString())).count();
    }

    Stream<Cell> getNeighbouringDeadCells() {
        return getLivingCells().stream().flatMap(Cell::getNeighbors).filter(this::isDeadCell).distinct();
    }

    private boolean isDeadCell(Cell cell) {
        return !isLivingCell(cell.toString());
    }
}