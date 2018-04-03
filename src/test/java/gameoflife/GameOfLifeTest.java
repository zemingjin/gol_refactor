package gameoflife;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameOfLifeTest {
    private static final Cell BOUNDARY = new Cell(2, 4);
    @Test(expected = RuntimeException.class)
    public void testInit() {
        new GameOfLife().getLiveCells();
    }

    @Test
    public void testSeed() {
        GameOfLife gameOfLife = new GameOfLife().seed("1|1, 1|2, 1|3", BOUNDARY);

        assertEquals(3, gameOfLife.getLiveCells().size());
        assertEquals("[1|1, 1|2, 1|3]", gameOfLife.getLiveCells().toString());
    }

    @Test
    public void testGetDeadCells() {
        GameOfLife gameOfLife = new GameOfLife().seed("1|0, 1|1, 1|2", new Cell(3, 3));

        assertEquals(6, gameOfLife.getDeadCells().size());
        assertEquals("[0|0, 0|1, 2|0, 2|1, 0|2, 2|2]", gameOfLife.getDeadCells().toString());
    }

    @Test
    public void testBlinker() {
        GameOfLife gameOfLife = new GameOfLife().seed("1|0, 1|1, 1|2", new Cell(3, 3));

        assertEquals("[0|1, 1|1, 2|1]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
        assertEquals("[1|0, 1|1, 1|2]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
    }

    @Test
    public void testBloker() {
        GameOfLife gameOfLife = new GameOfLife().seed("1|1, 1|2, 2|1, 2|2", BOUNDARY);

        assertEquals("[1|1, 1|2, 2|1, 2|2]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
    }

    @Test
    public void testToad() {
        GameOfLife gameOfLife = new GameOfLife().seed("2|2, 2|3, 2|4, 3|1, 3|2, 3|3", new Cell(4, 4));

        assertEquals("[1|3, 2|1, 2|4, 3|1]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
        assertEquals("[2|2]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
        assertEquals("[]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
    }

    @Test
    public void testBeacon() {
        GameOfLife gameOfLife = new GameOfLife().seed("1|1, 1|2, 2|1, 3|4, 4|3, 4|4", new Cell(5, 5));

        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]",
                     gameOfLife.setLiveCells(gameOfLife.tick()).toString());
        assertEquals("[1|1, 1|2, 2|1, 3|4, 4|3, 4|4]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]",
                     gameOfLife.setLiveCells(gameOfLife.tick()).toString());
    }

    @Test
    public void testGetMaxIndex() {
        assertEquals(new Cell(5, 5), new GameOfLife()
                .seed("1|1, 1|2, 2|1, 3|4, 4|3, 4|4", new Cell(5, 5)).getDimension());
    }

    @Test
    public void testIsLiveCell() {
        GameOfLife gameOfLife = new GameOfLife().seed("1|1, 1|2, 2|1, 3|4, 4|3, 4|4", new Cell(5, 5));

        assertTrue(gameOfLife.isLiveCell(new Cell(1, 1)));
        assertTrue(gameOfLife.isLiveCell(new Cell(4, 3)));
        assertFalse(gameOfLife.isLiveCell(new Cell(1, 4)));
    }

}
