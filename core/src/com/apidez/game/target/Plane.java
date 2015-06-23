package com.apidez.game.target;

import com.apidez.game.resource.Value;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.*;

/**
 * Created by nongdenchet on 6/14/15.
 */
public class Plane implements DrawableComponent {

    private Animation movingPlan;
    private float planeAnimTime;
    private Vector2 planeVelocity, planePosition, planeDefaultPosition;
    private Vector2 gravity, scrollVelocity;
    private TextureAtlas atlas;

    private Polygon bound;

    public Polygon getBound() {
        return bound;
    }

    public static Plane newInstance(TextureAtlas atlas) {
        return new Plane(atlas);
    }

    private Plane(TextureAtlas atlas) {
        this.atlas = atlas;
        init();
    }

    public Vector2 getPlaneVelocity() {
        return planeVelocity;
    }

    public void setPlaneVelocity(Vector2 planeVelocity) {
        this.planeVelocity = planeVelocity;
    }

    public Vector2 getPlanePosition() {
        return planePosition;
    }

    public void setPlanePosition(Vector2 planePosition) {
        this.planePosition = planePosition;
    }

    public Vector2 getGravity() {
        return gravity;
    }

    public void setGravity(Vector2 gravity) {
        this.gravity = gravity;
    }

    @Override
    public void init() {
        movingPlan = new Animation(0.05f,
                atlas.findRegion("planeRed1"),
                atlas.findRegion("planeRed2"),
                atlas.findRegion("planeRed3"),
                atlas.findRegion("planeRed2"));
        movingPlan.setPlayMode(Animation.PlayMode.LOOP); // request looping bewtween frame
        bound = new Polygon();
        gravity = new Vector2();
        scrollVelocity = new Vector2();
        planeVelocity = new Vector2();
        planePosition = new Vector2();
        planeDefaultPosition = new Vector2();
        reset();
    }

    @Override
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        planeAnimTime += deltaTime; // as time gone by
        planeVelocity.add(gravity); // apply gravity to the velocity
        planePosition.mulAdd(planeVelocity, deltaTime); // apply the final velocity to the position
        bound.setVertices(new float[]{
                planePosition.x, planePosition.y,
                planePosition.x + Value.PLANE_WIDTH, planePosition.y,
                planePosition.x + Value.PLANE_WIDTH, planePosition.y + Value.PLAN_HEIGHT,
                planePosition.x, planePosition.y + Value.PLAN_HEIGHT
        });
    }

    @Override
    public void draw(SpriteBatch batch) {
        batch.draw(movingPlan.getKeyFrame(planeAnimTime), planePosition.x, planePosition.y);
    }

    @Override
    public void reset() {
        planeAnimTime = 0;
        gravity.set(0, 0);
        planeDefaultPosition.set(Value.PLAN_POS_X, Value.PLAN_POS_Y);
        planePosition.set(planeDefaultPosition);
        planeVelocity.set(0, 0);
    }
}
