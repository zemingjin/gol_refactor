package gameoflife.app;

import gameoflife.helper.IOHelper;

import java.util.logging.Logger;

public class GameOfLifeUITest {
    private static final int ITERATIONS = 20;
    private static final Logger LOG = Logger.getLogger(GameOfLifeUITest.class.getName());

    static class Test extends GameOfLifeUI {
        private int iterations = ITERATIONS;

        Test(String[] params) {
            super(params);
        }

        @Override
        public boolean isContinueFlag() {
            return iterations-- >= 0;
        }
    }

    public static void main(String[] params) {
        long time = System.currentTimeMillis();
        new Test(params).run();
        LOG.info("Total time: " + IOHelper.format(System.currentTimeMillis() - time));
    }
}
