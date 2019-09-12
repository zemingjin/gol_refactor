package refactor.algorithm;

import org.apache.commons.collections4.ListUtils;
import java.util.function.Predicate;

import java.util.*;
import java.util.stream.Collectors;

public class Refactor {
    private final Map<String, Cell> livingCells;

    Refactor() {
        livingCells = Collections.emptyMap();
    }

    public Refactor(Map<String, Cell> livingCells) {
        this.livingCells = livingCells;
    }

    Cell findCellWithLeastWeight() {
        return getLivingCells().stream()
                .min(Comparator.comparingInt(Cell::getWeight))
                .orElse(null);
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
        } else
            return livingCells.values();
    }

    public Refactor tick() {
        return new Refactor(toNextMap());
    }

    private Map<String, Cell> toNextMap() {
        return ListUtils.union(getNextGenerationCells(), getReproductionCells()).stream()
                .collect(Collectors.toMap(Cell::toString, cell -> cell));
    }

    private List<Cell> getNextGenerationCells() {
        return getFilteredCells(getLivingCells(), n -> n == 2 || n == 3);
    }

    private List<Cell> getReproductionCells() {
        return getFilteredCells(getNeighbouringDeadCells(), n -> n == 3);
    }

    private List<Cell> getFilteredCells(Collection<Cell> cells, Predicate<Long> isNotFilter) {
        return cells.stream()
                .filter(cell -> isNotFilter.test(getNumberOfLiveNeighbours(cell)))
                .collect(Collectors.toList());
    }

    private long getNumberOfLiveNeighbours(Cell that) {
        return that.getNeighbours().stream()
                .filter(cell -> isLivingCell(cell.getName()))
                .count();
    }

    Set<Cell> getNeighbouringDeadCells() {
        return getLivingCells().stream()
                .flatMap(cell -> cell.getNeighbours().stream())
                .filter(this::isDeadCell)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private boolean isDeadCell(Cell cell) {
        return !isLivingCell(cell.getName());
    }
}