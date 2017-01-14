package com.helicopter.game;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.graphics.g2d.Animation;

/**
 * Created by mikef on 02-Apr-2016.
 */
public class Plane {

    static float planeAnim =0;

    Vector2 velocity = new Vector2();

    Vector2 position = new Vector2();

    Vector2 defPos = new Vector2();

    int width, height;

    public Animation plane;

    public Plane(TextureAtlas txt){

        plane = new Animation(0.05f, txt.findRegion("plane1"),
                txt.findRegion("plane2"),
                txt.findRegion("plane3"),
                txt.findRegion("plane2"));
        plane.setPlayMode(com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP);
        setDefPos();

        width = plane.getKeyFrame(0f).getRegionWidth();
        height = plane.getKeyFrame(0f).getRegionHeight();

    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public float getY(){

        return position.y;
    }

    public float getX(){

        return position.x;
    }

    public void setDefPos(){
        defPos.set(400-88/2,270-73/2);
        position.set(defPos.x, defPos.y);
    }

}
