package gameoflife;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class IOHelperTest {
    private static final String BEACON_FILE = "src/main/resources/beacon.seed";
    private static final String BEACON = "[#P 4|4, OO.., OO.., ..OO, ..OO]";


    @Test
    public void testLoadSeeds() {
        assertEquals(BEACON, Arrays.toString(gameoflife.IOHelper.loadSeeds(BEACON_FILE)));
    }
}
