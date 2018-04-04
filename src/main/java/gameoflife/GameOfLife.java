package gameoflife;

import java.util.*;
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
    private Map<String, Cell> cellMap;

    List<Cell> getLiveCells() {
        return Optional.of(liveCells)
                .filter(cells -> !cells.isEmpty())
                .orElseThrow(() -> new RuntimeException("No more living cells"));
    }

    List<Cell> setLiveCells(List<Cell> liveCells) {
        this.liveCells = liveCells;
        cellMap = getCellMap(liveCells);
        return liveCells;
    }

    private Map<String, Cell> getCellMap(List<Cell> liveCells) {
        return liveCells.stream()
                .collect(Collectors.toMap(Cell::toString, cell -> cell));
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

    public synchronized GameOfLife seed(String[] seeds) {
        boundary =  getBoundary(seeds[0]);
        setLiveCells(seedsToLiveCells(Arrays.copyOfRange(seeds, 1, seeds.length)));
        return this;
    }

    private Cell getBoundary(String info) {
        return getCell(info.split(" ")[1]);
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
        return getNeighbouringCells().stream()
                .map(getter)
                .reduce(Math::min)
                .orElse(0);
    }

    public boolean isLiveCell(Cell cell) {
        return cellMap.get(cell.toString()) != null;
    }

    private List<Cell> getNextGenerationCells() {
        return filterCellList(getLiveCells(), this::isNextGenerationCell);
    }

    private boolean isNextGenerationCell(Cell cell) {
        long numberOfNeighbours = getNumberOfNeighbours(cell);
        return 2 == numberOfNeighbours || numberOfNeighbours == 3;
    }

    private List<Cell> getReproductionCells() {
        return filterCellList(getNeighbouringCells(), cell -> getNumberOfNeighbours(cell) == 3);
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

    List<Cell> getNeighbouringCells() {
        return getLiveCells().stream()
                .flatMap(cell -> cell.getNeighbours().stream())
                .distinct()
                .filter(this::isDeadCell)
                .collect(Collectors.toList());
    }

    private boolean isDeadCell(Cell cell) {
        return !isLiveCell(cell);
    }

    private Cell getCell(String values) {
        String[] indices = values.split(INDICES_DELIMITER);
        return new Cell(Integer.parseInt(indices[0].trim()), Integer.parseInt(indices[1].trim()), boundary);
    }
}
