package utils;

import model.entity.Dalek;
import model.entity.Doctor;
import model.entity.Entity;
import model.entity.PileOfCrap;

public interface EntityVisitor {
    void visitEntity(Entity entity);

    void visitDalek(Dalek dalek);

    void visitDoctor(Doctor doctor);

    void visitPileOfCrap(PileOfCrap pileOfCrap);
}
