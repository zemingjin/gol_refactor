package gameoflife;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.collections4.ListUtils;

public class GameOfLife {
    private static final char LIVE_CELL = 'O';
    private static final String INDICES_DELIMITER = "\\|";

    private Cell boundary;
    private List<Cell> liveCells = new ArrayList<>();

    List<Cell> getLiveCells() {
        return Optional.of(liveCells)
                .filter(cells -> !cells.isEmpty())
                .orElseThrow(() -> new RuntimeException("No more living cells"));
    }

    List<Cell> setLiveCells(List<Cell> liveCells) {
        this.liveCells = liveCells;
        return liveCells;
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
        return new Cell(getIndex(Cell::getX), getIndex(Cell::getY));
    }

    public Cell getDimension() {
        return boundary;
    }

    synchronized GameOfLife seed(String values, Cell boundary) {
        this.boundary = boundary;
        setLiveCells(seedsToLiveCells(values));
        return this;
    }

    private List<Cell> seedsToLiveCells(String values) {
        return Stream.of(values.split(", "))
                .map(this::getCell)
                .collect(Collectors.toList());
    }

    public synchronized void seed(String[] seeds) {
        boundary =  getBoundary(seeds[0]);
        setLiveCells(seedsToLiveCells(Arrays.copyOfRange(seeds, 1, seeds.length)));
    }

    private Cell getBoundary(String info) {
        String[] indices = info.split(" ")[1].split(INDICES_DELIMITER);
        return new Cell(Integer.parseInt(indices[0]), Integer.parseInt(indices[1]));
    }

    private List<Cell> seedsToLiveCells(String[] seeds) {
        return IntStream.range(0, seeds.length)
                .mapToObj(y -> getRowOfCells(seeds, y))
                .flatMap(c -> c)
                .collect(Collectors.toList());
    }

    private Stream<Cell> getRowOfCells(String[] seeds, int y) {
        return IntStream.range(0, seeds[y].length())
                .filter(x -> seeds[y].charAt(x) == LIVE_CELL)
                .mapToObj(x -> new Cell(x, y, boundary));
    }

    private int getIndex(Function<Cell, Integer> getter) {
        return getDeadCells().stream()
                .map(getter)
                .reduce(Math::min)
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

    private Cell getCell(String values) {
        String[] indices = values.split(INDICES_DELIMITER);
        return new Cell(Integer.parseInt(indices[0].trim()), Integer.parseInt(indices[1].trim()), boundary);
    }
}
