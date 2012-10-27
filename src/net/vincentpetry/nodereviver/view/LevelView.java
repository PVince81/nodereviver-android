package net.vincentpetry.nodereviver.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import net.vincentpetry.nodereviver.model.Edge;
import net.vincentpetry.nodereviver.model.Level;
import net.vincentpetry.nodereviver.model.Node;

public class LevelView extends View {

    private Bitmap levelBitmap;
    private Canvas bitmapCanvas;
    private Level level;

    private Paint nodePaint;
    private Paint edgePaint;
    private Paint markedEdgePaint;

    private HudView hudView;

    private ViewContext viewContext;

    private float scale;

    public LevelView(Level level, ViewContext viewContext) {
        this.level = level;
        this.levelBitmap = null;
        this.bitmapCanvas = null;
        this.viewContext = viewContext;
    }

    public void init(int width, int height) {
        this.levelBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        this.bitmapCanvas = new Canvas(this.levelBitmap);
        this.nodePaint = new Paint();
        this.nodePaint.setColor(Color.rgb(255, 255, 255));
        this.edgePaint = new Paint();
        this.edgePaint.setColor(Color.rgb(128, 128, 128));
        this.edgePaint.setStrokeWidth(3);
        this.edgePaint.setStrokeCap(Paint.Cap.SQUARE);
        this.markedEdgePaint = new Paint();
        this.markedEdgePaint.setColor(Color.rgb(0, 255, 255));
        this.markedEdgePaint.setStrokeWidth(3);
        this.markedEdgePaint.setStrokeCap(Paint.Cap.SQUARE);
        this.hudView = new HudView(viewContext);
        this.hudView.setLevel(level);

        scale = viewContext.getScaling();

        // FIXME: do this properly
        if ( this.viewContext.getScaling() < 1.0f ){
            this.edgePaint.setStrokeWidth(1);
            this.markedEdgePaint.setStrokeWidth(1);
        }

        redrawLevel();
    }

    public void setLevel(Level level) {
        this.level = level;
        if ( this.hudView != null ){
            this.hudView.setLevel(level);
        }
        redrawLevel();
    }

    public void render(Canvas c) {
        if (level == null || levelBitmap == null) {
            return;
        }

        if (level.isDirty()) {
            redrawLevel();
        }
        c.drawBitmap(levelBitmap, 0, 0, null);
    }

    private void redrawLevel() {
        if (this.bitmapCanvas == null){
            return;
        }

        this.bitmapCanvas.drawARGB(255, 0, 0, 0);

        if (level == null) {
            return;
        }
        for (Edge edge : level.getEdges()) {
            this.drawEdge(edge);
        }

        for (Node node : level.getNodes()) {
            if (node.getType() == Node.TYPE_SQUARE) {
                this.drawNode(node);
            }
        }

        this.hudView.render(this.bitmapCanvas);
    }

    private void drawEdge(Edge edge) {
        Node node1 = edge.getSourceNode();
        Node node2 = edge.getTargetNode();
        int x1 = (int)(node1.getX() * scale);
        int y1 = (int)(node1.getY() * scale);
        int x2 = (int)(node2.getX() * scale);
        int y2 = (int)(node2.getY() * scale);
        this.bitmapCanvas.drawLine(x1, y1, x2, y2,
                edge.isMarked() ? markedEdgePaint : edgePaint);

        if (edge.isOneWay() && node2.getType() != Node.TYPE_JOINT) {
            // Draw arrow
            int diffX = node2.getX() - node1.getX();
            int diffY = node2.getY() - node1.getY();
            int offsetX = 0;
            int offsetY = 0;
            int spriteIndex = 0;

            if (diffX == 0) {
                if (diffY < 0) {
                    spriteIndex = SpriteManager.SPRITE_ARROW_UP;
                    offsetY = 8;
                }
                else {
                    spriteIndex = SpriteManager.SPRITE_ARROW_DOWN;
                    offsetY = -8;
                }
            }
            else {
                if (diffX < 0) {
                    spriteIndex = SpriteManager.SPRITE_ARROW_LEFT;
                    offsetX = 8;
                }
                else {
                    spriteIndex = SpriteManager.SPRITE_ARROW_RIGHT;
                    offsetX = -8;
                }
            }
            if (edge.isMarked()) {
                spriteIndex += 4;
            }

            offsetX *= scale;
            offsetY *= scale;

            x2 += offsetX;
            y2 += offsetY;
            viewContext.getSpriteManager().draw(spriteIndex, x2, y2,
                    this.bitmapCanvas);
        }
    }

    private void drawNode(Node node) {
        viewContext.getSpriteManager().draw(SpriteManager.SPRITE_NODE_NORMAL,
                node.getX() * scale, node.getY() * scale, this.bitmapCanvas);
    }
}
