package com.apidez.game.resource;

import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

/**
 * Created by nongdenchet on 6/17/15.
 */
public class Resource {
    private AssetManager manager;

    public Resource() {
        manager = new AssetManager();
    }

    public void dispose() {
        manager.dispose();
    }

    public void load() {
        manager.load("sounds/journey.mp3", Music.class);
        manager.load("sounds/star.ogg", Sound.class);
        manager.load("sounds/crash.ogg", Sound.class);
        manager.load("sounds/fuel.ogg", Sound.class);
        manager.load("impact-40.fnt", BitmapFont.class);
        manager.finishLoading();
    }

    public void playLoop() {
        manager.get("sounds/journey.mp3", Music.class).setLooping(true);
        manager.get("sounds/journey.mp3", Music.class).play();
    }

    public BitmapFont getFont() {
        return manager.get("impact-40.fnt", BitmapFont.class);
    }

    public void stopLoop() {
        manager.get("sounds/journey.mp3", Music.class).stop();
    }

    public void playStar() {
        manager.get("sounds/fuel.ogg", Sound.class).play();
    }

    public void playPop() {
        manager.get("sounds/star.ogg", Sound.class).play();
    }

    public void playCrash() {
        manager.get("sounds/crash.ogg", Sound.class).play();
    }
}
