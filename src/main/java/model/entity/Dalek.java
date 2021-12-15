package model.entity;

import utils.EntityVisitor;
import utils.Vector2d;

public class Dalek extends Entity implements Movable {

    public Dalek(int x, int y) {
        super(x, y);
    }

    @Override
    public void move(Vector2d direction){
        this.position = this.position.add(direction);
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visitDalek(this);
    }
}
