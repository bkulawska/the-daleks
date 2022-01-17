package model.entity;

import utils.EntityVisitor;
import utils.Vector2d;

public class PileOfCrap extends Entity {

    public PileOfCrap(int x, int y) {
        super(x, y);
    }

    public PileOfCrap(Vector2d position) {
        super(position.x(), position.y());
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }
}
