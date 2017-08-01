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

import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.math.MathUtils;

/**
 * This class manages what music is playing, and what volume to play it at.
 * @author Vincent Macri
 */
public class MusicManager {

	/**
	 * Enum for each song in the game.
	 * @author Vincent Macri
	 */
	protected enum Song {
		/** The music to play on the menu. */
		MENU_SONG,
		/** The music to play during the game. */
		GAME_SONG,
		/** The music to play during the credits. */
		CREDITS_SONG
	}

	/** The music to play on the menu. */
	private Music menuMusic;
	/** The music to play during the game. */
	private Music gameMusic;
	/** The music to play during the credits. */
	public Music creditsMusic;
	/** The game settings. */
	private Settings settings;

	/**
	 * Create a new MusicManager with the songs passed as parameters.
	 * @param menuMusic The music to play on the menu.
	 * @param gameMusic The music to play during the game.
	 * @param creditsMusic The music to play during the credits.
	 */
	public MusicManager(Music menuMusic, Music gameMusic, Music creditsMusic, Settings settings) {
		// Assign the Music instances.
		this.menuMusic = menuMusic;
		this.gameMusic = gameMusic;
		this.creditsMusic = creditsMusic;

		// Set the Music instances to loop.
		this.menuMusic.setLooping(true);
		this.gameMusic.setLooping(true);
		this.creditsMusic.setLooping(true);

		this.settings = settings;

		setVolume(settings.getMusicLevel());
	}

	/**
	 * Stop all currently playing songs and play the chosen song.
	 * @param newSong The song to play.
	 */
	public void playSong (Song newSong) {

		switch (newSong) {
		case MENU_SONG:
			menuMusic.play();
			gameMusic.stop();
			creditsMusic.stop();
			break;
		case GAME_SONG:
			gameMusic.play();
			menuMusic.stop();
			creditsMusic.stop();
			break;
		case CREDITS_SONG:
			creditsMusic.play();
			menuMusic.stop();
			gameMusic.stop();
			break;
		default:
			break;
		}
	}

	/**
	 * Setter for {@link #volume}. Sets the volume of all songs in the game. Clamped to be in the range [0, 1].
	 * @param volume The new music volume.
	 */
	public void setVolume(float volume) {
		settings.setMusicLevel(MathUtils.clamp(volume, 0f, 1f));
		menuMusic.setVolume(settings.getMusicLevel());
		gameMusic.setVolume(settings.getMusicLevel());
		creditsMusic.setVolume(settings.getMusicLevel());
	}

}
