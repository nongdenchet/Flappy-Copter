package com.apidez.game.target;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

/**
 * Created by nongdenchet on 6/14/15.
 */
public interface DrawableComponent {

    /**
     * Contain all logic to create new component
     */
    void init();

    /**
     * Contain all logic code to update the properties or models of the component
     */
    void update();

    /**
     * Contain all view logic to redraw the component
     */
    void draw(SpriteBatch batch);

    /**
     * Contain all code to reset the properties of the component
     */
    void reset();
}
