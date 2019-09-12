package refactor.algorithm;

import org.junit.Test;

import java.util.Collections;

public class LivingCellsTest {

    @Test(expected = RuntimeException.class)
    public void testGetLivingCells() {
        LivingCells.setLivingCells(Collections.emptyMap());
        LivingCells.getLivingCells();
    }

}
