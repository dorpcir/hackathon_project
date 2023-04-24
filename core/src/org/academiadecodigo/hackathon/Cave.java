package org.academiadecodigo.hackathon;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.academiadecodigo.hackathon.Screens.PlayScreen;

public class Cave extends Game {

	public static final	int V_WIDTH = 800;
	public static final int V_HEIGHT = 416;
	public static final float PPM = 100;
	public SpriteBatch batch;

	public static AssetManager manager;

	@Override
	public void create() {
		batch = new SpriteBatch();
		manager = new AssetManager(); // instantiate manager here
		manager.load("anotherone.wav", Music.class);
		manager.load("dead.wav", Sound.class);
		manager.load("jump.wav", Sound.class);
		manager.load("win.wav", Sound.class);
		manager.finishLoading();
		setScreen(new PlayScreen(this));
	}


	@Override
	public void render() {
		super.render();


	}
}
