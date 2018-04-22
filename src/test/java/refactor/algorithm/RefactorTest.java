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
        final Refactor refactor = new Refactor().setBoundary("3|4").seedGame("1|1, 1|2, 1|3");

        assertEquals(3, refactor.getLiveCells().size());
        assertEquals("[1|1, 1|2, 1|3]", refactor.getLiveCells().toString());
    }

    @Test
    public void testGetDeadCells() {
        final Refactor refactor = new Refactor().setBoundary("3|3").seedGame("1|0, 1|1, 1|2");

        assertEquals(12, refactor.getNeighbouringDeadCells().size());
        assertEquals("[0|-1, 1|-1, 2|-1, 0|0, 2|0, 0|1, 2|1, 0|2, 2|2, 0|3, 1|3, 2|3]",
                     refactor.getNeighbouringDeadCells().toString());
    }

    @Test
    public void testBlinker() {
        Refactor refactor = new Refactor().setBoundary("3|3").seedGame("1|0, 1|1, 1|2");

        refactor = refactor.tick();
        assertEquals("[0|1, 1|1, 2|1]", sort(refactor.getLiveCells()).toString());
        refactor = refactor.tick();
        assertEquals("[1|0, 1|1, 1|2]", sort(refactor.getLiveCells()).toString());
    }

    @Test
    public void testBloker() {
        final Refactor refactor = new Refactor().setBoundary("3|3").seedGame("1|1, 1|2, 2|1, 2|2");

        assertEquals("[1|1, 1|2, 2|1, 2|2]", sort(refactor.tick().getLiveCells()).toString());
    }

    @Test
    public void testToad() {
        Refactor refactor = new Refactor().setBoundary("4|4").seedGame("2|2, 2|3, 3|1, 3|2, 3|3");

        refactor = refactor.tick();
        assertEquals("[2|1, 2|3, 3|1, 3|3, 4|2]", sort(refactor.getLiveCells()).toString());
        refactor = refactor.tick();
        assertEquals("[3|1, 3|3, 4|2]", sort(refactor.getLiveCells()).toString());

    }

    @Test
    public void testBeacon() {
        Refactor refactor = new Refactor().setBoundary("5|5").seedGame("1|1, 1|2, 2|1, 3|4, 4|3, 4|4");

        refactor = refactor.tick();
        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", sort(refactor.getLiveCells()).toString());
        refactor = refactor.tick();
        assertEquals("[1|1, 1|2, 2|1, 3|4, 4|3, 4|4]", sort(refactor.getLiveCells()).toString());
        refactor = refactor.tick();
        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", sort(refactor.getLiveCells()).toString());
    }

    @Test
    public void testGetMaxIndex() {
        assertEquals(new Cell(5, 5), new Refactor()
                .setBoundary("5|5").seedGame("1|1, 1|2, 2|1, 3|4, 4|3, 4|4, 8|8").getBoundary());
    }

    @Test
    public void testIsLiveCell() {
        final Refactor refactor = new Refactor().setBoundary("5|5").seedGame("1|1, 1|2, 2|1, 3|4, 4|3, 4|4, 5|5");

        assertTrue(refactor.isLiveCell(1, 1));
        assertTrue(refactor.isLiveCell(4, 3));
        assertFalse(refactor.isLiveCell(1, 4));
        assertTrue(refactor.isLiveCell(5, 5));
    }

    @Test
    public void testGetLeastWeight() {
        final Refactor refactor = new Refactor().setBoundary("5|5").seedGame("1|2, 2|1, 3|4, 4|3, 4|4, 5|5, 1|1");

        assertEquals("1|1", refactor.findCellWithLeastWeight().toString());
    }

    @Test
    public void testPerformance() {
        final Refactor refactor = new Refactor().seedGame(IOHelper.loadSeeds(PERF_SEEDS));
        final long time = System.currentTimeMillis();

        IntStream.range(0, ITERATIONS)
                .forEach(i -> test(refactor));
        LOG.info("Finished in " + IOHelper.format(System.currentTimeMillis() - time));
    }

    private void test(Refactor input) {
        final Refactor refactor = input.tick();
        final Cell boundary = refactor.getBoundary();
        IntStream.range(0, boundary.y)
                .forEach(y -> testRow(refactor, y, boundary.x));
    }

    private void testRow(Refactor refactor, int y, int max) {
        IntStream.range(0, max)
                .forEach(x -> refactor.isLiveCell(x, y));
    }

    private Collection<Cell> sort(Collection<Cell> list) {
        return list.stream()
                .sorted()
                .collect(Collectors.toList());
    }

}
