package net.vincentpetry.nodereviver;

import net.vincentpetry.nodereviver.view.Display;
import android.view.SurfaceHolder;

/**
 * Main game thread
 * 
 * @author Vincent Petry <PVince81@yahoo.fr>
 */
public class GameThread extends Thread {

    private boolean terminated;
    private Display display;
    private Engine engine;

    // thread pause
    private boolean paused;

    public GameThread(SurfaceHolder holder, Display display, Engine gameEngine) {
        this.display = display;
        this.engine = gameEngine;
        this.paused = false;
    }

    @Override
    public void run() {
        terminated = false;
        engine.start();
        while (!terminated) {
            if (!this.paused) {
                engine.update();
                display.render();
                // TODO: fps limit ?
            } else {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        }
    }

    public void terminate() {
        this.terminated = true;
    }

    public void pause() {
        this.paused = true;
    }

    public void unpause() {
        this.paused = false;
    }

}
