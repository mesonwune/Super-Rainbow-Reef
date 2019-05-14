import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Random;

public class MapGenerator
{
    private Random rand = new Random();
    public int map[][];
    public int brickWidth, brickHeight, totalBricks, unbreakable;
    private BufferedImage block1, block2, block3;

    public MapGenerator(int row, int col)
    {

        map = new int[row][col];
        totalBricks = row * col;
        unbreakable = 0;

        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[0].length; j++)
            {

                if (unbreakable == 5)
                {
                    map[i][j] = rand.nextInt(1) + 2;
                }
                else
                {
                    map[i][j] = rand.nextInt((3 - 1) + 1) + 1;
                    if (map[i][j] == 1)
                    {
                        unbreakable++;
                    }
                }
            }
        }

        totalBricks -= unbreakable;

        brickWidth = 540 / col;
        brickHeight = 150 / row;

        try
        {
            block1 = ImageIO.read(new File("src/resources/Block_solid.gif"));
            block2 = ImageIO.read(new File("src/resources/Block6.gif"));
            block3 = ImageIO.read(new File("src/resources/Block7.gif"));
        } catch (Exception e) {
            System.out.print(e.getStackTrace());
        }
    }

    public void draw(Graphics2D g)
    {
        for (int i = 0; i < map.length; i++)
        {
            for (int j = 0; j < map[0].length; j++)
            {
                if (map[i][j] > 0)
                {
                    ////unbreakable
                    if (map[i][j] == 1)
                    {
                        g.drawImage(block1, j * brickWidth + 88, i * brickHeight + 50, brickWidth, brickHeight, null);
                    }
                    //one hit
                    else if (map[i][j] == 2)
                    {
                        g.drawImage(block2, j * brickWidth + 88, i * brickHeight + 50, brickWidth, brickHeight, null);
                    }
                    //two hits
                    else
                    {
                        g.drawImage(block3, j * brickWidth + 88, i * brickHeight + 50, brickWidth, brickHeight, null);
                    }


                    g.setStroke(new BasicStroke(3));
                    g.setColor(Color.black);
                    g.drawRect(j * brickWidth + 88, i * brickHeight + 50, brickWidth, brickHeight);

                }
            }
        }
    }

    public void setBrickValue(int value, int row, int col)
    {
        map[row][col] = value;
    }



    public int updateTotalBricks()
    {
        return totalBricks;
    }

}
