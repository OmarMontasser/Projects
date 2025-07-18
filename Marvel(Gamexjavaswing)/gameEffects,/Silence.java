package model.effects;

import model.world.Champion;

public class Silence extends Effect {

	public Silence( int duration) {
		super("Silence", duration, EffectType.DEBUFF);
		
	}

	@Override
	public void apply(Champion c) {
		// TODO Auto-generated method stub
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()+2);
		c.setCurrentActionPoints(c.getCurrentActionPoints()+2);
		
	}

	@Override
	public void remove(Champion c) {
		// TODO Auto-generated method stub
		c.setMaxActionPointsPerTurn(c.getMaxActionPointsPerTurn()-2);
		c.setCurrentActionPoints(c.getCurrentActionPoints()-2);
		
	}

}
