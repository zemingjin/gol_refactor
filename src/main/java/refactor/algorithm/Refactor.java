package refactor.algorithm;

import org.apache.commons.collections4.ListUtils;

import java.util.*;

public class Refactor {
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
    Refactor setBoundary(String boundary) {
        if (Objects.isNull(boundary)) {
            throw new RuntimeException("Invalid data: null boundary");
        }
        this.boundary = getBoundaryFromString(boundary);
        return this;
    }

    /**
     * This method is used only by tests
     * @param seeds the given seeds in the format of "1|1, 1|2, 1|3"
     * @return self
     */
    Refactor seedGame(String seeds) {
        if (Objects.isNull(seeds) || seeds.isEmpty()) {
            throw new RuntimeException(String.format("Invalid seeds: '%s'", seeds));
        }
        setLiveCellsWithMap(seedLiveCells(seeds));
        return this;
    }

    private List<Cell> seedLiveCells(String seeds) {
        final List<Cell> list = new ArrayList<>();

        for (final String elements : seeds.split(", ")) {
            final Cell cell = getCellFromString(elements);
            if (boundary.isInBound(cell)) {
                list.add(cell);
            }
        }
        return list;
    }

    /**
     *
     * @param seeds the first line contains the size info, such as "#P width|height".
     *              the rest is in the format of ".....OO.O" where the capital 'O's indicate live cell(s).
     * @return this
     */
    public Refactor seedGame(String[] seeds) {
        this.boundary =  getBoundaryFromString(getBoundaryFromHeader(seeds[0]));
        setLiveCellsWithMap(seedLiveCells(Arrays.copyOfRange(seeds, 1, seeds.length)));
        return this;
    }

    private String getBoundaryFromHeader(String seed) {
        return seed.split(" ")[1];
    }

    private List<Cell> seedLiveCells(String[] seeds) {
        final List<Cell> list = new ArrayList<>();

        for (int y = 0; y < Math.min(seeds.length, boundary.getY()); y++) {
            list.addAll(getRowOfCells(y, seeds[y]));
        }
        return list;
    }

    private List<Cell> getRowOfCells(int y, String line) {
        final List<Cell> list = new ArrayList<>();

        for (int x = 0; x < Math.min(line.length(), boundary.getX()); x++) {
            if (isLiveCell(line.charAt(x))) {
                list.add(new Cell(x, y));
            }
        }
        return list;
    }

    public Boundary getDimension() {
        return boundary;
    }

    private boolean isLiveCell(char c) {
        return LIVE_CELL == c;
    }

    public boolean isLiveCell(int x, int y) {
        return liveCellsMap.get(Cell.getString(x, y)) != null;
    }

    List<Cell> getLiveCells() {
        if (liveCells.isEmpty()) {
            throw new RuntimeException("No more living cells");
        }
        return liveCells;
    }

    private List<Cell> setLiveCellsWithMap(List<Cell> liveCells) {
        this.liveCells = liveCells;
        liveCellsMap = getLiveCellsMap(liveCells);
        return liveCells;
    }

    private Map<String, Cell> getLiveCellsMap(List<Cell> liveCells) {
        final Map<String, Cell> map = new HashMap<>();

        for (final Cell cell : liveCells) {
            map.put(cell.toString(), cell);
        }
        return map;
    }

    public List<Cell> evolve() {
        return setLiveCellsWithMap(tick());
    }

    private List<Cell> tick() {
        return ListUtils.union(getNextGenerationCells(), getReproductionCells());
    }

    private List<Cell> getNextGenerationCells() {
        final List<Cell> list = new ArrayList<>();

        for (final Cell cell : getLiveCells()) {
            if (isNextGenerationCell(cell)) {
                list.add(cell);
            }
        }
        return list;
    }

    private List<Cell> getReproductionCells() {
        final List<Cell> list = new ArrayList<>();

        for (final Cell cell : getNeighbouringDeadCells()) {
            if (isReproducingCell(cell)) {
                list.add(cell);
            }
        }
        return list;
    }

    private boolean isNextGenerationCell(Cell cell) {
        final long numberOfNeighbours = getNumberOfLiveNeighbours(cell);
        return 2 <= numberOfNeighbours && numberOfNeighbours <= 3;
    }

    private boolean isReproducingCell(Cell cell) {
        return getNumberOfLiveNeighbours(cell) == 3;
    }

    private long getNumberOfLiveNeighbours(Cell that) {
        long count = 0;
        for (final Cell cell : getLiveCells()) {
            if (cell.isNeighbour(that)) {
                count++;
            }
        }
        return count;
    }

    List<Cell> getNeighbouringDeadCells() {
        final List<Cell> list = new ArrayList<>();

        for (final Cell cell : getLiveCells()) {
            for (final Cell neighbour : cell.getNeighbours()) {
                if (boundary.isInBound(neighbour) && isDeadCell(neighbour) && !list.contains(neighbour)) {
                    list.add(neighbour);
                }
            }
        }
        return list;
    }

    private boolean isDeadCell(Cell cell) {
        return !isLiveCell(cell.getX(), cell.getY());
    }

    private Boundary getBoundaryFromString(String values) {
        final String[] indices = values.split(INDICES_DELIMITER);
        return new Boundary(Integer.parseInt(indices[0].trim()), Integer.parseInt(indices[1].trim()));
    }

    private Cell getCellFromString(String values) {
        final String[] indices = values.split(INDICES_DELIMITER);
        return new Cell(Integer.parseInt(indices[0].trim()), Integer.parseInt(indices[1].trim()));
    }
}