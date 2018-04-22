package refactor.algorithm;

public class Boundary extends Cell {
    Boundary(int x, int y) {
        super(x, y);
    }

    boolean isInBound(Cell that) {
        return checkBoundary(that.x, x) && checkBoundary(that.y, y);
    }

    private static boolean checkBoundary(int value, int boundary) {
        return 0 <= value && value < boundary;
    }

}

