package model.world;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.effects.Embrace;

public class Hero extends Champion implements Damageable {

	public Hero(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}

	
	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException  {
		// TODO Auto-generated method stub
		if(targets == null )return ;
		Effect n ; 
		Effect e = new Embrace(2) ; 
		
		for(int i = 0 ; i < targets.size(); i++) {
			Champion c = targets.get(i);
			for(int j = 0 ; j<c.getAppliedEffects().size();j++) {
				if(c.getAppliedEffects().get(j).getType() == EffectType.DEBUFF) {
					 n =  (Effect)(c.getAppliedEffects().get(j)).clone();
					 c.getAppliedEffects().remove(j);
				     n.remove(c);
				     j--;
				}
			
		}
		    Effect t = (Effect)e.clone();
			c.getAppliedEffects().add(t);
			t.apply(targets.get(i));
		}
	}

	
}
