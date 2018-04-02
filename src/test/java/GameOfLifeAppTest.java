import org.junit.Test;

import java.util.Arrays;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.Assert.*;

public class GameOfLifeAppTest {
    private static final String BEACON_FILE = "src/main/resources/beacon.seed";
    private static final String BEACON = "[1, 1, 0, 0], [1, 1, 0, 0], [0, 0, 1, 1], [0, 0, 1, 1]";


    @Test
    public void testLoadSeeds() {
        assertEquals(BEACON, toString(new GameOfLifeApp().loadSeeds(BEACON_FILE)));
    }

    private String toString(char[][] chars) {
        return Stream.of(chars)
                .map(Arrays::toString)
                .collect(Collectors.joining(", "));
    }
}
