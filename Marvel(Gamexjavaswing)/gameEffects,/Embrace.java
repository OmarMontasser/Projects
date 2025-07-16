package model.effects;

import model.world.Champion;

public class Embrace extends Effect {
	

	public Embrace(int duration) {
		super("Embrace", duration, EffectType.BUFF);
	}
	public void apply(Champion c) {
		int x = (int)  (c.getMana()*1.2);
    	c.setMana(x);
    	
    	
         x = (int) (c.getMaxHP()*20);
    	x = (int) (x/100.0);
    	c.setCurrentHP(x+c.getCurrentHP());
    	
    	
    	
        x =(int) (c.getSpeed()*1.2);
    	c.setSpeed(x);
    	
    	x =(int) (c.getAttackDamage()*1.2);
    	c.setAttackDamage(x);
    }
    public  void remove(Champion c) {
    	int x =  (int)((int)c.getSpeed()/1.2);
    	c.setSpeed(x);
    	 x =  (int)((int)c.getAttackDamage()/1.2);
    	c.setAttackDamage(x);
    }
    
}
