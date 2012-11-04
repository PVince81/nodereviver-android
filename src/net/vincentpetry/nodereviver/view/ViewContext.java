package net.vincentpetry.nodereviver.view;

import net.vincentpetry.nodereviver.model.GameContext;
import android.content.res.Resources;
import android.graphics.Typeface;

public class ViewContext {
    private Typeface typeface;
    private SpriteManager spriteManager;
    private int width;
    private int height;

    private float fontHeightNormal;
    private float fontHeightBig;

    private GameContext gameContext;

    public ViewContext(Resources resources, GameContext gameContext){
        this.gameContext = gameContext;
        this.typeface = Typeface.createFromAsset(resources.getAssets(), "fonts/DejaVuSansMono.ttf");
        this.spriteManager = new SpriteManager(resources);

        fontHeightNormal = 15.0f;
        fontHeightBig = 18.0f;
    }

    public Typeface getTypeface(){
        return typeface;
    }

    public float getFontHeightBig(){
        return fontHeightBig;
    }

    public float getFontHeightNormal(){
        return fontHeightNormal;
    }

    public SpriteManager getSpriteManager(){
        return spriteManager;
    }

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;

        if ( height < 600 ){
            fontHeightNormal = 12.0f;
            fontHeightBig = 15.0f;
        }
        if ( height < 300 ){
            fontHeightNormal = 9.0f;
            fontHeightBig = 11.0f;
        }
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public GameContext getGameContext(){
        return gameContext;
    }
}
