import guice.GuiceModule;
import javafx.util.Pair;
import model.entity.Dalek;
import model.entity.Doctor;
import model.entity.Entity;
import model.entity.PileOfCrap;
import utils.Vector2d;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExampleEntities {
    private Map<Vector2d, List<Entity>> daleks;
    private Map<Vector2d, List<Entity>> pilesOfCrap;
    private Doctor doctor;
    private List<Pair<Entity, Entity>> expectedCollisions;

    public ExampleEntities() {
        this.daleks = new HashMap<>();
        this.pilesOfCrap = new HashMap<>();
        this.expectedCollisions = new ArrayList<>();
        createSample();
    }

    public void placeDalek(Dalek dalek){
        var key = dalek.getPosition();
        daleks.computeIfAbsent(key, value -> new ArrayList<>());
        daleks.get(key).add(dalek);
    }

    public void placePileOfCrap(PileOfCrap pileOfCrap){
        var key = pileOfCrap.getPosition();
        pilesOfCrap.computeIfAbsent(key, value -> new ArrayList<>());
        pilesOfCrap.get(key).add(pileOfCrap);
    }

    public Map<Vector2d, List<Entity>> getEntitiesMap() {
        Map<Vector2d, List<Entity>> entities = new HashMap<>();
        entities.putAll(daleks);
        entities.putAll(pilesOfCrap);
        var key = this.doctor.getPosition();
        entities.computeIfAbsent(key, value -> new ArrayList<>());
        entities.get(key).add(doctor);
        return entities;
    }

    public List<Entity> getEntitiesList() {
        Map<Vector2d, List<Entity>> entities = getEntitiesMap();
        return entities.values().stream().flatMap(List::stream).toList();
    }

    public void createSample(){
        int smallerDimension = Math.min(GuiceModule.provideGridWidth(),GuiceModule.provideGridHeight());
        this.doctor = new Doctor(10, 10);

        //create dalek-doctor collision
        Dalek d = new Dalek(10, 10);
        placeDalek(d);
        expectedCollisions.add(new Pair<>(d, doctor));

        for (int i = 0; i < smallerDimension; i++) {
            // Create dalek-dalek collisions
            if (i % 2 == 0 && i % 3 != 0 && i % 5 != 0) {
                Dalek d1 = new Dalek(i, i);
                placeDalek(d1);
                Dalek d2 = new Dalek(i, i);
                placeDalek(d2);
                expectedCollisions.add(new Pair<>(d1, d2));
            }

            // Create dalek-pileOfCrap collisions
            if (i % 2 != 0 && i % 3 == 0 && i % 5 != 0) {
                Dalek d1 = new Dalek(i, i);
                placeDalek(d1);
                PileOfCrap p1 = new PileOfCrap(i, i);
                placePileOfCrap(p1);
                expectedCollisions.add(new Pair<>(d1, p1));
            }

            // Create pileOfCrap-pileOfCrap collisions
//            if (i % 2 != 0 && i % 3 != 0 && i % 5 == 0) {
//                PileOfCrap p1 = new PileOfCrap(i, i);
//                placePileOfCrap(p1);
//                PileOfCrap p2 = new PileOfCrap(i, i);
//                placePileOfCrap(p2);
//                expectedCollisions.add(new Pair<>(p1, p2));
//            }
        }
    }

    public Map<Vector2d, List<Entity>> getDaleks() { return daleks; }

    public void setDaleks(Map<Vector2d, List<Entity>> daleks) { this.daleks = daleks; }

    public Map<Vector2d, List<Entity>> getPilesOfCrap() { return pilesOfCrap; }

    public void setPilesOfCrap(Map<Vector2d, List<Entity>> pilesOfCrap) { this.pilesOfCrap = pilesOfCrap; }

    public Doctor getDoctor() { return doctor; }

    public void setDoctor(Doctor doctor) { this.doctor = doctor; }

    public List<Pair<Entity, Entity>> getExpectedCollisions() {
        return expectedCollisions;
    }

    public void setExpectedCollisions(List<Pair<Entity, Entity>> expectedCollisions) {
        this.expectedCollisions = expectedCollisions;
    }
}