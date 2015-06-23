package com.apidez.game.target;

import com.apidez.game.resource.Value;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;

/**
 * Created by nongdenchet on 6/14/15.
 */
public class TouchStub implements DrawableComponent {

    private static final int TOUCH_IMPULSE = 300;
    private static final float TAP_DRAW_TIME_MAX = 0.25f;
    private Vector3 touchPosition;
    private Vector2 tmpVector;
    private TextureRegion tapIndicator;
    private float tapDrawTime;
    private Camera camera;
    private Plane plane;
    private TextureAtlas atlas;

    public static TouchStub newInstance(Camera camera, TextureAtlas atlas, Plane plane) {
        return new TouchStub(camera, atlas, plane);
    }

    private TouchStub(Camera camera, TextureAtlas atlas, Plane plane) {
        this.atlas = atlas;
        this.camera = camera;
        this.plane = plane;
        init();
    }

    @Override
    public void init() {
        touchPosition = new Vector3();
        tmpVector = new Vector2();
        tapIndicator = atlas.findRegion("tap2");
    }

    @Override
    public void update() {
        float deltaTime = Gdx.graphics.getDeltaTime();
        if (Gdx.input.justTouched()) {
            float x = plane.getPlanePosition().x;
            float y = plane.getPlanePosition().y;

            touchPosition.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(touchPosition);
            tmpVector.set(x, y);
            tmpVector.sub(x, touchPosition.y).nor();
            if (tmpVector.y < 0) tmpVector.y *= -1;
            plane.setPlaneVelocity(new Vector2(0, 0).mulAdd(tmpVector, TOUCH_IMPULSE));
            tapDrawTime = TAP_DRAW_TIME_MAX;
        }
        tapDrawTime -= deltaTime;
    }

    @Override
    public void draw(SpriteBatch batch) {
        if (tapDrawTime > 0) {
            batch.draw(tapIndicator, touchPosition.x - Value.TAB_WIDTH / 2, touchPosition.y - Value.TAB_HEIGHT / 2);
        }
    }

    @Override
    public void reset() {

    }
}
