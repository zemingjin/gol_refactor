package refactor.algorithm;

import org.junit.Test;
import refactor.helper.SeedHelper;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class RefactorTest {
    @Test(expected = RuntimeException.class)
    public void testInit() {
        new Refactor().getLivingCells();
    }

    @Test
    public void testSeed() {
        final Refactor refactor = new Refactor(SeedHelper.getLiveCellsMap("1|1, 1|2, 1|3"));

        assertEquals(3, refactor.getLivingCells().size());
        assertEquals("[1|1, 1|2, 1|3]", refactor.getLivingCells().toString());
    }

    @Test
    public void testGetDeadCells() {
        final Refactor refactor = new Refactor(SeedHelper.getLiveCellsMap("1|0, 1|1, 1|2"));

        assertEquals(12, refactor.getNeighbouringDeadCells().count());
        assertEquals("[0|-1, 0|0, 0|1, 0|2, 0|3, 1|-1, 1|3, 2|-1, 2|0, 2|1, 2|2, 2|3]",
                     sort(refactor.getNeighbouringDeadCells().collect(Collectors.toSet())).toString());
    }

    @Test
    public void testBlinker() {
        Refactor refactor = new Refactor(SeedHelper.getLiveCellsMap("1|0, 1|1, 1|2"));

        refactor = refactor.tick();
        assertEquals("[0|1, 1|1, 2|1]", sort(refactor.getLivingCells()).toString());
        refactor = refactor.tick();
        assertEquals("[1|0, 1|1, 1|2]", sort(refactor.getLivingCells()).toString());
    }

    @Test
    public void testBloker() {
        final Refactor refactor = new Refactor(SeedHelper.getLiveCellsMap("1|1, 1|2, 2|1, 2|2"));

        assertEquals("[1|1, 1|2, 2|1, 2|2]", sort(refactor.tick().getLivingCells()).toString());
    }

    @Test
    public void testToad() {
        Refactor refactor = new Refactor(SeedHelper.getLiveCellsMap("2|2, 2|3, 3|1, 3|2, 3|3"));

        refactor = refactor.tick();
        assertEquals("[2|1, 2|3, 3|1, 3|3, 4|2]", sort(refactor.getLivingCells()).toString());
        refactor = refactor.tick();
        assertEquals("[3|1, 3|3, 4|2]", sort(refactor.getLivingCells()).toString());

    }

    @Test
    public void testBeacon() {
        Refactor refactor = new Refactor(SeedHelper.getLiveCellsMap("1|1, 1|2, 2|1, 3|4, 4|3, 4|4"));

        refactor = refactor.tick();
        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", sort(refactor.getLivingCells()).toString());
        refactor = refactor.tick();
        assertEquals("[1|1, 1|2, 2|1, 3|4, 4|3, 4|4]", sort(refactor.getLivingCells()).toString());
        refactor = refactor.tick();
        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", sort(refactor.getLivingCells()).toString());
    }

    @Test
    public void testIsLiveCell() {
        final Refactor refactor = new Refactor(SeedHelper.getLiveCellsMap("1|1, 1|2, 2|1, 3|4, 4|3, 4|4, 5|5"));

        assertTrue(refactor.isLivingCell(1, 1));
        assertTrue(refactor.isLivingCell(4, 3));
        assertFalse(refactor.isLivingCell(1, 4));
        assertTrue(refactor.isLivingCell(5, 5));
    }

    @Test
    public void testGetLeastWeight() {
        final Refactor refactor = new Refactor(SeedHelper.getLiveCellsMap("1|2, 2|1, 3|4, 4|3, 4|4, 5|5, 1|1"));

        assertEquals("1|1", refactor.findCellWithLeastWeight().toString());
    }

    private Collection<Cell> sort(Collection<Cell> list) {
        return list.stream()
                .sorted()
                .collect(Collectors.toList());
    }

}
