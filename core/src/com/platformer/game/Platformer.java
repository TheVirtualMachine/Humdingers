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

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.scenes.scene2d.ui.CheckBox.CheckBoxStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton.TextButtonStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Window.WindowStyle;

/**
 * The main game class. This is the entrance point for the game.
 * @author Vincent Macri
 */
public class Platformer extends Game {

	/** The SpriteBatch for drawing. */
	public SpriteBatch batch;
	/** The AssetManager. It manages all of the game's assets. */
	protected AssetManager assets;

	/** The number of background textures. */
	protected final int NUMBER_OF_BACKGROUND_TEXTURES = 10;

	/** The normal font to use. */
	protected final String NORMAL_FONT = "LiberationMono-Regular";
	/** The bold font to use. */
	protected final String BOLD_FONT = "LiberationMono-Bold";

	/** The size of the small font. */
	protected final int SMALL_FONT_SIZE = 30;
	/** The size of the regular font. */
	protected final int REGULAR_FONT_SIZE = 50;
	/** The size of the big font. */
	protected final int BIG_FONT_SIZE = 90;
	/** The size of the large font. */
	protected final int LARGE_FONT_SIZE = 150;


	/** The game's MusicManager. Handles which song to play, music fading in and out, etc. */
	protected MusicManager music;
	/** The settings for the game. */
	protected Settings settings;

	/** The main menu screen. */
	protected MainMenuScreen menuScreen;
	/** The options screen. */
	protected OptionsScreen optionsScreen;
	/** The tutorial screen. */
	protected IntroScreen introScreen;
	/** The credits screen. */
	protected CreditsScreen creditsScreen;
	/** The lose screen. */
	protected LoseScreen loseScreen;
	/** The win screen. */
	protected WinScreen winScreen;

	/** The player's stand right PGSI. */
	protected static PGSI playerStandRight;
	/** The player's stand left PGSI. */
	protected static PGSI playerStandLeft;
	/** The player's attack right PGSI array. */
	protected static PGSI[] playerAttackRight;
	/** The player's attack left PGSI array. */
	protected static PGSI[] playerAttackLeft = new PGSI[3];
	/** The player's walk right PGSI array. */
	protected static PGSI[] playerWalkRight = new PGSI[2];
	/** The player's walk left PGSI array. */
	protected static PGSI[] playerWalkLeft = new PGSI[2];
	/** The player's shoot fireball right PGSI array. */
	protected static PGSI[] playerFireRight = new PGSI[5];
	/** The player's shoot fireball left PGSI array. */
	protected static PGSI[] playerFireLeft = new PGSI[5];

	/** The fireball that moves to the right. */
	protected static TextureRegion fireballRight;
	/** The fireball that moves to the left. */
	protected static TextureRegion fireballLeft;

	/** The knight's stand right PGSI. */
	protected static PGSI knightStandRight;
	/** The knight's stand left PGSI. */
	protected static PGSI knightStandLeft;
	/** The player's attack right PGSI. */
	protected static PGSI[] knightAttackRight = new PGSI[7];
	/** The player's attack left PGSI. */
	protected static PGSI[] knightAttackLeft = new PGSI[7];
	/** The player's walk right PGSI array. */
	protected static PGSI[] knightWalkRight = new PGSI[2];
	/** The player's walk left PGSI array. */
	protected static PGSI[] knightWalkLeft = new PGSI[2];
	
	@Override
	public void create() {
		settings = new Settings(true);

		if(!settings.isFullscreen()){ // If the settings file says to be in windowed mode.
			// Set resolution to default and set full-screen to false.
			Gdx.graphics.setWindowedMode(Gdx.graphics.getDisplayMode().width, Gdx.graphics.getDisplayMode().height);
		}

		batch = new SpriteBatch(); // Create the SpriteBatch.
		assets = new AssetManager(); // Create the AssetManager.

		// Allow loading FreeTypeFonts.
		FileHandleResolver resolver = new InternalFileHandleResolver();
		assets.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		assets.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));

		loadAssets(); // Load the game assets.

		// Create the MusicManager.
		music = new MusicManager(assets.get("assets/Audio/AdventureMeme.mp3", Music.class),
				assets.get("assets/Audio/Overworld.mp3", Music.class),
				assets.get("assets/Audio/BlipStream.mp3", Music.class),
				settings);

		// Create the screens that don't change much.
		menuScreen = new MainMenuScreen(this);
		optionsScreen = new OptionsScreen(this);
		introScreen = new IntroScreen(this);
		creditsScreen = new CreditsScreen(this);
		loseScreen = new LoseScreen(this);
		winScreen = new WinScreen(this);

		this.setScreen(menuScreen);
	}

	/** Load game's the assets into {@link #assets}. */
	private void loadAssets() {
		long loadStartTime = System.nanoTime();

		// Load the normal fonts.
		loadFont(NORMAL_FONT, SMALL_FONT_SIZE);
		loadFont(NORMAL_FONT, REGULAR_FONT_SIZE);

		// Load the bold fonts.
		loadFont(BOLD_FONT, BIG_FONT_SIZE);
		loadFont(BOLD_FONT, LARGE_FONT_SIZE);

		assets.finishLoading(); // Make sure fonts are finished loading before moving on to loading the skin, which uses these fonts.

		// Load the Skin.
		loadSkin();

		// Load the music.
		assets.load("assets/Audio/AdventureMeme.mp3", Music.class); // Load the menu music.
		assets.load("assets/Audio/Overworld.mp3", Music.class); // Load the background music.
		assets.load("assets/Audio/BlipStream.mp3", Music.class); // Load the credits music.

		// Load the TextureAtlases.
		assets.load("assets/Environment/Clouds/Clouds.atlas", TextureAtlas.class); // Load the clouds.
		loadPGSI();

		// Load the Textures.
		assets.load("assets/Other/Logo.png", Texture.class); // The logo.
		assets.load("assets/UI/Bars/HealthBar.png", Texture.class); // The health bar texture.
		assets.load("assets/UI/Bars/ManaBar.png", Texture.class); // The health bar texture.

		for (int i = 0; i < NUMBER_OF_BACKGROUND_TEXTURES; i++) { // Load all of the level background images.
			assets.load("assets/Environment/Level1/Level1_"+ i + ".png", Texture.class); 
		}



		assets.finishLoading(); // Load everything before continuing.
		System.out.println("Game loaded in " + (System.nanoTime() - loadStartTime) + " nanoseconds.");
	}

	/**
	 * Load the PGSI for the player and the knight.
	 */
	private void loadPGSI() {
		// Player resources.
		assets.load("assets/PlayerSprites/Player.atlas", TextureAtlas.class); // Load the player atlas.
		assets.finishLoadingAsset("assets/PlayerSprites/Player.atlas"); // Make sure the atlas is loaded before continuing.

		TextureAtlas playerAtlas = assets.get("assets/PlayerSprites/Player.atlas", TextureAtlas.class); // Local variable for readability.

		playerStandRight = new PGSI(playerAtlas.findRegion("SheathedIdleRightSL"), playerAtlas.findRegion("SheathedIdleRightSC"), playerAtlas.findRegion("SheathedIdleRightSR"));

		playerStandLeft = new PGSI(playerAtlas.findRegion("SheathedIdleLeftSL"), playerAtlas.findRegion("SheathedIdleLeftSC"), playerAtlas.findRegion("SheathedIdleLeftSR"));

		playerAttackRight = new PGSI[3];
		playerAttackLeft = new PGSI[3];
		playerWalkRight = new PGSI[2];
		playerWalkLeft = new PGSI[2];
		playerFireRight = new PGSI[5];
		playerFireLeft = new PGSI[5];

		fireballRight = playerAtlas.findRegion("FireballRight");
		fireballLeft = playerAtlas.findRegion("FireballLeft");

		// Knight resources.
		assets.load("assets/Enemies/Knight/Knight.atlas", TextureAtlas.class); // Load the knight atlas.
		assets.finishLoadingAsset("assets/Enemies/Knight/Knight.atlas"); // Make sure the atlas is loaded before continuing.

		TextureAtlas knightAtlas = assets.get("assets/Enemies/Knight/Knight.atlas", TextureAtlas.class); // Local variable for readability.

		knightStandRight = new PGSI(knightAtlas.findRegion("KnightIdleRightSL"), knightAtlas.findRegion("KnightIdleRightSC"), knightAtlas.findRegion("KnightIdleRightSR"));
		knightStandLeft = new PGSI(knightAtlas.findRegion("KnightIdleLeftSL"), knightAtlas.findRegion("KnightIdleLeftSC"), knightAtlas.findRegion("KnightIdleLeftSR"));
		knightAttackRight = new PGSI[7];
		knightAttackLeft = new PGSI[7];
		knightWalkRight = new PGSI[2];
		knightWalkLeft = new PGSI[2];

		// Load the walking PGSIs.
		for (int i = 0; i < playerWalkRight.length; i++){
			playerWalkRight[i] = new PGSI(playerAtlas.findRegion("SheathedWalkRightSL", i), playerAtlas.findRegion("SheathedWalkRightSC", i), playerAtlas.findRegion("SheathedWalkRightSR", i));
			playerWalkLeft[i] = new PGSI(playerAtlas.findRegion("SheathedWalkLeftSL", i), playerAtlas.findRegion("SheathedWalkLeftSC", i), playerAtlas.findRegion("SheathedWalkLeftSR", i));

			knightWalkRight[i] = new PGSI(knightAtlas.findRegion("KnightWalkRightSL", i), knightAtlas.findRegion("KnightWalkRightSC", i), knightAtlas.findRegion("KnightWalkRightSR", i));
			knightWalkLeft[i] = new PGSI(knightAtlas.findRegion("KnightWalkLeftSL", i), knightAtlas.findRegion("KnightWalkLeftSC", i), knightAtlas.findRegion("KnightWalkLeftSR", i));

		}

		// Load the player attacking frame 0 PGSI.
		playerAttackRight[0] = new PGSI(playerAtlas.findRegion("PlayerAttackRightSL", 0), playerAtlas.findRegion("PlayerAttackRightSC", 0), playerAtlas.findRegion("PlayerAttackRightSR", 0), playerAtlas.findRegion("PlayerAttackRightST", 0));
		playerAttackLeft[0] = new PGSI(playerAtlas.findRegion("PlayerAttackLeftSL", 0), playerAtlas.findRegion("PlayerAttackLeftSC", 0), playerAtlas.findRegion("PlayerAttackLeftSR", 0), playerAtlas.findRegion("PlayerAttackLeftST", 0));

		// Load the player attacking frames 1-2 PGSIs.
		for (int i = 1; i < playerAttackRight.length; i++){
			playerAttackRight[i] = new PGSI(playerAtlas.findRegion("PlayerAttackRightSL", 0), playerAtlas.findRegion("PlayerAttackRightSC", i), playerAtlas.findRegion("PlayerAttackRightSR", i));
			playerAttackLeft[i] = new PGSI(playerAtlas.findRegion("PlayerAttackLeftSL", i), playerAtlas.findRegion("PlayerAttackLeftSC", i), playerAtlas.findRegion("PlayerAttackLeftSR", i));
		}
		// Load the player fireball PGSIs.
		for (int i = 0; i < playerFireLeft.length; i++){
			playerFireRight[i] = new PGSI(playerAtlas.findRegion("PlayerFireRightSL", i), playerAtlas.findRegion("PlayerFireRightSC", i), playerAtlas.findRegion("PlayerFireRightSR", i));
			playerFireLeft[i] = new PGSI(playerAtlas.findRegion("PlayerFireLeftSL", i), playerAtlas.findRegion("PlayerFireLeftSC", i), playerAtlas.findRegion("PlayerFireLeftSR", i));
		}

		// Load the knight's attack.
		for (int i = 0; i < knightAttackRight.length; i++){
			if (i != 1){ // Frame 1 has a top, but the other frames don't.
				knightAttackRight[i] = new PGSI(knightAtlas.findRegion("KnightAttackRightSL", i), knightAtlas.findRegion("KnightAttackRightSC", i), knightAtlas.findRegion("KnightAttackRightSR", i));
				knightAttackLeft[i] = new PGSI(knightAtlas.findRegion("KnightAttackLeftSL", i), knightAtlas.findRegion("KnightAttackLeftSC", i), knightAtlas.findRegion("KnightAttackLeftSR", i));
			}else{
				knightAttackRight[i] = new PGSI(knightAtlas.findRegion("KnightAttackRightSL", i), knightAtlas.findRegion("KnightAttackRightSC", i), knightAtlas.findRegion("KnightAttackRightSR", i), knightAtlas.findRegion("KnightAttackRightST", i));
				knightAttackLeft[i] = new PGSI(knightAtlas.findRegion("KnightAttackLeftSL", i), knightAtlas.findRegion("KnightAttackLeftSC", i), knightAtlas.findRegion("KnightAttackLeftSR", i), knightAtlas.findRegion("KnightAttackLeftST", i));
			}
		}
	}

	/**
	 * Load the UI skin.
	 */
	private void loadSkin() {
		assets.load("assets/UI/UI.json", Skin.class);
		assets.finishLoadingAsset("assets/UI/UI.json"); // Make sure the skin is loaded before continuing.
		Skin skin = assets.get("assets/UI/UI.json", Skin.class); // Retrieve the skin.

		// Set the fonts. This is not done in the .json in order to allow easier adjusting of font sizes.
		skin.get("normal-small", LabelStyle.class).font = getFont(NORMAL_FONT, SMALL_FONT_SIZE);
		skin.get("normal-regular", LabelStyle.class).font = getFont(NORMAL_FONT, REGULAR_FONT_SIZE);
		skin.get("bold-big", LabelStyle.class).font = getFont(BOLD_FONT, BIG_FONT_SIZE);
		skin.get("bold-large", LabelStyle.class).font = getFont(BOLD_FONT, LARGE_FONT_SIZE);
		skin.get(TextButtonStyle.class).font = getFont(NORMAL_FONT, REGULAR_FONT_SIZE);
		skin.get("transparent-button", TextButtonStyle.class).font = getFont(BOLD_FONT, BIG_FONT_SIZE);
		skin.get(CheckBoxStyle.class).font = getFont(NORMAL_FONT, REGULAR_FONT_SIZE);
		skin.get(WindowStyle.class).titleFont = getFont(NORMAL_FONT, REGULAR_FONT_SIZE);
	}

	/**
	 * Load a new font into {@link #assets}.
	 * @param fontNameThe name of the font in the Fonts folder, without the .ttf extension.
	 * @param fontSize The font size of the new font.
	 * @author Modified by Vincent Macri. Original from https://github.com/libgdx/libgdx/blob/master/tests/gdx-tests/src/com/badlogic/gdx/tests/extensions/FreeTypeFontLoaderTest.java
	 */
	private void loadFont(String fontName, int fontSize){
		FreeTypeFontLoaderParameter fontParams = new FreeTypeFontLoaderParameter();
		fontParams.fontFileName = "assets/Fonts/" + fontName + ".ttf";

		float horizontalAdjust = Gdx.graphics.getWidth() / 1920f;
		float verticalAdjust = Gdx.graphics.getHeight() / 1080f;
		float combinedAdjust = horizontalAdjust * verticalAdjust * 1.5f;
		float fontAdjust = Math.min(horizontalAdjust, Math.min(verticalAdjust, combinedAdjust));

		fontParams.fontParameters.size = (int) (fontSize * Gdx.graphics.getDensity() * fontAdjust);

		assets.load(fontName + fontSize + ".ttf", BitmapFont.class, fontParams);
	}

	@Override
	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		assets.dispose();
		super.dispose();
	}

	/**
	 * Return the given font, if loaded into {@link #assets}.
	 * @param name The name of the font.
	 * @param size The size of the font.
	 * @return The desired BitMapFont.
	 */
	protected BitmapFont getFont(String name, int size) {
		return assets.get(name  + size + ".ttf");
	}

	/**
	 * Get the UI skin.
	 * @return The skin.
	 */
	protected Skin getSkin() {
		return assets.get("assets/UI/UI.json", Skin.class); // Retrieve the skin.
	}

}
