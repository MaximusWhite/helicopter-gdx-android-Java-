package com.helicopter.game.game_scene;

import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by mikef on 29-Dec-2016.
 */
public class Pickup {

    public static final int STAR = 1;
    public static final int SHIELD = 2;
    public static final int ENERGY = 0;
    TextureRegion pickupTexture;
    Vector2 pickupPosition = new Vector2();

    public Pickup(int id){


    }


}
