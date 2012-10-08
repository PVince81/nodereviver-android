package net.vincentpetry.nodereviver.view;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import net.vincentpetry.nodereviver.model.Entity;
import net.vincentpetry.nodereviver.model.GameContext;
import net.vincentpetry.nodereviver.model.Level;
import net.vincentpetry.nodereviver.model.Player;
import net.vincentpetry.nodereviver.model.SimpleFoe;
import net.vincentpetry.nodereviver.model.TrackingFoe;

public class Display {
    
    private GameContext gameContext;
    
    private SurfaceHolder surfaceHolder;

    private int width;
    private int height;
    
    private LevelView levelView;
    private SpriteManager spriteManager;
    private List<View> entityViews;
    private EdgeView currentEdgeView;
    
    public Display(SurfaceHolder surfaceHolder, SpriteManager spriteManager, GameContext gameContext) {
        this.surfaceHolder = surfaceHolder;
        this.gameContext = gameContext;
        this.spriteManager = spriteManager;
        this.levelView = new LevelView(null);
        this.entityViews = new ArrayList<View>(10);
    }

    public void setLevel(Level level){
        ParticlesView playerParticles = null;
        this.levelView.setLevel(level);
        entityViews.clear();
        for ( Entity entity: level.getEntities() ){
            if ( entity instanceof Player ){
                Player player = (Player)entity;
                PlayerView playerView = new PlayerView(player, spriteManager);
                //ParticlesView playerParticles = new ParticlesView(40, 1);
                playerParticles = new ParticlesView(80, 1);
                entityViews.add( playerView );
                playerView.setParticlesView(playerParticles);
                
                this.currentEdgeView = new EdgeView(player);
            }
            else if ( (entity instanceof SimpleFoe) || (entity instanceof TrackingFoe) ){
                entityViews.add( new FoeView((Entity)entity, spriteManager) );
            }
        }
        // this ensures that particles are drawn first
        entityViews.add(0, playerParticles);
    }
    
    public void update(){
        for ( View view: entityViews ){
            view.update();
        }
        this.currentEdgeView.update();
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
        Level level = gameContext.getLevel();
        if ( level != null ) {
            levelView.draw(c);
            this.currentEdgeView.render(c);
            for ( View view: entityViews){
                view.render(c);
            }
        }
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
            this.levelView.init(width, height);
        }
    }
    
    public int getWidth(){
        return width;
    }
    
    public int getHeight(){
        return height;
    }
}
