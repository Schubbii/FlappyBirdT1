import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.URL;
import java.util.ArrayList;

import java.io.File;                            //Audio Imports
import java.util.Objects;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class GamePanel extends JPanel implements ActionListener, KeyListener {

    int boardWidth;
    int boardHeight;

    //gameStart
    boolean gameStarted = false;

    //score
    int score = 0;

    // Bird
    int birdX = 50;
    int birdY = 300;
    int birdWidth = 45;
    int birdHeight = 45;
    double velocity = 0;
    double gravity = 0.7;
    double jumpStrength = -8;

    // Images
    Image birdImage;
    Image pipeTopImage;
    Image pipeBottomImage;
    Image backgroundImage;

    // Pipes
    ArrayList<Pipe> pipes = new ArrayList<>();
    int pipeWidth = 64;
    int pipeGap = 200;
    double pipeSpeed = 3;

    Timer gameLoop;
    boolean gameOver = false;

    public GamePanel(int width, int height) {
        String filepath = "FlappyBirdMain/SoundFiles/MusicLoop.wav"; //start background music loop
        PlayMusic(filepath, 0);

        this.boardWidth = width;
        this.boardHeight = height;

        setPreferredSize(new Dimension(width, height));
        setFocusable(true);
        addKeyListener(this);
        requestFocusInWindow();

        // Beispielbilder
        birdImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/bilder/biene.png"))).getImage();
        pipeTopImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/bilder/roehre_runter.png"))).getImage();
        pipeBottomImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/bilder/roehre_hoch.png"))).getImage();
        backgroundImage = new ImageIcon(Objects.requireNonNull(getClass().getResource("/bilder/hintergrund.png"))).getImage();

        startGame();
    }

    public void startGame() {
        score = 0;
        gameStarted = false;

        pipes.clear();
        gameOver = false;
        birdY = boardHeight / 2;
        velocity = 0;

        spawnPipes();

        gameLoop = new Timer(16, this);
        gameLoop.start();
    }


    public void spawnPipes() {
        int randomY = (int) (Math.random() * (boardHeight - pipeGap - 200)) + 200;
        // obere Pipe
        pipes.add(new Pipe(boardWidth, (randomY - pipeGap) - pipeTopImage.getHeight(null), pipeWidth, pipeTopImage.getHeight(null), true));

        // untere Pipe
        pipes.add(new Pipe(boardWidth, randomY, pipeWidth, boardHeight, false));
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        // Hintergrund
        g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), null);

        // Bird
        g.drawImage(birdImage, birdX, birdY, birdWidth, birdHeight, null);

        // Pipes
        for (Pipe p : pipes) {
            if (p.isTop) {
                // obere Pipe
                g.drawImage(pipeTopImage, p.x, p.y, p.width, p.height, null);
            } else {
                // untere Pipe
                g.drawImage(pipeBottomImage, p.x, p.y, p.width, p.height, null);
            }
        }

        //score
        g.setColor(Color.black);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 10, 40);


        //start Screen
        if (!gameStarted && !gameOver) {
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 26));
            g.drawString("Bitte Leertaste drücken", 30, 300);
            return; // verhindert unnötiges Zeichnen
        }

        if (gameOver) {
            g.setColor(Color.black);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("GAME OVER", 70, 300);

            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Drücke R zum Neustart", 60, 340);

            String filepath = "FlappyBirdMain/SoundFiles/Wall-Hit2.wav"; //wall hit sound
            PlayMusic(filepath, 1);
            filepath = "FlappyBirdMain/SoundFiles/Wilhelm Scream - Sound Effect (HD) - Gaming Sound FX.wav"; //scream sound
            PlayMusic(filepath, 1);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if (!gameStarted){
            repaint();
            return;
        }

        if (!gameOver) {

            // Bird Physik
            velocity += gravity;
            birdY += (int) velocity;

            // Pipes bewegen
            for (Pipe p : pipes) {
                p.x -= (int) pipeSpeed;
            }
            for (Pipe p : pipes) {
                // Nur untere Pipe bewerten, sonst doppelte Punkte
                if (!p.isTop && !p.passed && p.x + p.width < birdX) {
                    p.passed = true;
                    score++;
                }
            }

            // Neue Pipes spawnen
            if (pipes.size() > 0 && pipes.get(pipes.size() - 1).x < boardWidth - 300) {
                spawnPipes();
            }

            if(score % 10 == 0){
                pipeSpeed += 0.02;
            }

            // Kollision
            Rectangle birdRect = new Rectangle(birdX, birdY, birdWidth, birdHeight);

            for (Pipe p : pipes) {
                Rectangle pipeRect = new Rectangle(p.x, p.y, p.width, p.height);
                if (birdRect.intersects(pipeRect)) {
                    gameOver = true;
                    gameLoop.stop();
                    pipeSpeed = 3;
                }
            }

            // Decke / Boden
            if (birdY < 0 || birdY + birdHeight > boardHeight) {
                gameOver = true;
                gameLoop.stop();
            }
        }

        repaint();
    }

    @Override
    public void keyPressed(KeyEvent e) {

        if (!gameStarted){
            gameStarted = true;
            return;
        }

        if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
            velocity = jumpStrength;
            String filepath = "FlappyBirdMain/SoundFiles/Swoop.wav"; //start background music loop
            PlayMusic(filepath, 1);
        }

        if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
            startGame();
        }
    }

    @Override public void keyTyped(KeyEvent e) {}
    @Override public void keyReleased(KeyEvent e) {}

    static class Pipe {

        boolean passed = false;

        int x, y, width, height;
        boolean isTop;

        public Pipe(int x, int y, int width, int height, boolean isTop) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.isTop = isTop;
        }
    }


    public static void PlayMusic(String location, int loop) {         //music player
        try
        {
            File musicPath = new File(location);

            if (musicPath.exists()) {
                AudioInputStream audioInput = AudioSystem.getAudioInputStream(musicPath);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInput);
                clip.start();
                if (loop != 1) {
                    if (loop == 0) {
                        clip.loop(Clip.LOOP_CONTINUOUSLY);
                    } else {
                        clip.loop(loop);
                    }
                }
            }
            else {System.out.println("Can*t find file");
            }
        }
        catch (Exception e)
        {
            System.out.println(e);
        }



    }
}
