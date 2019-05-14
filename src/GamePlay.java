import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class GamePlay extends JPanel implements KeyListener, ActionListener
{
    private Random rand = new Random();
    private boolean move = false;
    private int score = 0;

    private BufferedImage background, shell, star;
    private int totalBricks = 28;

    private Timer timer;
    private int delay = 8;

    private int playerX = 310;
    private int lives = 2;

    private int ballposX = rand.nextInt((396 - 88) + 1) + 88;
    private int ballposY = 350;
    private int ballXdir = -2;
    private int ballYdir = -2;

    private MapGenerator map;

    public GamePlay()
    {
        map = new MapGenerator(4, 7);
        System.out.println(totalBricks);

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        try{
            background = ImageIO.read(new File("src/resources/Background1.bmp"));
            shell = ImageIO.read(new File("src/resources/Katch.gif"));
            star = ImageIO.read(new File("src/resources/Pop.gif"));
        } catch (Exception e) {
            System.out.print(e.getStackTrace());
        }

        timer = new Timer(delay, this);
        timer.start();
    }

    public void paint(Graphics g)
    {
        //background

        g.drawImage(background, 1, 1, 692, 592, null);

        //draw map
        map.draw((Graphics2D) g);
        totalBricks = map.updateTotalBricks();
        System.out.println(totalBricks);

        //borders
        g.setColor(new Color(0, 0, 0, 1));
        g.fillRect(0, 0, 3, 592);
        g.fillRect(0, 0, 692, 3);
        g.fillRect(691, 0, 3, 592);

        //scores
        g.setColor(Color.white);
        g.setFont(new Font("serif", Font.BOLD, 25));
        g.drawString("Score: " + score, 580, 30);

        //lives
        int livesX = 20;
        for (int i = 0; i < lives; i++)
        {
            g.drawImage(star, livesX, 15, 20, 20, null);
            livesX += 30;
        }

        //paddle
        g.drawImage(shell, playerX, 550, 100, 30, null);

        //ball
        g.drawImage(star, ballposX, ballposY, 20, 20, null);

        if (totalBricks <= 0)
        {
            move = false;
            ballXdir = 0;
            ballYdir = 0;

            g.setColor(Color.blue);
            g.setFont(new Font("serif", Font.BOLD, 30));
            g.drawString("You Won!!!!!", 260, 300);

            g.setFont(new Font("serif", Font.BOLD, 20));
            g.drawString("Press Enter to restart", 230, 350);
        }

        if (ballposY > 570)
        {
            move = false;
            ballXdir = 0;
            ballYdir = 0;

            lives--;

            if (lives > 0)
            {

                ballposX = 200;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -1;

                repaint();
                move = true;
            }
            else
            {
                g.setColor(Color.red);
                g.setFont(new Font("serif", Font.BOLD, 30));
                g.drawString("Game Over, Score: " + score, 190, 300);

                g.setFont(new Font("serif", Font.BOLD, 20));
                g.drawString("Press Enter to restart", 230, 350);
            }

        }

        g.dispose();
    }


    public void actionPerformed(ActionEvent e)
    {
        timer.start();
        if (move)
        {
            if(new Rectangle(ballposX, ballposY, 20, 20).intersects(new Rectangle(playerX, 550, 100, 8)))
            {
                ballYdir *= -1;
            }

            for (int i = 0; i < map.map.length; i++)
            {
                for (int j = 0; j < map.map[0].length; j++)
                {
                    if (map.map[i][j] > 0)
                    {
                        int brickX = j * map.brickWidth + 80;
                        int brickY = i * map.brickHeight + 50;

                        Rectangle brickRect = new Rectangle(brickX, brickY, map.brickWidth, map.brickHeight);
                        Rectangle ballRectangle = new Rectangle(ballposX, ballposY, 20, 20);

                        if (ballRectangle.intersects(brickRect))
                        {
                            //unbreakable
                            if (map.map[i][j] == 1)
                            {
                                if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width
                                        || ballposY + 19 <= brickRect.y || ballposY + 1 >= brickRect.y + brickRect.height)
                                {
                                    ballXdir *= -1;
                                } else
                                {
                                    ballYdir *= -1;
                                }
                            }
                            //1 hit
                            else if (map.map[i][j] == 2)
                            {
                                map.setBrickValue(0, i, j);
                                totalBricks--;
                                score += 5;

                                if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width)
                                {
                                    ballXdir *= -1;
                                } else
                                {
                                    ballYdir *= -1;
                                }
                            }
                            //2 hit
                            else
                            {
                                if (ballposX + 19 <= brickRect.x || ballposX + 1 >= brickRect.x + brickRect.width)
                                {
                                    ballXdir *= -1;
                                } else
                                {
                                    ballYdir *= -1;
                                }

                                score += 5;
                                map.map[i][j] = 2;



                            }
                        }
                    }
                }
            }

            ballposX += ballXdir;
            ballposY += ballYdir;

            if (ballposX < 0 || ballposX > 670)
            {
                ballXdir *= -1;

            }

            if (ballposY < 0)
            {
                ballYdir *= -1;
            }
        }

        repaint();
    }

    public void keyTyped(KeyEvent e) { }

    public void keyReleased(KeyEvent e) { }

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            if (playerX < 10)
            {
                playerX = 10;
            }
            else
            {
                moveLeft();
            }
        }

        if (e.getKeyCode() == KeyEvent.VK_RIGHT)
        {
            if(playerX >= 600)
            {
                playerX = 600;
            }
            else
            {
                moveRight();
            }
        }

        if(e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            if(!move)
            {
                move = true;
                ballposX = rand.nextInt((396 - 88) + 1) + 88;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score = 0;
                lives = 2;
                totalBricks = 28;
                map = new MapGenerator(4, 7);

                repaint();
            }
        }
    }

    public void moveRight()
    {
        move = true;
        playerX += 20;
    }

    public void moveLeft()
    {
        move = true;
        playerX -= 20;
    }


}
