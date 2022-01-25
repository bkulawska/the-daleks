package model.memento;

import model.entity.Dalek;
import model.entity.Doctor;
import model.entity.PileOfCrap;
import utils.Vector2d;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GridSnapshot {
    private final Map<Vector2d, List<Dalek>> daleks;
    private final Map<Vector2d, PileOfCrap> pilesOfCrap;
    private final Doctor doctor;

    public GridSnapshot(Map<Vector2d, List<Dalek>> daleks, Map<Vector2d, PileOfCrap> pilesOfCrap, Doctor doctor) {
        this.daleks = deepCopyDaleks(daleks);
        this.pilesOfCrap = deepCopyPilesOfCrap(pilesOfCrap);
        this.doctor = doctor.copy();
    }


    public Map<Vector2d, List<Dalek>> deepCopyDaleks(Map<Vector2d, List<Dalek>> daleksToCopy) {
        return daleksToCopy
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    List<Dalek> clones = new ArrayList<>();
                    for (Dalek dalek : e.getValue()) {
                        clones.add(dalek.copy());
                    }
                    return clones;
                }));
    }

    public Map<Vector2d, PileOfCrap> deepCopyPilesOfCrap(Map<Vector2d, PileOfCrap> pilesOfCrapToCopy) {
        return pilesOfCrapToCopy
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().copy()));
    }

    public Map<Vector2d, List<Dalek>> getDaleks() {
        return daleks;
    }

    public Map<Vector2d, PileOfCrap> getPilesOfCrap() {
        return pilesOfCrap;
    }

    public Doctor getDoctor() {
        return doctor;
    }
}
