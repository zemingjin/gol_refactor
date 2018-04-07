package gameoflife.algorithm;

import org.apache.commons.collections4.ListUtils;

import java.util.*;

public class GameOfLife {
    private static final char LIVE_CELL = 'O';
    private static final String INDICES_DELIMITER = "\\|";

    private Boundary boundary;
    private List<Cell> liveCells = new ArrayList<>();
    private Map<String, Cell> liveCellsMap;

    GameOfLife setBoundary(String boundary) {
        this.boundary = getBoundaryFromString(boundary);
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
        final List<Cell> list = new ArrayList<>();

        for (final String elements : seeds.split(", ")) {
            final Cell cell = getCellFromString(elements);
            if (boundary.isInBound(cell)) {
                list.add(cell);
            }
        }
        return list;
    }

    public GameOfLife seedGame(String[] seeds) {
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
            final String line = seeds[y];

            for (int x = 0; x < Math.min(line.length(), boundary.getX()); x++) {
                if (isLiveCell(line.charAt(x))) {
                    list.add(new Cell(x, y));
                }
            }
        }
        return list;
    }

    public Boundary getDimension() {
        return boundary;
    }

    private boolean isLiveCell(char c) {
        return c == LIVE_CELL;
    }

    public boolean isLiveCell(String key) {
        return liveCellsMap.get(key) != null;
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
            final int numberOfNeighbours = getNumberOfLiveNeighbours(cell);

            if (boundary.isInBound(cell) && 2 <= numberOfNeighbours && numberOfNeighbours <= 3) {
                list.add(cell);
            }
        }
        return list;
    }

    private List<Cell> getReproductionCells() {
        final List<Cell> list = new ArrayList<>();

        for (final Cell cell : getNeighbouringCells()) {
            if (boundary.isInBound(cell) && getNumberOfLiveNeighbours(cell) == 3) {
                list.add(cell);
            }
        }
        return list;
    }

    private int getNumberOfLiveNeighbours(Cell that) {
        int count = 0;
        for (final Cell cell : getLiveCells()) {
            if (cell.isNeighbour(that)) {
                count++;
            }
        }
        return count;
    }

    List<Cell> getNeighbouringCells() {
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
        return !isLiveCell(cell.toString());
    }

    private Boundary getBoundaryFromString(String values) {
        final String[] indices = values.split(INDICES_DELIMITER);
        return new Boundary(Integer.parseInt(indices[0]), Integer.parseInt(indices[1]));
    }

    private Cell getCellFromString(String values) {
        final String[] indices = values.split(INDICES_DELIMITER);
        return new Cell(Integer.parseInt(indices[0].trim()), Integer.parseInt(indices[1].trim()));
    }
}