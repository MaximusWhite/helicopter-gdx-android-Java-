package com.helicopter.game.game_scene;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.helicopter.game.*;

public class MainGameScene extends ScreenAdapter
{
	SpriteBatch batch;
	TextureRegion background;

	//Viewport viewport;

	FPSLogger fpsLogger = new FPSLogger();

	OrthographicCamera camera;
	TextureRegion above;
	TextureRegion[] below;
	TextureRegion tapIndicator;
    TextureRegion tapMe;
	TextureRegion gameOver;
	TextureRegion point;


	float terOffset=0;

	int BIRD_COUNT = 20;

	float tapDrawTime;
	private static final float TAP_DRAW_TIME_MAX=0.5f;
	private static final int TOUCH_IMPULSE=300;
	int orientation;

    Plane plane;

	boolean touchInput = true;
	boolean debugMode = false;
	boolean planeOutOfBounds = false;
	int touchTime=0;

	Vector2 scrollVelocity = new Vector2();
	Vector2 gravity = new Vector2();
	Vector3 touchPos = new Vector3();
	Vector2 tmpVect = new Vector2();
	Vector2 rescue = new Vector2(70,0);

	BitmapFont debugFont = new BitmapFont();
	BitmapFont pointsFont = new BitmapFont();

	int points=0;
	int high_score = 0;
	int ptcount =0;

	private static final Vector2 damping = new Vector2(0.99f,0.99f);

	TextureAtlas textures = new TextureAtlas();

	Bird[] birds;

	Pickup energy;

	float pickupTimeout = 0;

	InputTracker inputProcessor = new InputTracker();


	static enum GameState{

        INIT, ACTION, GAME_OVER, PAUSE

    }

    GameState gameState;

	CollisionHandler collisions;

	private static ShapeRenderer debugRenderer;

	HelicopterGame game;


	// VARS DONE

	public MainGameScene(HelicopterGame helicoptGame) {


		Gdx.input.setInputProcessor(inputProcessor);

		game = helicoptGame;
		batch = game.batch;
		camera = game.camera;
		textures = (TextureAtlas)game.getAsset("textures.pack", textures);
		energy = new Pickup(3,textures);

		//camera = new OrthographicCamera();

		//textures =new TextureAtlas(Gdx.files.internal("textures.pack"));

		tapIndicator = textures.findRegion("tap2");
        // TEMP
        tapMe = textures.findRegion("messages/tap_me");
		gameOver = textures.findRegion("messages/game_over");
		background = textures.findRegion("environment/background");
		point = textures.findRegion("point");

		above=textures.findRegion("environment/sky");
		above.flip(true,true);

		below = new TextureRegion[10];
		below = randomizeGround(below);

		//debugFont = new BitmapFont(Gdx.files.internal("dfont.fnt"), Gdx.files.internal("dfont.png"), false);

		debugFont = (BitmapFont) game.getAsset("dfont.fnt", debugFont);
		debugFont.setColor(Color.RED);

		//pointsFont = new BitmapFont(Gdx.files.internal("points_font.fnt"), Gdx.files.internal("points_font.png"), false);

		pointsFont = (BitmapFont) game.getAsset("points_font.fnt", pointsFont);
		//pointsFont.setColor(Color.RED);


		debugRenderer = new ShapeRenderer();

		//camera.setToOrtho(false,800,480);

		camera.position.set(400, 240, 0);

		//batch = new SpriteBatch();

		birds = new Bird[BIRD_COUNT];

		for(int i=0; i<BIRD_COUNT; i++){

			birds[i] = new Bird(textures);
		}

		plane = new Plane(textures);

		collisions = new CollisionHandler(plane, birds, energy);

		if (Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer)){
			orientation = Gdx.input.getRotation();
		}

		resetScene();
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(1, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		//fpsLogger.log();

		updateScene();
		drawScene();
	}

	///////////////////////////// UPDATING SCENE ///////////////////////////////////

	public void updateScene(){

		float deltaTime = Gdx.graphics.getDeltaTime();


		if (gameState == GameState.PAUSE) {
			return;
		}


		// GAME IN ACTION

        if (gameState == GameState.ACTION) {

			ptcount++;


			switch(collisions.collisionHappened()){

				case 0: break;
				case 1:
					gameState = GameState.GAME_OVER;		// surrounding collision

					return;
					//break;

				case 2:
					gameState = GameState.GAME_OVER;		// bird collision

					//game.setScreen(new HelicopterMainMenu(game));  // changing screen

					return;
					//break;
			}

			//if(gameState == GameState.GAME_OVER) break;

			terOffset-=170*deltaTime;   // Terrain offset

		if (terOffset<above.getRegionWidth()*(-1) ) {

			terOffset=-2;
			below = shift(below);
		}

            Plane.planeAnim += deltaTime;			// updating animation frames
            Pickup.pickupAnimTime += deltaTime;


			if(plane.getX() < plane.getWidth()/2 * -1){planeOutOfBounds = true;}

			//System.out.println(plane.getX() + " :: " + plane.getY());

			plane.velocity.scl(damping);

			if(planeOutOfBounds && (plane.getX() < plane.getWidth() )) {

				plane.position.mulAdd(rescue,deltaTime);
				plane.velocity.set(0,0);

			}else{
				if (touchInput) {

					plane.velocity.add(gravity);
					//plane.velocity.add(scrollVelocity);  // QUESTIONABLE (velocity forward)

				}
				plane.position.mulAdd(plane.velocity, deltaTime);

			}
			if(plane.getX() > plane.getWidth() ){
				planeOutOfBounds = false;
			}

			checkAndCreateEnergy(deltaTime);
			energy.updatePickup();

            for (Bird bird : birds) {
					bird.updateBird(deltaTime);
                    //if(bird.type == Bird.BirdType.KILL_BONUS) continue;

				  // if(ptcount > 60) {
					   points += pointCalc(plane, bird);
					  // System.out.println("POINTS UPDATED BY??? == " + pointCalc(plane, bird));
					 //  ptcount =0;
				 //  }

			}
           // if (!touchInput)
             //   plane.position.add(Gdx.input.getAccelerometerY() * 0.7f, Gdx.input.getAccelerometerX() * (-0.7f));

            ///////// INPUT ////////

            if (touchInput && Gdx.input.justTouched() && !planeOutOfBounds) {
                touchTime = 0;
                touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
                camera.unproject(touchPos);
                tmpVect.set(plane.getX(), plane.getY());
                tmpVect.sub(touchPos.x, touchPos.y).nor();
                plane.velocity.mulAdd(tmpVect, TOUCH_IMPULSE - MathUtils.clamp(Vector2.dst(touchPos.x, touchPos.y, plane.getX(), plane.getY()), 0, TOUCH_IMPULSE));
                tapDrawTime = TAP_DRAW_TIME_MAX;
            }

			touchTime++;

			//System.out.println("touchTime == " + touchTime);
			/*
			if (touchTime >= 120) {

				touchInput = touchInput ? false : true;

				touchTime = 0;
			}
			*/
        }
		// ACTION SECTION DONE

		if (Gdx.input.justTouched()) {
            if (gameState == GameState.INIT){

               gameState = GameState.ACTION;

            }else if (gameState == GameState.GAME_OVER){
                if(points > high_score) high_score = points;
                resetScene();

            }
		}

		tapDrawTime-=deltaTime;
	}

	///////////////////////////////// DRAWING SCENE /////////////////////////////////////////////

	public void drawScene(){


		camera.update();
		batch.setProjectionMatrix(camera.combined );
		batch.begin();

		batch.disableBlending();

		batch.draw(background, 0, 0);

		batch.enableBlending();

		for (int i=0; i<10; i++ ){

			batch.draw(below[i], terOffset+below[i].getRegionWidth()*i, 0);

			batch.draw(above, terOffset, 480 - above.
					getRegionHeight());
			batch.draw(above, terOffset + above.
					getRegionWidth()*i, 480 - above.getRegionHeight());

		}

		/// drawing birds

		for (Bird bird : birds) {

			if (bird.bird.getKeyFrame(bird.getAnim()) != null) {
				batch.draw(bird.bird.getKeyFrame(bird.getAnim()), bird.position.x - 25, bird.position.y - 20);

					if (collisions.centerLength(plane.position, bird.position) <= 100) {

						batch.draw(point, bird.getX(), bird.getY() + 20);

					}


				if(debugMode) debugFont.draw(batch,""+collisions.centerLength(plane.position, bird.position), bird.getX(), bird.getY());

			}
		}


		pointsFont.draw(batch, ""+points, 15, 45);

		pointsFont.draw(batch, "High: "+high_score, 550, 45);

		// drawing plane

		batch.draw(plane.plane.getKeyFrame(Plane.planeAnim), plane.getX()-45f, plane.getY()-30f);
			if(debugMode) batch.draw(point, plane.position.x, plane.position.y);

		batch.draw(energy.pickupAnimation.getKeyFrame(Pickup.pickupAnimTime), energy.getX()-10f, energy.getY()-10f );

		if (tapDrawTime>0) batch.draw(tapIndicator, touchPos.x-15f, touchPos.y-15f);

        if(gameState == GameState.INIT){

			batch.draw(tapMe, 75, 160);

		}

		if(gameState == GameState.GAME_OVER) {

			batch.draw(gameOver, 75, 160);

		}

		/////// DEBUG OUTPUT ////////////////////////////////////////

		//	debugFont.draw(batch, "X: "+ Gdx.input.getAccelerometerX() +
		//	" == Y: "+Gdx.input.getAccelerometerY() + " == Z: " +
		//	Gdx.input.getAccelerometerZ(), 50, 250);
		//
		batch.end();

		// NOTE: can put centerLength as a variable in Bird -> less operations to get it
        if(debugMode) {
			for (Bird b : birds) {

				if (collisions.centerLength(plane.position, b.position) <= 100) {
					DrawDebugLine(plane.position, b.position, 4, Color.RED);
				} else {

					DrawDebugLine(plane.position, b.position, 2, Color.GREEN);

				}
			}
		}

	}

	public String pickAGroundTexture(){

		Random rand = new Random();
		int number = rand.nextInt(4) +1;

		switch(number){

			case 1: return "environment/ground_grass1";
			case 2: return "environment/ground_grass2";
			case 3: return "environment/ground_grass3";
			case 4: return "environment/ground_grass4";

		}

		return "failed.png";
	}

	public TextureRegion[] randomizeGround(TextureRegion[] arr){

		TextureRegion[] result = arr.clone();

		for (int i=0; i<arr.length; i++){

			result[i] = textures.findRegion(pickAGroundTexture());

		}
		return result;
	}

	public TextureRegion[] shift(TextureRegion[] arr){

		TextureRegion[] result = arr.clone();

		TextureRegion first = textures.findRegion(pickAGroundTexture());


		for (int i = 1; i<arr.length; i++){

			result[i-1]=result[i];

		}
		result[9]=first;

		return result;
	}



	public void DrawDebugLine(Vector2 start, Vector2 end, int lineWidth, Color color)
	{
		Gdx.gl.glLineWidth(lineWidth);
		debugRenderer.setProjectionMatrix(camera.combined);
		debugRenderer.begin(ShapeRenderer.ShapeType.Line);
		debugRenderer.setColor(color);
		debugRenderer.line(start, end);
		debugRenderer.end();
		Gdx.gl.glLineWidth(1);
	}
	// TODO: figure out distance for points

	public int pointCalc(Plane plane, Bird bird){

		return (int) (150/collisions.centerLength(plane.position, bird.position));
	}

	private void resetScene(){
		terOffset = 0;
		Plane.planeAnim = 0;
		for(int i =0; i<BIRD_COUNT; i++){

			birds[i].resetPos();

		}
		plane.velocity.set(0,0);
		scrollVelocity.set(2,0);   // QUESTIONABLE
		gravity.set(0,-4);
		plane.setDefPos();
		plane.position.set(plane.defPos.x, plane.defPos.y);
		gameState = GameState.INIT;
		below = randomizeGround(below);
		points = 0;
	}

	private void checkAndCreateEnergy(float delta){

		pickupTimeout-=delta;
		if (pickupTimeout <=0) {
			pickupTimeout=(float)(0.5+Math.random()*0.5);
			if(addEnergy(Pickup.ENERGY))
				pickupTimeout=1+(float)Math.random()*5;
		}

	}

	private boolean addEnergy(int pickupType)
	{

		Vector2 randomPosition=new Vector2();
		randomPosition.x=820;
		randomPosition.y=(float) (80+Math.random()*320);
		Pickup tempPickup=new Pickup(pickupType, textures);
		tempPickup.pickupPosition.set(randomPosition);
		energy = tempPickup;
		return true;
	}

}
