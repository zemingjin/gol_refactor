package refactor.algorithm;

import org.junit.Test;
import refactor.helper.SeedHelper;

import java.util.Collection;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class RefactorTest {
    @Test(expected = RuntimeException.class)
    public void testException() {
        new Refactor();
        LivingCells.getLivingCells();
    }

    @Test
    public void testSeed() {
        new Refactor(SeedHelper.getLiveCellsMap("1|1, 1|2, 1|3"));

        assertEquals(3, LivingCells.getLivingCells().size());
        assertEquals("[1|1, 1|2, 1|3]", LivingCells.getLivingCells().toString());
    }

    @Test
    public void testGetDeadCells() {
        final Refactor refactor = new Refactor(SeedHelper.getLiveCellsMap("1|0, 1|1, 1|2"));

        assertEquals(12, refactor.getNeighbouringDeadCells().size());
        assertEquals("[0|-1, 0|0, 0|1, 0|2, 0|3, 1|-1, 1|3, 2|-1, 2|0, 2|1, 2|2, 2|3]",
                     sort(refactor.getNeighbouringDeadCells()).toString());
    }

    @Test
    public void testBlinker() {
        Refactor refactor = new Refactor(SeedHelper.getLiveCellsMap("1|0, 1|1, 1|2"));

        refactor = refactor.tick();
        assertEquals("[0|1, 1|1, 2|1]", sort(LivingCells.getLivingCells()).toString());
        refactor.tick();
        assertEquals("[1|0, 1|1, 1|2]", sort(LivingCells.getLivingCells()).toString());
    }

    @Test
    public void testBloker() {
        final Refactor refactor = new Refactor(SeedHelper.getLiveCellsMap("1|1, 1|2, 2|1, 2|2"));

        refactor.tick();
        assertEquals("[1|1, 1|2, 2|1, 2|2]", sort(LivingCells.getLivingCells()).toString());
    }

    @Test
    public void testToad() {
        Refactor refactor = new Refactor(SeedHelper.getLiveCellsMap("2|2, 2|3, 3|1, 3|2, 3|3"));

        refactor = refactor.tick();
        assertEquals("[2|1, 2|3, 3|1, 3|3, 4|2]", sort(LivingCells.getLivingCells()).toString());
        refactor.tick();
        assertEquals("[3|1, 3|3, 4|2]", sort(LivingCells.getLivingCells()).toString());

    }

    @Test
    public void testBeacon() {
        Refactor refactor = new Refactor(SeedHelper.getLiveCellsMap("1|1, 1|2, 2|1, 3|4, 4|3, 4|4"));

        refactor = refactor.tick();
        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", sort(LivingCells.getLivingCells()).toString());
        refactor = refactor.tick();
        assertEquals("[1|1, 1|2, 2|1, 3|4, 4|3, 4|4]", sort(LivingCells.getLivingCells()).toString());
        refactor.tick();
        assertEquals("[1|1, 1|2, 2|1, 2|2, 3|3, 3|4, 4|3, 4|4]", sort(LivingCells.getLivingCells()).toString());
    }

    @Test
    public void testIsLiveCell() {
        new Refactor(SeedHelper.getLiveCellsMap("1|1, 1|2, 2|1, 3|4, 4|3, 4|4, 5|5"));

        assertTrue(LivingCells.isLivingCell(1, 1));
        assertTrue(LivingCells.isLivingCell(4, 3));
        assertFalse(LivingCells.isLivingCell(1, 4));
        assertTrue(LivingCells.isLivingCell(5, 5));
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
