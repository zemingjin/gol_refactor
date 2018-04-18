package refactor.app;

import refactor.helper.IOHelper;

import java.util.logging.Logger;

class RefactorUITest extends RefactorUI {
    private static final int ITERATIONS = 200;
    private static final Logger LOG = Logger.getLogger(RefactorUITest.class.getName());

    private int iterations = ITERATIONS;

    private RefactorUITest(String[] params) {
        super(params);
    }

    @Override
    public boolean isContinueFlag() {
        return iterations-- >= 0;
    }

    public static void main(String[] params) {
        final long time = System.currentTimeMillis();
        new RefactorUITest(params).run();
        LOG.info("Total time: " + IOHelper.format(System.currentTimeMillis() - time));
    }
}
