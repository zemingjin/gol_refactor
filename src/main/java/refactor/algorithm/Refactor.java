package refactor.algorithm;

import org.apache.commons.collections4.ListUtils;

import java.util.*;

public class Refactor {
    private final Map<String, Cell> livingCells;

    Refactor() {
        this.livingCells = Collections.emptyMap();
    }

    public Refactor(Map<String, Cell> livingCells) {
        this.livingCells = livingCells;
    }

    Cell findCellWithLeastWeight() {
        Cell min = null;
        for (Cell cell : getLivingCells()) {
            if (min == null || min.getWeight() > cell.getWeight()) {
                min = cell;
            }
        }
        return min;
    }

    public boolean isLivingCell(int x, int y) {
        return isLivingCell(Cell.toString(x, y));
    }

    private boolean isLivingCell(String key) {
        return livingCells.get(key) != null;
    }

    Collection<Cell> getLivingCells() {
        if (livingCells.isEmpty()) {
            throw new RuntimeException("No more living cells");
        }
        return livingCells.values();
    }

    public Refactor tick() {
        final Map<String, Cell> map = new HashMap<>();

        for (final Cell cell : ListUtils.union(getNextGenerationCells(), getReproductionCells())) {
            map.put(cell.toString(), cell);
        }
        return new Refactor(map);
    }

    private List<Cell> getNextGenerationCells() {
        final List<Cell> list = new ArrayList<>();

        for (final Cell cell : getLivingCells()) {
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
        return numberOfNeighbours == 2 || numberOfNeighbours == 3;
    }

    private boolean isReproducingCell(Cell cell) {
        return getNumberOfLiveNeighbours(cell) == 3;
    }

    private long getNumberOfLiveNeighbours(Cell that) {
        long count = 0;
        for (final Cell cell : that.getNeighbours()) {
            if (isLivingCell(cell.string)) {
                count++;
            }
        }
        return count;
    }

    Set<Cell> getNeighbouringDeadCells() {
        final Set<Cell> set = new LinkedHashSet<>();

        for (final Cell cell : getLivingCells()) {
            for (final Cell neighbour : cell.getNeighbours()) {
                if (isDeadCell(neighbour)) {
                    set.add(neighbour);
                }
            }
        }
        return set;
    }

    private boolean isDeadCell(Cell cell) {
        return !isLivingCell(cell.string);
    }
}