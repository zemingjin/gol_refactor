package refactor.algorithm;

import org.apache.commons.collections4.ListUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Refactor {
    private Map<String, Cell> liveCells;

    Refactor() {
    }

    public Refactor(Map<String, Cell> liveCells) {
        setLiveCells(liveCells);
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

    private void setLiveCells(Map<String, Cell> liveCells) {
        this.liveCells = liveCells;
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
}