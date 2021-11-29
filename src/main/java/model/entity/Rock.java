package model.entity;

import utils.EntityVisitor;

public class Rock extends Entity {

    public Rock(int x, int y) {
        super(x, y);
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visitRock(this);
    }
}
