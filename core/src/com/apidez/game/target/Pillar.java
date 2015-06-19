package com.apidez.game.target;

import com.apidez.game.resource.Value;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by nongdenchet on 6/16/15.
 */
public class Pillar implements DrawableComponent {
    private Vector2 pos;
    private TextureRegion pillar;
    private TextureAtlas atlas;
    private Polygon bound;

    public Polygon getBound() {
        return bound;
    }

    private Pillar(Vector2 pos, TextureAtlas atlas) {
        this.pos = pos;
        this.atlas = atlas;
        init();
    }

    public static Pillar newInstance(Vector2 pos, TextureAtlas atlas) {
        return new Pillar(pos, atlas);
    }

    public Vector2 getPos() {
        return pos;
    }

    public void setPos(Vector2 pos) {
        this.pos = pos;
    }

    @Override
    public void init() {
        bound = new Polygon();
        pillar = pos.y == 0 ? atlas.findRegion("rockGrassUp") : atlas.findRegion("rockGrassDown");
        reset();
    }

    @Override
    public void update() {
        pos.x -= 200 * Gdx.graphics.getDeltaTime();
        if (pos.y == 0) {
            bound.setVertices(new float[]{
                    pos.x, pos.y,
                    pos.x + 65, pos.y + 220,
                    pos.x + 108, pos.y
            });
        } else {
            bound.setVertices(new float[]{
                    pos.x, Value.HEIGHT,
                    pos.x + 65, Value.HEIGHT - pillar.getRegionHeight() + 10,
                    pos.x + 108, Value.HEIGHT
            });
        }
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (pos.y == 0)
            batch.draw(pillar, pos.x, 0);
        else
            batch.draw(pillar, pos.x, Value.HEIGHT - pillar.getRegionHeight());
    }

    @Override
    public void reset() {
    }
}
