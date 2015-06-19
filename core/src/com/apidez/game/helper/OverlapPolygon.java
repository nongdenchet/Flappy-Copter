package com.apidez.game.helper;

import com.badlogic.gdx.math.Polygon;

/**
 * Created by nongdenchet on 6/17/15.
 */
public class OverlapPolygon {
    public static boolean overlap(Polygon a, Polygon b) {
        float[] vertexA = a.getVertices();
        float[] vertexB = b.getVertices();
        for (int i = 0; i < vertexA.length / 2; i++) {
            if (b.contains(vertexA[i * 2], vertexA[i * 2 + 1]))
                return true;
        }
        for (int i = 0; i < vertexB.length / 2; i++) {
            if (a.contains(vertexB[i * 2], vertexB[i * 2 + 1]))
                return true;
        }
        return false;
    }
}
