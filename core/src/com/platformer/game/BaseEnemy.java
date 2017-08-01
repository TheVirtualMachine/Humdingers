/*******************************************************************************************
 * Humdingers is a simple fighting platformer game created in Java with the libGDX library.
 * Copyright (C) 2017 Vincent Macri
 * 
 * This file is part of Humdingers.
 *
 * Humdingers is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Humdingers is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Humdingers.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************************/

package com.platformer.game;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Class that all enemies inherit from. Has all of the base code needed for creating an enemy.
 * Originally there were supposed to be multiple different enemies in the game, but that was removed. This class is left in place in case we change our mind again. Also, refactoring is a pain.
 * @author Vincent Macri
 */
public abstract class BaseEnemy extends GameEntity {

	/** How stupid the enemy is. This is used in function that use randomness to determine the enemy's actions. */
	int stupidity;
	/** Whether or not the enemy is thinking. */
	boolean thinking;
	/** If the enemy is currently fighting the player. */
	boolean attackAI;
	/** Whether or not the player has been see yet. */
	boolean seenPlayer;

	/**
	 * 0 - standing left. 1 - standing right. 2 - walking left. 3 - walking right.
	 * 4 - jump left. 5 - jump right. 6 - attack left 7 - attack right.
	 */
	int currentAction, lastAction;

	/**
	 * Basic action performed. Direction is added to get complete action.
	 * 0 - standing. 2 - walking. 4 - jumping. 6 - attacking.
	 */
	int baseAction;

	/**
	 * 0 is left, 1 is right.
	 */
	int direction;

	/**
	 * 
	 * @param loc Location of enemy.
	 * @param newMaxHealth Maximum enemy health.
	 * @param sightRadius Distance enemy can see.
	 * @param attackRadius Distance enemy can attack.
	 * @param newSpeed Base enemy speed.
	 * @param newBaseShape The base shape of the Entity.
	 */
	public BaseEnemy(Vector2 loc, int newMaxHealth, float newSpeed, TextureRegion newBaseShape, BodyDataType newBDType) {
		super(loc, newMaxHealth, newBaseShape, newBDType, 0.5f, StaticVars.ENEMY_MASK, StaticVars.BULLET_MASK);
		seenPlayer = false;
		speed = newSpeed * StaticVars.PIXELS_PER_METER;
		thinking = true; //The enemy defaults to being able to think.
		initFrames();
	}

	@Override
	public void update(){
		if(!seenPlayer){
			seenPlayer = checkSight();
		}else{
			if(thinking){
				runAI();
			}
		}
		getFrame();
	}

	/** Run the enemy AI. This method must be implemented by the subclasses. */
	public abstract void runAI();

	/** Walk towards player. */
	void goToPlayer() {
		
		if (Help.calcXDistance(body.getPosition(), StaticVars.player.body.getPosition()) <= 0.1f){
			Help.setBodyXVelocity(body, 0);
			baseAction = 0;
			return;
		}
		
		if (setDirection() == 0){ //If player is to the left.
			moveLeft(); //Move left.
		}else{ //If player is to the right.
			moveRight();
		}
	}

	/** Walk away from player. */
	void goAwayFromPlayer() {
		if (setDirection() == 0){ //If player is to the left.
			moveRight(); //Move right.
		}else{ //If player is to the right.
			moveLeft(); //Move left.
		}
	}

	/**
	 * Check what direction the enemy should travel.
	 * @return 0 if enemy should go left, else 1.
	 */
	int setDirection() {
		if (body.getPosition().x > StaticVars.player.body.getPosition().x) { //If player is to the left.
			return 0; //Enemy should go left.
		}else{ 
			return 1; //Enemy should go right.
		}
	}

	/** Attack player. */
	public abstract void attack();

	/**
	 * This method checks if the player is within the sight distance of this enemy. Currently the sight distance is 15 meters.
	 * @return If the enemy can see the player.
	 */
	private boolean checkSight() {
		return Help.calcDistance(body.getPosition(), StaticVars.player.body.getPosition()) <= 15f;		
	}

	void moveLeft(){
		Help.setBodyXVelocity(body, -speed);
		direction = 0;
		baseAction = 2;
	}

	void moveRight(){
		Help.setBodyXVelocity(body, speed);
		direction = 1;
		baseAction = 2;
	}

	/**
	 * Make the enemy jump to a given height.
	 * @param meters How high to jump in meters.
	 */
	void jump(float meters){
		if (meters > 0){ // Only do something if we want to jump up.
			// Gravity is given per second but we want time step values here
			float t = 1 / 60f;
			float stepGravity = t * t * StaticVars.world.getGravity().y; // m/s/s

			// Quadratic equation setup (ax� + bx + c = 0).
			float a = 0.5f / stepGravity;
			float b = 0.5f;
			float c = meters;

			// Check both possible solutions.
			float quadraticSolution1 = (float) (-b - Math.sqrt(b*b - 4*a*c) ) / (2*a);
			float quadraticSolution2 = (float) (-b + Math.sqrt(b*b - 4*a*c) ) / (2*a);

			// Use the greater (positive) solution.
			// The * 1.5f makes the enemy overshoot. This looks more natural and less calculated.
			Help.setBodyYVelocity(body, (Math.max(quadraticSolution1, quadraticSolution2) * 60f) * 1.5f);
		}
	}

}
