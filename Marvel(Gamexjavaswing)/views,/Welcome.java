package views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class Welcome extends JFrame implements ActionListener {
	JButton b ;
 public Welcome (){
	    super("Marvel"); this.setBackground(Color.DARK_GRAY);
	    ImageIcon img = new ImageIcon("Marvel2.png");
	    final ImageIcon imgee = new ImageIcon("MarvelBackground.jpg");
	//     img = new ImageIcon("Marvel4.jpg");
	    this.setIconImage(img.getImage());
	   
	   //  this.setDefaultLookAndFeelDecorated(false);
        //this.getRootPane().putClientProperty("JRootPane.titleBarForeground", Color.red);
	    JPanel jj = new JPanel()
	    {
		       @Override
		       protected void paintComponent(Graphics g)
		       {
		          super.paintComponent(g);
		          g.drawImage(imgee.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
		       }
		    };
	   
	//   jj.setBackground(Color.BLUE);
	
	    jj.setBackground(Color.BLUE);
	    jj.setLayout(null);
	   jj.setPreferredSize(new Dimension(500,500));
	
	   // this.setLayout(null);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Image image = null;
		try {
			image = ImageIO.read(new File("Marvel4.jpg")).getScaledInstance(90, 50, Image.SCALE_DEFAULT);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(image == null) {
			b = new JButton("New game");
		}else {
		b = new JButton(new ImageIcon(image));
		}	
		//b.setPreferredSize(new Dimension(100,100));
		
		b.addActionListener(this);
		b.setBackground(new Color(0 , 0 ,60));
		b.setForeground(Color.white);
        this.setSize(500,500);
        b.setBounds(170,185, 160, 120);
      //  b.setFont(new Font("Nnx", 3, 10));
       
        b.setBorderPainted(false);
      //  b.setContentAreaFilled(false);
	  //  this.add(b);
        jj.add(b);
	    JLabel l = new JLabel("Welcome To Marvel World");
	    l.setBackground(Color.BLUE);
	    l.setForeground(Color.red);
	    l.setBounds(170,0, 200, 100);
	//    this.add(l);
	    jj.add(l);
	    JLabel j = new JLabel("Good Luck");
	    j.setBounds(210,400, 200, 100);
	    j.setForeground(Color.red);
	    jj.add(j);
	    this.setLocationRelativeTo(null);
	   // this.setBackground(Color.blue);
	    //this.repaint();
	    
        this.add(jj);
        this.pack();
        this.setResizable(false); 
	     this.setVisible(true);
 
 }
@Override
public void actionPerformed(ActionEvent e) {
	if(e.getSource()== b) {
		new Sett();
		this.dispose();
	}
	
}
}
