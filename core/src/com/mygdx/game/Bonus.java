package com.mygdx.game;

import com.badlogic.gdx.math.Vector2;


public class Bonus {
	enum BonusType {
		ATK,
		VIT,
		DEF,
		PV,
		PS;
	}
	
	final BonusType type;
	final int value;
	float time;
	final Vector2 coord;
	
	public Bonus(BonusType type, int value, Vector2 coord, float time) {
		this.type = type;
		this.time = time;
		this.value = value;
		this.coord = coord;
	}
	
	
	void coolTimeBonus (float delta){
		this.time -= delta; 
	}
	
	void applyBonus (){
		
	}
	
	
}