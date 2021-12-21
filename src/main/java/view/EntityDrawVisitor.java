package view;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.transform.Affine;
import model.entity.Dalek;
import model.entity.Doctor;
import model.entity.Entity;
import model.entity.PileOfCrap;
import utils.EntityVisitor;
import utils.Vector2d;

/**
 * Class responsible for drawing various entities and subclasses.
 * It uses Visitor design pattern.
 */
public record EntityDrawVisitor(GraphicsContext context, Affine worldTransform) implements EntityVisitor {

    @Override
    public void visitEntity(Entity entity) {
        drawColoredSquare(entity.position, Color.HOTPINK);
    }

    @Override
    public void visitDalek(Dalek dalek) {
        drawTexture(dalek.getPosition(), "image/dalek.png");
    }

    @Override
    public void visitDoctor(Doctor doctor) {
        drawTexture(doctor.getPosition(), "image/doctor.png");
    }

    @Override
    public void visitPileOfCrap(PileOfCrap pileOfCrap) {
        drawTexture(pileOfCrap.getPosition(), "image/crap.png");    }

    private void drawColoredSquare(Vector2d position, Color color) {
        context.setTransform(worldTransform);
        context.setFill(color);
        context.fillRect(position.x(), position.y(), 1, 1);
        context.setTransform(new Affine());
    }

    private void drawTexture(Vector2d position, String url) {
        context.drawImage(new Image(url), position.x() * worldTransform.getMxx(), position.y() * worldTransform.getMyy());
    }
}
