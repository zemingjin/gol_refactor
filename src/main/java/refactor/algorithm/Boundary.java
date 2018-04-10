package refactor.algorithm;

public class Boundary extends Cell {
    Boundary(int x, int y) {
        super(x, y);
    }

    boolean isInBound(Cell that) {
        return checkBoundary(that.getX(), getX()) && checkBoundary(that.getY(), getY());
    }

    private static boolean checkBoundary(int value, int boundary) {
        return 0 <= value && value < boundary;
    }

}

