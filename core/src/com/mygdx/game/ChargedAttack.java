//package com.mygdx.game;
//
//import java.util.ArrayList;
//
//import com.badlogic.gdx.Gdx;
//import com.badlogic.gdx.math.Vector2;
//
//public class ChargedAttack extends AttackPhase {
//	int key;
//	ArrayList<AttackPhase> phases;
//	
//	private ChargedAttack (int key, float maxChargeTime, ArrayList<AttackPhase> phases) {
//		this.key = key;
//		this.totalTime = maxChargeTime;
//		this.currentTime = 0f;
//		this.phases = phases;
//	}
//	
//	public static ChargedAttack onProjectile () {
//		
//	}
// 	
//	@Override
//	boolean hasNextStep() {
//		return Gdx.input.isKeyPressed(key) && currentTime < totalTime;
//	}
//
//	@Override
//	boolean nextStep(boolean rightSide, Vector2 position) {
//		if (hasNextStep ()) {
//			
//		}
//		reset ();
//		return false;
//	}
//
//	@Override
//	void reset() {
//		active = false;
//		currentTime = 0;
//	}
//
//	@Override
//	int hitCharacter(boolean rightSide, Character c) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//}
