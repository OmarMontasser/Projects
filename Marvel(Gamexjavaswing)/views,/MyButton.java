package views;

import java.awt.Image;

import javax.swing.Icon;
import javax.swing.JButton;

import model.world.Champion;

public class MyButton extends JButton{
Champion c ; 
public MyButton( Icon i ,Champion x ) {
	super(i);
	c= x ; 
}
public MyButton( Champion x ) {
	super();
	c= x ; 
}
public Champion getC() {
	return c;
}
public void setC(Champion c) {
	this.c = c;
}

}
