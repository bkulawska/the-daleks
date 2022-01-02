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

    public static final String DALEK_IMAGE = "image/dalek.png";
    public static final String DOCTOR_IMAGE = "image/doctor.png";
    public static final String CRAP_IMAGE = "image/crap.png";
    public static final Color UNKNOWN_ENTITY_COLOR = Color.HOTPINK;
    @Override
    public void visit(Dalek dalek) {
        drawTexture(dalek.getPosition(), DALEK_IMAGE);
    }

    @Override
    public void visit(Doctor doctor) {
        drawTexture(doctor.getPosition(), DOCTOR_IMAGE);
    }

    @Override
    public void visit(PileOfCrap pileOfCrap) {
        drawTexture(pileOfCrap.getPosition(), CRAP_IMAGE);    }

    @Override
    public void visit(Entity entity) {
        drawColoredSquare(entity.position, UNKNOWN_ENTITY_COLOR);
    }

    private void drawTexture(Vector2d position, String url) {
        context.drawImage(new Image(url), position.x() * worldTransform.getMxx(), position.y() * worldTransform.getMyy());
    }

    private void drawColoredSquare(Vector2d position, Color color) {
        context.setTransform(worldTransform);
        context.setFill(color);
        context.fillRect(position.x(), position.y(), 1, 1);
        context.setTransform(new Affine());
    }

}
