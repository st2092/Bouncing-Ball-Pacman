package com.example.sony.bouncingballpacman;

import android.graphics.Paint;
import android.graphics.RectF;

/**
 *  This class is a helper class that will be used to represent sprites, which
 *  are in-game objects that can move around.
 *  This class gathers together several variables that would be associated with
 *  in-game sprite objects such as location (x,y), width & height,
 *  velocity (dx/dy), and color.
 */

public class Sprite {
    //variables to keep track of state of sprite object
    public RectF rect = new RectF();
    public float dx = 0;
    public float dy = 0;
    public Paint color = new Paint();

    /*
     * Constructs a default empty sprite.
     */
    public
    Sprite()
    {
        //empty
    }

    /*
     * Constructs a sprite of given location (x,y) and size (width & height).
     */
    public
    Sprite(float x, float y, float width, float height)
    {
        setSize(width, height);
        setLocation(x,y);
    }

    /*
     * Sets the sprite's location on screen given the coordinates.
     */
    public void
    setLocation(float x, float y)
    {
        rect.offsetTo(x, y);
    }

    /*
     * Sets the sprite's size based on the values given.
     */
    public void
    setSize(float width, float height)
    {
        rect.right = rect.left + width;
        rect.bottom = rect.top + height;
    }

    /*
     * Sets the sprite's velocity based on the given values.
     */
    public void
    setVelocity(float dx, float dy)
    {
        this.dx = dx;
        this.dy = dy;
    }

    /*
     * Tells the sprite to move itself based on the velocity.
     */
    public void
    move()
    {
        rect.offset(dx, dy);
    }

    /*
     * Tells the sprite to stop moving by setting velocity to 0, 0.
     */
    public void
    stopMoving()
    {
        setVelocity(0,0);
    }
}
