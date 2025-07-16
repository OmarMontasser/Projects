package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.time.chrono.MinguoDate;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import engine.Game;
import engine.Player;
import model.world.AntiHero;
import model.world.Champion;
import model.world.Hero;

public class Myframe extends JFrame implements ActionListener{
	boolean flag ;
	JPanel Champions ; 
	JLabel p1 ; 
	JLabel p2 ; 
	JLabel turn ;
	ArrayList<Champion> availableChampions ;
	ArrayList<Champion> team1;
	ArrayList<Champion> team2;
	String n1; 
	String n2;
	JButton but; 
	public Myframe(String s1 , String s2) throws IOException  {
		
		super("Marvel : Pick Teams ");
		final ImageIcon imgee = new ImageIcon("MarvelBackground.jpg");
		ImageIcon img = new ImageIcon("Marvel2.png");
	    this.setIconImage(img.getImage());
		JLabel main = new JLabel();
		main.setBackground(Color.BLUE);
		int r =(int)( Math.random()*2);
		if(r==1) {
		flag = true ;
		}else {
			flag = false ;
		}
		but = new JButton("Next ");
		but.setEnabled(false);
		but.addActionListener(this);
		n1 = s1; n2 = s2 ;
		 team1 = new ArrayList<Champion> ();
		 team2 = new ArrayList<Champion> ();
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Champions  = new JPanel();
		Champions.setLayout(new GridLayout(5,5));
		Game g = new Game(new Player("Ahbxj"), new Player("nxknx"));
		try {
			g.loadAbilities("Abilities.csv");
			g.loadChampions("Champions.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		availableChampions = g.getAvailableChampions();
		buildPanel(Champions);
		Champions.setBounds(0, 0, 500, 500);
		this.setLayout(new BorderLayout());
		this.add(Champions, BorderLayout.WEST);
		p1 = new JLabel(s1+" : "+ "\n");
		p2 = new JLabel(s2+ " : "+ "\n");
		JPanel pp = new JPanel(){
		       @Override
		       protected void paintComponent(Graphics g)
		       {
		          super.paintComponent(g);
		          g.drawImage(imgee.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
		       }
		    };
		pp.setBackground(Color.blue);
	//	pp.setLayout(new BorderLayout());
	//	pp.add(p1, BorderLayout.NORTH);
	//	pp.add(p2, BorderLayout.SOUTH);
		pp.setLayout(new FlowLayout());
		pp.add(p1);
		pp.add(p2);
		turn = new JLabel();
		updateturnlabel();
		pp.add(turn);
		this.add(pp, BorderLayout.CENTER);
		but.setBackground(Color.black);
		but.setForeground(Color.white);
		this.add(but, BorderLayout.EAST);
	     this.setSize(1200,800);
	    
	    this.setLocationRelativeTo(null);
	    this.setResizable(false); 
	     this.setVisible(true);
	}
	private void updateturnlabel() {
		// TODO Auto-generated method stub
		
        if(team2.size()==3&&team1.size()==3) {
        	turn.setForeground(Color.red);
        	turn.setText(" ( Teams are Ready )");
        	return ;
		}
		turn.setForeground(Color.green);
		
		if(flag) {
			turn.setText(" ( "+n1 +"  chooses )");
		}
		else {
			turn.setText(" ( "+n2 +"  chooses )");
		}
	}
	public void buildPanel(JPanel c ) throws IOException  {
		Game g = new Game(new Player("Nknkn"), new Player("bjcb"));
		try {
			g.loadAbilities("Abilities.csv");
			g.loadChampions("Champions.csv");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ArrayList<Champion> availableChampions = g.getAvailableChampions();
		for(int i = 0 ; i < availableChampions.size(); i++) {
			JLabel lab = new JLabel();
			lab.setLayout(new BorderLayout());
			lab.setPreferredSize(new Dimension (200,50));
			//JButton b = new JButton(availableChampions.get(i).getName());
			Image image = null;
			if(availableChampions.get(i) instanceof Hero) {
				try {
				image = ImageIO.read(new File("H-Icon.jpg")).getScaledInstance(20, 15, Image.SCALE_DEFAULT);
				}
				catch(Exception e) {
					
				}
				}else {
				
				if(availableChampions.get(i) instanceof AntiHero) {
					try {
					image = ImageIO.read(new File("Letter-A-icon.png")).getScaledInstance(20, 15, Image.SCALE_DEFAULT);
					}catch(Exception e) {
						
					}
					
				}else {
					try {
					image = ImageIO.read(new File("Letter-V-icon.png")).getScaledInstance(20, 15, Image.SCALE_DEFAULT);
					}
					catch(Exception e) {
						
					}
					
				}
			}
			MyButton b ;
			if(image != null)
			 b = new MyButton(new ImageIcon(image),availableChampions.get(i));
			else
			 b = new MyButton(availableChampions.get(i));
			b.setBackground(Color.black);
			b.setForeground(Color.white);
			b.setText(availableChampions.get(i).toString());
			JTextArea  l = new JTextArea ();
		//	l.setPreferredSize(new Dimension(50,50));
		//	l.setVerticalTextPosition(0);l.setHorizontalTextPosition(0);
			l.setText(availableChampions.get(i).toString2());
			l.setEditable(false);
			l.setBackground(new Color(0 , 0 ,60));
			lab.add(b,BorderLayout.SOUTH);
			lab.add(l, BorderLayout.NORTH);
		b.addActionListener(this);
		c.add(lab);
		//	c.add(b);
		//	c.add(l);
		}
	}
	
	public void actionPerformed2(ActionEvent e) {
		if(e.getSource()== but) {
			Leaddd d = 	new Leaddd(n1,n2 , team1 , team2 );
			this.dispose();
			
		}
		else {
		JButton j = (JButton)e.getSource();
		j.setEnabled(false);
		String s = e.getActionCommand();
		for(int i =0 ; i< availableChampions.size(); i++ ) {
			if(s.equals(availableChampions.get(i).getName())) {
				if(team1.size()<3) {
					team1.add(availableChampions.get(i));
					updatet1();
					updateturnlabel();
				}
				else {
					if(team2.size()<3) {
						team2.add(availableChampions.get(i));
						updatet2();
						updateturnlabel();
						if(team2.size()==3) {
							but.setEnabled(true);
						}
					}else {
						but.setEnabled(true);
					
						
					}
				}
			}
		}
		}
		
	}
	public void actionPerformed(ActionEvent e) {
		if(e.getSource()== but) {
			Leaddd d = 	new Leaddd(n1,n2 , team1 , team2 );
			this.dispose();
			
		}
		else {
		MyButton j = (MyButton)e.getSource();
		j.setEnabled(false);
		if(team1.size()<3 && flag) {
			team1.add(j.getC());
			updatet1();
			flag = false ;
		    updateturnlabel();
		    if(team2.size()==3&&team1.size()==3) {
				but.setEnabled(true);
			}
		}
		else {
			if(team2.size()<3) {
				team2.add(j.getC());
				updatet2();
				flag = true ;
				updateturnlabel();
			if(team2.size()==3&&team1.size()==3) {
				but.setEnabled(true);
			}
			}
		}
		
		}
		
	}
	void updatet1() {
		  String t = n1+  ": "+ "\n";
		  for(int i = 0 ; i<team1.size(); i++) {
			t += team1.get(i).getName();
			t+="    ";
			
		  }
		  
		p1.setForeground(Color.white);
		  p1.setText(t);
	  }
	void updatet2() {
		  String t =  n2+" : "+ "\n";
		  for(int i = 0 ; i<team2.size(); i++) {
			t += team2.get(i).getName();
			t+= "    " ;
			
		  }
		  p2.setForeground(Color.white);
		  p2.setText(t);
		  
	  }

}
