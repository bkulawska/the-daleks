package model.entity;

import utils.EntityVisitor;
import utils.Vector2d;

public class Doctor extends Entity implements Movable {

    public boolean dead;

    public Doctor(int x, int y) {
        super(x, y);
        this.dead = false;
    }

    public void kill() {
        this.dead = true;
    }

    @Override
    public void move(Vector2d direction) {
        this.position = this.position.add(direction);
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }
}
