package refactor.algorithm;

import org.junit.Test;

import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class CellTest {
    @Test
    public void testGetNeighbours() {
        assertEquals("[0|0, 1|0, 2|0, 0|1, 2|1, 0|2, 1|2, 2|2]",
                     new Cell(1, 1).getNeighbours().toString());
        assertEquals("[-1|0, 0|0, 1|0, -1|1, 1|1, -1|2, 0|2, 1|2]",
                     new Cell(0, 1).getNeighbours().toString());
        assertEquals("[-1|-1, 0|-1, 1|-1, -1|0, 1|0, -1|1, 0|1, 1|1]",
                     new Cell(0, 0).getNeighbours().toString());
    }

    @Test
    public void testEquals() {
        Cell cell = new Cell(1, 1);

        assertEquals(cell, cell);
        assertEquals(cell, new Cell(1, 1));
        assertNotEquals(cell, null);
        assertNotEquals(cell, new Cell(1, 2));
    }
}
