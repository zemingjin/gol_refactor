package refactor.app;

import refactor.algorithm.Cell;
import refactor.algorithm.LivingCells;
import refactor.algorithm.Refactor;
import refactor.helper.SeedHelper;

import javax.swing.WindowConstants;
import javax.swing.JComponent;
import javax.swing.JFrame;
import java.awt.KeyboardFocusManager;
import java.awt.event.KeyEvent;
import java.awt.KeyEventPostProcessor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Insets;
import java.awt.Color;
import java.util.function.Function;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RefactorUI extends JComponent implements KeyEventPostProcessor {
    private static final int MAX_CELL_SIZE = 100;
    private static final int MIN_CELL_SIZE = 6;
    private static final Logger LOG = Logger.getLogger(RefactorUI.class.getName());

    private Refactor refactor;
    private final JFrame window = new JFrame();
    private int cellSize = MAX_CELL_SIZE;
    private boolean continueFlag = true;
    private int evolveToggle = 1;
    private boolean automaton;
    private String path;
    private Boundary boundary;
    private int iteration;
    private int waitTime;

    public RefactorUI(String[] params) {
        Configurations configurations = new Configurations(params);

        path = configurations.getFilePath();
        refactor= new Refactor(SeedHelper.getLiveCellsMap(configurations.getSeeds()));
        boundary = configurations.getBoundary();
        automaton = configurations.isAutomaton();
        waitTime = configurations.getWaitTime();
    }

    public Refactor getRefactor() {
        return refactor;
    }

    public boolean isContinueFlag() {
        return continueFlag;
    }

    void run() {
        setupKeyboardListener();
        setupFrame();

        while (isContinueFlag()) {
            repaint();
            waitAWhile();
            if (isContinueToEvolve()) {
                evolve();
            }
        }

        close();
    }

    private boolean isContinueToEvolve() {
        return automaton || evolveToggle == 0;
    }

    private void close() {
        window.setVisible(false);
        window.dispose();
    }

    private void setupKeyboardListener() {
        KeyboardFocusManager.getCurrentKeyboardFocusManager()
                .addKeyEventPostProcessor(this);
    }

    private void setupFrame() {
        setCellSize(calculateCellSize());

        final int width = calculatePanelSize(boundary.x);
        final int height = calculatePanelSize(boundary.y);

        setSize(width, height);
        setFocusable(true);
        window.setTitle(getTitle(path));
        window.setResizable(false);
        window.setVisible(true);
        window.setBounds(getHorizontalPosition(width), getVerticalPosition(height),
                         getFrameWidth(width), getFrameHeight(height));
        window.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        window.getContentPane().add(this);
    }

    private int calculateCellSize() {
        final Dimension screenSize = getScreenSize();
        return Math.max(Math.min(Math.min(calculateCellSize(screenSize.height, boundary.y),
                                          calculateCellSize(screenSize.width, boundary.x)),
                                 MAX_CELL_SIZE),
                        MIN_CELL_SIZE);
    }

    private int calculateCellSize(int screenSize, int numberOfCells) {
        return screenSize * 9 / 10 / numberOfCells;
    }

    private int calculatePanelSize(int position) {
        return position * cellSize;
    }

    private int getHorizontalPosition(int panelWidth) {
        return calculatePosition(getScreenSize().width, getFrameWidth(panelWidth));
    }

    private int getVerticalPosition(int panelHeight) {
        return calculatePosition(getScreenSize().height, getFrameHeight(panelHeight));
    }

    private int calculatePosition(int screenSize, int frameSize) {
        return (screenSize - frameSize) / 2;
    }

    private Dimension getScreenSize() {
        return Toolkit.getDefaultToolkit().getScreenSize();
    }

    private int getFrameWidth(int panelWidth) {
        return panelWidth + calculateInsertsValue(insets -> insets.left + insets.right);
    }

    private int getFrameHeight(int panelHeight) {
        return panelHeight + calculateInsertsValue(insets -> insets.top + insets.bottom);
    }

    private int calculateInsertsValue(Function<Insets, Integer> getter) {
        return getter.apply(window.getInsets());
    }

    private void evolve() {
        refactor = refactor.tick();
        evolveToggle++;
        window.setTitle(String.format("%s - #%d", path, iteration));
        iteration++;
    }

    @Override
    public void paint(Graphics graphics) {
        for (int y = 0; y < boundary.y; y++) {
            paintRow(graphics, y);
        }
    }

    private void paintRow(Graphics graphics, int y) {
        for (int x = 0; x < boundary.x; x++) {
            paintCell(graphics, x, y);
        }
    }

    private void paintCell(Graphics graphics, int x, int y) {
        fillCell(graphics, x, y);
        drawBorder(graphics, x, y);
    }

    private void fillCell(Graphics graphics, int x, int y) {
        graphics.setColor(getColor(x, y));
        graphics.fillRect(getFillPosition(x), getFillPosition(y), getFillSize(), getFillSize());
    }

    private void drawBorder(Graphics graphics, int x, int y) {
        graphics.setColor(getForeground());
        graphics.drawRect(getCellPosition(x), getCellPosition(y), cellSize, cellSize);
    }

    private Color getColor(int x, int y) {
        return LivingCells.isLivingCell(x, y) ? getForeground() : getBackground();
    }

    private int getFillPosition(int index) {
        return getCellPosition(index) + 1;
    }

    private int getFillSize() {
        return cellSize - 2;
    }

    private int getCellPosition(int index) {
        return index * cellSize;
    }

    private void waitAWhile() {
        if (waitTime > 0) {
            try {
                synchronized (this) {
                    wait(waitTime);
                }
            } catch (InterruptedException ex) {
                LOG.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
    }

    private void setCellSize(int value) {
        this.cellSize = value;
    }

    @Override
    public boolean postProcessKeyEvent(KeyEvent e) {
        boolean result = false;
        if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            continueFlag = false;
        } else if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            automaton = e.isControlDown();
            evolveToggle = evolveToggle < 2 ? evolveToggle + 1 : 0;
            e.consume();
            result = true;
        }
        return result;
    }

    private String getTitle(String path) {
        return String.format("Seed: %s", path);
    }

    public static void main(String[] params) {
        new RefactorUI(params).run();
    }
}
