import javax.swing.*;

public class GameOfLifeApp {
    JFrame frame = new JFrame();
    JTable table = new JTable();
    private GameOfLifeApp(String[] params) {
        table.setShowGrid(true);
    }

    private void run() {

    }

    public static void main(String[] params) {
        new GameOfLifeApp(params).run();
    }
}
