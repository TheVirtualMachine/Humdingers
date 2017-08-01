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
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;

/**
 * The main menu screen.
 * @author Vincent Macri
 */
public class MainMenuScreen implements Screen {

	/** @param PLATFORMER The Platformer instance to be used. */
	private final Platformer PLATFORMER;
	/** The Stage which contains all of the main menu options. */
	private Stage menu;
	/** The Table container for the main menu buttons. */
	private Table table;

	/** The play button. */
	private TextButton playButton;
	/** The options button. */
	private TextButton optionsButton;
	/** The credits button. */
	private TextButton creditsButton;
	/** The quit button. */
	private TextButton quitButton;
	/** The license button. */
	private TextButton licenseButton;

	/** The window to display the license information. */
	private Dialog licenseDialog;

	/** The menu title. */
	private Label menuTitle;


	/**
	 * Creates the main menu.
	 * @param PLATFORMER The Platformer instance to be used.
	 */
	public MainMenuScreen(final Platformer PLATFORMER) {
		this.PLATFORMER = PLATFORMER;
		menu = new Stage(new ScreenViewport(), this.PLATFORMER.batch);

		table = new Table();

		table.setFillParent(true);
		table.bottom();
		menu.addActor(table);

		// Get the skin.
		Skin skin = this.PLATFORMER.getSkin();

		// Label style.
		LabelStyle header = skin.get("bold-large", LabelStyle.class);

		// Create widgets.

		menuTitle = new Label("Humdingers", header);

		// Play button.
		playButton = new TextButton("Play", skin.get(TextButtonStyle.class));

		playButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) { // The changed event for the play button.
				if (playButton.isPressed()) {
					PLATFORMER.setScreen(PLATFORMER.introScreen); // Go to the introduction screen.
				}
			}
		});

		// Options button.
		optionsButton = new TextButton("Options", skin.get(TextButtonStyle.class));

		optionsButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) { // The changed event for the reset settings button.
				if (optionsButton.isPressed()) {
					PLATFORMER.setScreen(PLATFORMER.optionsScreen); // Switch to the options screen.
				}
			}
		});

		// Credits button.
		creditsButton = new TextButton("Credits", skin.get(TextButtonStyle.class));

		creditsButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) { // The changed event for the reset settings button.
				if (creditsButton.isPressed()) {
					PLATFORMER.setScreen(PLATFORMER.creditsScreen); // Switch to the credits screen.
				}
			}
		});

		// License dialog.
		licenseDialog = new Dialog("License", skin);
		licenseDialog.button("Dismiss", null, skin.get("transparent-button", TextButtonStyle.class));
		licenseDialog.text("\n"
				+ "Humdingers is a simple fighting platformer game created in Java with the libGDX library.\n"
				+ "Copyright (C) 2017  Vincent Macri, Kayton Yan, and Alex Chen\n\n"
				+ "Humdingers is free software: you can redistribute it and/or modify\n"
				+ "it under the terms of the GNU General Public License as published by\n"
				+ "the Free Software Foundation, either version 3 of the License, or\n"
				+ "(at your option) any later version.\n\n"
				+ "Humdingers is distributed in the hope that it will be useful,\n"
				+ "but WITHOUT ANY WARRANTY; without even the implied warranty of\n"
				+ "MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the\n"
				+ "GNU General Public License for more details.\n\n"
				+ "You should have received a copy of the GNU General Public License\n"
				+ "along with this program.  If not, see <http://www.gnu.org/licenses/>.", skin.get("normal-small", LabelStyle.class));
		// License button.
		licenseButton = new TextButton("License", skin);
		licenseButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) { // The changed event for the reset settings button.
				if (licenseButton.isPressed()) {
					licenseDialog.show(menu);
				}
			}
		});

		// Quit button.
		quitButton = new TextButton("Quit", skin);

		quitButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) { // The changed event for the reset settings button.
				if (quitButton.isPressed()) {
					Gdx.app.exit(); // Stop the game.
				}
			}
		});
	}

	@Override
	public void show() {
		Gdx.input.setInputProcessor(menu);
		Gdx.input.setCursorCatched(false); // Don't lock the cursor on the menu.
		PLATFORMER.music.playSong(MusicManager.Song.MENU_SONG);
		table.setDebug(PLATFORMER.settings.isDebug());
		licenseDialog.setDebug(PLATFORMER.settings.isDebug());
	}

	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(1, 1, 1, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


		menu.act(Gdx.graphics.getDeltaTime());
		menu.draw();

	}

	@Override
	public void resize(int width, int height) {
		menu.getViewport().update(width, height, true);
		layoutMenu(width, height);
	}

	/**
	 * Clear the table and layout the menu. This is useful for when we first make the menu, when the screen size is changed, etc.
	 * @param width The width of the screen.
	 * @param height The height of the screen.
	 */
	private void layoutMenu (int width, int height) {
		table.clear(); // Clear the table.

		// Add the game title.
		table.add(menuTitle).minHeight(height / 6).maxHeight(height / 4).prefHeight(height / 8).expand();

		table.row();

		// Add the buttons.
		table.add(playButton).minSize(width / 4, height / 20).maxSize(width, height / 8).prefSize(width / 2, height / 10).padBottom(10);
		table.row();
		table.add(optionsButton).minSize(width / 4, height / 20).maxSize(width, height / 8).prefSize(width / 2, height / 10).padBottom(10);
		table.row();
		table.add(creditsButton).minSize(width / 4, height / 20).maxSize(width, height / 8).prefSize(width / 2, height / 10).padBottom(10);
		table.row();
		table.add(licenseButton).minSize(width / 4, height / 20).maxSize(width, height / 8).prefSize(width / 2, height / 10).padBottom(10);
		table.row();
		table.add(quitButton).minSize(width / 4, height / 20).maxSize(width, height / 8).prefSize(width / 2, height / 10).padBottom(10);
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
		menu.dispose();
	}

}
