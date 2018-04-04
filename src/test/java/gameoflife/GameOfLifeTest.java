package gameoflife;

import org.junit.Test;

import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class GameOfLifeTest {
    private static final Cell BOUNDARY = new Cell(2, 4);
    private static final String PERF_SEEDS = "src/main/resources/sidecar_gun.seed";

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

        assertEquals(6, gameOfLife.getNeighbouringCells().size());
        assertEquals("[0|0, 0|1, 2|0, 2|1, 0|2, 2|2]", gameOfLife.getNeighbouringCells().toString());
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

    private static final int iterations = 10;

    @Test
    public void testPerformance() {
        GameOfLife gameOfLife = new GameOfLife().seed(IOHelper.loadSeeds(PERF_SEEDS));
        long time = System.currentTimeMillis();

        IntStream.range(0, iterations)
                .forEach(i -> test(gameOfLife));
        System.out.println("Finished in " + IOHelper.format(System.currentTimeMillis() - time));
    }

    private void test(GameOfLife gameOfLife) {
        gameOfLife.evolve();
        Cell boundary = gameOfLife.getDimension();
        IntStream.range(0, boundary.getY())
                .forEach(y -> testRow(gameOfLife, y, boundary.getX()));
    }

    private void testRow(GameOfLife gameOfLife, int y, int max) {
        IntStream.range(0, max)
                .forEach(x -> gameOfLife.isLiveCell(new Cell(x, y)));
    }





}
