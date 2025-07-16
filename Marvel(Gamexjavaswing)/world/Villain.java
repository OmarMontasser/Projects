package model.world;

import java.util.ArrayList;

public class Villain extends Champion {

	public Villain(String name, int maxHP, int maxMana, int actions, int speed, int attackRange, int attackDamage) {
		super(name, maxHP, maxMana, actions, speed, attackRange, attackDamage);

	}

	@Override
	public void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException {
		// TODO Auto-generated method stub
		if(targets == null )return ;
		for(int i = 0 ; i < targets.size(); i++) {
			Champion c = targets.get(i);
		/*	if((int)c.getCurrentHP() < (int)(c.getMaxHP() * 0.3)) {
						
				c.setCurrentHP(0);
				c.setCondition(Condition.KNOCKEDOUT);		
			}*/
			//	if((int)c.getCurrentHP()/(int)c.getMaxHP() < 0.3) {
			//	System.out.println(c.getCurrentHP());
			//	System.out.println(c.getMaxHP());
				c.setCurrentHP(0);
				c.setCondition(Condition.KNOCKEDOUT);	
		//	}
		}
	}

	
	

	
}
