package views;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JTextPane;

import engine.Player;

public class Goodbye extends JFrame {
	
	
 public Goodbye(Player p) {
	 
	 super("Marvel");
	 ImageIcon img = new ImageIcon("Marvel2.png");
	   this.setIconImage(img.getImage());
	   final ImageIcon imgee = new ImageIcon("gameover3.jpg");
	 this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	 this.setSize(500,500);
	 this.setLayout(null);
	 //--------------------------------------------------------------//
	 String s =  p.getName() +"\n" +" Wins ";
	 JTextField j = new JTextField();
	 j.setLayout(new FlowLayout());
	 j.setText(s);
	 j.setEditable(false);
     j.setHorizontalAlignment(JTextField.CENTER);
	 j.setAlignmentY(200);
//	 j.setBounds(200, 200, 100, 100);
	 j.setBackground(Color.darkGray);
	 j.setForeground(Color.white);
	 j.setFont(new Font("hbh", 1,25));
	 //----------------------------------------------------------//
	 JPanel l = new JPanel(){
	       @Override
	       protected void paintComponent(Graphics g)
	       {
	          super.paintComponent(g);
	          g.drawImage(imgee.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
	       }
	    };
	    l.setForeground(Color.white);
	// l.setLayout(null);
	   l.setBounds(0, 0, 500, 500);
	   l.setFont(new Font("hbh", 1,25));
	   l.add(j);
	 this.add(l);
	 this.setLocationRelativeTo(null);
	 this.setResizable(false); 
	 this.setVisible(true);
 }
 public static void main (String[] args) {
	 new Goodbye(new Player("Omar Montasser "));
 }
}
