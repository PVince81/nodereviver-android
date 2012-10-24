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
    private ViewContext viewContext;
    
    private SurfaceHolder surfaceHolder;
    
    private LevelView levelView;
    private List<View> entityViews;
    private EdgeView currentEdgeView;

    private HudView hudView;
    
    public Display(SurfaceHolder surfaceHolder, ViewContext viewContext, GameContext gameContext) {
        this.surfaceHolder = surfaceHolder;
        this.gameContext = gameContext;
        this.viewContext = viewContext;
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
                PlayerView playerView = new PlayerView(player, viewContext);
                //ParticlesView playerParticles = new ParticlesView(40, 1);
                playerParticles = new ParticlesView(80, 1);
                entityViews.add( playerView );
                playerView.setParticlesView(playerParticles);
                
                this.currentEdgeView = new EdgeView(player);
            }
            else if ( (entity instanceof SimpleFoe) || (entity instanceof TrackingFoe) ){
                entityViews.add( new FoeView((Entity)entity, viewContext) );
            }
        }
        // this ensures that particles are drawn first
        entityViews.add(0, playerParticles);
        if ( hudView == null ) {
            hudView = new HudView(viewContext);
        }
        hudView.setLevel(level);
    }
    
    public void update(){
        for ( View view: entityViews ){
            view.update();
        }
        this.currentEdgeView.update();
        this.hudView.update();
    }
    
    public void render() {
        Canvas canvas = null;
        try {
            canvas = surfaceHolder.lockCanvas(null);
            synchronized (surfaceHolder) {
                doRender(canvas);
            }
        } finally {
            if (canvas != null) {
                surfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }
    
    private void doRender(Canvas c) {
        Level level = gameContext.getLevel();
        if ( level != null ) {
            levelView.render(c);
            this.currentEdgeView.render(c);
            for ( View view: entityViews){
                view.render(c);
            }
            hudView.render(c);
            level.resetDirty();
        }
    }

    /**
     * Sets the display size.
     * @param width
     * @param height
     */
    public void setSize(int width, int height){
        synchronized (surfaceHolder) {
            viewContext.setSize(width, height);
            this.levelView.init(width, height);
        }
    }
}