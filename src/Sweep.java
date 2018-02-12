import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import javax.swing.JToggleButton;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
/*
 * JFrame Class 
 * Designed by Eryk Szlachetka.
 */

public class Sweep extends JFrame {
	private static final long serialVersionUID = 1L;
	private final int wid = 9, hei = 9, noOfBombs = 10;  // Declare and initialize width, height and number of bombs.
	private JToggleButton[][] blocks = new JToggleButton[hei][wid]; // Array of Toggle Buttons
	// Block's states
	// -2 : Opened but no bomb
	// -1 : Has a bomb
	// 0  : Not open
	// 1-8: Number of bombs around it
	private int[][] blockStates = new int[hei][wid];
	boolean first, canPlay, endGame;
	
	// ActionListener listens for the event
	ActionListener actionListener = new ActionListener(){
		public void actionPerformed(ActionEvent e){
			int i= 0, j= 0;
			boolean found = false;
			for(i =0; i < hei; i++){
				for(j=0;j<wid;j++){
					if(e.getSource() == blocks[i][j]){
						// found
						found = true;
						break;
					}else{
						found = false; // set the found to false
					}
				}
				if(found) break; // break if found 
			}
			
			if(canPlay){
				blocks[i][j].setSelected(true);
				if(!first){
					spawn(i,j);
					first = true;
				}else if(blockStates[i][j] == -1){
					explode();
				}
				if(canPlay){
					open(i,j);
					reval();
				}
			}else{
				if(!blocks[i][j].isSelected()){
					System.out.println("Not selected!");
					blocks[i][j].setSelected(true);
				}else{
					System.out.println("Selected!");
					blocks[i][j].setSelected(false);
				}
			}
		}
	};
	
	
	private JPanel contentPane; // JPanel

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Sweep frame = new Sweep();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	// Initialize the frame
	private void init(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 533, 446);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		contentPane.addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				resiz();
			}
		});
		setContentPane(contentPane);
	}
	
	/**
	 * Create the frame.
	 */
	public Sweep() {
		init();
		// Loop to initialize blocks (JToggleButtons)
		// Outer loop
		for(int i =0; i < hei; i++ ){
			// Inner loop
			for(int j=0; j < wid; j++){
				blocks[i][j] = new JToggleButton();   // Initialize the buttons
				blocks[i][j].addActionListener(actionListener);
			}
		}
		resiz();
		
		first = false;
		canPlay = true;
		endGame = false;
	}
	
	// Function that resizes the blocks
	private void resiz(){
		// Resize the blocks
		for(int i =0; i < hei; i++ ){
			// Inner loop
			for(int j=0; j < wid ; j++){
				blocks[i][j].setSize(contentPane.getWidth() / (wid*2),contentPane.getHeight()/(hei*2)); // set the size of each button
				
				
				blocks[i][j].setLocation((j+wid/2) * contentPane.getWidth()/ (wid*2), (i+hei/2) * contentPane.getHeight()/ (hei*2)); // set location
				
				contentPane.add(blocks[i][j]);  // Add the block
			}
		}
	}
	
	// Method to spawn new bomb
	private void spawn(int y, int x){
			System.out.println("Spawn, first");
			for(int l = 1; l < noOfBombs;l++){ // Iterate through all the bombs 
				int i,j;
				
				
				// Keep searching for the location
				do{
					i = (int) (Math.random() * (wid + .01)); 
					j = (int) (Math.random() * (hei + .01));
					
					
				}while(blockStates[i][j] == -1 && i == y && j == x); // make sure not to place a bomb where it already exists
				
				
				blockStates[i][j] = -1; // set the bomb to that location
				
				
			}
	}
	
	private void open(int y, int x){
	
		if(y < 0 || x < 0  || x > wid-1 || y > hei-1 || blockStates[y][x] != 0){
			return;
		}
		
		
		int b = 0;  // bomb
		for(int i = y-1; i <= y+ 1; i++){
			for(int j = x -1; j<= x + 1; j++){
				if(!(j<0 || i < 0 || j > wid-1 || i > hei-1) && blockStates[i][j] == -1){
					b++; // increment the bombs
				}
			}
		}
		
		if(b == 0){
			blockStates[y][x] = -2;
			for(int i = y-1; i <= y+ 1; i++){
				for(int j = x -1; j<= x + 1; j++){
					if(!(j<0 || i < 0 || j > wid-1 || i > hei-1)){
						if( i != y || j != x) {
							open(i,j);   // recursive algorithm
						}
					}
				}
			}
		}else{
			blockStates[y][x] = b;
		}
	}
	
	private void reval(){
		for(int i = 0; i < hei;i++){
			for(int j=0; j < wid;j++){
				if(blockStates[i][j] == -2){
					blocks[i][j].setText("");
					blocks[i][j].setSelected(true);
				}else if(blockStates[i][j] > 0){
						blocks[i][j].setText(""+blockStates[i][j]);
						blocks[i][j].setSelected(true);
				}
			}
		}
	}

	private void explode(){
		canPlay = false;
		for(int i=0; i < hei; i++){
			for(int j=0; j < wid;j++){
				if(blockStates[i][j] == -1){
					blocks[i][j].setSelected(true);
					blocks[i][j].setText("BOOM!");
				}/*else if(blockStates[i][j] == -2){
					//blocks[i][j].setSelected(false);
				}else if(blockStates[i][j] > 0){
					//blocks[i][j].setText(""+blockStates[i][j]);
					//blocks[i][j].setSelected(true);
				}*/
			}
		}
	}
}
