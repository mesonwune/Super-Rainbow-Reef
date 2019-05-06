import javax.swing.*;

public class Game
{
    public static void main(String[] args)
    {
        JFrame object = new JFrame();
        GamePlay gamePlay = new GamePlay();
        object.setBounds(10, 10, 700, 600);
        object.setTitle("Super Rainbow Reef!!!!!");
        object.setResizable(false);
        object.add(gamePlay);
        object.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        object.setVisible(true);
    }
}
