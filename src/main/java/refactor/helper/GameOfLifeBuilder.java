package refactor.helper;

import refactor.algorithm.Refactor;
import refactor.algorithm.Refactor;

import java.util.Optional;

public class GameOfLifeBuilder {
    public static Refactor build(String[] params, String defPath) {
        return Optional.of(getPath(params, defPath))
                .map(IOHelper::loadSeeds)
                .map(SeedHelper::getLiveCellsMap)
                .map(Refactor::new)
                .orElseThrow();
    }

    private static String getPath(String[] params, String defPath) {
        return Optional.ofNullable(params)
                .filter(p -> p.length > 0)
                .map(p -> p[0])
                .orElse(defPath);
    }
}
