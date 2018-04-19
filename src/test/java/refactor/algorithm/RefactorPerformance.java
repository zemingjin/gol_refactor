package refactor.algorithm;

import refactor.app.RefactorUI;

import java.util.logging.Logger;

public class RefactorPerformance {
    private static final int ITERATIONS = 500;
    private static final Logger LOG = Logger.getLogger(RefactorPerformance.class.getName());

    public static void main(String[] params) {
        Refactor refactor = new RefactorUI(params).getRefactor();
        final long time = System.currentTimeMillis();

        LOG.info("Started...");
        for (int i = 0; i < ITERATIONS; i++) {
            refactor = refactor.tick();
        }
        LOG.info(format(System.currentTimeMillis() - time));
    }

    private static String format(long time) {
        return String.format("Finished in %tM:%tS.%tL", time, time, time);
    }
}
