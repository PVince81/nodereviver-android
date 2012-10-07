package net.vincentpetry.nodereviver.view;

import java.util.ArrayList;
import java.util.List;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

import net.vincentpetry.nodereviver.model.Entity;
import net.vincentpetry.nodereviver.model.GameContext;
import net.vincentpetry.nodereviver.model.Level;
import net.vincentpetry.nodereviver.model.Player;

public class Display {
    
    private GameContext gameContext;
    
    private SurfaceHolder surfaceHolder;

    private int width;
    private int height;
    
    private LevelView levelView;
    private SpriteManager spriteManager;
    private List<EntityView> entityViews;
    
    public Display(SurfaceHolder surfaceHolder, SpriteManager spriteManager, GameContext gameContext) {
        this.surfaceHolder = surfaceHolder;
        this.gameContext = gameContext;
        this.spriteManager = spriteManager;
        this.levelView = new LevelView(null);
        this.entityViews = new ArrayList<EntityView>(10);
    }

    public void setLevel(Level level){
        this.levelView.setLevel(level);
        entityViews.clear();
        for ( Entity entity: level.getEntities() ){
            if ( entity instanceof Player ){
                Player player = (Player)entity;
                entityViews.add( new PlayerView(player, spriteManager, gameContext) );
            }
        }
    }
    
    public void update(){
        for ( EntityView view: entityViews ){
            view.update();
        }
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
            for ( EntityView view: entityViews){
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
