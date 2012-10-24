package net.vincentpetry.nodereviver.view;

import net.vincentpetry.nodereviver.model.Level;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.PorterDuff.Mode;

public class HudView extends View {

    private Level level;
    private Paint paint;
    private Bitmap surface;
    private Canvas surfaceCanvas;

    public HudView(ViewContext viewContext){
        paint = new Paint();
        paint.setARGB(255, 0, 255, 0);
        paint.setTypeface(viewContext.getTypeface());
        paint.setTextSize(15.0f);
        paint.setTextAlign(Align.CENTER);
        this.level = null;
        this.surface = Bitmap.createBitmap(viewContext.getWidth(), 20, Bitmap.Config.RGB_565);
        this.surfaceCanvas = new Canvas(this.surface);
    }

    public void setLevel(Level level){
        if ( this.level != level ){
            this.level = level;
            redraw();
        }
    }

    @Override
    public void render(Canvas c) {
        c.drawBitmap(surface, 0.0f, 0.0f, null);
        //c.drawText(level.getTitle(), 10, 10, paint);
    }

    private void redraw(){
        // TODO: center
        this.surfaceCanvas.drawColor(0, Mode.CLEAR);
        surfaceCanvas.drawText(level.getTitle(), this.surface.getWidth() / 2, 15, paint);
        // TODO: subtitle
    }
}
