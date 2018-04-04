package gameoflife;

public class Boundary extends Cell {
    Boundary(int x, int y) {
        super(x, y);
    }

    boolean isInBound(Cell that) {
        return 0 <= that.getX() && that.getX() < getX() && 0 <= that.getY() && that.getY() < getY();
    }

}
