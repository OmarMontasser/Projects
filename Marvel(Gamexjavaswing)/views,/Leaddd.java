package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import engine.Game;
import engine.Player;
import model.world.Champion;

public class Leaddd extends JFrame implements ActionListener {
	JComboBox<Champion> c1 ;
	JComboBox<Champion> c2 ;
	JButton button ; 
	String n1 ; 
	String n2 ; 
	ArrayList<Champion>team1 ;
	ArrayList<Champion>team2 ;
	Champion lead1 ; 
	Champion lead2 ;
	
	
public Leaddd (String s1 , String s2 , ArrayList<Champion> t1 , ArrayList<Champion>t2) {
	super("Marvel : Choose Leader ");
	ImageIcon img = new ImageIcon("Marvel2.png");
	final ImageIcon imgee = new ImageIcon("MarvelBackground.jpg");
    this.setIconImage(img.getImage());
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	JPanel j =  new JPanel(new BorderLayout()){
	       @Override
	       protected void paintComponent(Graphics g)
	       {
	          super.paintComponent(g);
	          g.drawImage(imgee.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
	       }
	    };
	j.setBackground(Color.black);
	this.setLayout(new BorderLayout());
	 this.setSize(400,300);
	n1 = s1 ; n2 = s2 ;
	team1 = t1 ; 
	team2 = t2 ;
	c1 = new JComboBox<Champion>();
	c1.setBackground(new Color(0 , 0 ,60));
	c1.setForeground(Color.white);
	c2 = new JComboBox<Champion>();
	c2.setBackground(new Color(0 , 0 ,60));
	c2.setForeground(Color.white);
	c1.addItem(team1.get(0));
	c1.addItem(team1.get(1));
	c1.addItem(team1.get(2));
	c2.addItem(team2.get(0));
	c2.addItem(team2.get(1));
	c2.addItem(team2.get(2));
	button = new JButton("Next");
	button.setBackground(Color.black);
	button.setForeground(Color.white);
	button.addActionListener(this);
	JPanel pan1 = new JPanel();
	pan1.setBackground(new Color(0 , 0 ,60));
	pan1.setLayout(new BorderLayout());
	JLabel lab1 = new JLabel("Player 1 : "+s1);
	lab1.setForeground(Color.white);
	pan1 .add(lab1, BorderLayout.NORTH);
	pan1.add(c1, BorderLayout.CENTER);
	
	JPanel pan2 = new JPanel();
	pan2.setBackground(new Color(0 , 0 ,60));
	pan2.setLayout(new BorderLayout());
	JLabel lab2 = new JLabel("Player 2 : "+s2+ "  ");
	lab2.setForeground(Color.white);
	pan2 .add(lab2, BorderLayout.NORTH);
	pan2.add(c2, BorderLayout.CENTER);
	JLabel ll = new JLabel("            Choose Leader");
	ll.setBackground(Color.blue);
	j.add(pan1,BorderLayout.WEST);
	j.add(pan2,BorderLayout.EAST);
//	this.add(ll,BorderLayout.CENTER);
	j.add(button, BorderLayout.SOUTH);
	this.add(j);
	this.setLocationRelativeTo(null);
	//this.pack();
	this.setResizable(false); 
    this.setVisible(true);
	
}


@Override
public void actionPerformed(ActionEvent e) {
	// TODO Auto-generated method stub
	if(e.getSource()== button) {
	//	System.out.println("inijm");
		lead1 =(Champion) c1.getSelectedItem();
		lead2 = (Champion)c2 .getSelectedItem();
		
		Player player1 = new Player(n1);
		for(int i = 0 ; i< team1.size() ; i++) {
			player1.getTeam().add(team1.get(i));
			
		}
		player1.setLeader(lead1);
		Player player2 = new Player(n2);
		for(int i = 0 ; i< team2.size() ; i++) {
			player2.getTeam().add(team2.get(i));
			
		}
		player2.setLeader(lead2);
		Game g = new Game(player1 , player2);
		new Gameview(g);
		String s = "";
		s+="Player1 : Black  , Player2  : DarkRed   " ; 
		s+= "\n";
		s+= "\n";
		s +="Move Up : Click 'Up' Button "+"\n";
		s +="Move Down : Click 'Down' Button "+"\n";
		s +="Move Left : Click 'Left' Button"+"\n";
		s +="Move Right : Click 'Right' Button "+"\n";
		s +="\n";
		s+= "Attack Up : Click 'W' Button ";
		s +="\n";
		s+= "Attack Down : Click 'S' Button ";
		s +="\n";
		s+= "Attack Left : Click 'A' Button ";
		s +="\n";
		s+= "Attack Right : Click 'D' Button ";
		s +="\n";s +="\n";
		s+="Hover on Cell to Know it's position";
	//	JOptionPane.showMessageDialog(null, s);
		Image image = null;
		try {
			image = ImageIO.read(new File("Marvel4.jpg")).getScaledInstance(90, 50, Image.SCALE_DEFAULT);
		} catch (IOException ee) {
			// TODO Auto-generated catch block
			ee.printStackTrace();
		}
		if(image!= null)
		JOptionPane.showMessageDialog(null, s, "Controls ", 1, new ImageIcon(image));
		else 
			JOptionPane.showMessageDialog(null, s, "Controls ", 1);	
		this.dispose();
		
		
	}
}
}
