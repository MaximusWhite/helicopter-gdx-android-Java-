package com.helicopter.game.game_scene;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by mikef on 29-Dec-2016.
 */
public class Pickup {

    public static final int STAR = 1;
    public static final int SHIELD = 2;
    public static final int ENERGY = 0;
    public static float pickupAnimTime = 0;
    Animation pickupAnimation;
    Vector2 pickupPosition = new Vector2();
    Vector2 velocity = new Vector2(-3, 0);


    public Pickup(int id, TextureAtlas atlas){

        pickupAnimation = new Animation(0.05f, atlas.findRegion("pickups/energy1"),
                atlas.findRegion("pickups/energy2"),
                atlas.findRegion("pickups/energy3"),
                atlas.findRegion("pickups/energy4"),
                atlas.findRegion("pickups/energy5"),
                atlas.findRegion("pickups/energy6"));
        pickupAnimation.setPlayMode(com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP);

    }

    public float getX(){
        return pickupPosition.x;
    }

    public float getY(){
        return pickupPosition.y;
    }

    public void updatePickup(){

        pickupPosition.add(velocity);
    }

}
