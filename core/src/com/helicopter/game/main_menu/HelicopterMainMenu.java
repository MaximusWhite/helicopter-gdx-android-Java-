package com.helicopter.game.main_menu;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.helicopter.game.HelicopterGame;
import com.helicopter.game.game_scene.MainGameScene;

/**
 * Created by mikef on 15-Jun-2016.
 */
public class HelicopterMainMenu extends ScreenAdapter {

    HelicopterGame game;

    SpriteBatch batch;

    TextureRegion background;

    OrthographicCamera camera;

    TextureAtlas textures = new TextureAtlas();

    TextureAtlas UIAtlas = new TextureAtlas();

    Button back;

    public HelicopterMainMenu(HelicopterGame heliGame){

        game = heliGame;

        batch = game.batch;

        camera = game.camera;
        textures = (TextureAtlas)game.getAsset("textures.pack", textures);

        UIAtlas = (TextureAtlas)game.getAsset("UI.pack", UIAtlas);

        back = new Button(new TextureRegionDrawable(UIAtlas.findRegion("ctrl_back_up")), new TextureRegionDrawable(UIAtlas.findRegion("ctrl_back_down")), new TextureRegionDrawable(UIAtlas.findRegion("ctrl_back_checked")));

        background = textures.findRegion("menu_back");

       // textures = (TextureAtlas) game.getAsset("", textures.getClass());

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        updateScene();
        drawScene();
    }

    private void updateScene() {



        if(back.isPressed()){

            game.setScreen(new MainGameScene(game));
        }

    }

    private void drawScene(){

        camera.update();
        batch.setProjectionMatrix(camera.combined );

        batch.begin();

        batch.disableBlending();

        batch.draw(background, 0, 0);

       // back.draw(batch, 0.1f);

        batch.enableBlending();

        batch.end();

    }

}
