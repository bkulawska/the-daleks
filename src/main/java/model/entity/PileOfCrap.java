package model.entity;

import utils.EntityVisitor;

public class PileOfCrap extends Entity {

    public PileOfCrap(int x, int y) {
        super(x, y);
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }
}
