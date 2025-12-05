// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.

import javax.swing.*;


public class Main {
    public static void main(String[] args) {

        // Spielfenster

        int boardWidth = 360;
        int boardHeight = 640;

        JFrame frame = new JFrame("Flappy Bird");
        frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);  //in der Mitte des Screens plaziert
        frame.setResizable(false);   // Feld kann nicht größenverändert werden
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // wenn x gedrückt, Programm beendet

    }
}