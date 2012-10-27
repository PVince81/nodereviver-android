package net.vincentpetry.nodereviver.view;

import net.vincentpetry.nodereviver.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class SpriteManager {
    private static class SpriteDef{
        Rect rect;
        int width;
        int height;
        int midHeight;
        int midWidth;

        SpriteDef(Rect rect, int width, int height){
            this.rect = rect;
            this.width = width;
            this.height = height;
            this.midWidth = (int)Math.floor(this.width / 2.0f);
            this.midHeight = (int)Math.floor(this.height / 2.0f);
        }
    };

    private static SpriteDef[] SPRITES_DEF;

    public static int SPRITE_PLAYER = 0;
    public static int SPRITE_FOE1 = 1;
    public static int SPRITE_FOE2 = 2;
    public static int SPRITE_ARROW_UP = 3;
    public static int SPRITE_ARROW_DOWN = 4;
    public static int SPRITE_ARROW_LEFT = 5;
    public static int SPRITE_ARROW_RIGHT = 6;
    public static int SPRITE_ARROW_UP_ACTIVE = 7;
    public static int SPRITE_ARROW_DOWN_ACTIVE = 8;
    public static int SPRITE_ARROW_LEFT_ACTIVE = 9;
    public static int SPRITE_ARROW_RIGHT_ACTIVE = 10;
    public static int SPRITE_NODE_NORMAL = 11;
    public static int SPRITE_NODE_ACTIVE = 12;

    private static Rect[] SPRITES_RECT = {
        new Rect(0, 0, 20, 20),
        new Rect(20, 0, 40, 20),
        new Rect(40, 0, 60, 20),
        new Rect(60, 12, 72, 18),
        new Rect(72, 12, 84, 18),
        new Rect(66, 0, 72, 12),
        new Rect(60, 0, 66, 12),
        new Rect(84, 12, 96, 18),
        new Rect(96, 12, 108, 18),
        new Rect(90, 0, 96, 12),
        new Rect(84, 0, 90, 12),
        new Rect(96, 0, 106, 10),
        new Rect(106, 0, 116, 10)
    };

    private Bitmap sprites;
    private Paint paint;
    private Rect tempRect;

    public SpriteManager(Resources resources) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        sprites = BitmapFactory.decodeResource(resources, R.drawable.sprites,
                options);
        paint = new Paint();
        tempRect = new Rect();
        paint.setAlpha(255);
    }

    public void setAlpha(int alpha){
        paint.setAlpha(alpha);
    }

    /**
     * Draws the sprite with the given index onto the given rectangle.
     * @param spriteIndex
     * @param rect
     * @param c
     */
    public void draw(int spriteIndex, Rect rect, Canvas c){
        SpriteDef def = SPRITES_DEF[spriteIndex];
        c.drawBitmap(sprites, def.rect, rect, paint);
    }

    /**
     * Draws the sprite with the given index at the given position.
     * The center of the sprite will correspond with the given
     * position.
     * @param spriteIndex
     * @param x
     * @param y
     * @param c
     */
    public void draw(int spriteIndex, int x, int y, Canvas c){
        SpriteDef def = SPRITES_DEF[spriteIndex];
        tempRect.left = x - def.midWidth;
        tempRect.top = y - def.midHeight;
        tempRect.right = x + def.midWidth;
        tempRect.bottom = y + def.midHeight;
        c.drawBitmap(sprites, def.rect, tempRect, paint);
    }

    static{
        SPRITES_DEF = new SpriteManager.SpriteDef[SPRITES_RECT.length];
        for ( int i = 0; i < SPRITES_RECT.length; i++ ){
            Rect rect = SPRITES_RECT[i];
            SpriteDef def = new SpriteManager.SpriteDef(
                    rect,
                    rect.right - rect.left,
                    rect.bottom - rect.top
            );
            SPRITES_DEF[i] = def;
        }
    }
}
