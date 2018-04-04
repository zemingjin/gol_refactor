package gameoflife.algorithm;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.apache.commons.collections4.ListUtils;

public class GameOfLife {
    private static final char LIVE_CELL = 'O';
    private static final String INDICES_DELIMITER = "\\|";

    private Boundary boundary;
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

    public Boundary getDimension() {
        return boundary;
    }

    GameOfLife seed(String values, Boundary boundary) {
        this.boundary = boundary;
        setLiveCells(seedsToLiveCells(values));
        return this;
    }

    private List<Cell> seedsToLiveCells(String values) {
        return Stream.of(values.split(", "))
                .map(this::getCell)
                .collect(Collectors.toList());
    }

    public GameOfLife seed(String[] seeds) {
        boundary =  getBoundary(seeds[0]);
        setLiveCells(seedsToLiveCells(Arrays.copyOfRange(seeds, 1, seeds.length)));
        return this;
    }

    private Boundary getBoundary(String info) {
        return getCell(info.split(" ")[1]);
    }

    private List<Cell> seedsToLiveCells(String[] seeds) {
        return IntStream.range(0, seeds.length)
                .mapToObj(y -> getLiveCellsFromRow(seeds, y))
                .flatMap(c -> c)
                .collect(Collectors.toList());
    }

    private Stream<Cell> getLiveCellsFromRow(String[] seeds, int y) {
        return IntStream.range(0, seeds[y].length())
                .filter(x -> isLiveCell(seeds[y], x))
                .mapToObj(x -> new Cell(x, y));
    }

    private boolean isLiveCell(String line, int x) {
        return line.charAt(x) == LIVE_CELL;
    }

    public boolean isLiveCell(Cell cell) {
        return cellMap.get(cell.toString()) != null;
    }

    private List<Cell> getNextGenerationCells() {
        return filterCellList(getLiveCells(), this::isNextGenerationCell);
    }

    private boolean isNextGenerationCell(Cell cell) {
        final long numberOfNeighbours = getNumberOfNeighbours(cell);
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
                .filter(boundary::isInBound)
                .filter(this::isDeadCell)
                .collect(Collectors.toList());
    }

    private boolean isDeadCell(Cell cell) {
        return !isLiveCell(cell);
    }

    private Boundary getCell(String values) {
        final String[] indices = values.split(INDICES_DELIMITER);
        return new Boundary(Integer.parseInt(indices[0].trim()), Integer.parseInt(indices[1].trim()));
    }
}
