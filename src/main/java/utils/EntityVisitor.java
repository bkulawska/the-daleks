package utils;

import model.entity.Animal;
import model.entity.Entity;
import model.entity.Rock;

public interface EntityVisitor {
    void visitEntity(Entity entity);

    void visitAnimal(Animal animal);

    void visitRock(Rock rock);
}
