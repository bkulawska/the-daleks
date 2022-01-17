package model.entity;

import model.Grid;
import utils.EntityVisitor;
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
        var height = grid.getHeight();
        var width = grid.getWidth();
        List<Vector2d> freePositions = IntStream
                .rangeClosed(0, width * height - 1)
                .mapToObj(i -> new Vector2d(i % width, i / width))
                .collect(Collectors.toCollection(ArrayList::new));
        Collections.shuffle(freePositions, new Random(System.currentTimeMillis()));
        grid.getDoctor().teleport(freePositions.get(0));
    }
}
