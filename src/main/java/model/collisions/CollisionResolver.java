package model.collisions;

import com.google.inject.Inject;
import javafx.util.Pair;
import model.Grid;
import model.entity.Dalek;
import model.entity.Doctor;
import model.entity.Entity;
import model.entity.PileOfCrap;
import utils.Vector2d;

import java.util.List;
import java.util.Map;

public class CollisionResolver {
    private final CollisionHandlersMap handlersMap;
    private final Grid grid;

    /**
     * Temporary attribute for CollisionResolver tests
     */
    private Map<Pair<Entity, Entity>, Boolean> collisionFlags;

    @Inject
    public CollisionResolver(Grid grid) {
        this.grid = grid;
        this.handlersMap = new CollisionHandlersMap();

        this.handlersMap.putHandler(new Pair<>(Dalek.class, Doctor.class),
                (dalek, doctor) -> collideDalekDoctor((Dalek) dalek, (Doctor) doctor));

        this.handlersMap.putHandler(new Pair<>(Doctor.class, PileOfCrap.class),
                (doctor, pileOfCrap) -> collideDoctorPileOfCrap((Doctor) doctor, (PileOfCrap) pileOfCrap));

        this.handlersMap.putHandler(new Pair<>(Dalek.class, Dalek.class),
                (dalek, otherDalek) -> collideDaleks((Dalek) dalek, (Dalek) otherDalek));

        this.handlersMap.putHandler(new Pair<>(Dalek.class, PileOfCrap.class),
                (dalek, pileOfCrap) -> collideDalekPileOfCrap((Dalek) dalek, (PileOfCrap) pileOfCrap));

        this.handlersMap.setDefaultHandler((e1, e2) ->
                System.out.println("Handler for " + e1.getClass().getName() + " and " + e2.getClass().getName() + " not found")
        );
    }

    public void resolve(List<Pair<Entity, Entity>> collisions) {
        for (Pair<Entity, Entity> collision : collisions) {
            handlersMap.getHandler(collision).run();
        }
    }

    public void collideDalekDoctor(Dalek dalek, Doctor doctor) {
        // Actual impact
        grid.getDoctor().kill();

        // For test purposes
        markCollisionAsSolved(dalek, doctor);
        System.out.println("Solved a dalek-doctor collision");
    }

    // Actually nothing happens in this case
    public void collideDoctorPileOfCrap(Doctor d, PileOfCrap p) {
        // For test purposes only - no actual impact
        markCollisionAsSolved(d, p);
        System.out.println("Solved a doctor-pileOfCrap collision");
    }

    public void collideDaleks(Dalek d1, Dalek d2) {
        // Actual impact:
        // Remove both daleks from the grid
        Vector2d mutualPosition = d1.getPosition();
        grid.getDaleksMap().remove(mutualPosition);
        // Place a pileOfCrap in situ
        grid.placePileOfCrap(new PileOfCrap(mutualPosition.x(), mutualPosition.y()));

        // For test purposes
        markCollisionAsSolved(d1, d2);
        System.out.println("Solved a dalek-dalek collision");
    }

    public void collideDalekPileOfCrap(Dalek d, PileOfCrap p) {
        // Actual impact:
        // Remove dalek from the grid and leave pileOfCrap on its place
        grid.getDaleksMap().remove(d.getPosition());

        // For test purposes
        markCollisionAsSolved(d, p);
        System.out.println("Solved a dalek-pileOfCrap collision");
    }

    public void collidePilesOfCrap(PileOfCrap p1, PileOfCrap p2) throws Exception {
        // This should not take place, unintended case - error
        markCollisionAsSolved(p1, p2);
        throw new Exception( "Detected a pilesOfCrap-pileOfCrap collision - this shouldn't actually happen!");
    }

    public void markCollisionAsSolved(Entity e1, Entity e2){
        if (collisionFlags != null) {
            Pair<Entity, Entity> collidedEntitiesPair = new Pair<>(e1, e2);
            collisionFlags.put(collidedEntitiesPair, true);
        }
    }

    public void setCollisionFlags(Map<Pair<Entity, Entity>, Boolean> collisionFlags) {
        this.collisionFlags = collisionFlags;
    }

    public Map<Pair<Entity, Entity>, Boolean> getCollisionFlags() {
        return collisionFlags;
    }
}
