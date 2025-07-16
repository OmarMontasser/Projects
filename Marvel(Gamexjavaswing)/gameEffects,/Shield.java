package model.effects;

import model.world.Champion;

public class Shield extends Effect {

	public Shield( int duration) {
		super("Shield", duration, EffectType.BUFF);
		
	}

	 public void apply(Champion c) {
	    	int x =(int) (c.getSpeed()*0.02);
	    	c.setSpeed(x+c.getSpeed());
	    }
	    public  void remove(Champion c) {
	    	int x =  (int) (c.getSpeed()/1.02);
	    	c.setSpeed(x);
	    }
	    
}
