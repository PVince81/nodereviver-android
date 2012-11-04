package net.vincentpetry.nodereviver;

import net.vincentpetry.nodereviver.model.GameContext;
import net.vincentpetry.nodereviver.view.Display;
import net.vincentpetry.nodereviver.view.ViewContext;
import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameSurfaceView extends SurfaceView implements
        SurfaceHolder.Callback {

    private GameThread thread;
    private Display gameDisplay;
    private GameContext gameContext;
    private ViewContext viewContext;

    private static float minMoveThreshold = 10.0f;
    private float lastX;
    private float lastY;

    public GameSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);

        gameContext = new GameContext();
        viewContext = new ViewContext(context.getResources(), gameContext);

        this.gameDisplay = new Display(holder, viewContext, gameContext);
        thread = new GameThread(context, gameContext, gameDisplay);

        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width,
            int height) {
        this.gameDisplay.setSize(width, height);
        gameContext.setViewportWidth(width);
        gameContext.setViewportHeight(height);
        if (gameContext.getLevel() != null) {
            gameContext.getLevel().centerInView(width, height);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        thread.terminate();
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
            }
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int vx = 0;
        int vy = 0;
        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            vy = -1;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            vy = 1;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_LEFT) {
            vx = -1;
        } else if (keyCode == KeyEvent.KEYCODE_DPAD_RIGHT) {
            vx = 1;
        }
        if (vx != 0 || vy != 0) {
            thread.movePlayerToDirection(vx, vy);
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK){
            return thread.actionBack();
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * Standard window-focus override. Notice focus lost so we can pause on
     * focus lost. e.g. user switches to take a call.
     */
    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        if (!hasWindowFocus)
            thread.pause();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final int action = event.getAction();
        switch (action) {
        case MotionEvent.ACTION_DOWN:
            lastX = event.getX();
            lastY = event.getY();
            break;
        case MotionEvent.ACTION_UP:
            float moveX = event.getX() - lastX;
            float moveY = event.getY() - lastY;
            int vx = 0;
            int vy = 0;
            if ( Math.abs(moveX) < minMoveThreshold ){
                moveX = 0;
            }
            if ( Math.abs(moveY) < minMoveThreshold ){
                moveY = 0;
            }
            if ( Math.abs(moveX) > Math.abs(moveY) ){
                if ( moveX > 0 ){
                    vx = 1;
                }
                else {
                    vx = -1;
                }
            }
            else {
                if ( moveY > 0 ){
                    vy = 1;
                }
                else {
                    vy = -1;
                }
            }

            if (vx != 0 || vy != 0) {
                thread.movePlayerToDirection(vx, vy);
                return true;
            }
            else{
                thread.playerAction();
            }
            break;
        }
        return true;
    }

    public GameThread getGameThread(){
        return thread;
    }
}
