package refactor.helper;

import refactor.app.Boundary;
import refactor.algorithm.Cell;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class SeedHelper {
    private static final char LIVE_CELL = 'O';
    private static final String INDICES_DELIMITER = "\\|";
    private static final String CELL_DELIMITER = ", ";

    private static boolean isLiveCell(char c) {
        return SeedHelper.LIVE_CELL == c;
    }

    public static Boundary getBoundaryFromString(String values) {
        final String[] indices = values.split(INDICES_DELIMITER);
        return new Boundary(Integer.parseInt(indices[0].trim()), Integer.parseInt(indices[1].trim()));
    }

    private static Cell getCellFromString(String values) {
        final String[] indices = values.split(INDICES_DELIMITER);
        return new Cell(Integer.parseInt(indices[0].trim()), Integer.parseInt(indices[1].trim()));
    }

    /**
     * This method is used only by tests
     * @param seeds the given seeds in the format of "1|1, 1|2, 1|3"
     * @return self
     */
    public static Map<String, Cell> getLiveCellsMap(String seeds) {
        if (Objects.isNull(seeds) || seeds.isEmpty()) {
            throw new RuntimeException(String.format("Invalid seeds: '%s'", seeds));
        }
        final Map<String, Cell> map = new HashMap<>();

        for (final String elements : seeds.split(CELL_DELIMITER)) {
            final Cell cell = getCellFromString(elements);
            map.put(cell.toString(), cell);
        }
        return map;
    }

    public static Map<String, Cell> getLiveCellsMap(String[] seeds) {
        final Map<String, Cell> map = new HashMap<>();

        for (int y = 0; y < seeds.length; y++) {
            for (final Cell cell : getRowOfCells(y, seeds[y])) {
                map.put(cell.toString(), cell);
            }
        }
        return map;
    }

    private static List<Cell> getRowOfCells(int y, String line) {
        final List<Cell> list = new ArrayList<>();

        for (int x = 0; x < line.length(); x++) {
            if (isLiveCell(line.charAt(x))) {
                list.add(new Cell(x, y));
            }
        }
        return list;
    }


}
