package views;

import javax.swing.JButton;

import model.abilities.Ability;

public class MyButton2  extends JButton{
Ability a ; 
public MyButton2(Ability ab) {
	super();
	a = ab ; 
}
public MyButton2(Ability ab,String s) {
	super(s);
	a = ab ; 
}
public Ability getA() {
	return a;
}
public void setA(Ability a) {
	this.a = a;
}


}
