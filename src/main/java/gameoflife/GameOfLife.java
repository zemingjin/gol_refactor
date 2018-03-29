package gameoflife;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class GameOfLife {
    private List<Cell> liveCells = new ArrayList<>();

    public List<Cell> getLiveCells() {
        return liveCells;
    }

    public GameOfLife setLiveCells(List<Cell> liveCells) {
        this.liveCells = liveCells;
        return this;
    }

    public GameOfLife seed(String values) {
        Stream.of(values.split(", "))
                .forEach(this::addCell);
        return this;
    }

    public List<Cell> tick() {
        List<Cell> list = getNextGenerationCells();

        list.addAll(getReproductionCells());
        return list;
    }

    private List<Cell> getNextGenerationCells() {
        return filterCellList(getLiveCells(), cell -> 2 <= getNumberOfNeighbours(cell) && 3 >= getNumberOfNeighbours(cell));
    }

    private List<Cell> filterCellList(List<Cell> list, Predicate<Cell> predicate) {
        return list.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    private List<Cell> getReproductionCells() {
        return filterCellList(getDeadCells(), cell -> getNumberOfNeighbours(cell) == 3);
    }

    private long getNumberOfNeighbours(Cell cell) {
        return getLiveCells().stream()
                .filter(c -> !cell.equals(c) && cell.isNeighbour(c))
                .count();
    }

    public List<Cell> getDeadCells() {
        return getLiveCells().stream()
                .flatMap(cell -> cell.getNeighbours().stream())
                .distinct()
                .filter(cell -> !getLiveCells().contains(cell))
                .collect(Collectors.toList());
    }

    private void addCell(String values) {
        String[] indices = values.split("\\|");
        liveCells.add(new Cell(Integer.parseInt(indices[0]), Integer.parseInt(indices[1])));
    }
}
