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
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;

/**
 * The player class. Contains methods and variables for handling player actions.
 * @author Vincent Macri
 */
public class Player extends GameEntity {

	/** How long the player has been running this animation. */
	private float animationStateTime;

	/**
	 * 0 - standing left. 1 - standing right. 2 - walking left. 3 - walking
	 * right. 4 - jump left. 5 - jump right. 6 - attack left 7 - attack right.
	 */
	private int currentAction, lastAction;

	/**
	 * Basic action performed. Direction is added to get complete action.
	 * 0 - standing. 2 - walking. 4 - jumping. 6 - melee attack. 8 - fireball attack
	 */
	private int basicAction;

	/**
	 * 0 is left, 1 is right.
	 */
	private int direction;

	/** The time between attacking frames. */
	private final float ATTACK_FRAME_DELAY = 0.1f;
	/** The time between non-attacking frames. */
	private final float FRAME_DELAY = 0.15f;

	/** The amount of mana the player currently has. */
	public float mana;

	/** The maximum mana the player can have. */
	private static final int MAX_MANA = 100;

	/** How much mana a fireball uses. */
	private static final float FIREBALL_MANA_COST = 10;

	/** If the fireball being created has been shot yet. */
	private boolean fireballShot;

	public Player(Vector2 loc) {
		super(loc, 100, Platformer.playerStandRight.centre, BodyDataType.PLAYER, 0.5f, StaticVars.PLAYER_MASK, StaticVars.PLATFORM_MASK);
		speed = 0.125f * StaticVars.PIXELS_PER_METER;
		mana = 100;
		//Default states.
		currentAction = 1;
		lastAction = 1;
		basicAction = 0;
		direction = 1;
		healRate = 0.3f;
		animationStateTime = 0;
		attackRange = 1.4f;
		attackDamage = 5f;
		body.setUserData(this);
	}

	/** Player update method. */
	@Override
	public void update() {
		updateMana();
		animationStateTime += GameScreen.deltaTime;
		healDamage(healRate * GameScreen.deltaTime);
		moveX();
		if (basicAction == 6){ // If we are doing a melee attack.
			attack();
		}else if (basicAction == 8){ // If we are shooting a fireball.
			handleFireball();
		}
		getFrame();
	}

	/** Update how much mana the player has. */
	private void updateMana() {
		mana += 5 * GameScreen.deltaTime;
		if (mana > MAX_MANA) { // Stop mana from exceeding max mana.
			mana = MAX_MANA;
		}
	}

	/** Manage the playing of the fireball casting animation and the creation of the fireball itself. */
	private void handleFireball() {
		if (Help.hasFramePassed(animationStateTime, ATTACK_FRAME_DELAY, Platformer.playerFireLeft.length - 2) && !fireballShot){ // If it is time to shoot the fireball.
			fireballShot = true;
			StaticVars.bulletList.add(new Fireball(new Vector2(
					body.getPosition().x * StaticVars.PIXELS_PER_METER - currentPGSI.centre.getRegionWidth() / 2 +
					(direction == 0 ? -Platformer.fireballLeft.getRegionWidth() : currentPGSI.centre.getRegionWidth()),

					body.getPosition().y * StaticVars.PIXELS_PER_METER - baseShape.getRegionHeight() / 4), 
					new Vector2(direction == 0 ? -50 : 50, 10)));

			mana -= FIREBALL_MANA_COST; // Subtract the mana cost of the fireball.
		}
		// If the attack animation has finished.
		if (Help.hasFramePassed(animationStateTime, ATTACK_FRAME_DELAY, Platformer.playerFireLeft.length - 1)){
			// Check if a movement key is being held down.
			if (direction == 0) { // If facing left, prioritize moving left.
				if (Gdx.input.isKeyPressed(Input.Keys.A)){
					moveLeft();
				}else if (Gdx.input.isKeyPressed(Input.Keys.D)){
					moveRight();
				}else{
					stopMoving();
				}
			} else { // Else if facing racing right, prioritize moving right.
				if (Gdx.input.isKeyPressed(Input.Keys.D)){
					moveRight();
				}else if (Gdx.input.isKeyPressed(Input.Keys.A)){
					moveLeft();
				}else{
					stopMoving();
				}
			}

		}

	}

	/** Do damage to the enemies. */
	private void attack() {

		// If the attack is over.
		if (Help.hasFramePassed(animationStateTime, ATTACK_FRAME_DELAY, Platformer.playerAttackRight.length - 1)){
			// Check if a movement key is being held down.
			if (direction == 0) { // If facing left, prioritize moving left.
				if (Gdx.input.isKeyPressed(Input.Keys.A)){
					moveLeft();
				}else if (Gdx.input.isKeyPressed(Input.Keys.D)){
					moveRight();
				}else{
					stopMoving();
				}
			} else { // Else if facing racing right, prioritize moving right.
				if (Gdx.input.isKeyPressed(Input.Keys.D)){
					moveRight();
				}else if (Gdx.input.isKeyPressed(Input.Keys.A)){
					moveLeft();
				}else{
					stopMoving();
				}
			}
			return;
		}

		for (BaseEnemy enemy : StaticVars.enemyList) {
			if (Help.calcDistance(body.getPosition(), enemy.body.getPosition()) <= attackRange){
				if (direction == 0){ // If facing left.
					if (body.getPosition().x >= enemy.body.getPosition().x){ // If enemy is to the left of the player.
						enemy.takeDamage(attackDamage * Gdx.graphics.getDeltaTime());
					}
				}else if(body.getPosition().x <= enemy.body.getPosition().x) { // Else if facing right (implied) and enemy is to the right.
					enemy.takeDamage(attackDamage * Gdx.graphics.getDeltaTime());
				}
			}
		}
	}

	@Override
	public void draw(SpriteBatch batch){
		currentPGSI.draw(batch, body);
	}

	@Override
	void getFrame() {

		currentAction = basicAction + direction; // Get complex action.

		if(currentAction != lastAction){ // If we are performing a new action.
			lastAction = currentAction;
			animationStateTime = 0;
		}

		if(currentAction == 0){ //Standing left.
			currentPGSI = Platformer.playerStandLeft;
		}else if(currentAction == 1){ //Standing right.
			currentPGSI = Platformer.playerStandRight;
		}else if(currentAction == 2){ //Walking left.
			currentPGSI = Platformer.playerWalkLeft[Help.calcFrameNumber(animationStateTime, Platformer.playerWalkLeft.length, FRAME_DELAY)];
		}else if(currentAction == 3){ //Walking right.
			currentPGSI = Platformer.playerWalkRight[Help.calcFrameNumber(animationStateTime, Platformer.playerWalkRight.length, FRAME_DELAY)];
		}else if(currentAction == 4){ //Jumping left.
			currentPGSI = Platformer.playerStandLeft; //There is no jump animation yet.
		}else if(currentAction == 5){ //Jumping right.
			currentPGSI = Platformer.playerStandRight; //There is no jump animation yet.
		}else if(currentAction == 6){ //Attacking left.
			currentPGSI = Platformer.playerAttackLeft[Help.calcFrameNumber(animationStateTime, Platformer.playerAttackLeft.length, ATTACK_FRAME_DELAY)];
		}else if(currentAction == 7){ //Attacking right.
			currentPGSI = Platformer.playerAttackRight[Help.calcFrameNumber(animationStateTime, Platformer.playerAttackRight.length, ATTACK_FRAME_DELAY)];
		}else if (currentAction == 8) { // Shoot fireball to left.
			currentPGSI = Platformer.playerFireLeft[Help.calcFrameNumber(animationStateTime, Platformer.playerFireLeft.length, ATTACK_FRAME_DELAY)];
		}else if (currentAction == 9) { // Shoot fireball to right.
			currentPGSI = Platformer.playerFireRight[Help.calcFrameNumber(animationStateTime, Platformer.playerFireRight.length, ATTACK_FRAME_DELAY)];
		}else{ //Default to standing right. Since this is the most likely action, we still check it first. Also, not skipping '1' makes my code look nicer.
			currentPGSI = Platformer.playerStandRight;
		}
	}

	// Set variables to start jumping.
	void jump() {
		if (Math.abs(body.getLinearVelocity().y)<= StaticVars.BASICALLY_ZERO) {
			//body.applyLinearImpulse(new Vector2(0, jumpPower), body.getPosition(), true);
			Help.setBodyYVelocity(body, jumpPower);
		}
	}

	/** Start to shoot a fireball to the left. */
	public void startFireLeft () {
		if (mana >= FIREBALL_MANA_COST) { // Make sure we have enough mana, otherwise the player may change directions when they probably didn't want to.
			direction = 0; // Set out direction to left.
			prepareFireball(); // Prepare the fireball.
		}
	}

	/** Start to shoot a fireball to the right. */
	public void startFireRight () {
		if (mana >= FIREBALL_MANA_COST) { // Make sure we have enough mana, otherwise the player may change directions when they probably didn't want to.
			direction = 1; // Set our direction to right.
			prepareFireball(); // Prepare the fireball.
		}
	}

	/** Start to do a melee attack to the left. */
	public void startAttackLeft() {
		direction = 0;
		prepareAttack();
	}

	/** Start to do a melee attack to the right. */
	public void startAttackRight() {
		direction = 1;
		prepareAttack();
	}

	/** Set the player's action to standing. */
	public void stopMoving() {
		basicAction = 0;
	}

	public void moveLeft() {
		direction = 0;
		basicAction = 2;
	}

	public void moveRight() {
		direction = 1;
		basicAction = 2;
	}

	/** Prepare a fireball to be shot. */
	private void prepareFireball () {
		if (basicAction == 8) { // If we are already shooting a fireball.
			return; // Do nothing.
		}
		basicAction = 8; // Set the basic action to fireball.
		animationStateTime = 0; // Reset the timer, or else the fireball may be fired too soon.
		fireballShot = false; // Make sure that we know that we have not fired the fireball yet.
	}

	/** Do checks and set variables needed for melee attack. */
	private void prepareAttack () {
		if (basicAction == 6) { // If we are already attacking.
			return; // Do nothing.
		}
		basicAction = 6;
		animationStateTime = 0; // Reset the timer, or else we may get stuck.
	}

	/** Handle x velocity. */
	private void moveX(){
		if (basicAction == 2){ // If we are walking.
			if (direction == 0){ // And going left.
				body.setLinearVelocity(-speed, body.getLinearVelocity().y);
			}else{ // Else if we are going right.
				body.setLinearVelocity(speed, body.getLinearVelocity().y);
			}
		}else if ((basicAction == 6 || basicAction == 8) && Math.abs(body.getLinearVelocity().y) <= StaticVars.BASICALLY_ZERO){ // Else if we are attacking and on ground.
			body.setLinearVelocity(0, body.getLinearVelocity().y); // Set x velocity to 0.
		}else if (basicAction == 0){ // Else if we are standing.
			if (body.getLinearVelocity().x != 0){
				if (Math.abs(body.getLinearVelocity().x) <= 0.1){
					body.setLinearVelocity(0f, body.getLinearVelocity().y);
				}else{
					Help.setBodyXVelocity(body, body.getLinearVelocity().x / 2);
				}
			}

		}
		// Don't do anything to velocity while jumping.
	}

	/**
	 * Getter for direction.
	 * 0 is left, 1 is right.
	 * @return direction
	 */
	public int getDirection() {
		return direction;
	}

}
