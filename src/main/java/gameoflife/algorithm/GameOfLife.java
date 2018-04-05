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

    GameOfLife setBoundary(String boundary) {
        this.boundary = getCellFromString(boundary, Boundary::new);
        return this;
    }

    GameOfLife convertSeeds(String seeds) {
        setLiveCellsWithMap(seedsToLiveCells(seeds));
        return this;
    }

    private List<Cell> seedsToLiveCells(String seeds) {
        return Stream.of(seeds.split(", "))
                .map(seed -> getCellFromString(seed, Cell::new))
                .collect(Collectors.toList());
    }

    public GameOfLife convertSeeds(String[] seeds) {
        this.boundary =  getCellFromString(getBoundaryFromHeader(seeds[0]), Boundary::new);
        setLiveCellsWithMap(seedsToLiveCells(Arrays.copyOfRange(seeds, 1, seeds.length)));
        return this;
    }

    private String getBoundaryFromHeader(String seed) {
        return seed.split(" ")[1];
    }

    private List<Cell> seedsToLiveCells(String[] seeds) {
        return IntStream.range(0, seeds.length)
                .mapToObj(y -> getLiveCellsFromRow(seeds[y], y))
                .flatMap(c -> c)
                .collect(Collectors.toList());
    }

    private Stream<Cell> getLiveCellsFromRow(String line, int y) {
        return IntStream.range(0, line.length())
                .filter(x -> isLiveCell(line.charAt(x)))
                .mapToObj(x -> new Cell(x, y));
    }

    public Boundary getDimension() {
        return boundary;
    }

    private boolean isLiveCell(char c) {
        return c == LIVE_CELL;
    }

    public boolean isLiveCell(int x, int y) {
        return cellMap.get(Cell.getString(x, y)) != null;
    }

    List<Cell> getLiveCells() {
        return Optional.of(liveCells)
                .filter(cells -> !cells.isEmpty())
                .orElseThrow(() -> new RuntimeException("No more living cells"));
    }

    private List<Cell> setLiveCellsWithMap(List<Cell> liveCells) {
        this.liveCells = liveCells;
        cellMap = getCellMap(liveCells);
        return liveCells;
    }

    private Map<String, Cell> getCellMap(List<Cell> liveCells) {
        return liveCells.stream()
                .collect(Collectors.toMap(Cell::toString, cell -> cell));
    }

    public List<Cell> evolve() {
        return setLiveCellsWithMap(tick());
    }

    private List<Cell> tick() {
        return ListUtils.union(getNextGenerationCells(), getReproductionCells()).stream()
                .sorted()
                .collect(Collectors.toList());
    }

    private List<Cell> getNextGenerationCells() {
        return filterCells(getLiveCells(), this::isNextGenerationCell);
    }

    private boolean isNextGenerationCell(Cell cell) {
        final long numberOfNeighbours = getNumberOfNeighbours(cell);
        return 2 == numberOfNeighbours || numberOfNeighbours == 3;
    }

    private List<Cell> getReproductionCells() {
        return filterCells(getNeighbouringCells(), cell -> getNumberOfNeighbours(cell) == 3);
    }

    private List<Cell> filterCells(List<Cell> list, Predicate<Cell> isCellToKeep) {
        return list.stream()
                .filter(isCellToKeep)
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
        return !isLiveCell(cell.getX(), cell.getY());
    }

    private <T extends Cell> T getCellFromString(String values,
                                                 BiFunction<Integer, Integer, T> supplier) {
        final String[] indices = values.split(INDICES_DELIMITER);
        return supplier.apply(Integer.parseInt(indices[0].trim()), Integer.parseInt(indices[1].trim()));
    }
}
