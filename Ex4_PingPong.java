package examples;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class Ex4_PingPong extends JFrame {

    public Ex4_PingPong() {
        setTitle("Ping Pong");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        GamePanel panel = new GamePanel();
        add(panel);
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }


    static class GamePanel extends JPanel implements ActionListener, MouseMotionListener, KeyListener {

       
        private final int WIDTH = 600;
        private final int HEIGHT = 400;

       
        private final int PADDLE_WIDTH = 10;
        private final int PADDLE_HEIGHT = 80;
        private int playerY = HEIGHT / 2 - PADDLE_HEIGHT / 2;

   
        private int aiY = HEIGHT / 2 - PADDLE_HEIGHT / 2;

        private int ballX = WIDTH / 2;
        private int ballY = HEIGHT / 2;
        private int ballSize = 20;
        private int ballXDir = -3;
        private int ballYDir = 3;

        
        private int playerScore = 0;
        private int aiScore = 0;

   
        private Timer timer;

        public GamePanel() {
            setPreferredSize(new Dimension(WIDTH, HEIGHT));
            setBackground(Color.WHITE);

           
            addMouseMotionListener(this);

         
            setFocusable(true);
            addKeyListener(this);

           
            timer = new Timer(15, this);
            timer.start();
        }

      
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            
            g.setColor(Color.BLACK);

            
            for (int i = 0; i < HEIGHT; i += 15) {
                g.fillRect(WIDTH / 2 - 1, i, 2, 10);
            }

           
            g.fillRect(10, playerY, PADDLE_WIDTH, PADDLE_HEIGHT);
            g.fillRect(WIDTH - 20, aiY, PADDLE_WIDTH, PADDLE_HEIGHT);

            
            g.fillOval(ballX, ballY, ballSize, ballSize);

            
            g.setFont(new Font("Consolas", Font.BOLD, 20));
            g.drawString("Player: " + playerScore, 50, 30);
            g.drawString("AI: " + aiScore, WIDTH - 150, 30);
        }

       
        private final int AI_MAX_SPEED = 10;
        private final int AI_REACTION_DELAY = 5; 
        private int reactionCounter = 0;
        private int targetY = HEIGHT / 2; 

        private int predictBallY() {
            int predictedY = ballY;
            int predictedDirY = ballYDir;
            int predictedX = ballX;
            int predictedDirX = ballXDir;

            
            while (predictedX < WIDTH - 20 && predictedX > 0) {
                predictedX += predictedDirX;
                predictedY += predictedDirY;

                if (predictedY <= 0 || predictedY + ballSize >= HEIGHT) {
                    predictedDirY = -predictedDirY; 
                }
            }
            return predictedY;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            
            ballX += ballXDir;
            ballY += ballYDir;

         
            if (ballY <= 0 || ballY + ballSize >= HEIGHT) {
                ballYDir = -ballYDir;
            }

            
            if (ballX <= 20 && ballY + ballSize >= playerY && ballY <= playerY + PADDLE_HEIGHT) {
                ballXDir = -ballXDir;
            }
            if (ballX + ballSize >= WIDTH - 20 && ballY + ballSize >= aiY && ballY <= aiY + PADDLE_HEIGHT) {
                ballXDir = -ballXDir;
            }

            
            if (reactionCounter < AI_REACTION_DELAY) {
                reactionCounter++;
            } else {
                reactionCounter = 0;
               
                int predictedY = predictBallY();

              
                int errorMargin = 30; 
                int randomOffset = (int)(Math.random() * 2 * errorMargin) - errorMargin;

                targetY = predictedY - PADDLE_HEIGHT / 2 + randomOffset;

               
                if (targetY < 0) targetY = 0;
                if (targetY + PADDLE_HEIGHT > HEIGHT) targetY = HEIGHT - PADDLE_HEIGHT;
            }

      
            if (aiY < targetY) {
                aiY += AI_MAX_SPEED;
                if (aiY > targetY) aiY = targetY;
            } else {
                aiY -= AI_MAX_SPEED;
                if (aiY < targetY) aiY = targetY;
            }

           
            if (aiY + PADDLE_HEIGHT > HEIGHT) aiY = HEIGHT - PADDLE_HEIGHT;

            // Punctaj și reset
            if (ballX < 0) {
                aiScore++;
                resetMingea();
            }
            if (ballX > WIDTH) {
                playerScore++;
                resetMingea();
            }

            repaint();
        }

    
        private void resetMingea() {
            ballX = WIDTH / 2;
            ballY = HEIGHT / 2;
            ballXDir = (Math.random() > 0.5) ? 3 : -3;
            ballYDir = (Math.random() > 0.5) ? 3 : -3;
        }

     
        @Override
        public void mouseMoved(MouseEvent e) {
            int mouseY = e.getY();
            playerY = mouseY - PADDLE_HEIGHT / 2;

            if (playerY < 0) playerY = 0;
            if (playerY + PADDLE_HEIGHT > HEIGHT) playerY = HEIGHT - PADDLE_HEIGHT;

            repaint();
        }

        @Override
        public void mouseDragged(MouseEvent e) {
          
        }


        @Override
        public void keyPressed(KeyEvent e) {
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_UP) {
                playerY -= 10;
                if (playerY < 0) playerY = 0;
            }
            if (key == KeyEvent.VK_DOWN) {
                playerY += 10;
                if (playerY + PADDLE_HEIGHT > HEIGHT) playerY = HEIGHT - PADDLE_HEIGHT;
            }
            repaint();
        }

        @Override
        public void keyReleased(KeyEvent e) { }
        @Override
        public void keyTyped(KeyEvent e) { }
    }

    public static void main(String[] args) {
   
        SwingUtilities.invokeLater(() -> new Ex4_PingPong());
    }
}
