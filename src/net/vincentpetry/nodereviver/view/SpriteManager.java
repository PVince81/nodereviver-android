package net.vincentpetry.nodereviver.view;

import net.vincentpetry.nodereviver.R;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class SpriteManager {
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
    
    public SpriteManager(Resources resources){
        sprites = BitmapFactory.decodeResource(
                resources,
                R.drawable.sprites);
        paint = new Paint();
    }

    public void draw(int spriteIndex, Rect rect, int alpha, Canvas c){
        paint.setAlpha(alpha);
        c.drawBitmap(sprites, SPRITES_RECT[spriteIndex], rect, paint);
    }
}
