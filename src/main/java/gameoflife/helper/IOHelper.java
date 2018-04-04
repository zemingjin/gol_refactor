package gameoflife.helper;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public final class IOHelper {
    public static String[] loadSeeds(String path) {
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            return reader.lines()
                    .toArray(String[]::new);
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    public static String format(final long time) {
        return String.format("%tM:%tS.%tL", time, time, time);
    }

    private IOHelper() {
    }

}
