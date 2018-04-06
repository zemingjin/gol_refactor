package gameoflife.algorithm;

public class Boundary extends Cell {
    Boundary(int x, int y) {
        super(x, y);
    }

    boolean isInBound(Cell that) {
        return isInBound(getX(), that.getX()) && isInBound(getY(), that.getY());
    }

    private static boolean isInBound(int limit, int value) {
        return 0 <= value && value < limit;
    }

}
