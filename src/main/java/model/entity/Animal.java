package model.entity;

import utils.EntityVisitor;
import utils.Vector2d;

public class Animal extends Entity implements Movable {

    public Animal(int x, int y) {
        super(x, y);
    }

    @Override
    public void move(Vector2d direction){
        this.position.add(direction);
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visitAnimal(this);
    }
}
