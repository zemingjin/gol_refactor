package gameoflife;

import java.util.ArrayList;
import java.util.List;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.collections4.ListUtils;

public class GameOfLife {
    private static final BinaryOperator<Cell> MIN_X = (a, b) -> a.isLess(b, Cell::getX) ? a : b;
    private static final BinaryOperator<Cell> MIN_Y = (a, b) -> a.isLess(b, Cell::getY) ? a : b;
    private static final BinaryOperator<Cell> MAX_X = (a, b) -> !a.isLess(b, Cell::getX) ? a : b;
    private static final BinaryOperator<Cell> MAX_Y = (a, b) -> !a.isLess(b, Cell::getY) ? a : b;

    private List<Cell> liveCells = new ArrayList<>();

    List<Cell> getLiveCells() {
        return liveCells;
    }

    public List<Cell> setLiveCells(List<Cell> liveCells) {
        this.liveCells = liveCells;
        return getLiveCells();
    }

    public synchronized GameOfLife seed(String values) {
        getLiveCells().clear();
        Stream.of(values.split(", "))
                .forEach(this::addCell);
        return this;
    }

    public List<Cell> tick() {
        return ListUtils.union(getNextGenerationCells(), getReproductionCells()).stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Cell getOffset() {
        return new Cell(getIndex(MIN_X, Cell::getX), getIndex(MIN_Y, Cell::getY));
    }

    public Cell getDimension() {
        Cell offset = getOffset();
        Cell maximum = new Cell(getIndex(MAX_X, Cell::getX), getIndex(MAX_Y, Cell::getY));
        return new Cell(maximum.getX() - offset.getX(), maximum.getY() - offset.getY());
    }

    private int getIndex(BinaryOperator<Cell> operator, Function<Cell, Integer> getter) {
        return getter.apply(getDeadCells().stream()
                .reduce(operator)
                .orElseThrow(() -> {
                    System.out.println(getDeadCells().toString());
                    return new RuntimeException("Invalid Live Cells");
                }));
    }

    public boolean isLiveCell(Cell cell) {
        return getLiveCells().contains(cell);
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
        liveCells.add(new Cell(Integer.parseInt(indices[0].trim()), Integer.parseInt(indices[1].trim())));
    }
}
