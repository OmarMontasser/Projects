package model.abilities;

import java.util.ArrayList;

import model.effects.Effect;
import model.world.Champion;
import model.world.Damageable;

public class CrowdControlAbility extends Ability {
	private Effect effect;

	public CrowdControlAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area, int required,
			Effect effect) {
		super(name, cost, baseCoolDown, castRadius, area, required);
		this.effect = effect;

	}

	public Effect getEffect() {
		return effect;
	}

	
	public void incpower() {
		// TODO Auto-generated method stub
		
	}

	
	public void decpower() {
		// TODO Auto-generated method stub
		
	}
	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Champion  x ; 
		for(int i = 0 ; i < targets.size(); i++ ) {
			Effect e = (Effect)((this.getEffect()).clone());
			x = (Champion )targets.get(i);
			e.apply(x);	
			x.getAppliedEffects().add(e);
			
		}
	}
	public String toString () {
		String s = super.toString();
		s+= ":" + this.getName()+ ":"+getEffect().getName();
		return s ;
 	}
	public String toString2() {
		String s = super.toString2();
		s+= "Effcet : "+ this.getEffect();
		return s ;
	}
}
