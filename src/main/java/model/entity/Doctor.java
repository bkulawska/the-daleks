package model.entity;

import utils.EntityVisitor;
import utils.Vector2d;

public class Doctor extends Entity implements Movable {

    public boolean isDead;

    public Doctor(int x, int y) {
        super(x, y);
        this.isDead = false;
    }

    public void kill() {
        this.isDead = true;
    }

    @Override
    public void move(Vector2d direction) {
        this.position = this.position.add(direction);
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visitDoctor(this);
    }
}
