package model.collisions;

import javafx.util.Pair;
import model.entity.Entity;

import java.util.*;
import java.util.function.BiConsumer;


public class CollisionHandlersMap {
    static class EntitiesComparator implements Comparator<Class<? extends Entity>> {
        @Override
        public int compare(Class<? extends Entity> c1, Class<? extends Entity> c2) {
            return c1.getName().compareTo(c2.getName());
        }
    }

    private final Map<SortedSet<Class<? extends Entity>>, BiConsumer<Entity, Entity>> handlersMap = new HashMap<>();

    private BiConsumer<Entity, Entity> defaultHandler = (e1, e2) -> {
    };

    public void setDefaultHandler(BiConsumer<Entity, Entity> defaultHandler) {
        this.defaultHandler = defaultHandler;
    }

    public void putHandler(Pair<Class<? extends Entity>, Class<? extends Entity>> entities, BiConsumer<Entity, Entity> handler) {
        var entitiesSet = new TreeSet<>(new EntitiesComparator());
        entitiesSet.add(entities.getKey());
        entitiesSet.add(entities.getValue());

        handlersMap.put(entitiesSet, handler);
    }

    public Runnable getHandler(Pair<Entity, Entity> entities) {
        var entitiesList = Arrays.asList(entities.getKey(), entities.getValue());
        entitiesList.sort((e1, e2) -> new EntitiesComparator().compare(e1.getClass(), e2.getClass()));

        var entitiesSet = new TreeSet<>(new EntitiesComparator());
        entitiesSet.addAll(entitiesList.stream().map(Entity::getClass).toList());

        var handler = handlersMap.getOrDefault(entitiesSet, this.defaultHandler);

        return () -> handler.accept(entitiesList.get(0), entitiesList.get(1));
    }
}
