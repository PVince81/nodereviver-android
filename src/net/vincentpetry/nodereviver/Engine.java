package net.vincentpetry.nodereviver;

import net.vincentpetry.nodereviver.model.GameContext;

public class Engine {
    private GameContext gameContext;
    
    private long lastTime;
    private double oldDelta;
    
    public Engine(GameContext gameContext){
        this.gameContext = gameContext;
        this.lastTime = 0l;
    }
    
    public void start(){
        this.lastTime = System.currentTimeMillis();
    }
    
    public void update(){
        long now = System.currentTimeMillis();

        // Do nothing if mLastTime is in the future.
        // This allows the game-start to delay the start of the physics
        // by 100ms or whatever.
        if (lastTime > now) return;

        double elapsed = (now - lastTime) / 1000.0;
        double delta = elapsed * 30 + oldDelta;
        int updates = (int)Math.floor(delta);
        //System.out.println("Elapsed: " + elapsed + " Delta: " + delta + " Updates: " + updates);
        oldDelta = delta - updates;
        for ( int u = 0; u < updates; u++ ) {
            doUpdate();
        }
        lastTime = now;
    }
    
    public void doUpdate(){
        // TODO        
        gameContext.dummyValue += 0.1f;
    }
    
}
