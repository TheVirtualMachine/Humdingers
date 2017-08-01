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
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * The screen to be shown to the player when they lose.
 * @author Vincent Macri
 */
public class LoseScreen implements Screen {

	/** The Platformer instance to be used. */
	private final Platformer PLATFORMER;
	/** The Stage to hold the contents of the lose screen. */
	private final Stage lose;
	/** The layout Table to hold all of the widgets. */
	private Table table;

	/** A button to send the user back to the main menu. */
	private TextButton backToMenuButton;

	/** The Label that says you lose. */
	private Label loseTitle;

	/** The Label with a message. */
	private Label message;

	/**
	 * Create a new instance of the lose screen.
	 * @param PLATFORMER The Platformer instance to be used.
	 */
	public LoseScreen (final Platformer PLATFORMER) {
		this.PLATFORMER = PLATFORMER;

		lose = new Stage(new ScreenViewport(), this.PLATFORMER.batch);

		table = new Table();

		table.top();
		table.setFillParent(true);

		lose.addActor(table);
		
		// Get the skin.
		Skin skin = this.PLATFORMER.getSkin();
		
		LabelStyle header = skin.get("bold-large", LabelStyle.class);
		LabelStyle contents = skin.get("normal-regular", LabelStyle.class);

		// Create the widgets.

		loseTitle = new Label("YOU LOSE", header);
		message = new Label ("Keep trying", contents);

		backToMenuButton = new TextButton("Main Menu", this.PLATFORMER.getSkin());
		backToMenuButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) { // The changed event for the reset settings button.
				if (backToMenuButton.isPressed()) {
					PLATFORMER.setScreen(PLATFORMER.menuScreen); // Switch to the main menu screen.
				}
			}
		});
	}

	/**
	 * Layout the lose screen.
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	private void layoutLoseScreen(int width, int height) {
		table.clearChildren(); // Clear the table.

		table.add(loseTitle).pad(10f).expandY().top();
		table.row();

		table.add(message).pad(10f).expandY().center();
		table.row();

		table.add(backToMenuButton).pad(10f).expandY().bottom().minSize(width / 4, height / 20).maxSize(width, height / 5).prefSize(width / 3, height / 15);
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(lose);
		table.setDebug(PLATFORMER.settings.isDebug()); // Enable debug lines for tables if necessary.
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		lose.act(Gdx.graphics.getDeltaTime());
		lose.draw();
	}

	@Override
	public void resize(int width, int height) {
		layoutLoseScreen(width, height);
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
	}

}
