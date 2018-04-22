package refactor.app;

import refactor.helper.IOHelper;
import refactor.helper.SeedHelper;

import java.util.Arrays;

public class Configurations {
    private static final String OPT_STEP = "-s";
    private static final int WAIT_TIME = 100;
    private static final String OPT_WAIT = "-w";

    private final String filePath;
    private final String[] seeds;
    private final Boundary boundary;
    private final boolean automaton;
    private final int waitTime;

    public Configurations(String[] params) {
        if (params.length == 0) {
            throw new RuntimeException("Missing seeds");
        }
        filePath = params[0];
        String[] contents = IOHelper.loadSeeds(filePath);
        seeds = Arrays.copyOfRange(contents, 1, contents.length);
        boundary = SeedHelper.getBoundaryFromString(contents[0].split(" ")[1]);
        automaton = isAutomaton(params);
        waitTime = getWaitTime(params);
    }

    private boolean isAutomaton(String[] params) {
        return !Arrays.asList(params).contains(OPT_STEP);
    }

    private int getWaitTime(String[] params) {
        for (final String param : params) {
            if (param.startsWith(OPT_WAIT)) {
                return Integer.parseInt(param.substring(OPT_WAIT.length()));
            }
        }
        return WAIT_TIME;
    }

    public Boundary getBoundary() {
        return boundary;
    }

    public String[] getSeeds() {
        return seeds;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getWaitTime() {
        return waitTime;
    }

    public boolean isAutomaton() {
        return automaton;
    }
}
