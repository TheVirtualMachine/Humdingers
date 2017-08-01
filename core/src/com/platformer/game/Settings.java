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
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.Json;

/**
 * This class holds the settings for the game settings. It also reads and stores them in a file so the settings don't change when the user quits then plays again later.
 * @author Vincent Macri
 */
public class Settings {

	/** The button that makes the player jump. */
	private int jumpButton;
	/** The button that makes the player move right. */
	private int moveRightButton;
	/** The button that makes the player move left. */
	private int moveLeftButton;
	/** The button that switches the player to magic. */
	private int magicButton;
	/** The button that switches the player to melee. */
	private int meleeButton;
	/** The button that makes the player attack in the direction they are currently facing. */
	private int attackButton;
	/** The button that makes the player attack right. */	
	private int attackRightButton;
	/** The button that makes the player attack left. */	
	private int attackLeftButton;

	/** The volume of the game music, in the range [0, 1]. */
	private float musicLevel;

	/** If the game is in fullscreen mode. */
	private boolean fullscreen;

	/** If the game is in debug mode. */
	private boolean debug;

	/**
	 * Create a new Settings instance with no arguments.
	 * This is used by the JSON parser to create a new instance of Settings before settings the values to the ones stores in the settings file.
	 */
	public Settings () {
	}

	/**
	 * Create a new Settings instance.
	 * @param readFromFile True if the settings should be set to the values in the settings file.
	 */
	public Settings (boolean readFromFile) {
		if (readFromFile) {
			readOptions();
		}
	}

	/**
	 * If the settings file is valid, use those settings. Else, reset the settings.
	 */
	public void readOptions() {
		FileHandle f = Gdx.files.local(StaticVars.SETTINGS_FILE_NAME);

		Json json = new Json();
		Settings savedOptions;
		try {
			savedOptions = json.fromJson(Settings.class, f);
		} catch (Exception e) {
			System.out.println("Invalid settings.");
			resetOptions();
			writeOptions();
			return;
		}

		jumpButton = savedOptions.jumpButton;
		moveRightButton = savedOptions.moveRightButton;
		moveLeftButton = savedOptions.moveLeftButton;
		magicButton = savedOptions.magicButton;
		meleeButton = savedOptions.meleeButton;
		attackButton = savedOptions.attackButton;
		attackRightButton = savedOptions.attackRightButton;
		attackLeftButton = savedOptions.attackLeftButton;
		musicLevel = savedOptions.musicLevel;
		fullscreen = savedOptions.fullscreen;
		debug = savedOptions.debug;
	}

	/**
	 * Reset all of the settings to the default values.
	 */
	public void resetOptions() {
		this.jumpButton = Keys.W;
		this.moveRightButton = Keys.D;
		this.moveLeftButton = Keys.A;

		this.magicButton = Keys.E;
		this.meleeButton = Keys.Q;

		this.attackButton = Keys.SPACE;
		this.attackRightButton = Keys.RIGHT;
		this.attackLeftButton = Keys.LEFT;

		this.musicLevel = 1f;
		this.fullscreen = true;
		this.debug = false;
	}

	/**
	 * Write the currents settings as JSON to Settings.json.
	 */
	public void writeOptions() {
		FileHandle f = Gdx.files.local(StaticVars.SETTINGS_FILE_NAME);

		Json json = new Json();
		json.setUsePrototypes(false);

		f.writeString(json.prettyPrint(json.toJson(this)), false);
	}

	/**
	 * Getter for jumpButton.
	 * @return jumpButton
	 */
	public int getJumpButton() {
		return jumpButton;
	}

	/**
	 * Setter for jumpButton.
	 * @param jumpButton The new value of jumpButton.
	 */
	public void setJumpButton(int jumpButton) {
		this.jumpButton = jumpButton;
	}

	/**
	 * Getter for moveRightButton.
	 * @return moveRightButton
	 */
	public int getMoveRightButton() {
		return moveRightButton;
	}

	/**
	 * Setter for moveRightButton.
	 * @param moveRightButton The new value of moveRightButton.
	 */
	public void setMoveRightButton(int moveRightButton) {
		this.moveRightButton = moveRightButton;
	}

	/**
	 * Getter for moveLeftButton.
	 * @return moveLeftButton
	 */
	public int getMoveLeftButton() {
		return moveLeftButton;
	}

	/**
	 * Setter for moveLeftButton.
	 * @param moveLeftButton The new value of moveLeftButton.
	 */
	public void setMoveLeftButton(int moveLeftButton) {
		this.moveLeftButton = moveLeftButton;
	}

	/**
	 * Getter for magicButton.
	 * @return magicButton
	 */
	public int getMagicButton() {
		return magicButton;
	}

	/**
	 * Setter for magicButton.
	 * @param magicButton The new value of magicButton.
	 */
	public void setMagicButton(int magicButton) {
		this.magicButton = magicButton;
	}

	/**
	 * Getter for meleeButton.
	 * @return meleeButton
	 */
	public int getMeleeButton() {
		return meleeButton;
	}

	/**
	 * Setter for meleeButton.
	 * @param meleeButton The new value of meleeButton.
	 */
	public void setMeleeButton(int meleeButton) {
		this.meleeButton = meleeButton;
	}

	/**
	 * Getter for attackButton.
	 * @return attackButton
	 */
	public int getAttackButton() {
		return attackButton;
	}

	/**
	 * Setter for attackButton.
	 * @param attackButton The new value of attackButton.
	 */
	public void setAttackButton(int attackButton) {
		this.attackButton = attackButton;
	}

	/**
	 * Getter for attackRightButton.
	 * @return attackRightButton
	 */
	public int getAttackRightButton() {
		return attackRightButton;
	}

	/**
	 * Setter for attackRightButton.
	 * @param attackRightButton The new value of attackRightButton.
	 */
	public void setAttackRightButton(int attackRightButton) {
		this.attackRightButton = attackRightButton;
	}

	/**
	 * Getter for attackLeftButton.
	 * @return attackLeftButton
	 */
	public int getAttackLeftButton() {
		return attackLeftButton;
	}

	/**
	 * Setter for attackLeftButton.
	 * @param attackLeftButton The new value of attackLeftButton.
	 */
	public void setAttackLeftButton(int attackLeftButton) {
		this.attackLeftButton = attackLeftButton;
	}

	/**
	 * Getter for musicLevel.
	 * @return musicLevel
	 */
	public float getMusicLevel() {
		return musicLevel;
	}

	/**
	 * Setter for musicLevel.
	 * @param musicLevel The new value of musicLevel.
	 */
	public void setMusicLevel(float musicLevel) {
		this.musicLevel = musicLevel;
	}

	/**
	 * Getter for fullscreen.
	 * @return fullscreen
	 */
	public boolean isFullscreen() {
		return fullscreen;
	}

	/**
	 * Setter for fullscreen.
	 * @param fullscreen The new value of fullscreen.
	 */
	public void setFullscreen(boolean fullscreen) {
		this.fullscreen = fullscreen;
	}

	/**
	 * Getter for debug.
	 * @return debug
	 */
	public boolean isDebug() {
		return debug;
	}

	/**
	 * Setter for debug.
	 * @param debug The new value of debug.
	 */
	public void setDebug(boolean debug) {
		this.debug = debug;
	}


}
