package refactor.algorithm;

import refactor.app.RefactorUI;

public class RefactorPerformance {
    private static final int ITERATIONS = 500;

    public static void main(String[] params) {
        Refactor refactor = new RefactorUI(params).getRefactor();
        long time = System.currentTimeMillis();

        System.out.println("Started...");
        for (int i = 0; i < ITERATIONS; i++) {
            refactor = refactor.tick();
        }
        System.out.println(format(System.currentTimeMillis() - time));
    }

    private static String format(long time) {
        return String.format("Finished in %tM:%tS.%tL", time, time, time);
    }
}
