
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.LinkedList;
import java.util.HashMap;

public class GamePanel extends JPanel implements ActionListener {
	
	//Variables for calculating to the pixels and GUI
	static final int SCREEN_WIDTH = 750;
	static final int SCREEN_HEIGHT = 750;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/(UNIT_SIZE*UNIT_SIZE);
	static final int X_SIZE = SCREEN_WIDTH/UNIT_SIZE;
	static final int Y_SIZE = SCREEN_HEIGHT/UNIT_SIZE;
	
	int applesEaten;
	char direction = 'R';
	boolean running = false;
	boolean win = false;
	static int Delay = 100;
	Timer timer;
	Random random;
	boolean restart = false;
	boolean end = false;
	
    static HashMap<String, Integer> users = new HashMap<String, Integer>();
    static String Player;
	Grid grid = new Grid();
	Snake snake = new Snake();
	Apple apple = new Apple();
	
	GamePanel(){
		//Screen setup
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH ,SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		
		//Enter name pop up
		do {
		Player = JOptionPane.showInputDialog("Enter player name (Max 10 characters)\n", "Name");
		}while(Player.length() >= 10);
		
		if(users.containsKey(Player) == false) {
        	users.put(Player, 0);
        }
		startGame();
	}
	
	public void startGame() {
		// Initialization 
		grid.createGrid();
		snake.createSnake();
		apple.newApple();
		
		running = true;
		timer = new Timer(Delay,this);
		timer.start();
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}
	
	public void draw(Graphics g) {
		if(win) {
			win(g);
		}else if(running) {
				//Draw grid lines
				for(int i = 0; i < SCREEN_HEIGHT/UNIT_SIZE; i++) {
					g.setColor(new Color(83,83,83));
					g.drawLine(i*UNIT_SIZE, 0, i*UNIT_SIZE, SCREEN_HEIGHT);
					g.drawLine(0, i*UNIT_SIZE, SCREEN_WIDTH, i*UNIT_SIZE);
				}
				
				//Draw snake, apple, and wall
				for(int i = 0; i < X_SIZE; i++) {
					for(int j = 0; j < Y_SIZE; j++) {
						switch(grid.grid[i][j].getType()) {
							case "Apple":
								g.setColor(Color.red); // apple
								g.fillOval(i*UNIT_SIZE, j*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
								break;
							case "Snake":
								g.setColor(Color.green); // snake
								g.fillRect(i*UNIT_SIZE, j*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
								break;
							case "Wall":
								g.setColor(Color.white); // wall 
								g.fillRect(i*UNIT_SIZE, j*UNIT_SIZE, UNIT_SIZE, UNIT_SIZE);
						}
					}
				}
				
				//Draw name and score at the very top
				g.setColor(Color.red);
				g.setFont( new Font("Ink Free",Font.BOLD, 40));
				FontMetrics metrics = getFontMetrics(g.getFont());
				g.drawString("Name: "+ Player + "\t\tScore: "+ applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Name: "+ Player + "\t\tScore: "+ applesEaten))/2, g.getFont().getSize());
			}
			else {
				gameOver(g);
			}
		
	}
	
	public void win(Graphics g) {
		//Score
		g.setColor(Color.blue);
		g.setFont( new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Name: "+ Player + "\t\tScore: "+ applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Name: "+ Player + "\t\tScore: "+ applesEaten))/2, g.getFont().getSize());
		
		//Win text
		g.setColor(Color.blue);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("CONGRAGULATIONS!", (SCREEN_WIDTH - metrics2.stringWidth("CONGRAGULATIONS!"))/2, SCREEN_HEIGHT/2);
	}
	
	public void gameOver(Graphics g) {
		//Score
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 40));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("Name: "+ Player + "\t\tScore: "+ applesEaten, (SCREEN_WIDTH - metrics1.stringWidth("Name: "+ Player + "\t\tScore: "+ applesEaten))/2, g.getFont().getSize());
		
		//Game Over text
		g.setColor(Color.red);
		g.setFont( new Font("Ink Free",Font.BOLD, 75));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Game Over", (SCREEN_WIDTH - metrics2.stringWidth("Game Over"))/2, SCREEN_HEIGHT/2);
		
		//Ranking Text
		g.setFont( new Font("Ink Free",Font.BOLD, 35));
		FontMetrics metrics3 = getFontMetrics(g.getFont());
		if(applesEaten >= users.get(Player)) {
			users.replace(Player, applesEaten);
			g.drawString("New High score!" , (SCREEN_WIDTH - metrics3.stringWidth("New High score!"))/2, SCREEN_HEIGHT/2 + (UNIT_SIZE*3));
		}
		
		g.drawString("Your Highest Score: " + users.get(Player), (SCREEN_WIDTH - metrics3.stringWidth("Your Highest Score: " + users.get(Player)))/2, SCREEN_HEIGHT/2 + (UNIT_SIZE*6));
		
		
		end = true;
		//Restart
		g.drawString("Press Enter to restart", (SCREEN_WIDTH - metrics3.stringWidth("Press Enter to restart"))/2, SCREEN_HEIGHT/2 + (UNIT_SIZE*9));
		
		
		if(restart == true) {
			restart = false;
			restart();
			draw(g);
		}
	}
	
	public void restart() {
		//Enter name pop up
		Player = JOptionPane.showInputDialog("Enter player name\n", "Name");
		if(users.containsKey(Player) == false) {
        	users.put(Player, 0);
        }
		
		//reset variables, snake, apple, and grid
		direction = 'R';
		running = true;
		end = false;
		applesEaten = 0;
		grid.createGrid();
		snake.body.clear();
		snake.body.addFirst(new Coordinates(2,2));
		apple.newApple();
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(running) {
			snake.move();
		}
		repaint();
	}
	
	
	public class MyKeyAdapter extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			switch(e.getKeyCode()) {
			case KeyEvent.VK_LEFT:
				if(direction != 'R') {
					direction = 'L';
				}
				break;
			case KeyEvent.VK_RIGHT:
				if(direction != 'L') {
					direction = 'R';
				}
				break;
			case KeyEvent.VK_UP:
				if(direction != 'D') {
					direction = 'U';
				}
				break;
			case KeyEvent.VK_DOWN:
				if(direction != 'U') {
					direction = 'D';
				}
				break;
			}
			if(e.getKeyCode() == KeyEvent.VK_ENTER && end == true) {
				restart = true;
			}
		}
	}
	
	class Snake{
		LinkedList<Coordinates> body = new LinkedList<Coordinates>();
		
		public void createSnake(){
			body.add(new Coordinates(2,2));
		}
		
		
		public void move() {
			int x = body.getFirst().getX();
			int y = body.getFirst().getY();
			
			if(direction == 'U'){
				y = y-1;
			}
			if(direction == 'D') {
				y = y+1;
			}
			if(direction == 'L') {
				x = x-1;
			}
			if(direction == 'R') {
				x = x+1;
			}
			
			//check if snake is eating an apple
			if(this.eatApple(x, y) == false) {
				grid.grid[body.getLast().getX()][body.getLast().getY()].setType("Null");
				body.removeLast();	
			}
			
			// check if the snake is crashing into the wall or itself
			this.Collisions(x, y);
			
	
			grid.grid[x][y].setType("Snake");
			body.addFirst(new Coordinates(x, y));
			
		}
		
		private boolean eatApple(int x, int y) {
			
			if(grid.grid[x][y].getType() == "Apple"){
				applesEaten++;
				apple.newApple();
				return true;
			}else {
				return false;
			}
		}
		
		
		private void Collisions(int x, int y) {
			
			if(grid.grid[x][y].getType() == "Snake" || grid.grid[x][y].getType() == "Wall") {
				running = false;
			}
			
		}
	}
	
	class Grid{
		Cell grid[][] = new Cell[X_SIZE][Y_SIZE];
		
		public void createGrid() {
			
			for(int i = 0; i < grid.length; i++) {
				for(int j = 0; j < grid[0].length; j++) {
					if(i <= 1 || i >= X_SIZE-2 || j <= 1 || j >= Y_SIZE-2) {
						grid[i][j] = new Cell("Wall"); //creates a boarder 
					}else {
						grid[i][j]= new Cell("Null");
					}
				}	
			}
		}
		
	}
	
	class Apple{
		public void newApple() {
			int full = 0;
			
			//check the grid to see if its full
			for(int i = 0; i < X_SIZE; i++) {
				for(int j = 0; j < Y_SIZE; j++) {
					if(grid.grid[i][j].getType() == "Snake" || grid.grid[i][j].getType() == "Wall") {
						full++;
					}
				}
			}
			//win if there is no room to place an apple
			if(full == GAME_UNITS) {
				 win = true;
				 return;
			}
			
			int tempx = random.nextInt((int)X_SIZE);
			int tempy = random.nextInt((int)Y_SIZE);
			
			//rerandomize if the cell is not an empty type
			while(grid.grid[tempx][tempy].getType() == "Snake" || grid.grid[tempx][tempy].getType() == "Wall") {
				tempx = random.nextInt((int)X_SIZE);
				tempy = random.nextInt((int)Y_SIZE);
			}
			grid.grid[tempx][tempy].setType("Apple");

		}
	}
	
	class Cell{
		private String type;
		
		Cell(String type) {
			this.type = type;
		}
		
		public String getType() {
			return this.type;
		}
		
		public void setType(String type) {
			this.type = type;
		}
	}
	
	class Coordinates{
		private int x;
		private int y;
		
		Coordinates(int x, int y) {
			this.x = x;
			this.y = y;
		}
		
		public int getX() {
			return this.x;
		}
		public int getY() {
			return this.y;
		}

	}
	
}




