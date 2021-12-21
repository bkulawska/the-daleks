package view;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.transform.Affine;
import model.entity.Entity;
import utils.EntityVisitor;

import java.util.List;


public class Renderer {

    private final Canvas canvas;
    private final GraphicsContext context;
    private final int worldWidth, worldHeight;
    private final Affine worldTransform;
    private final EntityVisitor entityDrawVisitor;

    public Renderer(Canvas canvas, int worldWidth, int worldHeight) {
        this.canvas = canvas;
        this.context = canvas.getGraphicsContext2D();
        this.worldWidth = worldWidth;
        this.worldHeight = worldHeight;
        worldTransform = new Affine();
        worldTransform.appendScale(canvas.getWidth() / (float) worldWidth, canvas.getHeight() / (float) worldHeight);
        this.entityDrawVisitor = new EntityDrawVisitor(context, worldTransform);
    }

    public void updateCanvas(List<Entity> entities) {
        drawBackground();
        drawEntities(entities);
        drawGrid();
    }

    private void drawEntities(List<Entity> entities) {
        entities.forEach(entity -> entity.accept(entityDrawVisitor));
    }

    private void drawBackground() {
        context.drawImage(new Image("image/grass.png"), 0, 0);
    }

    private void drawGrid() {
        context.setStroke(Color.LIGHTGRAY);
        double wScale = canvas.getWidth() / (float) worldWidth;
        double hScale = canvas.getHeight() / (float) worldHeight;

        // vertical lines
        for (int x=1; x<=worldWidth; x++) {
            context.strokeLine(x * wScale, 0, x * wScale, canvas.getHeight());
        }

        // horizontal lines
        for (int y=1; y<=worldHeight; y++) {
            context.strokeLine(0, y * hScale, canvas.getWidth(), y * hScale);
        }
    }

}
