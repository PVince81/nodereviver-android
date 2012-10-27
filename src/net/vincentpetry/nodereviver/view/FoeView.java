package net.vincentpetry.nodereviver.view;

import net.vincentpetry.nodereviver.model.Entity;
import net.vincentpetry.nodereviver.model.SimpleFoe;
import android.graphics.Canvas;

public class FoeView extends View {
    private Entity entity;
    private SpriteManager spriteManager;
    private int spriteIndex;
    private float scale;

    public FoeView(Entity entity, ViewContext viewContext){
        this.entity = entity;
        this.scale = viewContext.getScaling();
        this.spriteManager = viewContext.getSpriteManager();
        if ( entity instanceof SimpleFoe ){
            this.spriteIndex = SpriteManager.SPRITE_FOE1;
        }
        else{
            this.spriteIndex = SpriteManager.SPRITE_FOE2;
        }
    }

    @Override
    public void render(Canvas c) {
        spriteManager.draw(spriteIndex, entity.getX() * scale, entity.getY() * scale, c);
    }

}
