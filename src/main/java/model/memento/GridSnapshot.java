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

    public GridSnapshot(Map<Vector2d, List<Dalek>> daleks, Map<Vector2d, PileOfCrap> pilesOfCrap, Doctor doctor){
        this.daleks = deepCopyDaleks(daleks);
        this.pilesOfCrap = deepCopyPilesOfCrap(pilesOfCrap);
        this.doctor = deepCopyDoctor(doctor);
    }

    public Doctor deepCopyDoctor(Doctor doctor) {
        try {
            return (Doctor)doctor.clone();
        } catch(CloneNotSupportedException cloneNotSupportedException) {
            Doctor clone = new Doctor(doctor.getPosition().x(), doctor.getPosition().y());
            clone.setOwnedTeleports(doctor.getOwnedTeleports());
            clone.setOwnedTimeTurners(doctor.getOwnedTimeTurners());
            return clone;
        }
    }

    public Map<Vector2d, List<Dalek>> deepCopyDaleks(Map<Vector2d, List<Dalek>> daleksToCopy) {
        return daleksToCopy.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    List<Dalek> clones = new ArrayList<>();
                    for(Dalek d : e.getValue()) {
                        try {
                            clones.add((Dalek) d.clone());
                        } catch (CloneNotSupportedException cloneNotSupportedException) {
                            clones.add(new Dalek(d.getPosition().x(), d.getPosition().y()));
                        }
                    }
                    return clones;
                }));
    }

    public Map<Vector2d, PileOfCrap> deepCopyPilesOfCrap(Map<Vector2d, PileOfCrap> pilesOfCrapToCopy) {
        return pilesOfCrapToCopy.entrySet().stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e -> {
                    try {
                        return (PileOfCrap) e.getValue().clone();
                    } catch (CloneNotSupportedException cloneNotSupportedException) {
                        return new PileOfCrap(e.getKey().x(), e.getKey().y());
                    }

                }));
    }

    public Map<Vector2d, List<Dalek>> getDaleks() { return daleks; }

    public Map<Vector2d, PileOfCrap> getPilesOfCrap() { return pilesOfCrap; }

    public Doctor getDoctor() { return doctor; }
}
