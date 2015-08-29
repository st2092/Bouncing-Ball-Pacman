package com.example.sony.bouncingballpacman;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * This class is a simple graphical view of an animated application. A ball
 * bounces off the edges of the screen. There is also a yellow pac-man sprite
 * that moves around in response to the user touching the screen at its
 * various edges.
 */
public class BouncingBallPacmanView extends View {
    private static final float BALL_SIZE = 100;
    private static final float BALL_MAX_VELOCITY = 80;
    private static final float PACMAN_SIZE = 120;

    private Sprite ball;
    private Sprite pacman;
    private DrawingThread drawing_thread;

    /*
     * Direction of pacman:
     *  0 - Top edge
     *  1 - Bottom edge
     *  2 - left edge
     *  3 - right edge
     *  4 - no edge (stop)
     */
    private int pacman_direction_x = 0;
    private int pacman_direction_y = 0;
    private static final int TOP_EDGE = 0;
    private static final int BOTTOM_EDGE = 1;
    private static final int LEFT_EDGE = 2;
    private static final int RIGHT_EDGE = 3;
    private static final int NO_EDGE = 4;

    /*
     * Constructor that sets up the initial state of the view and sprites.
     */
    public BouncingBallPacmanView(Context context, AttributeSet attribute_set)
    {
        super(context, attribute_set);

        // set up initial state for ball
        ball = new Sprite();
        ball.setSize(BALL_SIZE, BALL_SIZE);
        ball.setLocation(100, 300);
        ball.setVelocity(
                (float) ((Math.random() - .5) * 2 * BALL_MAX_VELOCITY), // dx
                (float) ((Math.random() - .5) * 2 * BALL_MAX_VELOCITY)  // dy
        );
        ball.color.setARGB(255, 125, 175, 200); //alpha, red, green, blue

        // set up initial state for pacman
        pacman = new Sprite();
        pacman.setSize(PACMAN_SIZE, PACMAN_SIZE);
        pacman.setLocation(0, 0);
        pacman.color.setARGB(255, 200, 200, 0); //yellow

        // start drawing thread to animate screen at 50 frames per second
        drawing_thread = new DrawingThread(this, 50);
        drawing_thread.start();
    }

    /*
     * This method gets call when the user touches the screen.
     * This method is use to control Pac-Man's movement.
     * When the user touches the edges of the screen, Pac-Man moves toward that edge.
     * Touching any non-edge on the screen will stop Pac-Man's movement.
     */
    @Override
    public boolean
    onTouchEvent(MotionEvent motion_event)
    {
        float x = motion_event.getX();
        float y = motion_event.getY();
        int width = getWidth();
        int height = getHeight();
        float left_edge_of_screen = width/5;
        float right_edge_of_screen = width * 4/5;
        float top_edge_of_screen = height/5;
        float bottom_edge_of_screen = height * 4/5;

        if (x <= left_edge_of_screen)
        {
            pacman.dx = -10;                    // move towards left edge
            pacman_direction_x = LEFT_EDGE;
        }
        else if (x > right_edge_of_screen)
        {
            pacman.dx = 10;                     // move towards right edge
            pacman_direction_x = RIGHT_EDGE;
        }
        else                                    // non-edge
        {
            pacman.dx = 0;
            pacman_direction_x = NO_EDGE;
        }

        if (y <= top_edge_of_screen)
        {
            pacman.dy = -10;                    // move towards top edge
            pacman_direction_y = TOP_EDGE;
        }
        else if (y > bottom_edge_of_screen)
        {
            pacman.dy = 10;                     // move towards bottom edge
            pacman_direction_y = BOTTOM_EDGE;
        }
        else                                    // non-edge
        {
            pacman.dy = 0;
            pacman_direction_y = NO_EDGE;
        }

        return super.onTouchEvent(motion_event);
    }

    /*
     * This method draws the bouncing ball and Pac-Man on the screen. It
     * also updates their position for the next time the screen is redrawn.
     */
    @Override
    public void
    onDraw(Canvas canvas)
    {
        super.onDraw(canvas);

        canvas.drawARGB(255, 153, 0, 153);
        canvas.drawOval(ball.rect, ball.color);
        canvas.drawOval(pacman.rect, pacman.color);
        updateSprites();
    }

    /*
     * Updates the sprites' positions between frames of animation.
     */
    private void
    updateSprites()
    {
        ball.move();

        // handle ball bouncing off edges
        if (ball.rect.left < 0 || ball.rect.right >= getWidth())   // ball hit left or right edge
        {
            ball.dx = -ball.dx; // reverse the ball's direction on x-axis
        }
        if (ball.rect.top < 0 || ball.rect.bottom >= getHeight())  // ball hit top or bottom edge
        {
            ball.dy = -ball.dy; // reverse the ball's direction on y-axis
        }

        // handle for pacman going offscreen
        if (pacman_direction_x == LEFT_EDGE && pacman.rect.left <= 0)                       // hit left edge
        {
            pacman.stopMoving();
            pacman_direction_x = NO_EDGE;
        }
        else if (pacman_direction_x == RIGHT_EDGE && pacman.rect.right >= getWidth())       // hit right edge
        {
            pacman.stopMoving();
            pacman_direction_x = NO_EDGE;
        }

        if (pacman_direction_y == TOP_EDGE && pacman.rect.top <= 0)                         // hit top edge
        {
            pacman.stopMoving();
            pacman_direction_y = NO_EDGE;
        }
        else if (pacman_direction_y == BOTTOM_EDGE && pacman.rect.bottom >= getHeight())    // bottom edge
        {
            pacman.stopMoving();
            pacman_direction_y = NO_EDGE;
        }

        pacman.move();
    }
}
