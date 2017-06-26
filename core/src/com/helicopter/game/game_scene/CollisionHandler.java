package com.helicopter.game.game_scene;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by mikef on 05-Apr-2016.
 */
public class CollisionHandler {

    Plane plane;
    Bird[] birds;
    Pickup pickup;

    public CollisionHandler(Plane plane, Bird[] birds, Pickup pickup){

        this.plane = plane;
        this.birds = birds;
        this.pickup = pickup;

    }

    public int collisionHappened(){

        if(plane.getY() < 80 || plane.getY() + 73> 600){

            return 1; // plane above or below
        }

        for(Bird b : birds){

          //  if(b.type == Bird.BirdType.KILL_BONUS) continue;

            if(plane.getY() > b.getY() + 25){    // plane above bird

                if (b.bird.getKeyFrameIndex(b.getAnim()) == 0 || (b.bird.getKeyFrameIndex(b.getAnim()) >4 && b.bird.getKeyFrameIndex(b.getAnim())< 9)) {

                    if (centerLength(plane.position, b.position) <= 30) {

                        return 2;

                    }
                }else{
                    if (centerLength(plane.position, b.position) <= 40) {

                        return 2;

                    }
                }

            }else if(plane.getY() < b.getY() - 25){     // plane below bird

                if(centerLength(plane.position, b.position) <= 50){

                    return 2;

                }

            }
            else if(centerLength(plane.position, b.position) <= 44 + 10){    //elsewhere

                return 2;

            }
        }

        return 0;
    }

    public double centerLength(Vector2 planePos, Vector2 birdPos){

        return Math.sqrt((planePos.x - birdPos.x) * (planePos.x - birdPos.x) + (planePos.y - birdPos.y) * (planePos.y - birdPos.y));
    }
}
