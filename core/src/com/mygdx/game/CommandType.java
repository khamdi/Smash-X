package com.mygdx.game;

import com.badlogic.gdx.Input;

public class CommandType {
	
	/* Input enum : not final because we can map controls in game */
	int attack ;
	int special ;
	int projectile;
	
	int  jump;

	int specialMove;
	
	int up ;
	int down ;
	int right ;
	int left ;

	int guard;
	public CommandType(int type){
		switch (type){
			case 1:
				up = Input.Keys.Z;
				down = Input.Keys.S;
				right = Input.Keys.D;
				left = Input.Keys.Q;
				
				jump = Input.Keys.SPACE;
				
				attack = Input.Keys.G;
				special = Input.Keys.H;
				projectile = Input.Keys.J;
				
				
				guard = Input.Keys.B;
				specialMove = Input.Keys.C; 

				break;
			
			case 2:
				up = Input.Keys.UP;
				down = Input.Keys.DOWN;
				right = Input.Keys.RIGHT;
				left = Input.Keys.LEFT;
			 	
				jump = Input.Keys.NUMPAD_0;
				
				attack = Input.Keys.NUMPAD_4;
				special = Input.Keys.NUMPAD_5;
				projectile = Input.Keys.NUMPAD_6;
				guard = Input.Keys.NUMPAD_7;
				specialMove = Input.Keys.NUMPAD_1; 
				break;
				
			case 3:
				up = Input.Keys.O;
				down = Input.Keys.L;
				right = Input.Keys.M;
				left = Input.Keys.K;
				
				jump = Input.Keys.NUMPAD_3;
				
				attack = Input.Keys.NUMPAD_1;
				special = Input.Keys.NUMPAD_2;
				projectile = Input.Keys.NUMPAD_5;
				guard = Input.Keys.NUMPAD_7;

				specialMove = Input.Keys.NUMPAD_6; 

				break;

			case 4 :
				up = Input.Keys.Z;
				down = Input.Keys.S;
				right = Input.Keys.D;
				left = Input.Keys.Q;
				
				jump = Input.Keys.SPACE;
				
				attack = Input.Keys.C;
				special = Input.Keys.V;
				projectile = Input.Keys.X;
				guard = Input.Keys.W;
				specialMove = Input.Keys.E; 

				break;
		}
		
		
	}
	
}
