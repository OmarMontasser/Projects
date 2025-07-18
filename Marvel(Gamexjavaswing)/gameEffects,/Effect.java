package model.effects;

import model.world.Champion;

public abstract class Effect implements Cloneable {
	private String name;
	private EffectType type;
	private int duration;

	public Effect(String name, int duration, EffectType type) {
		this.name = name;
		this.type = type;
		this.duration = duration;
	}

	public String getName() {
		return name;
	}

	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}

	public EffectType getType() {
		return type;
	}
	
	public  Object clone() throws CloneNotSupportedException {  
		// TODO Auto-generated method stub  
		return super.clone();  
		}  
	public abstract void apply(Champion c) ; 
	public abstract void remove(Champion c) ;
	public String toString() {
		return  this.getName()+" , Duration : "+this.getDuration(); 
	}

}
