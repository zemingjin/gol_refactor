package refactor.algorithm;

import refactor.helper.RefactorBuilder;

public class RefactorPerformance {
    private static final int ITERATIONS = 500;
    private static final String DEF_PARAMS = "src/main/resources/sidecar_gun.seed";

    public static void main(String[] params) {
        System.out.println(String.format("Testing %s in %d times...", DEF_PARAMS, ITERATIONS));

        Refactor refactor = RefactorBuilder.build(params, DEF_PARAMS);

        System.out.println("Started...");
        final long time = System.currentTimeMillis();
        for (int i = 0; i < ITERATIONS; i++) {
            refactor = refactor.tick();
        }
        System.out.println(format(System.currentTimeMillis() - time));
    }

    private static String format(long time) {
        return String.format("Finished in %tM:%tS.%tL", time, time, time);
    }
}
