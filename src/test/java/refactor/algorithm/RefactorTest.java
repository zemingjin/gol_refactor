package refactor.algorithm;

import refactor.helper.IOHelper;
import org.junit.Test;

import java.util.Collection;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class RefactorTest {
    private static final int ITERATIONS = 1;
    private static final String PERF_SEEDS = "src/main/resources/sidecar_gun.seed";
    private static final Logger LOG = Logger.getLogger(RefactorTest.class.getName());

    @Test(expected = RuntimeException.class)
    public void testInit() {
        new Refactor().getLiveCells();
    }

    @Test
    public void testSeed() {
        final Refactor gameOfLife = new Refactor().setBoundary("3|4").seedGame("1|1, 1|2, 1|3");

        assertEquals(3, gameOfLife.getLiveCells().size());
        assertEquals("[1|1, 1|2, 1|3]", gameOfLife.getLiveCells().toString());
    }

    @Test
    public void testGetDeadCells() {
        final Refactor gameOfLife = new Refactor().setBoundary("3|3").seedGame("1|0, 1|1, 1|2");

        assertEquals(6, gameOfLife.getNeighbouringDeadCells().size());
        assertEquals("[0|0, 2|0, 0|1, 2|1, 0|2, 2|2]", gameOfLife.getNeighbouringDeadCells().toString());
    }

    @Test
    public void testBlinker() {
        final Refactor gameOfLife = new Refactor().setBoundary("3|3").seedGame("1|0, 1|1, 1|2");

        assertEquals("[0|1, 1|1, 2|1]", sort(gameOfLife.evolve().values()).toString());
        assertEquals("[1|0, 1|1, 1|2]", sort(gameOfLife.evolve().values()).toString());
    }

    @Test
    public void testBloker() {
        final Refactor gameOfLife = new Refactor().setBoundary("3|3").seedGame("1|1, 1|2, 2|1, 2|2");

        assertEquals("[1|1, 1|2, 2|1, 2|2]", sort(gameOfLife.evolve().values()).toString());
    }

    @Test
    public void testToad() {
        final Refactor gameOfLife = new Refactor().setBoundary("4|4").seedGame("2|2, 2|3, 3|1, 3|2, 3|3");

        assertEquals("[2|1, 2|3, 3|1, 3|3]", sort(gameOfLife.evolve().values()).toString());
        assertEquals("[]", gameOfLife.evolve().values().toString());
    }

    @Test
    public void testBeacon() {
        final Refactor gameOfLife = new Refactor().setBoundary("5|5").seedGame("1|1, 1|2, 2|1, 3|4, 4|3, 4|4");

        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", sort(gameOfLife.evolve().values()).toString());
        assertEquals("[1|1, 1|2, 2|1, 3|4, 4|3, 4|4]", sort(gameOfLife.evolve().values()).toString());
        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]",
                     sort(gameOfLife.evolve().values()).toString());
    }

    @Test
    public void testGetMaxIndex() {
        assertEquals(new Cell(5, 5), new Refactor()
                .setBoundary("5|5").seedGame("1|1, 1|2, 2|1, 3|4, 4|3, 4|4, 8|8").getBoundary());
    }

    @Test
    public void testIsLiveCell() {
        final Refactor gameOfLife = new Refactor().setBoundary("5|5").seedGame("1|1, 1|2, 2|1, 3|4, 4|3, 4|4, 5|5");

        assertTrue(gameOfLife.isLiveCell(1, 1));
        assertTrue(gameOfLife.isLiveCell(4, 3));
        assertFalse(gameOfLife.isLiveCell(1, 4));
        assertFalse(gameOfLife.isLiveCell(5, 5));
    }

    @Test
    public void testGetLeastWeight() {
        final Refactor refactor = new Refactor().setBoundary("5|5").seedGame("1|2, 2|1, 3|4, 4|3, 4|4, 5|5, 1|1");

        assertEquals("1|1", refactor.findCellWithLeastWeight().toString());
    }

    @Test
    public void testPerformance() {
        final Refactor gameOfLife = new Refactor().seedGame(IOHelper.loadSeeds(PERF_SEEDS));
        final long time = System.currentTimeMillis();

        IntStream.range(0, ITERATIONS)
                .forEach(i -> test(gameOfLife));
        LOG.info("Finished in " + IOHelper.format(System.currentTimeMillis() - time));
    }

    private void test(Refactor gameOfLife) {
        gameOfLife.evolve();
        final Cell boundary = gameOfLife.getBoundary();
        IntStream.range(0, boundary.getY())
                .forEach(y -> testRow(gameOfLife, y, boundary.getX()));
    }

    private void testRow(Refactor gameOfLife, int y, int max) {
        IntStream.range(0, max)
                .forEach(x -> gameOfLife.isLiveCell(x, y));
    }

    private Collection<Cell> sort(Collection<Cell> list) {
        return list.stream()
                .sorted()
                .collect(Collectors.toList());
    }

}
