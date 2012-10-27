package net.vincentpetry.nodereviver.view;

import net.vincentpetry.nodereviver.model.Entity;
import net.vincentpetry.nodereviver.model.SimpleFoe;
import android.graphics.Canvas;
import android.graphics.Rect;

public class FoeView extends View {
    private Entity entity;
    private Rect rect;
    private SpriteManager spriteManager;
    private int spriteIndex;

    public FoeView(Entity entity, ViewContext viewContext){
        this.entity = entity;
        this.spriteManager = viewContext.getSpriteManager();
        if ( entity instanceof SimpleFoe ){
            this.spriteIndex = SpriteManager.SPRITE_FOE1;
        }
        else{
            this.spriteIndex = SpriteManager.SPRITE_FOE2;
        }
        this.rect = new Rect();
    }

    @Override
    public void update(){
        this.rect.left = this.entity.getX() - 10;
        this.rect.top = this.entity.getY() - 10;
        this.rect.bottom = this.rect.top + 20;
        this.rect.right = this.rect.left + 20;
    }

    @Override
    public void render(Canvas c) {
        spriteManager.draw(spriteIndex, rect, c);
    }

}
