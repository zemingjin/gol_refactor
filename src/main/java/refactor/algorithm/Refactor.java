package refactor.algorithm;

import org.apache.commons.collections4.ListUtils;
import java.util.function.Predicate;

import java.util.*;
import java.util.stream.Collectors;

public class Refactor {

    Refactor() {
        LivingCells.setLivingCells(Collections.emptyMap());
    }

    public Refactor(Map<String, Cell> livingCells) {
        LivingCells.setLivingCells(livingCells);
    }

    Cell findCellWithLeastWeight() {
        return LivingCells.getLivingCells().stream()
                .min(Comparator.comparingInt(Cell::getWeight))
                .orElse(null);
    }

    public Refactor tick() {
        return new Refactor(toNextMap());
    }

    private Map<String, Cell> toNextMap() {
        return ListUtils.union(getNextGenerationCells(), getReproductionCells()).stream()
                .collect(Collectors.toMap(Cell::toString, cell -> cell));
    }

    private List<Cell> getNextGenerationCells() {
        return getFilteredCells(LivingCells.getLivingCells(), n -> n == 2 || n == 3);
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
                .filter(Cell::isLivingCell)
                .count();
    }

    Set<Cell> getNeighbouringDeadCells() {
        return LivingCells.getLivingCells().stream()
                .flatMap(cell -> cell.getNeighbours().stream())
                .filter(this::isDeadCell)
                .collect(Collectors.toCollection(LinkedHashSet::new));
    }

    private boolean isDeadCell(Cell cell) {
        return !cell.isLivingCell();
    }
}