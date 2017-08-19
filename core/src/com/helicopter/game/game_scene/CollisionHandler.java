package com.helicopter.game.game_scene;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by mikef on 05-Apr-2016.
 */
public class CollisionHandler {

    Plane plane;
    Bird[] birds;
    Pickup pickup;
    public static final int CLEAR = 0;
    public static final int OUT_OF_BOUNDS = 1;
    public static final int BIRD_CRASH = 2;
    public static final int PICKUP_COLLISION = 3;

    public CollisionHandler(Plane plane, Bird[] birds){
        this.plane = plane;
        this.birds = birds;
    }

    public int collisionHappened(){

        if(plane.getY() < 80 || plane.getY() + 73> 600){

            return OUT_OF_BOUNDS; // plane above or below
        }

        for(Bird b : birds){

          //  if(b.type == Bird.BirdType.KILL_BONUS) continue;

            if(plane.getY() > b.getY() + 25){    // plane above bird
                if (b.bird.getKeyFrameIndex(b.getAnim()) == 0 || (b.bird.getKeyFrameIndex(b.getAnim()) >4 && b.bird.getKeyFrameIndex(b.getAnim())< 9)) {
                    if (centerLength(plane.position, b.position) <= 30) {
                        return BIRD_CRASH;
                    }
                }else{
                    if (centerLength(plane.position, b.position) <= 40) {
                        return BIRD_CRASH;
                    }
                }
            }else if(plane.getY() < b.getY() - 25){     // plane below bird
                if(centerLength(plane.position, b.position) <= 50){
                    return BIRD_CRASH;
                }
            }
            else if(centerLength(plane.position, b.position) <= 44 + 10){    //elsewhere
                return BIRD_CRASH;
            }
        }
        return CLEAR;
    }

    public double centerLength(Vector2 planePos, Vector2 birdPos){

        return Math.sqrt((planePos.x - birdPos.x) * (planePos.x - birdPos.x) + (planePos.y - birdPos.y) * (planePos.y - birdPos.y));
    }
}
