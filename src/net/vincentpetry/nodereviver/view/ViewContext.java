package net.vincentpetry.nodereviver.view;

import android.content.res.Resources;
import android.graphics.Typeface;

public class ViewContext {
    private Typeface typeface;
    private SpriteManager spriteManager;
    private int width;
    private int height;

    public ViewContext(Resources resources){
        this.typeface = Typeface.createFromAsset(resources.getAssets(), "fonts/DejaVuSansMono.ttf");
        this.spriteManager = new SpriteManager(resources);
    }

    public Typeface getTypeface(){
        return typeface;
    }

    public SpriteManager getSpriteManager(){
        return spriteManager;
    }

    public void setSize(int width, int height){
        this.width = width;
        this.height = height;
    }

    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }
}
