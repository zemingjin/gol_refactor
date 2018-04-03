package gameoflife;

import org.junit.Test;

import static org.junit.Assert.*;

public class CellTest {
    private static final Cell BOUNDARY = new Cell(100, 100);
    @Test
    public void testIsNeighbour() {
        assertTrue(new Cell(1, 1).isNeighbour(new Cell(2, 2)));
        assertFalse(new Cell(1, 1).isNeighbour(new Cell(1, 3)));
        assertFalse(new Cell(1, 1).isNeighbour(new Cell(1, 1)));
    }

    @Test
    public void testGetNeighbours() {
        assertEquals("[0|0, 0|1, 0|2, 1|0, 1|2, 2|0, 2|1, 2|2]",
                     new Cell(1, 1, BOUNDARY).getNeighbours().toString());
        assertEquals("[0|0, 0|2, 1|0, 1|1, 1|2]",
                     new Cell(0, 1, BOUNDARY).getNeighbours().toString());
        assertEquals("[0|1, 1|0, 1|1]",
                     new Cell(0, 0, BOUNDARY).getNeighbours().toString());
    }
}
