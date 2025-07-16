package model.effects;

import model.world.Champion;

public class Dodge extends Effect {

	public Dodge(int duration) {
		super("Dodge", duration, EffectType.BUFF);
		}
    public void apply(Champion c) {
    	int x =(int) ((int) c.getSpeed()*1.05);
    	c.setSpeed(x);
    }
    public  void remove(Champion c) {
    	int x =   (int) ((int) c.getSpeed()/1.05);
    	c.setSpeed(x);
    }
    
    
    
    
    
    
    
    
}