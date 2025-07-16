package model.effects;

import model.world.Champion;

public class Shock extends Effect {

	public Shock(int duration) {
		super("Shock", duration, EffectType.DEBUFF);
		
	}
	

	@Override
	public void apply(Champion c) {
		// TODO Auto-generated method stub
	
		int x = (int)(c.getSpeed()*0.9);
		c.setSpeed(x);
		x = (int)(c.getAttackDamage()*0.9);
		c.setAttackDamage(x);
		c.setCurrentActionPoints(c.getCurrentActionPoints()-1);
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-1);
		
	}

	@Override
	public void remove(Champion c) {
		// TODO Auto-generated method stub
		int x = (int)(c.getSpeed()/0.9);
		c.setSpeed(x);
		x = (int)(c.getAttackDamage()/0.9);
		c.setAttackDamage(x);
		
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+1);
		c.setCurrentActionPoints(c.getCurrentActionPoints()+1);
		
		
	}

}
