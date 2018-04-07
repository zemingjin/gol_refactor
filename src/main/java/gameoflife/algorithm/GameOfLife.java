package gameoflife.algorithm;

import java.util.*;
import java.util.function.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class GameOfLife {
    private static final char LIVE_CELL = 'O';
    private static final String INDICES_DELIMITER = "\\|";

    private Boundary boundary;
    private List<Cell> liveCells = new ArrayList<>();
    private Map<String, Cell> liveCellsMap;

    /**
     * This method is only called by tests
     * @param boundary the given boundary in the format of "width|height".
     * @return this
     */
    GameOfLife setBoundary(String boundary) {
        this.boundary = getCellFromString(boundary, Boundary::new);
        return this;
    }

    /**
     * This method is used only by tests
     * @param seeds the given seeds in the format of "1|1, 1|2, 1|3"
     * @return self
     */
    GameOfLife seedGame(String seeds) {
        setLiveCellsWithMap(seedLiveCells(seeds));
        return this;
    }

    private List<Cell> seedLiveCells(String seeds) {
        return Stream.of(seeds.split(", "))
                .map(seed -> getCellFromString(seed, Cell::new))
                .filter(boundary::isInBound)
                .collect(Collectors.toList());
    }

    /**
     *
     * @param seeds the first line contains the size info, such as "#P width|height".
     *              the rest is in the format of ".....OO.O" where the capital 'O' indicate live cell(s).
     * @return this
     */
    public GameOfLife seedGame(String[] seeds) {
        this.boundary =  getCellFromString(getBoundaryFromHeader(seeds[0]), Boundary::new);
        setLiveCellsWithMap(seedLiveCells(Arrays.copyOfRange(seeds, 1, seeds.length)));
        return this;
    }

    private String getBoundaryFromHeader(String seed) {
        return seed.split(" ")[1];
    }

    private List<Cell> seedLiveCells(String[] seeds) {
        return IntStream.range(0, Math.min(seeds.length, boundary.getY()))
                .mapToObj(y -> getLiveCellsFromRow(seeds[y], y))
                .flatMap(stream -> stream)
                .collect(Collectors.toList());
    }

    private Stream<Cell> getLiveCellsFromRow(String line, int y) {
        return IntStream.range(0, Math.min(line.length(), boundary.getX()))
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
        return liveCellsMap.get(Cell.getString(x, y)) != null;
    }

    List<Cell> getLiveCells() {
        return Optional.of(liveCells)
                .filter(cells -> !cells.isEmpty())
                .orElseThrow(() -> new RuntimeException("No more living cells"));
    }

    private List<Cell> setLiveCellsWithMap(List<Cell> liveCells) {
        this.liveCells = liveCells;
        liveCellsMap = getLiveCellsMap(liveCells);
        return liveCells;
    }

    private Map<String, Cell> getLiveCellsMap(List<Cell> liveCells) {
        return liveCells.stream()
                .collect(Collectors.toMap(Cell::toString, cell -> cell));
    }

    public List<Cell> evolve() {
        return setLiveCellsWithMap(tick());
    }

    private List<Cell> tick() {
        return Stream.concat(getNextGenerationCells(), getReproductionCells())
                .collect(Collectors.toList());
    }

    private Stream<Cell> getNextGenerationCells() {
        return getLiveCells().stream().filter(this::isNextGenerationCell);
    }

    private boolean isNextGenerationCell(Cell cell) {
        final long numberOfNeighbours = getNumberOfNeighbours(cell);
        return 2 == numberOfNeighbours || numberOfNeighbours == 3;
    }

    private Stream<Cell> getReproductionCells() {
        return getNeighbouringDeadCells().filter(cell -> getNumberOfNeighbours(cell) == 3);
    }

    private long getNumberOfNeighbours(Cell cell) {
        return getLiveCells().stream()
                .filter(cell::isNeighbour)
                .count();
    }

    Stream<Cell> getNeighbouringDeadCells() {
        return getLiveCells().stream()
                .flatMap(Cell::getNeighbours)
                .filter(boundary::isInBound)
                .filter(this::isDeadCell)
                .distinct();
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
