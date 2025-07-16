package model.abilities;

import java.util.ArrayList;

import model.world.Damageable;

public  class HealingAbility extends Ability {
	private int healAmount;

	public HealingAbility(String name,int cost, int baseCoolDown, int castRadius, AreaOfEffect area,int required, int healingAmount) {
		super(name,cost, baseCoolDown, castRadius, area,required);
		this.healAmount = healingAmount;
	}

	public int getHealAmount() {
		return healAmount;
	}

	public void setHealAmount(int healAmount) {
		this.healAmount = healAmount;
	}

	public void incpower() {
		// TODO Auto-generated method stub
		int x = (int)(this.getHealAmount()*0.2);
		this.setHealAmount(x+this.getHealAmount());
	}

	public void decpower() {
		// TODO Auto-generated method stub
		int x = (int)(this.getHealAmount()/1.2);
		this.setHealAmount(x);
	}

	@Override
	public void execute(ArrayList<Damageable> targets) {
		// TODO Auto-generated method stub
		Damageable x ; 
		for(int i = 0 ; i < targets.size(); i++ ) {
			x = targets.get(i);
			x.setCurrentHP(x.getCurrentHP()+this.getHealAmount());
		}
		
	}
	public String toString () {
		String s = super.toString();
		s+= " : " + this.getName()+ ": "+ getHealAmount();
		return s ;
 	}
	public String toString2() {
		String s = super.toString2();
		s+="Healing Amount : " +this.getHealAmount();
		return s ;
	}
	

	

}
