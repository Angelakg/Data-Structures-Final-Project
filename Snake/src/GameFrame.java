import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class GameFrame extends JFrame{
	static JTextField t;
    static JFrame f;
    static JButton b;
    static JLabel l;
    static HashMap<String, Integer> users = new HashMap<String, Integer>();
    static String Player;
    
	//Creates game screen 
	GameFrame(){
		GamePanel panel = new GamePanel();
		this.add(panel);
		this.setTitle("Snake Game"); //title of the game
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); //closing the game
		this.setResizable(false); //set screen size
		this.pack(); 
		this.setVisible(true);
		this.setLocationRelativeTo(null);
	}
}
