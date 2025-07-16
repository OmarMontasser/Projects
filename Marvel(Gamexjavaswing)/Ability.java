package model.abilities;

import java.util.ArrayList;

import model.world.Damageable;

public abstract  class Ability {
	private String name;
	private int manaCost;
	private int baseCooldown;
	private int currentCooldown;
	private int castRange;
	private AreaOfEffect castArea;
	private int requiredActionPoints;

	public Ability(String name, int cost, int baseCoolDown, int castRange, AreaOfEffect area, int required) {
		this.name = name;
		this.manaCost = cost;
		this.baseCooldown = baseCoolDown;
		this.currentCooldown = 0;
		this.castRange = castRange;
		this.castArea = area;
		this.requiredActionPoints = required;
	}

	public int getCurrentCooldown() {
		return currentCooldown;
	}

	public void setCurrentCooldown(int currentCoolDown) {
		if (currentCoolDown < 0)
			currentCoolDown = 0;
		else if (currentCoolDown > baseCooldown)
			currentCoolDown = baseCooldown;
		this.currentCooldown = currentCoolDown;
	}

	public String getName() {
		return name;
	}

	public int getManaCost() {
		return manaCost;
	}

	public int getBaseCooldown() {
		return baseCooldown;
	}

	public int getCastRange() {
		return castRange;
	}

	public AreaOfEffect getCastArea() {
		return castArea;
	}

	public int getRequiredActionPoints() {
		return requiredActionPoints;
	}
   public abstract void incpower() ;	   
   
   public abstract void decpower(); 
   public abstract void execute(ArrayList<Damageable> targets) throws CloneNotSupportedException ;
   public String toString () {
	   String s = "";
	   if(this instanceof DamagingAbility)
		   s+= "DAM";
	   if(this instanceof HealingAbility)
		   s+= "HEAL";
	   if(this instanceof CrowdControlAbility)
		   s+="CCA";
	   return s;
   }
   public String toString2() {
	   String s = this.getName() + " : ";
	   if(this instanceof DamagingAbility)
		   s+= "DamagingAbility";
	   if(this instanceof HealingAbility)
		   s+= "HealingAbility";
	   if(this instanceof CrowdControlAbility)
		   s+="CrowdControlAbility";
	   s+="\n";
	   s+= this.getCastArea()+ " : "+ this.getCastRange();
	   s+="Mana Cost :"+ this.getManaCost()+ " , Action cost :"+this.getRequiredActionPoints()+" ";
	   s+="\n";
	   s+="Base "+this.getBaseCooldown()+" ,Current "+this.getCurrentCooldown()+" ";
	   s+="\n";
	   return s;
   }
}
