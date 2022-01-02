package utils;

import model.entity.Dalek;
import model.entity.Doctor;
import model.entity.Entity;
import model.entity.PileOfCrap;

public interface EntityVisitor {
    void visit(Entity entity);

    void visit(Dalek dalek);

    void visit(Doctor doctor);

    void visit(PileOfCrap pileOfCrap);
}
