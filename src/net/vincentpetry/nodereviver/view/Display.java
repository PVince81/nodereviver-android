package net.vincentpetry.nodereviver.view;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import net.vincentpetry.nodereviver.model.GameContext;

public class Display {
    
    private GameContext gameContext;
    
    private SurfaceHolder surfaceHolder;

    private int width;
    private int height;
    
    public Display(SurfaceHolder surfaceHolder, GameContext gameContext) {
        this.surfaceHolder = surfaceHolder;
        this.gameContext = gameContext;
    }

    public void render() {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas(null);
            synchronized (surfaceHolder) {
                draw(canvas);
            }
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
    
    private void draw(Canvas c) {
        int v = (int)Math.floor((Math.sin(gameContext.dummyValue) + 1.0) / 2.0 * 255.0);
        c.drawARGB(255, v, v, v);
    }

    /**
     * Sets the display size.
     * @param width
     * @param height
     */
    public void setSize(int width, int height){
        synchronized (surfaceHolder) {
            this.width = width;
            this.height = height;
        }
    }
}
