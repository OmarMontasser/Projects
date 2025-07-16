package model.effects;

import java.util.ArrayList;

import model.world.Champion;
import model.world.Condition;

public class Root extends Effect {

	public Root( int duration) {
		super("Root", duration, EffectType.DEBUFF);
		
	}

	@Override
	public void apply(Champion c) {
		// TODO Auto-generated method stub
		if(c.getCondition()==Condition.ACTIVE)
			c.setCondition(Condition.ROOTED);
	}

	@Override
	public void remove(Champion c) {
		// TODO Auto-generated method stub
		if(c.getCondition() == Condition.INACTIVE)
			return ; 
		if(checkeffect(c.getAppliedEffects()))
			c.setCondition(Condition.ACTIVE);
		
	}
   public boolean checkeffect(ArrayList<Effect> x ) {
	   for(int i = 0 ; i < x.size(); i++) {
		   if(x.get(i).getName().equals("Root"))
			   return false ;
		   if(x.get(i).getName().equals("Stun"))
			   return false ;
	   }
	   return true ; 
   }
}
