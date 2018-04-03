package gameoflife;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.collections4.ListUtils;

public class GameOfLife {
    private static final BinaryOperator<Integer> MIN = (a, b) -> a <= b ? a : b;
    private static final BinaryOperator<Integer> MAX = (a, b) -> a > b ? a : b;
    private static final char LIVE_CELL = '1';

    private List<Cell> liveCells = new ArrayList<>();

    List<Cell> getLiveCells() {
        return Optional.of(liveCells)
                .filter(cells -> !cells.isEmpty())
                .orElseThrow(() -> new RuntimeException("No more living cells"));
    }

    List<Cell> setLiveCells(List<Cell> liveCells) {
        this.liveCells = liveCells;
        return getLiveCells();
    }

    public void evolve() {
        setLiveCells(tick());
    }

    List<Cell> tick() {
        return ListUtils.union(getNextGenerationCells(), getReproductionCells()).stream()
                .sorted()
                .collect(Collectors.toList());
    }

    public Cell getOffset() {
        return new Cell(getIndex(MIN, Cell::getX), getIndex(MIN, Cell::getY));
    }

    public Cell getDimension() {
        return new Cell(getIndex(MAX, Cell::getX), getIndex(MAX, Cell::getY));
    }

    synchronized GameOfLife seed(String values) {
        liveCells.clear();
        Stream.of(values.split(", "))
                .forEach(this::addCell);
        return this;
    }

    public synchronized void seed(char[][] seeds) {
        setLiveCells(seedsToCells(seeds));
    }

    private List<Cell> seedsToCells(char[][] seeds) {
        return IntStream.range(0, seeds.length)
                .mapToObj(y -> getRowOfCells(seeds, y))
                .flatMap(c -> c)
                .collect(Collectors.toList());
    }

    private Stream<Cell> getRowOfCells(char[][] seeds, int y) {
        return IntStream.range(0, seeds[y].length)
                .filter(x -> seeds[y][x] == LIVE_CELL)
                .mapToObj(x -> new Cell(x, y));
    }

    private int getIndex(BinaryOperator<Integer> operator, Function<Cell, Integer> getter) {
        return getDeadCells().stream()
                .map(getter)
                .reduce(operator)
                .orElse(0);
    }

    public boolean isLiveCell(Cell cell) {
        return getLiveCells().contains(cell);
    }

    private List<Cell> getNextGenerationCells() {
        return filterCellList(getLiveCells(), this::isNextGenerationCell);
    }

    private boolean isNextGenerationCell(Cell cell) {
        long numberOfNeighbours = getNumberOfNeighbours(cell);
        return 2 <= numberOfNeighbours && numberOfNeighbours <= 3;
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
