package model.effects;

import java.util.ArrayList;

import model.abilities.Ability;
import model.abilities.AreaOfEffect;
import model.abilities.DamagingAbility;
import model.world.Champion;

public class Disarm extends Effect {
	

	public Disarm( int duration) {
		super("Disarm", duration, EffectType.DEBUFF);
		
	}

	
	public void apply(Champion c) {
		// TODO Auto-generated method stub
		Ability e = new DamagingAbility("Punch",0,1,1,AreaOfEffect.SINGLETARGET,1,50) ;
		if(searchAbility(c.getAbilities())==-1)
		c.getAbilities().add(e);
		
	}

	
	public void remove(Champion c) {
		// TODO Auto-generated method stub
		if(checkeffect(c.getAppliedEffects())) {
			if(searchAbility(c.getAbilities())==-1) return ;
			c.getAbilities().remove(searchAbility(c.getAbilities()));
		}
	}
	private int searchAbility (ArrayList<Ability> x ) {
		for(int i = 0 ;i< x.size(); i++) {
			if(x.get(i).getName().equals("Punch"))
				return i;
		}
		return -1 ; 
	}
	
	public boolean checkeffect(ArrayList<Effect> x ) {
		   for(int i = 0 ; i < x.size(); i++) {
			   if(x.get(i).getName().equals("Disarm"))
				   return false ;
			  
		   }
		   return true ; 
	   }
	
	
	
	
	
	
	
	
	
	
	
}
