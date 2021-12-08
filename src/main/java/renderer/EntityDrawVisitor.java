package renderer;

import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;
import model.entity.Animal;
import model.entity.Entity;
import model.entity.Rock;
import utils.EntityVisitor;
import utils.Vector2d;

/**
 * Class responsible for drawing various entities and subclasses.
 * It uses Visitor design pattern.
 */
public class EntityDrawVisitor implements EntityVisitor {

    private final GraphicsContext context;

    public EntityDrawVisitor(GraphicsContext context) {
        this.context = context;
    }

    @Override
    public void visitEntity(Entity entity) {
        drawColoredSquare(entity.position, Color.HOTPINK);
    }

    @Override
    public void visitAnimal(Animal animal) {
        drawColoredSquare(animal.position, Color.BROWN);
    }

    @Override
    public void visitRock(Rock rock) {
        drawColoredSquare(rock.position, Color.GREY);
    }

    private void drawColoredSquare(Vector2d position, Color color) {
        context.setFill(color);
        context.fillRect(position.x(), position.y(), 1, 1);
    }
}
