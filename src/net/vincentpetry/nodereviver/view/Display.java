package net.vincentpetry.nodereviver.view;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import net.vincentpetry.nodereviver.model.Entity;
import net.vincentpetry.nodereviver.model.GameContext;
import net.vincentpetry.nodereviver.model.GameState;
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

    private TitleScreenView titleScreen;

    public Display(SurfaceHolder surfaceHolder, ViewContext viewContext, GameContext gameContext) {
        this.surfaceHolder = surfaceHolder;
        this.gameContext = gameContext;
        this.viewContext = viewContext;
        this.levelView = new LevelView(null, viewContext);
        this.entityViews = new ArrayList<View>(10);

        this.titleScreen = null;
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
                playerParticles = new ParticlesView(viewContext, 80, 1);
                entityViews.add( playerView );
                playerView.setParticlesView(playerParticles);

                this.currentEdgeView = new EdgeView(player, viewContext);
            }
            else if ( (entity instanceof SimpleFoe) || (entity instanceof TrackingFoe) ){
                entityViews.add( new FoeView((Entity)entity, viewContext) );
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
            level.resetDirty();
        }

        if ( gameContext.getGameState().getState() == GameState.STATE_TITLE && titleScreen != null ){
            titleScreen.render(c);
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
            this.titleScreen = new TitleScreenView(viewContext);
        }
    }
}
