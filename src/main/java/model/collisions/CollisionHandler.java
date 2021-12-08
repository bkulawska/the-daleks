package model.collisions;

import model.entity.Entity;

import java.util.function.BiConsumer;

@FunctionalInterface
public interface CollisionHandler extends BiConsumer<Entity, Entity> {
}
