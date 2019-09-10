package refactor.helper;

import refactor.algorithm.Refactor;

import java.util.Optional;

public class RefactorBuilder {
    public static Refactor build(String[] params, String defPath) {
        return Optional.of(getPath(params, defPath))
                .map(IOHelper::loadSeeds)
                .map(SeedHelper::getLiveCellsMap)
                .map(Refactor::new)
                .orElseThrow(() -> new RuntimeException("Invalid seed file."));
    }

    private static String getPath(String[] params, String defPath) {
        return Optional.ofNullable(params)
                .filter(p -> p.length > 0)
                .map(p -> p[0])
                .orElse(defPath);
    }
}