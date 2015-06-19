package com.apidez.game.target;

import com.apidez.game.resource.Value;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

/**
 * Created by nongdenchet on 6/14/15.
 */
public class MovingBackground implements DrawableComponent {

    private TextureAtlas atlas;
    private TextureRegion terrainBelow, terrainAbove;
    private float terrainOffset1;
    private float terrainOffset2;
    private float terrainWidth;

    public static MovingBackground newInstance(TextureAtlas atlas) {
        return new MovingBackground(atlas);
    }

    private MovingBackground(TextureAtlas atlas) {
        this.atlas = atlas;
        init();
    }

    @Override
    public void init() {
        terrainBelow = atlas.findRegion("groundGrass");
        terrainAbove = new TextureRegion(terrainBelow);
        terrainAbove.flip(true, true);
        terrainWidth = terrainAbove.getRegionWidth();
        terrainOffset1 = 0;
        terrainOffset2 = terrainWidth;
        reset();
    }

    public TextureRegion getTerrainAbove() {
        return terrainAbove;
    }

    public void setTerrainAbove(TextureRegion terrainAbove) {
        this.terrainAbove = terrainAbove;
    }

    public TextureRegion getTerrainBelow() {
        return terrainBelow;
    }

    public void setTerrainBelow(TextureRegion terrainBelow) {
        this.terrainBelow = terrainBelow;
    }

    @Override
    public void draw(SpriteBatch batch) {
        // Draw the moving terrain
        batch.draw(terrainBelow, terrainOffset1, 0);
        batch.draw(terrainBelow, terrainOffset2, 0);
        batch.draw(terrainAbove, terrainOffset1, Value.HEIGHT - terrainAbove.getRegionHeight());
        batch.draw(terrainAbove, terrainOffset2, Value.HEIGHT - terrainAbove.getRegionHeight());
    }

    @Override
    public void reset() {

    }

    /**
     * The idea is maintain two terrain to make the screen keep moving
     */
    @Override
    public void update() {
        terrainOffset1 -= 200 * Gdx.graphics.getDeltaTime();
        terrainOffset2 -= 200 * Gdx.graphics.getDeltaTime();
        terrainOffset1 = terrainOffset1 < -terrainWidth ? terrainWidth + terrainOffset2 : terrainOffset1;
        terrainOffset2 = terrainOffset2 < -terrainWidth ? terrainWidth + terrainOffset1 : terrainOffset2;
    }
}
