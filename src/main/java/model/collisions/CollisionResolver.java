package model.collisions;

import com.google.inject.Inject;
import javafx.util.Pair;
import model.Grid;
import model.entity.*;
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

        this.handlersMap.putHandler(new Pair<>(Dalek.class, Dalek.class),
                (dalek, otherDalek) -> collideDaleks((Dalek) dalek, (Dalek) otherDalek));

        this.handlersMap.putHandler(new Pair<>(Dalek.class, PileOfCrap.class),
                (dalek, pileOfCrap) -> collideDalekPileOfCrap((Dalek) dalek, (PileOfCrap) pileOfCrap));

        this.handlersMap.putHandler(new Pair<>(Doctor.class, Teleport.class),
                (doctor, teleport) -> collideDoctorTeleport((Doctor) doctor, (Teleport) teleport));

        this.handlersMap.putHandler(new Pair<>(Doctor.class, TimeTurner.class),
                (doctor, timeTurner) -> collideDoctorTimeTurner((Doctor) doctor, (TimeTurner) timeTurner));

        this.handlersMap.setDefaultHandler((e1, e2) ->
                System.out.println("Handler for " + e1.getClass().getName() + " and " + e2.getClass().getName() + " not found")
        );
    }

    public void resolve(List<Pair<Entity, Entity>> collisions) {
        for (Pair<Entity, Entity> collision : collisions) {
            handlersMap.getHandler(collision).run();
            markCollisionAsSolved(collision.getKey(), collision.getValue());
        }
    }

    public void collideDalekDoctor(Dalek dalek, Doctor doctor) {
        // Actual impact
        grid.getDoctor().kill();
        System.out.println("Solved a dalek-doctor collision");
    }

    public void collideDaleks(Dalek d1, Dalek d2) {
        // Actual impact:
        // Remove both daleks from the grid
        Vector2d mutualPosition = d1.getPosition();
        grid.getDaleksMap().remove(mutualPosition);
        // Place a pileOfCrap in situ
        grid.placePileOfCrap(new PileOfCrap(mutualPosition.x(), mutualPosition.y()));

        // For test purposes
        System.out.println("Solved a dalek-dalek collision");
    }

    public void collideDalekPileOfCrap(Dalek d, PileOfCrap p) {
        // Actual impact:
        // Remove dalek from the grid and leave pileOfCrap on its place
        grid.getDaleksMap().remove(d.getPosition());

        // For test purposes
        System.out.println("Solved a dalek-pileOfCrap collision");
    }

    public void collideDoctorTeleport(Doctor d, Teleport t) {
        // Actual impact:
        // Remove teleport from the grid
        grid.getPowerUpsMap().remove(t.getPosition());

        //Add teleport to Doctor
        d.addTeleport(t);

        // For test purposes
        System.out.println("Solved a doctor-teleport collision");
    }

    public void collideDoctorTimeTurner(Doctor d, TimeTurner t) {
        // Actual impact:
        // Remove time turner from the grid
        grid.getPowerUpsMap().remove(t.getPosition());

        //Add time turner to Doctor
        d.addTimeTurner(t);

        // For test purposes
        System.out.println("Solved a doctor-timeTurner collision");
    }

    // Actually nothing happens in this case
    public void collideDoctorPileOfCrap(Doctor d, PileOfCrap p) {
        // For test purposes only - no actual impact
        System.out.println("Solved a doctor-pileOfCrap collision");
    }

    // Actually nothing happens in this case
    public void collideTeleportPileOfCrap(Teleport t, PileOfCrap p) {
        // For test purposes only - no actual impact
        System.out.println("Solved a teleport-pileOfCrap collision");
    }

    // Actually nothing happens in this case
    public void collideTimeTurnerPileOfCrap(TimeTurner t, PileOfCrap p) {
        // For test purposes only - no actual impact
        System.out.println("Solved a timeTurner-pileOfCrap collision");
    }

    // Actually nothing happens in this case
    public void collideDalekTeleport(Dalek d, Teleport t) {
        // For test purposes only - no actual impact
        System.out.println("Solved a dalek-teleport collision");
    }

    // Actually nothing happens in this case
    public void collideDalekTimeTurner(Dalek d, TimeTurner t) {
        // For test purposes only - no actual impact
        System.out.println("Solved a dalek-timeTurner collision");
    }

    public void collideTeleports(Teleport t1, Teleport t2) throws Exception {
        // This should not take place, unintended case - error
        throw new Exception( "Detected a teleport-teleport collision - this shouldn't actually happen!");
    }

    public void collideTimeTurners(TimeTurner t1, TimeTurner t2) throws Exception {
        // This should not take place, unintended case - error
        throw new Exception( "Detected a timeTurner-timeTurner collision - this shouldn't actually happen!");
    }

    public void collidePilesOfCrap(PileOfCrap p1, PileOfCrap p2) throws Exception {
        // This should not take place, unintended case - error
        throw new Exception( "Detected a pileOfCrap-pileOfCrap collision - this shouldn't actually happen!");
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
