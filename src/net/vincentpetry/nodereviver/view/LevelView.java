package net.vincentpetry.nodereviver.view;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import net.vincentpetry.nodereviver.model.Edge;
import net.vincentpetry.nodereviver.model.Level;
import net.vincentpetry.nodereviver.model.Node;

public class LevelView {

    private Bitmap levelBitmap;
    private Canvas bitmapCanvas;
    private Level level;

    private Paint nodePaint;
    private Paint edgePaint;
    private Paint markedEdgePaint;

    private int nodeSize;

    public LevelView(Level level) {
        this.level = level;
        this.levelBitmap = null;
        this.bitmapCanvas = null;
    }

    public void init(int width, int height) {
        int maxDim = Math.max(width, height);
        nodeSize = maxDim / 80;

        this.levelBitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.RGB_565);
        this.bitmapCanvas = new Canvas(this.levelBitmap);
        this.nodePaint = new Paint();
        this.nodePaint.setColor(Color.rgb(255, 255, 255));
        this.edgePaint = new Paint();
        this.edgePaint.setColor(Color.rgb(128, 128, 128));
        this.edgePaint.setStrokeWidth(nodeSize / 6);
        this.markedEdgePaint = new Paint();
        this.markedEdgePaint.setColor(Color.rgb(0, 255, 255));
        this.markedEdgePaint.setStrokeWidth(nodeSize / 6);

        redrawLevel();
    }

    public void setLevel(Level level) {
        this.level = level;
        redrawLevel();
    }

    public void draw(Canvas c) {
        if (level == null || levelBitmap == null) {
            return;
        }
        if (level.isDirty()) {
            level.resetDirty();
            redrawLevel();
        }
        c.drawBitmap(levelBitmap, 0, 0, null);
    }

    private void redrawLevel() {
        this.bitmapCanvas.drawARGB(255, 0, 0, 0);

        if (level == null) {
            return;
        }
        for (Edge edge : level.getEdges()) {
            this.drawEdge(edge);
        }

        for (Node node : level.getNodes()) {
            if (node.getType() ==  Node.TYPE_SQUARE){
                this.drawNode(node);
            }
        }
    }

    private void drawEdge(Edge edge) {
        Node node1 = edge.getSourceNode();
        Node node2 = edge.getTargetNode();
        int x1 = node1.getX();
        int y1 = node1.getY();
        int x2 = node2.getX();
        int y2 = node2.getY();
        this.bitmapCanvas.drawLine(x1, y1, x2, y2, edge.isMarked()?markedEdgePaint:edgePaint);
    }

    private void drawNode(Node node) {
        int x = node.getX();
        int y = node.getY();
        this.bitmapCanvas.drawRect(x - nodeSize, y - nodeSize, x + nodeSize, y
                + nodeSize, nodePaint);
    }
}
