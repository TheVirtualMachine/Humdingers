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

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * This is the class for the knight enemy. It is the most basic enemy.
 * @author Vincent Macri
 *
 */
public class Knight extends BaseEnemy {

	/** If enemy is currently attacking. */
	boolean attacking;
	/** If enemy has decided behaviour in this attack cycle. */
	boolean attackBehaviourDecided;
	private float animationStateTime;
	private final int meleeAttackDistance = 80;

	/** The time between attacking frames. */
	private final float attackFrameDelay = 0.1f;
	/** The time between non-attacking frames. */
	private final float frameDelay = 0.15f;
	
	/**
	 * The Knight constructor.
	 * @param loc Location of knight.
	 */
	public Knight(Vector2 loc) {
		super(loc, 3, 0.09f, Platformer.knightStandRight.centre, BodyDataType.KNIGHT);
		attackDamage = 5f;
		body.setUserData(this);
		currentPGSI = Platformer.knightStandLeft; //Default.
		attackRange = 1.4f;
	}

	@Override
	public void draw(SpriteBatch batch){
		//System.out.println(currentPGSI + " " + currentPGSI.left + " " + currentPGSI.centre + " " + currentPGSI.right);
		currentPGSI.draw(batch, body);
	}

	@Override
	public void runAI() {
		if (Help.calcDistance(body.getPosition(), StaticVars.player.body.getPosition()) <= attackRange){ //If in melee range.
			if (attacking){ //If we are already attacking.
				attack();
			}else{
				attackAI();
			}
		} else {
			goToPlayer();
		}

	}


	/** Decide if we want to attack. */
	private void attackAI() {
		/** Difference in position. */
		int randomBase = (int) Math.abs(StaticVars.player.body.getPosition().x - body.getPosition().x);
		if(Math.random() * randomBase < 0.1f){ //If we feel like attacking.
			attack();
		}else if (!attackBehaviourDecided){
			if (Math.random() < 0.5 && (randomBase > meleeAttackDistance / 2)){ //Maybe the enemy will walk forward.
				goToPlayer();
			}else{ //Or walk away.
				goAwayFromPlayer();
			}
		}
		attackBehaviourDecided = true;
	}

	@Override
	void getFrame() {
		animationStateTime += GameScreen.deltaTime;
		currentAction = baseAction + direction; //Get complex action.

		if(currentAction != lastAction){ //If we are performing a new action.
			lastAction = currentAction;
			animationStateTime = 0;
		}

		if(currentAction == 0){ //Standing left.
			currentPGSI = Platformer.knightStandLeft;
		}else if(currentAction == 1){ //Standing right.
			currentPGSI = Platformer.knightStandRight;
		}else if(currentAction == 2){ //Walking left.
			currentPGSI = Platformer.knightWalkLeft[Help.calcFrameNumber(animationStateTime, Platformer.knightWalkLeft.length, frameDelay)];
		}else if(currentAction == 3){ //Walking right.
			currentPGSI = Platformer.knightWalkRight[Help.calcFrameNumber(animationStateTime, Platformer.knightWalkRight.length, frameDelay)];
		}else if(currentAction == 4){ //Jumping left.
			currentPGSI = Platformer.knightStandLeft;
		}else if(currentAction == 5){ //Jumping right.
			currentPGSI = Platformer.knightStandRight;
		}else if(currentAction == 6){ //Attacking left.
			currentPGSI = Platformer.knightAttackLeft[Help.calcFrameNumber(animationStateTime, Platformer.knightAttackLeft.length, attackFrameDelay)];
		}else if(currentAction == 7){ //Attacking right.
			currentPGSI = Platformer.knightAttackRight[Help.calcFrameNumber(animationStateTime, Platformer.knightAttackRight.length, attackFrameDelay)];
		}else{ //Default to standing right. Since this is the most likely action, we still check it first.
			currentPGSI = Platformer.knightStandRight;
		}
		 
	}

	/** If we are done attacking. */
	private boolean doneAttacking(){
		if(baseAction == 6){ //It doesn't matter if we are facing left or right, since attack is same length.
			return Help.calcFrameNumber(animationStateTime, Platformer.knightAttackRight.length + 1, attackFrameDelay) == Platformer.knightAttackRight.length;
		}else{
			return false;
		}
	}

	@Override
	public void attack() {
		if (doneAttacking()){
			attacking = false;
			baseAction = 0;
			attackBehaviourDecided = false;
		}else{
			attacking = true;
			baseAction = 6;
			direction = setDirection();
			body.setLinearVelocity(0, body.getLinearVelocity().y);
			
			StaticVars.player.takeDamage(attackDamage * Gdx.graphics.getDeltaTime());
			
		}
	}

}
