package model.entity;

import model.Grid;
import utils.EntityVisitor;
import utils.Vector2d;

public abstract class PowerUpEntity extends Entity {

    public Vector2d position;

    public PowerUpEntity(int x, int y){
        super(x,y);
    }

    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }

    public void use(Grid grid){

    }
}
