package net.vincentpetry.nodereviver;

import java.io.IOException;

import net.vincentpetry.nodereviver.model.Entity;
import net.vincentpetry.nodereviver.model.GameContext;
import net.vincentpetry.nodereviver.model.GameState;
import net.vincentpetry.nodereviver.model.Level;
import net.vincentpetry.nodereviver.model.Player;
import net.vincentpetry.nodereviver.model.PlayerState;
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
    private int[] TITLE_DEMO =
            { 3, 0, 0, 3, 0, 1, 2, 1, 1, 3, 0, 2, 0, 3, 1,
            2, 1, 3, 0, 2, 0, 3, 1, 2, 1, 3, 0, 2, 0, 0, 3, 2, 1, 3, 2, 1, 3,
            3, 2, 1, 3, 1, 2, 2, 2, 2, 2, 2, 2, 1, 2, 1, 1, 0, 3, 1, 2, 3, 0,
            2, 0, 3, 1, 2, 1, 1, 3, 2, 0, 3, 2, 0, 3, 3, 2, 0, 3, 1, 1, 3, 3,
            3, 0, 0, 1, 2, 2, 0, 0, 0, 3, 1, 1, 2, 3, 3, 2, 0, 2, 3, 3, 2, 0,
            3, 1, 1, 3, 3, 0, 0, 1, 2, 2, 0, 0, 0, 3, 1, 3, 2, 2, 1, 3, 2, 1,
            3, 2, 0, 0, 3, 0, 3, 1, 2, 1, 1, 0, 0, 3, 3, 1, 2, 2, 3, 1, 1, 2 };

    private int titleDemoIndex;

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

    private void showTitleScreen(){
        loadLevel(0);
        player.getState().setState(PlayerState.STATE_DEMO);
        gameContext.getGameState().setState(GameState.STATE_TITLE);
        this.lastTime = System.currentTimeMillis();
        player.setSpeed(4);
        titleDemoIndex = 0;
    }

    private void startGame(){
        loadLevel(1);
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
        player.getState().setState(PlayerState.STATE_APPEAR, 1000, PlayerState.STATE_NORMAL);
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
        gameContext.getGameState().setState(GameState.STATE_GAME);
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
        GameState state = gameContext.getGameState();
        Level level;

        state.update();
        if (state.getState() == GameState.STATE_RESTART_LEVEL){
            this.restartLevel();
            return;
        }
        else if (state.getState() == GameState.STATE_NEXT_LEVEL){
            this.nextLevel();
            return;
        }
        else if (state.getState() == GameState.STATE_TITLE){
            if ( !player.isMoving() && titleDemoIndex < TITLE_DEMO.length ){
                int direction = TITLE_DEMO[titleDemoIndex];
                int vx = 0;
                int vy = 0;
                switch(direction){
                    case 0:
                        vx = 0;
                        vy = -1;
                        break;
                    case 1:
                        vx = 0;
                        vy = 1;
                        break;
                    case 2:
                        vx = -1;
                        vy = 0;
                        break;
                    case 3:
                        vx = 1;
                        vy = 0;
                        break;
                }
                player.moveToDirection(vx, vy);
                titleDemoIndex++;
            }
        }

        level = gameContext.getLevel();
        for ( Entity entity: level.getEntities() ){
            entity.update();
        }
        display.update();
        if ( state.getState() == GameState.STATE_GAME ){
            if ( level.hasAllEdgesMarked() ){
                this.player.getState().setState(PlayerState.STATE_DISAPPEAR, 1000, PlayerState.STATE_NORMAL);
                state.setState(GameState.STATE_LEVEL_END, 1000, GameState.STATE_NEXT_LEVEL);
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
                    state.setState(GameState.STATE_DEAD, 1000, GameState.STATE_RESTART_LEVEL);
                }
            }
        }
        if ( state.getState() == GameState.STATE_TITLE ){
            if ( level.hasAllEdgesMarked() ){
                this.showTitleScreen();
            }
        }
    }

    public GameContext getGameContext() {
        return this.gameContext;
    }

    @Override
    public void run() {
        terminated = false;

        this.lastTime = System.currentTimeMillis();
        showTitleScreen();
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
        this.lastTime = System.currentTimeMillis();
    }

    public synchronized void movePlayerToDirection(int vx, int vy){
        int state = gameContext.getGameState().getState();
        if ( state == GameState.STATE_GAME ){
            Player player = gameContext.getPlayer();
            if ( player.isMoving() || !player.canMove() ){
                return;
            }
            player.moveToDirection(vx, vy);
        }
        else if ( state == GameState.STATE_TITLE ){
            this.startGame();
        }
    }

    public synchronized void playerAction(){
        if ( gameContext.getGameState().getState() == GameState.STATE_TITLE ){
            this.startGame();
        }
    }

    public synchronized boolean actionBack(){
        if ( gameContext.getGameState().getState() != GameState.STATE_TITLE ){
            showTitleScreen();
            return true;
        }
        return false;
    }
}
