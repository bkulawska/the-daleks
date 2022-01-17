package model.entity;

import model.Grid;
import utils.EntityVisitor;
import utils.Vector2d;

public class TimeTurner extends PowerUpEntity {

    public TimeTurner(int x, int y) {
        super(x, y);
    }

    public TimeTurner(Vector2d position) {
        super(position.x(), position.y());
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }

    public void use(Grid grid){
        grid.restoreLatestSnapshot();
    }
}
