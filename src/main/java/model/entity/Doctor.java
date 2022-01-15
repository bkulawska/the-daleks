package model.entity;

import model.Grid;
import utils.EntityVisitor;
import utils.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends Entity implements Movable {

    public boolean dead;

    private int numberOfTeleports = 0;
    private int numberOfTimeTurners = 0;

    private List<Teleport> teleports = new ArrayList<>();
    private List<TimeTurner> timeTurners = new ArrayList<>();

    public Doctor(int x, int y) {
        super(x, y);
        this.dead = false;
    }

    public void kill() {
        this.dead = true;
    }

    @Override
    public void move(Vector2d direction) {
        this.position = this.position.add(direction);
    }

    public void teleport(Vector2d teleportPosition) {
        this.position = teleportPosition;
    }

    @Override
    public void accept(EntityVisitor visitor) {
        visitor.visit(this);
    }

    public void addTeleport(Teleport teleport){
        teleports.add(teleport);
        this.numberOfTeleports++;
    }

    public void addTimeTurner(TimeTurner timeTurner){
        timeTurners.add(timeTurner);
        this.numberOfTimeTurners++;
    }

    public void useTeleport(Grid grid){
        if (numberOfTeleports>0){
            Teleport t = teleports.get(0);
            t.use(grid);
            teleports.remove(t);
            this.numberOfTeleports--;
        }
    }

    public void useTimeTurner(Grid grid){
        if (numberOfTimeTurners>0){
            TimeTurner t = timeTurners.get(0);
            t.use(grid);
            timeTurners.remove(t);
            this.numberOfTimeTurners--;
        }
    }

    public int getNumberOfTeleportsAvailable() {
        return this.numberOfTeleports;
    }

    public int getNumberOfTimeTurnersAvailable() {
        return this.numberOfTimeTurners;
    }

}
