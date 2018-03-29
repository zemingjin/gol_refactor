package gameoflife;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.ListUtils;

public class GameOfLife {
    private List<Cell> liveCells = new ArrayList<>();

    public List<Cell> getLiveCells() {
        return liveCells;
    }

    public List<Cell> setLiveCells(List<Cell> liveCells) {
        this.liveCells = liveCells;
        return getLiveCells();
    }

    public GameOfLife seed(String values) {
        Stream.of(values.split(", "))
                .forEach(this::addCell);
        return this;
    }

    public List<Cell> tick() {
        return ListUtils.union(getNextGenerationCells(), getReproductionCells());
    }

    private List<Cell> getNextGenerationCells() {
        return filterCellList(getLiveCells(),
                              cell -> 2 <= getNumberOfNeighbours(cell) && 3 >= getNumberOfNeighbours(cell));
    }

    private List<Cell> getReproductionCells() {
        return filterCellList(getDeadCells(), cell -> getNumberOfNeighbours(cell) == 3);
    }

    private List<Cell> filterCellList(List<Cell> list, Predicate<Cell> predicate) {
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private long getNumberOfNeighbours(Cell cell) {
        return getLiveCells().stream()
                .filter(cell::isNeighbour)
                .count();
    }

    List<Cell> getDeadCells() {
        return getLiveCells().stream()
                .flatMap(cell -> cell.getNeighbours().stream())
                .distinct()
                .filter(this::isDeadCell)
                .collect(Collectors.toList());
    }

    private boolean isDeadCell(Cell cell) {
        return !getLiveCells().contains(cell);
    }

    private void addCell(String values) {
        String[] indices = values.split("\\|");
        liveCells.add(new Cell(Integer.parseInt(indices[0]), Integer.parseInt(indices[1])));
    }
}
