package refactor.algorithm;

import org.apache.commons.collections4.ListUtils;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class Refactor {
    private static final Predicate<Long> isReproducting = n -> n == 3;
    private static final Predicate<Long> isNextGeneration = isReproducting.or(n -> n == 2);

    private final Map<String, Cell> liveCells;

    public Refactor(Map<String, Cell> liveCells) {
        this.liveCells = liveCells;
    }

    Cell findCellWithLeastWeight() {
        return getLiveCells().stream()
                .min(Comparator.comparingInt(Cell::getWeight))
                .orElse(null);
    }

    public boolean isLiveCell(int x, int y) {
        return isLiveCell(Cell.toString(x, y));
    }

    private boolean isLiveCell(String key) {
        return liveCells.get(key) != null;
    }

    Collection<Cell> getLiveCells() {
        if (liveCells.isEmpty()) {
            throw new RuntimeException("No more living cells");
        }
        return liveCells.values();
    }

    public Refactor tick() {
        return new Refactor(getNextMap());
    }

    @NotNull
    private Map<String, Cell> getNextMap() {
        return ListUtils.union(getNextGenerationCells(), getReproductionCells()).stream()
                .collect(Collectors.toMap(Cell::toString, cell -> cell));
    }

    private List<Cell> getNextGenerationCells() {
        return filterCells(getLiveCells(), isNextGeneration);
    }

    private List<Cell> getReproductionCells() {
        return filterCells(getNeighbouringDeadCells(), isReproducting);
    }

    private List<Cell> filterCells(Collection<Cell> list, Predicate<Long> condition) {
        return list.stream()
                .filter(c -> condition.test(getNumberOfLiveNeighbours(c)))
                .collect(Collectors.toList());
    }

    private long getNumberOfLiveNeighbours(Cell that) {
        return that.getNeighbours().stream()
                .filter(cell -> isLiveCell(cell.toString()))
                .count();
    }

    Set<Cell> getNeighbouringDeadCells() {
        return getLiveCells().stream()
                .flatMap(cell -> cell.getNeighbours().stream())
                .filter(this::isDeadCell)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private boolean isDeadCell(Cell cell) {
        return !isLiveCell(cell.toString());
    }
}