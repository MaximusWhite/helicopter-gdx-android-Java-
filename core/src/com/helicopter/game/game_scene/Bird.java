package com.helicopter.game.game_scene;

import java.util.Random;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Animation.PlayMode;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Bird {


    Vector2 position = new Vector2();

    Vector2 velocity = new Vector2(-3, 0);

    Random rand = new Random();

    float birdAnim=0;

    public Animation bird;

    TextureAtlas txt;

    int width, height;

    enum BirdType{

        REG, KILL_BONUS
    }

    BirdType type;

    public Bird(TextureAtlas txt){

        this.txt = txt;
        resetPos();
        Random rand = new Random();
        birdAnim = rand.nextFloat();

        bird = new Animation(0.08f, txt.findRegion("birds/bird12"),
                txt.findRegion("birds/bird22"),
                txt.findRegion("birds/bird32"),
                txt.findRegion("birds/bird42"),
                txt.findRegion("birds/bird52"),
                txt.findRegion("birds/bird62"),
                txt.findRegion("birds/bird72"),
                txt.findRegion("birds/bird82"),
                txt.findRegion("birds/bird92"));

        bird.setPlayMode(PlayMode.LOOP);

        width = bird.getKeyFrame(0f).getRegionWidth();
        height = bird.getKeyFrame(0f).getRegionHeight();

    }


    public void updateBird(float dtime){

        position.add(velocity);
        birdAnim+=dtime;
        if (position.x <  bird.getKeyFrame(birdAnim).getRegionWidth() * -1) resetPos();


    }

    public void resetPos(){

        position.set(rand.nextInt(3000) + 800, rand.nextInt(350) + 75);



    }

    public float getX(){
        return position.x;
    }

    public float getY(){
        return position.y;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public float getAnim(){return birdAnim;}

}
