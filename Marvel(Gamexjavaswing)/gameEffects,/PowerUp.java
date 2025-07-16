package model.effects;

import model.abilities.Ability;
import model.world.Champion;

public class PowerUp extends Effect {
	

	public PowerUp(int duration) {
		super("PowerUp", duration, EffectType.BUFF);
		
	}

	
	public void apply(Champion c) {
	Ability x  ;
		for(int i = 0 ; i < c.getAbilities().size(); i++) {
			(c.getAbilities().get(i)).incpower(); 
			
		}
	}

	
	
	public void remove(Champion c) {
		Ability x  ; 
		for(int i = 0 ; i < c.getAbilities().size(); i++) {
			x = c.getAbilities().get(i); 
			x.decpower();
		}
		
	}
	
}
