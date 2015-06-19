package com.apidez.game.target;

import com.apidez.game.resource.Value;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Polygon;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by nongdenchet on 6/17/15.
 */
public class Star implements DrawableComponent {

    private TextureRegion star;
    private Vector2 postion;
    private TextureAtlas atlas;
    private Polygon bound;

    public Polygon getBound() {
        return bound;
    }

    public void setBound(Polygon bound) {
        this.bound = bound;
    }

    public Vector2 getPostion() {
        return postion;
    }

    public void setPostion(Vector2 postion) {
        this.postion = postion;
    }

    private Star(Vector2 postion, TextureAtlas atlas) {
        this.postion = postion;
        this.atlas = atlas;
        init();
    }

    public static Star newInstance(Vector2 postion, TextureAtlas atlas) {
        return new Star(postion, atlas);
    }

    @Override
    public void init() {
        star = atlas.findRegion("star_pickup");
        bound = new Polygon();
        reset();
    }

    @Override
    public void update() {
        postion.x -= 300 * Gdx.graphics.getDeltaTime();
        bound.setVertices(new float[]{
                postion.x, postion.y + Value.STAR_SIDE * 2 / 3,
                postion.x + Value.STAR_SIDE / 2, postion.y + Value.STAR_SIDE,
                postion.x + Value.STAR_SIDE, postion.y + Value.STAR_SIDE * 2 / 3,
                postion.x + Value.STAR_SIDE * 3 / 4, postion.y,
                postion.x + Value.STAR_SIDE / 4, postion.y
        });
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(star, postion.x, postion.y);
    }

    @Override
    public void reset() {

    }
}
