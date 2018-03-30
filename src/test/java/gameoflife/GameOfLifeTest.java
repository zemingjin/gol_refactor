package gameoflife;

import org.junit.Test;

import static org.junit.Assert.*;

public class GameOfLifeTest {
    @Test
    public void testInitialized() {
        GameOfLife gameOfLife = new GameOfLife();
        assertTrue(gameOfLife.getLiveCells().isEmpty());
    }

    @Test
    public void testSeed() {
        GameOfLife gameOfLife = new GameOfLife().seed("1|1, 1|2, 1|3");

        assertEquals(3, gameOfLife.getLiveCells().size());
        assertEquals("[1|1, 1|2, 1|3]", gameOfLife.getLiveCells().toString());
    }

    @Test
    public void testGetDeadCells() {
        GameOfLife gameOfLife = new GameOfLife().seed("1|1, 1|2, 1|3");

        assertEquals(12, gameOfLife.getDeadCells().size());
        assertEquals("[0|0, 0|1, 0|2, 1|0, 2|0, 2|1, 2|2, 0|3, 2|3, 0|4, 1|4, 2|4]",
                     gameOfLife.getDeadCells().toString());
    }

    @Test
    public void testBlinker() {
        GameOfLife gameOfLife = new GameOfLife().seed("1|1, 1|2, 1|3");

        assertEquals("[0|2, 1|2, 2|2]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
        assertEquals("[1|1, 1|2, 1|3]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
        assertEquals("[0|2, 1|2, 2|2]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
    }

    @Test
    public void testBloker() {
        GameOfLife gameOfLife = new GameOfLife().seed("1|1, 1|2, 2|1, 2|2");

        assertEquals("[1|1, 1|2, 2|1, 2|2]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
    }

    @Test
    public void testToad() {
        GameOfLife gameOfLife = new GameOfLife().seed("2|2, 2|3, 2|4, 3|1, 3|2, 3|3");

        assertEquals("[1|3, 2|1, 2|4, 3|1, 3|4, 4|2]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
        assertEquals("[2|2, 2|3, 2|4, 3|1, 3|2, 3|3]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
        assertEquals("[1|3, 2|1, 2|4, 3|1, 3|4, 4|2]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
    }

    @Test
    public void testBeacon() {
        GameOfLife gameOfLife = new GameOfLife().seed("1|1, 1|2, 2|1, 3|4, 4|3, 4|4");

        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]",
                     gameOfLife.setLiveCells(gameOfLife.tick()).toString());
        assertEquals("[1|1, 1|2, 2|1, 3|4, 4|3, 4|4]", gameOfLife.setLiveCells(gameOfLife.tick()).toString());
        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]",
                     gameOfLife.setLiveCells(gameOfLife.tick()).toString());
    }

}
