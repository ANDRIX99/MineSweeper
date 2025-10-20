import javax.swing.*;
import java.util.Random;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.*;

class Swing{
	final static int size = 9;
	JPanel panel = new JPanel();
	JPanel points = new JPanel();
	JFrame window = new JFrame("Minesweeper");
	JButton restart = new JButton();
	JButton [][]btn = new JButton[size][size];
	JLabel text = new JLabel();
	JLabel timer = new JLabel();
	Mina [][]mina = new Mina[size][size];
	boolean [][]calculate = new boolean[size][size];
	boolean [][]flag = new boolean[size][size];
	boolean [][]is_clicked = new boolean[size][size];
	static boolean [][]visited = new boolean[size][size];
	boolean click = false;
	long start, elapsed_time;
	int point = 0;
	int placed = 0;
	int total_bee = 0;
	
	Swing() {
		prepareGUI();
		initialize();
		buttonInitialize();
		addButton();
		setMina();
		stampa();
	}
	
	public void initialize() {
		point = 0;
		placed = 0;
		total_bee = 0;
		click = false;
		text.setText("Punti 0");
		timer.setText("0");
		for(int i = 0; i < is_clicked.length; i++) {
			for(int j = 0; j < is_clicked[0].length; j++) {
				is_clicked[i][j] = false;
			}
		}
		for(int i = 0; i < flag.length; i++) {
			for(int j = 0; j < flag[0].length; j++) {
				flag[i][j] = false;
			}
		}
		
		for(int i = 0; i < calculate.length; i++) {
			for(int j = 0; j < calculate[0].length; j++) {
				calculate[i][j] = false;
			}
		}
	}
	
	public void visited() {
		for(int i = 0; i < visited.length; i++) {
			for(int j = 0; j < visited[0].length; j++) {
				visited[i][j] = false;
			}
		}
	}
	
	private void status() {
		System.out.println("*");
		for(int i = 0; i < visited.length; i++) {
			for(int j = 0; j < visited[0].length; j++) {
				System.out.print(visited[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	public void empty_cell(int x, int y) {
		if(mine_counter(x, y) != 0) {
			status();
			return;
		} else {
			int i, j=y;
			int row_limits = mina.length-1;
			int col_limits = mina[0].length-1;
			for(i = Math.max(0, x-1); i <= Math.min(x+1, row_limits); i++) {
				for(j = Math.max(0, y-1); j <= Math.min(y+1, col_limits); j++) {
					if(mina[i][j].getStato() == 0 && !visited[i][j]) {
						btn[i][j].setText(Integer.toString(mine_counter(i, j)));
						is_clicked[i][j] = true;
						visited[i][j] = true;
						empty_cell(i, j);
					}
				}
			}
		}
	}
	
	public void setMina() {
		Random r = new Random();
		for(int j = 0; j < mina.length; j++)
			for(int i = 0; i < mina.length; i++) {
				mina[j][i] = new Mina();
				total_bee++;
			}
		do {
			int x = r.nextInt(mina.length);
			int y = r.nextInt(mina[0].length);
			if(mina[x][y].getStato() == 0) {
				mina[x][y].setMina();
				placed++;
			}
		} while(placed < 10);
	}
	
	public void prepareGUI() {
		//window.setSize(500, 500);
		window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		window.setVisible(true);
		window.add(panel, BorderLayout.NORTH);
		window.add(points);
		panel.setLayout(new GridLayout(size, size));
		panel.setPreferredSize(new Dimension(500, 500));
		points.add(text);
		points.add(restart);
		points.add(timer);
		timer.setText("0");
		text.setText("Punti 0");
		restart.setText("Restart");
		restart.addMouseListener(mouseListener);
		window.pack();
	}
	
	public void buttonInitialize() {
		for(int i = 0; i < btn.length; i++) {
			for(int j = 0; j < btn.length; j++) {
				btn[i][j] = new JButton();
			}
		}
	}
	
	public void removeButton() {
		for(int i = 0; i < btn.length; i++) {
			for(int j = 0; j < btn.length; j++) {
				panel.remove(btn[i][j]);
				panel.validate();
			}
		}
	}
	
	public void points_calculator() {
		for(int i = 0; i < calculate.length; i++) {
			for(int j = 0; j < calculate[0].length; j++) {
				if(is_clicked[i][j] && !calculate[i][j]) {
					calculate[i][j] = true;
					point++;
				}
			}
		}
	}
	
	MouseListener mouseListener = new MouseAdapter() {
		public void mousePressed(MouseEvent e) {
			if(e.getSource() == restart) {
				initialize();
				removeButton();
				buttonInitialize();
				addButton();
				setMina();
				stampa();
			}
			
			for(int i = 0; i < size; i++) {
				for(int j = 0; j < size; j++) {
					if(e.getSource() == btn[i][j]) {
						if(e.getButton() == MouseEvent.BUTTON1) {
							if(mina[i][j].getStato() == 0) {
								if(!click) {
									click = true;
									start = System.currentTimeMillis();
								}
								elapsed_time = System.currentTimeMillis() - start;
								timer.setText(Float.toString(elapsed_time / 1000F));
								visited();
								btn[i][j].setText(Integer.toString(mine_counter(i, j)));
								empty_cell(i, j);
								points_calculator();
								if(!is_clicked[i][j]) {
									is_clicked[i][j] = true;
									calculate[i][j] = true;
									point++;
								}
								
								if(point >= total_bee - placed) {
									JOptionPane.showMessageDialog(null, "You win" + Float.toString(elapsed_time / 1000F));
								}
								text.setText("Punti " + Integer.toString(point));
							} else {
								if(mina[i][j].getStato() == 1) {
									btn[i][j].setIcon(new ImageIcon(preload()));
									JOptionPane.showMessageDialog(null, "You lose");
								}
							}
						}
						if(e.getButton() == MouseEvent.BUTTON3) {
							System.out.println("right clicked");
							Image img = Toolkit.getDefaultToolkit().getImage("C:\\Users\\andre\\Documents\\GitHub\\MineSweeper\\MineSweeper\\src\\Image\\flag.png");
							img = img.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
							if(!flag[i][j]) {
								btn[i][j].setIcon(new ImageIcon(img));
								flag[i][j] = true;
							} else {
								btn[i][j].setIcon(null);
								flag[i][j] = false;
							}
							System.out.println(btn[i][j]);
						}
					}
				}
			}
		}
	};
	
	public void addButton() {
		for(int k = 0; k < btn.length; k++) {
			for(int l = 0; l < btn.length; l++) {
				btn[k][l].setSize(50, 50);
				panel.add(btn[k][l]);
				btn[k][l].addMouseListener(mouseListener);
			}
		}
	}
	
	public void stampa() {
		System.out.println(total_bee);
		for(int i = 0; i < mina.length; i++) {
			for(int j = 0; j < mina.length; j++) {
				if(mina[i][j].getStato() == 0) {
					System.out.print("O");
				} else {
					System.out.print("X");
				}
			}
			System.out.println();
		}
	}
	
	public int mine_counter(int x, int y) {
		int total = 0;
		int row_limits = mina.length-1;
		int col_limits = mina[0].length-1;
		for(int i = Math.max(0, x-1); i <= Math.min(x+1, row_limits); i++) {
			for(int j = Math.max(0, y-1); j <= Math.min(y+1, col_limits); j++) {
				if(mina[i][j].getStato() == 1)
					total++;
			}
		}
		
		return total;
	}
	
	public void showmap() {
		for(int i = 0; i < btn.length; i++) {
			for(int j = 0; j < btn.length; j++) {
				if(mina[i][j].getStato() == 0) {
					btn[i][j].setBackground(Color.green);
				} else {
					btn[i][j].setIcon(new ImageIcon(preload()));
				}
			}
		}
	}
	
	private Image preload() {
		Image img = Toolkit.getDefaultToolkit().getImage("C:\\Users\\andre\\Documents\\GitHub\\MineSweeper\\MineSweeper\\src\\Image\\mine.png");
		img = img.getScaledInstance(30, 30, Image.SCALE_DEFAULT);
		return img;
	}
}

public class Gui {
	public static void main(String[] args) {
		new Swing();
	}
}