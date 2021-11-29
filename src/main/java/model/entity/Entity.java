package model.entity;

import utils.EntityVisitor;
import utils.Vector2d;

public abstract class Entity {

    public Vector2d position;

    public Entity(int x, int y){
        this.position = new Vector2d(x, y);
    }

    public Vector2d getPosition(){
        return position;
    }

    public void accept(EntityVisitor visitor) {
        visitor.visitEntity(this);
    }
}
