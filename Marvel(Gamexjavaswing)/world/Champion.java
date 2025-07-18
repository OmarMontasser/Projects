package model.world;

import java.awt.Point;
import java.util.ArrayList;


import model.abilities.Ability;
import model.effects.Effect;

public abstract  class Champion implements Damageable ,Comparable {
	private String name;
	private int maxHP;
	private int currentHP;
	private int mana;
	private int maxActionPointsPerTurn;
	private int currentActionPoints;
	private int attackRange;
	private int attackDamage;
	private int speed;
	private ArrayList<Ability> abilities;
	private ArrayList<Effect> appliedEffects;
	private Condition condition;
	private Point location;
	

	public Champion(String name, int maxHP, int mana, int actions, int speed, int attackRange, int attackDamage) {
		this.name = name;
		this.maxHP = maxHP;
		this.mana = mana;
		this.currentHP = this.maxHP;
		this.maxActionPointsPerTurn = actions;
		this.speed = speed;
		this.attackRange = attackRange;
		this.attackDamage = attackDamage;
		this.condition = Condition.ACTIVE;
		this.abilities = new ArrayList<Ability>();
		this.appliedEffects = new ArrayList<Effect>();
		this.currentActionPoints=maxActionPointsPerTurn;
	}

	public int getMaxHP() {
		return maxHP;
	}

	public String getName() {
		return name;
	}

	public void setCurrentHP(int hp) {

		if (hp < 0) {
			currentHP = 0;
			
		} 
		else if (hp > maxHP)
			currentHP = maxHP;
		else
			currentHP = hp;

	}

	
	public int getCurrentHP() {

		return currentHP;
	}

	public ArrayList<Effect> getAppliedEffects() {
		return appliedEffects;
	}

	public int getMana() {
		return mana;
	}

	public void setMana(int mana) {
		this.mana = mana;
	}

	public int getAttackDamage() {
		return attackDamage;
	}

	public void setAttackDamage(int attackDamage) {
		this.attackDamage = attackDamage;
	}

	public int getSpeed() {
		return speed;
	}

	public void setSpeed(int currentSpeed) {
		if (currentSpeed < 0)
			this.speed = 0;
		else
			this.speed = currentSpeed;
	}

	public Condition getCondition() {
		return condition;
	}

	public void setCondition(Condition condition) {
		this.condition = condition;
	}

	public Point getLocation() {
		return location;
	}

	public void setLocation(Point currentLocation) {
		this.location = currentLocation;
	}

	public int getAttackRange() {
		return attackRange;
	}

	public ArrayList<Ability> getAbilities() {
		return abilities;
	}

	public int getCurrentActionPoints() {
		return currentActionPoints;
	}

	public void setCurrentActionPoints(int currentActionPoints) {
		if(currentActionPoints>maxActionPointsPerTurn)
			currentActionPoints=maxActionPointsPerTurn;
		else 
			if(currentActionPoints<0)
			currentActionPoints=0;
		this.currentActionPoints = currentActionPoints;
	}

	public int getMaxActionPointsPerTurn() {
		return maxActionPointsPerTurn;
	}

	public void setMaxActionPointsPerTurn(int maxActionPointsPerTurn) {
		this.maxActionPointsPerTurn = maxActionPointsPerTurn;
	}

	public int compareTo(Object o) {
		// TODO Auto-generated method stub
		Champion c = (Champion) o;
		if(this.getSpeed()==c.getSpeed())
			return this.getName().compareTo(c.getName());
		return c.getSpeed()-this.getSpeed(); 
	}
	public String toString() {
		return this.getName();
	}
	public String toString2() {
		String s = "";
		s+= "Name :"+ getName();
		if(this instanceof Hero)
			s+= "  , Type : Hero";
		if(this instanceof AntiHero)
			s+= "  , Type : AntiHero";
		if(this instanceof Villain)
			s+= "  , Type : Villain";
		s+= "\n";
		s+="MaxHP : "+ getMaxHP();
		s+= "\n";
		s+="Mana : "+ getMana();
		s+= " , ";
		s+="Speed : "+ getSpeed();
		s+= "\n";
		s+="Maxactions : "+ getMaxActionPointsPerTurn();
		s+=" , ";
		s+="AttackDamage : "+ getAttackDamage();
		s+= "\n";
		s+="AttackRange : "+ getAttackRange();
		s+="\n";
		s+= "Abilities:";
		for(int i = 0 ; i < getAbilities().size(); i++) {
			s+= getAbilities().get(i);
			if(i< getAbilities().size()-1)
				s+="\n";
		}
		return s;
	
	}
    public abstract void useLeaderAbility(ArrayList<Champion> targets) throws CloneNotSupportedException ; 
		// TODO Auto-generated method stub
		
}
