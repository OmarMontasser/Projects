package model.abilities;

import java.util.ArrayList;

import model.effects.Effect;
import model.effects.EffectType;
import model.world.Champion;
import model.world.Condition;
import model.world.Damageable;

public class DamagingAbility extends Ability {
	
	private int damageAmount;
	public DamagingAbility(String name, int cost, int baseCoolDown, int castRadius, AreaOfEffect area,int required,int damageAmount) {
		super(name, cost, baseCoolDown, castRadius, area,required);
		this.damageAmount=damageAmount;
	}
	public int getDamageAmount() {
		return damageAmount;
	}
	public void setDamageAmount(int damageAmount) {
		this.damageAmount = damageAmount;
	}
	public void incpower() {
		// TODO Auto-generated method stub
		int x = (int)(this.getDamageAmount()*0.2);
		this.setDamageAmount(x+this.getDamageAmount());
	}

	
	public void decpower() {
		// TODO Auto-generated method stub
		int x = (int)(this.getDamageAmount()/1.2);
		this.setDamageAmount(x);
	}
	public void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		Damageable x ; 
		Champion c ;
		for(int i = 0 ; i < targets.size(); i++ ) {
			x = targets.get(i);
			if(x instanceof Champion) {
				c = (Champion)x ;
				if(hasShield(c.getAppliedEffects())) {
					removeShileld(c);
				}
				else {
					x.setCurrentHP(x.getCurrentHP()-this.getDamageAmount());
					if(x.getCurrentHP() == 0 )
						c.setCondition(Condition.KNOCKEDOUT);
				}
			}else {
				x.setCurrentHP(x.getCurrentHP()-this.getDamageAmount());
			}
			
		}
		
	}
	public boolean hasShield (ArrayList<Effect> x ) {
		for(int i = 0 ; i < x.size(); i++) {
			   if(x.get(i).getName().equals("Shield"))
				   return true ;
			  
		   }
		   return false ; 
	}
	//to be checked later 
	public void removeShileld (Champion c  ) throws CloneNotSupportedException {
		ArrayList<Effect> a = c.getAppliedEffects();
		for(int j = 0 ; j<a.size();j++) {
			if(a.get(j).getName().equals("Shield")) {
				Effect n = (Effect)a.get(j).clone();
				a.remove(j);
				n.remove(c);
				return ; 
			}
		}
	}
	public String toString () {
		String s = super.toString();
		s+= " : " + this.getName()+ ": "+ getDamageAmount();
		return s ;
 	}
	public String toString2() {
		String s = super.toString2();
		s+="Damaging Amount : " + this.getDamageAmount();
		return s ;
	}

}
