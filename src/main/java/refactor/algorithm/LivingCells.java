package refactor.algorithm;

import java.util.Collection;
import java.util.Map;

public class LivingCells {
    private static Map<String, Cell> livingCells;

    public static boolean isLivingCell(int x, int y) {
        return isLivingCell(Cell.toString(x, y));
    }

    static boolean isLivingCell(String key) {
        return livingCells.get(key) != null;
    }

    public static Collection<Cell> getLivingCells() {
        if (livingCells.isEmpty()) {
            throw new RuntimeException("No more living cells");
        } else {
            return livingCells.values();
        }
    }

    static void setLivingCells(Map<String, Cell> livingCells) {
        LivingCells.livingCells = livingCells;
    }
}