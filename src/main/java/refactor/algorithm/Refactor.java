package refactor.algorithm;

import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class Refactor {
    private static final char LIVE_CELL = 'O';
    private static final String INDICES_DELIMITER = "\\|";
    private static final String CELL_DELIMITER = ", ";

    private Boundary boundary;
    private Map<String, Cell> liveCells;

    public Refactor() {
    }

    private Refactor(Map<String, Cell> liveCells) {
        setLiveCells(liveCells);
    }

    /**
     * This method is only called by tests
     * @param boundary the given boundary in the format of "width|height".
     * @return this
     */
    Refactor setBoundary(String boundary) {
        if (Objects.isNull(boundary)) {
            throw new RuntimeException("Invalid data: null boundary");
        }
        return setBoundary(getBoundaryFromString(boundary));
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
        return setLiveCells(seedLiveCells(seeds));
    }

    private Map<String, Cell> seedLiveCells(String seeds) {
        final Map<String, Cell> map = new HashMap<>();

        for (final String elements : seeds.split(CELL_DELIMITER)) {
            final Cell cell = getCellFromString(elements);
            map.put(cell.toString(), cell);
        }
        return map;
    }

    Cell findCellWithLeastWeight() {
        Cell min = null;
        for (Cell cell : getLiveCells()) {
            if (min == null || min.getWeight() > cell.getWeight()) {
                min = cell;
            }
        }
        return min;
    }

    /**
     *
     * @param seeds the first line contains the size info, such as "#P width|height".
     *              the rest is in the format of ".....OO.O" where the capital 'O's indicate live cell(s).
     * @return this
     */
    public Refactor seedGame(String[] seeds) {
        setBoundary(getBoundaryFromString(getBoundaryFromHeader(seeds[0])));
        setLiveCells(seedLiveCells(Arrays.copyOfRange(seeds, 1, seeds.length)));
        return this;
    }

    private String getBoundaryFromHeader(String seed) {
        return seed.split(" ")[1];
    }

    private Map<String, Cell> seedLiveCells(String[] seeds) {
        final Map<String, Cell> map = new HashMap<>();

        for (int y = 0; y < seeds.length; y++) {
            for (final Cell cell : getRowOfCells(y, seeds[y])) {
                map.put(cell.toString(), cell);
            }
        }
        return map;
    }

    private List<Cell> getRowOfCells(int y, String line) {
        final List<Cell> list = new ArrayList<>();

        for (int x = 0; x < line.length(); x++) {
            if (isLiveCell(line.charAt(x))) {
                list.add(new Cell(x, y));
            }
        }
        return list;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    private Refactor setBoundary(Boundary boundary) {
        this.boundary = boundary;
        return this;
    }

    private boolean isLiveCell(char c) {
        return LIVE_CELL == c;
    }

    public boolean isLiveCell(int x, int y) {
        return isLiveCell(Cell.toString(x, y));
    }

    private boolean isLiveCell(String key) {
        return liveCells.get(key) != null;
    }

    Collection<Cell> getLiveCells() {
        if (liveCells.isEmpty()) {
            throw new RuntimeException("No more living cells");
        }
        return liveCells.values();
    }

    private Refactor setLiveCells(Map<String, Cell> liveCells) {
        this.liveCells = liveCells;
        return this;
    }

    public Refactor tick() {
        final Map<String, Cell> map = new HashMap<>();

        for (final Cell cell : ListUtils.union(getNextGenerationCells(), getReproductionCells())) {
            map.put(cell.toString(), cell);
        }
        return new Refactor(map).setBoundary(boundary);
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
        for (final Cell cell : that.getNeighbours()) {
            if (isLiveCell(cell.toString())) {
                count++;
            }
        }
        return count;
    }

    List<Cell> getNeighbouringDeadCells() {
        final List<Cell> list = new ArrayList<>();

        for (final Cell cell : getLiveCells()) {
            for (final Cell neighbour : cell.getNeighbours()) {
                if (isDeadCell(neighbour) && !list.contains(neighbour)) {
                    list.add(neighbour);
                }
            }
        }
        return list;
    }

    private boolean isDeadCell(Cell cell) {
        return !isLiveCell(cell.toString());
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