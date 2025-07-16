package views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

public class Sett extends JFrame implements ActionListener {
	JTextField t1; 
	JTextField t2;
	JButton b ; 
public Sett() {
	super("Marvel : Enter Players Names ");
	final ImageIcon imgee = new ImageIcon("MarvelBackground.jpg");
	ImageIcon img = new ImageIcon("Marvel2.png");
    this.setIconImage(img.getImage());
	this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	this.setLayout(new BorderLayout());
	JPanel j = new JPanel()
	 {
	       @Override
	       protected void paintComponent(Graphics g)
	       {
	          super.paintComponent(g);
	          g.drawImage(imgee.getImage(), 0, 0, this.getWidth(), this.getHeight(), null);
	       }
	    };
	j.setLayout(new BorderLayout());
	j.setBackground(Color.blue);
	t1 = new JTextField();
	t1.setBackground(new Color(0 , 0 ,60));
	t1.setCaretColor(Color.white);
	t1.setForeground(Color.white);
	t1.setText("Player 1");
	
	t2 = new JTextField();
	t2.setBackground(new Color(0 , 0 ,60));
//	t2.setBackground(Color.OPAQUE);
	t2.setCaretColor(Color.white);
	t2.setForeground(Color.white);
	t2.setText("Player 2");
	b = new JButton("Next");
	b.setBackground(Color.black);
	b.setForeground(Color.white);
	b.addActionListener(this);
//	b.setContentAreaFilled(false);
    this.setSize(500,500);
	
	j.add(t1, BorderLayout.WEST);
	j.add(t2, BorderLayout.EAST);
	j.add(b,BorderLayout.SOUTH);
	this.add(j);
	this.setLocationRelativeTo(null);
	//this.pack();
	this.setResizable(false); 
    this.setVisible(true);
}
@Override
public void actionPerformed(ActionEvent e) {
	if(e.getSource() == b ) {
		try {
			new Myframe(t1.getText(), t2.getText());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.dispose();
	}
	
}
}
