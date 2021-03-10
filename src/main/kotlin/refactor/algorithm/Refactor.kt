package refactor.algorithm;

import org.apache.commons.collections4.ListUtils;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Refactor {
    private final Map<String, Cell> livingCells;

    Refactor() {
        livingCells = Collections.emptyMap();
    }

    public Refactor(Map<String, Cell> livingCells) {
        this.livingCells = livingCells;
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
        return ListUtils.union(getNextGenerationCells(), getReproducibleCells()).stream()
                .collect(Collectors.toMap(Cell::toString, cell -> cell, (a, b) -> b));
    }

    private List<Cell> getNextGenerationCells() {
        return getFilteredCells(getLivingCells(), n -> n == 2 || n == 3);
    }

    private List<Cell> getReproducibleCells() {
        return getFilteredCells(getNeighbouringDeadCells(), n -> n == 3);
    }

    private List<Cell> getFilteredCells(Collection<Cell> cells, Predicate<Long> test) {
        return cells.stream()
                .filter(it -> test.test(getNumberOfLivingNeighbours(it)))
                .collect(Collectors.toList());
    }

    private long getNumberOfLivingNeighbours(Cell that) {
        return that.getNeighbours().stream()
                .filter(cell -> isLivingCell(cell.name))
                .count();
    }

    List<Cell> getNeighbouringDeadCells() {
        return getLivingCells().stream()
                .map(Cell::getNeighbours)
                .flatMap(List::stream)
                .filter(this::isDeadCell)
                .distinct()
                .collect(Collectors.toList());
    }

    private boolean isDeadCell(Cell cell) {
        return !isLivingCell(cell.toString());
    }
}