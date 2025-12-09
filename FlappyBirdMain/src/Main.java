import javax.swing.*;

public class Main {
    public static void main(String[] args) {

        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Bird");
        GamePanel panel = new GamePanel(boardWidth, boardHeight);

        frame.add(panel);
        frame.setSize(boardWidth, boardHeight);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
