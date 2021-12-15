package renderer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
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
public record EntityDrawVisitor(GraphicsContext context) implements EntityVisitor {

    @Override
    public void visitEntity(Entity entity) {
        drawColoredSquare(entity.position, Color.HOTPINK);
    }

    @Override
    public void visitDalek(Dalek dalek) {
        drawColoredSquare(dalek.position, Color.BLACK);
    }

    @Override
    public void visitDoctor(Doctor doctor) {
        drawColoredSquare(doctor.position, Color.BLUE);
    }

    @Override
    public void visitPileOfCrap(PileOfCrap pileOfCrap) {
        drawColoredSquare(pileOfCrap.position, Color.ORANGE);
    }

    private void drawColoredSquare(Vector2d position, Color color) {
        context.setFill(color);
        context.fillRect(position.x(), position.y(), 1, 1);
    }
}
