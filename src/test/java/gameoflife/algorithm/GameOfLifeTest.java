package gameoflife.algorithm;

import gameoflife.helper.IOHelper;
import org.junit.Test;

import java.util.logging.Logger;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class GameOfLifeTest {
    private static final int ITERATIONS = 1;
    private static final String PERF_SEEDS = "src/main/resources/sidecar_gun.seed";
    private static final Logger LOG = Logger.getLogger(GameOfLifeTest.class.getName());

    @Test(expected = RuntimeException.class)
    public void testInit() {
        new GameOfLife().getLiveCells();
    }

    @Test
    public void testSeed() {
        final GameOfLife gameOfLife = new GameOfLife().setBoundary("3|4").convertSeeds("1|1, 1|2, 1|3");

        assertEquals(3, gameOfLife.getLiveCells().size());
        assertEquals("[1|1, 1|2, 1|3]", gameOfLife.getLiveCells().toString());
    }

    @Test
    public void testGetDeadCells() {
        final GameOfLife gameOfLife = new GameOfLife().setBoundary("3|3").convertSeeds("1|0, 1|1, 1|2");

        assertEquals(6, gameOfLife.getNeighbouringCells().size());
        assertEquals("[0|0, 2|0, 0|1, 2|1, 0|2, 2|2]", gameOfLife.getNeighbouringCells().toString());
    }

    @Test
    public void testBlinker() {
        final GameOfLife gameOfLife = new GameOfLife().setBoundary("3|3").convertSeeds("1|0, 1|1, 1|2");

        assertEquals("[0|1, 1|1, 2|1]", gameOfLife.evolve().toString());
        assertEquals("[1|0, 1|1, 1|2]", gameOfLife.evolve().toString());
    }

    @Test
    public void testBloker() {
        final GameOfLife gameOfLife = new GameOfLife().setBoundary("3|3").convertSeeds("1|1, 1|2, 2|1, 2|2");

        assertEquals("[1|1, 1|2, 2|1, 2|2]", gameOfLife.evolve().toString());
    }

    @Test
    public void testToad() {
        final GameOfLife gameOfLife = new GameOfLife().setBoundary("4|4").convertSeeds("2|2, 2|3, 2|4, 3|1, 3|2, 3|3");

        assertEquals("[1|3, 2|1, 2|4, 3|1]", gameOfLife.evolve().toString());
        assertEquals("[2|2]", gameOfLife.evolve().toString());
        assertEquals("[]", gameOfLife.evolve().toString());
    }

    @Test
    public void testBeacon() {
        final GameOfLife gameOfLife = new GameOfLife().setBoundary("5|5").convertSeeds("1|1, 1|2, 2|1, 3|4, 4|3, 4|4");

        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", gameOfLife.evolve().toString());
        assertEquals("[1|1, 1|2, 2|1, 3|4, 4|3, 4|4]", gameOfLife.evolve().toString());
        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", gameOfLife.evolve().toString());
    }

    @Test
    public void testGetMaxIndex() {
        assertEquals(new Cell(5, 5), new GameOfLife()
                .setBoundary("5|5").convertSeeds("1|1, 1|2, 2|1, 3|4, 4|3, 4|4").getDimension());
    }

    @Test
    public void testIsLiveCell() {
        final GameOfLife gameOfLife = new GameOfLife().setBoundary("5|5").convertSeeds("1|1, 1|2, 2|1, 3|4, 4|3, 4|4");

        assertTrue(gameOfLife.isLiveCell(1, 1));
        assertTrue(gameOfLife.isLiveCell(4, 3));
        assertFalse(gameOfLife.isLiveCell(1, 4));
    }

    @Test
    public void testPerformance() {
        final GameOfLife gameOfLife = new GameOfLife().convertSeeds(IOHelper.loadSeeds(PERF_SEEDS));
        final long time = System.currentTimeMillis();

        IntStream.range(0, ITERATIONS)
                .forEach(i -> test(gameOfLife));
        LOG.info("Finished in " + IOHelper.format(System.currentTimeMillis() - time));
    }

    private void test(GameOfLife gameOfLife) {
        gameOfLife.evolve();
        final Cell boundary = gameOfLife.getDimension();
        IntStream.range(0, boundary.getY())
                .forEach(y -> testRow(gameOfLife, y, boundary.getX()));
    }

    private void testRow(GameOfLife gameOfLife, int y, int max) {
        IntStream.range(0, max)
                .forEach(x -> gameOfLife.isLiveCell(x, y));
    }

}
