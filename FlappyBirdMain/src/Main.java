import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {

        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Floppy Bee");
        GamePanel panel = new GamePanel(boardWidth, boardHeight);




        Image icon = new ImageIcon(Main.class.getResource("/Bilder/Biene.png")).getImage();
        frame.setIconImage(icon);

        frame.add(panel);
        frame.setSize(boardWidth, boardHeight);
        frame.setVisible(true);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
