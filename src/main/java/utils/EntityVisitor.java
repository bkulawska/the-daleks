package utils;

import model.entity.*;

public interface EntityVisitor {
    void visit(Entity entity);

    void visit(Dalek dalek);

    void visit(Doctor doctor);

    void visit(PileOfCrap pileOfCrap);

    void visit(Teleport teleport);

    void visit(TimeTurner timeTurner);

}
