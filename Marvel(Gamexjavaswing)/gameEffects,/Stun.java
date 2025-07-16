package model.effects;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Condition;

public class Stun extends Effect {

	public Stun(int duration) {
		super("Stun", duration, EffectType.DEBUFF);
	}

	@Override
	public void apply(Champion c) {
		// TODO Auto-generated method stub
		c.setCondition(Condition.INACTIVE);
	}

	@Override
	public void remove(Champion c) {
		// TODO Auto-generated method stub
		if(checkStun(c.getAppliedEffects()))return ;
		if(checkRoot(c.getAppliedEffects())) {
			c.setCondition(Condition.ROOTED);
			return ;
		}
		c.setCondition(Condition.ACTIVE);	
	}
	public boolean checkRoot(ArrayList<Effect> x ) {
		   for(int i = 0 ; i < x.size(); i++) {
			   if(x.get(i).getName().equals("Root"))
				   return true ;
			  
		   }
		   return false  ; 
	   }
	public boolean checkStun(ArrayList<Effect> x ) {
		   for(int i = 0 ; i < x.size(); i++) {
			   if(x.get(i).getName().equals("Stun"))
				   return true ;
			  
		   }
		   return false ; 
	   }

}
