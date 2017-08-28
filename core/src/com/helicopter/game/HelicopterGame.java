package com.helicopter.game;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.helicopter.game.game_scene.MainGameScene;
import com.matsemann.libgdxloadingscreen.screen.LoadingScreen;

/**
 * Created by mikef on 21-May-2016.
 */


public class HelicopterGame extends Game {


    public static final int screenWidth = 800;
    public static final int screenHeight = 480;

    public  OrthographicCamera camera;
    public Viewport viewport;
    public SpriteBatch batch;
    public boolean soundEnabled = true;
    public float soundVolume = 1;

    public static AssetManager manager = new AssetManager();

    public Music music;

    public HelicopterGame(){
        FPSLogger fpsLogger = new FPSLogger();
        camera = new OrthographicCamera();
        camera.position.set(screenWidth/2, screenHeight/2, 0);
        viewport = new StretchViewport(screenWidth, screenHeight, camera);
    }
    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new LoadingScreen(this));
    }

    @Override
    public void render(){
        super.render();
    }

    @Override
    public void resize(int width, int height){

        viewport.update(width, height);
    }

    @Override
    public void dispose(){
        batch.dispose();
        music.dispose();
    }

    public static void loadAssets(){

        manager.load("textures.pack", TextureAtlas.class);
        manager.load("UI.pack", TextureAtlas.class);
        manager.load("points_font.fnt", BitmapFont.class);
        manager.load("dfont.fnt", BitmapFont.class);
        manager.load("fx/Smoke", ParticleEffect.class);
        manager.load("title.png", Texture.class);
        manager.finishLoading();
    }

    public Object getAsset(String name, Object obj){

        return manager.get(name, obj.getClass());

    }

}
