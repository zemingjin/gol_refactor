package refactor.app;

import refactor.algorithm.Cell;

public class Boundary extends Cell {
    public Boundary(int x, int y) {
        super(x, y);
    }

    boolean isInBound(Cell that) {
        return checkBoundary(that.getX(), getY()) && checkBoundary(that.getY(), getY());
    }

    private static boolean checkBoundary(int value, int boundary) {
        return 0 <= value && value < boundary;
    }

}

