package net.vincentpetry.nodereviver;

import java.io.IOException;

import net.vincentpetry.nodereviver.model.Entity;
import net.vincentpetry.nodereviver.model.GameContext;
import net.vincentpetry.nodereviver.model.GameState;
import net.vincentpetry.nodereviver.model.Level;
import net.vincentpetry.nodereviver.model.Player;
import net.vincentpetry.nodereviver.model.TrackingFoe;
import net.vincentpetry.nodereviver.view.Display;
import android.content.Context;
import android.content.res.AssetManager;

/**
 * Main game thread
 * 
 * @author Vincent Petry <PVince81@yahoo.fr>
 */
public class GameThread extends Thread {
    private boolean terminated;
    private Display display;

    private GameContext gameContext;
    
    private long lastTime;
    private double oldDelta;
    private LevelLoader levelLoader;
    private AssetManager assetManager;

    // thread pause
    private boolean paused;
    private Player player;

    public GameThread(Context appContext, GameContext gameContext, Display display) {
        this.display = display;
        this.gameContext = gameContext;
        this.lastTime = 0l;
        this.levelLoader = new LevelLoader();
        this.assetManager = appContext.getAssets();

        this.paused = false;
    }

    private void startGame(){
        loadLevel(1);
        //gameContext.getGameState().setState(GameState.STATE_LEVEL_START, 1000, GameState.STATE_GAME);
        gameContext.getGameState().setState(GameState.STATE_GAME);
        this.lastTime = System.currentTimeMillis();
    }

    public Level loadLevel(int levelNum){
        Level level;
        try {
            level = this.levelLoader.load(assetManager, levelNum);
            level.centerInView(gameContext.getViewportWidth(), gameContext.getViewportHeight());
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        
        player = new Player(level.getPlayerStartNode());
        player.setCurrentNode(level.getPlayerStartNode());
        level.addEntity(player);
        
        // init foes
        for ( Entity entity: level.getEntities() ){
            if (entity instanceof TrackingFoe){
                ((TrackingFoe)entity).setTrackedEntity(player);
            }
        }
        
        gameContext.setLevelNum(levelNum);
        gameContext.setLevel(level);
        gameContext.setPlayer(player);
        display.setLevel(level);
        System.gc();
        return level;
    }
    
    public void nextLevel(){
        int levelNum = gameContext.getLevelNum();
        loadLevel(levelNum + 1);
    }
    
    public void restartLevel(){
        // TODO: just reset the level instead of reloading
        loadLevel(gameContext.getLevelNum());
    }

    public void update(){
        long now = System.currentTimeMillis();

        // Do nothing if mLastTime is in the future.
        // This allows the game-start to delay the start of the physics
        // by 100ms or whatever.
        if (lastTime > now) return;

        double elapsed = (now - lastTime) / 1000.0;
        double delta = elapsed * 60 + oldDelta;
        int updates = (int)Math.floor(delta);
        //System.out.println("Elapsed: " + elapsed + " Delta: " + delta + " Updates: " + updates);
        oldDelta = delta - updates;
        for ( int u = 0; u < updates; u++ ) {
            doUpdate();
        }
        lastTime = now;
    }
    
    public synchronized void doUpdate(){
        Level level = gameContext.getLevel();
        for ( Entity entity: level.getEntities() ){
            entity.update();
        }
        display.update();
        if ( level.hasAllEdgesMarked() ){
            this.nextLevel();
        }

        // check for dead player
        for ( Entity entity: level.getEntities() ){
            if ( entity == this.player ){
                continue;
            }
            int distX = Math.abs(entity.getX() - this.player.getX());
            int distY = Math.abs(entity.getY() - this.player.getY());
            if ( distX < 10 && distY < 10 ){
                this.player.die();
                this.restartLevel();
            }
        }

    }

    public GameContext getGameContext() {
        return this.gameContext;
    }
    
    @Override
    public void run() {
        terminated = false;

        startGame();
        while (!terminated) {
            if (!this.paused) {
                update();
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

    public synchronized void movePlayerToDirection(int vx, int vy){
        Player player = gameContext.getPlayer();
        if ( player.isMoving() || player.isDead() ){
            return;
        }
        player.moveToDirection(vx, vy);
    }
}