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
    private final Map<Vector2d, List<Dalek>> daleks;
    private final Map<Vector2d, PileOfCrap> pilesOfCrap;
    private Doctor doctor;
    private final List<Pair<Entity, Entity>> expectedCollisions;

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
        pilesOfCrap.putIfAbsent(key, pileOfCrap);
    }

    public Map<Vector2d, List<Entity>> getEntitiesMap() {
        Map<Vector2d, List<Entity>> entities = new HashMap<>();

        daleks.keySet().forEach(position -> entities.put(position, new ArrayList<>()));
        pilesOfCrap.keySet().forEach(position -> entities.put(position, new ArrayList<>()));

        for (var daleksEntry : daleks.entrySet()) {
            entities.get(daleksEntry.getKey()).addAll(daleksEntry.getValue());
        }
        for (var pilesOfCrapEntry : pilesOfCrap.entrySet()) {
            entities.get(pilesOfCrapEntry.getKey()).add(pilesOfCrapEntry.getValue());
        }

        var key = this.doctor.getPosition();
        entities.computeIfAbsent(key, value -> new ArrayList<>());
        entities.get(key).add(doctor);
        return entities;
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

        }
    }

    public Map<Vector2d, List<Dalek>> getDaleks() { return daleks; }

    public Map<Vector2d, PileOfCrap> getPilesOfCrap() { return pilesOfCrap; }

    public Doctor getDoctor() { return doctor; }

    public List<Pair<Entity, Entity>> getExpectedCollisions() {
        return expectedCollisions;
    }
}