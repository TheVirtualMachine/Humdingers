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

/**
 * 
 */
package com.platformer.game;

import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * @author Vincent Macri
 */
public class IntroScreen implements Screen {

	/** The Platformer instance to be used. */
	private final Platformer PLATFORMER;

	/** The Stage to hold the contents of the intro. */
	private final Stage intro;

	/** The Table to hold intro information. */
	private Table table;
	/** A Table to hold the settings for the game controls. */
	private Table controlsTable;

	/** The Label that says what screen we're on. */
	private Label screenHeader;
	/** The Label that stores the intro information. */
	private Label information;
	/** The Label for the controls table. */
	private Label controlsHeader;

	/** Array of Labels to store all of the possible game actions. */
	private Label[] actions;
	/** Array of Labels to store the current control mappings. */
	private Label[] actionControls;

	/** A button to send the user back to the main menu. */
	private TextButton playButton;

	/** The String containing the game story. */
	private String story;

	/**
	 * Create a new IntroScreen.
	 * @param PLATFORMER The Platformer instance to be used.
	 */
	public IntroScreen(final Platformer PLATFORMER) {
		this.PLATFORMER = PLATFORMER;

		story = readStory ("assets/Other/Story.txt");

		intro = new Stage(new ScreenViewport(), this.PLATFORMER.batch);


		table = new Table();

		table.bottom();
		table.setFillParent(true);

		controlsTable = new Table(); // Create the controls Table.


		intro.addActor(table);

		table.setDebug(PLATFORMER.settings.isDebug()); // Enable debug lines for tables if necessary.
		controlsTable.setDebug(PLATFORMER.settings.isDebug());

		// Get the skin.
		Skin skin = this.PLATFORMER.getSkin();

		// The Label styles.
		LabelStyle header = skin.get("bold-big", LabelStyle.class);
		LabelStyle storyStyle = skin.get("normal-small", LabelStyle.class);
		LabelStyle controlsStyle = skin.get("normal-regular", LabelStyle.class);
		
		screenHeader = new Label ("Humdinger Day", header);
		information = new Label (story, storyStyle);
		information.setWrap(true); // Set the story to wrap text.


		actions = new Label[]{
				new Label ("Jump", controlsStyle),
				new Label ("Move right", controlsStyle),
				new Label ("Move left", controlsStyle),
				new Label ("Switch to magic", controlsStyle),
				new Label ("Switch to melee", controlsStyle),
				new Label ("Attack forwards", controlsStyle),
				new Label ("Attack right", controlsStyle),
				new Label ("Attack left", controlsStyle),	
		};

		actionControls = new Label[]{
				new Label(Keys.toString(PLATFORMER.settings.getJumpButton()), controlsStyle),
				new Label(Keys.toString(PLATFORMER.settings.getMoveRightButton()), controlsStyle),
				new Label(Keys.toString(PLATFORMER.settings.getMoveLeftButton()), controlsStyle),			
				new Label(Keys.toString(PLATFORMER.settings.getMagicButton()), controlsStyle),
				new Label(Keys.toString(PLATFORMER.settings.getMeleeButton()), controlsStyle),
				new Label(Keys.toString(PLATFORMER.settings.getAttackButton()), controlsStyle),								
				new Label(Keys.toString(PLATFORMER.settings.getAttackRightButton()), controlsStyle),								
				new Label(Keys.toString(PLATFORMER.settings.getAttackLeftButton()), controlsStyle),
		};

		// Create the button to start the game.
		playButton = new TextButton("Start", this.PLATFORMER.getSkin());

		layoutIntro(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		for (int i = 0; i < actionControls.length; i++) {
			actionControls[i].setAlignment(1);
		}
	}

	/**
	 * @param file The name of the file containing the story.
	 * @return The entire story, as a String.
	 */
	private String readStory(String file) {

		Scanner s = null;

		s = new Scanner(Gdx.files.internal(file).read()); // Create a scanner to read the file.
		s.useDelimiter("\\Z"); 
		String result = s.next();
		s.close();
		return result;
	}

	/**
	 * Layout the intro screen.
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	private void layoutIntro(int width, int height) {
		table.clear(); // Clear the table.

		table.add(screenHeader).padBottom(15f);
		table.row();

		table.add(information).pad(10f).width(width * 0.75f);
		table.row();

		table.add(controlsHeader).pad(10f);
		table.row();

		table.add(controlsTable);

		controlsTable.clear(); // Clear the controls table.
		for (int i = 0; i < actions.length; i++) {
			controlsTable.add(actionControls[i]).pad(5f).fill().width(actionControls[i].getHeight() * 4);
			controlsTable.add(actions[i]).left().pad(5f);
			
			controlsTable.row();
		}
		table.row();

		table.add(playButton).expandY().bottom().minSize(width / 4, height / 20).maxSize(width, height / 5).prefSize(width / 3, height / 15).pad(10f);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(intro);
		table.setDebug(PLATFORMER.settings.isDebug()); // Enable debug lines for tables if necessary.
		controlsTable.setDebug(PLATFORMER.settings.isDebug());
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		intro.act(Gdx.graphics.getDeltaTime());
		intro.draw();

		if (playButton.isPressed()) { // If play is pressed.
			PLATFORMER.setScreen(new GameScreen(PLATFORMER)); // Start the game.
		}
	}

	@Override
	public void resize(int width, int height) {
		layoutIntro(width, height);
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void hide() {
	}

	@Override
	public void dispose() {
		intro.dispose();
	}

}
