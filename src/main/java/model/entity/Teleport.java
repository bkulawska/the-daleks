package model.entity;

import utils.EntityVisitor;
import utils.Vector2d;

public class Teleport extends Entity {

    public Teleport(int x, int y) {
        super(x, y);
    }

    public Teleport(Vector2d position) {
        super(position.x(), position.y());
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }
}
