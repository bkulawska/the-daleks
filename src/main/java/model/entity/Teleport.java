package model.entity;

import model.Grid;
import utils.EntityVisitor;
import utils.GameComputations;
import utils.Vector2d;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Teleport extends PowerUpEntity {

    public Teleport(int x, int y) {
        super(x, y);
    }

    public Teleport(Vector2d position) {
        super(position.x(), position.y());
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }

    public void use(Grid grid){
        List<Vector2d> freePositions = GameComputations.getFreeShuffledPositions(grid);
        grid.getDoctor().teleport(freePositions.get(0));
    }
}
