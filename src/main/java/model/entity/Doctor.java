package model.entity;

import model.Grid;
import utils.EntityVisitor;
import utils.Vector2d;

import java.util.ArrayList;
import java.util.List;

public class Doctor extends Entity implements Movable {

    public boolean dead;

    private List<Teleport> ownedTeleports = new ArrayList<>();
    private List<TimeTurner> ownedTimeTurners = new ArrayList<>();

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
        ownedTeleports.add(teleport);
    }

    public void addTimeTurner(TimeTurner timeTurner){
        ownedTimeTurners.add(timeTurner);
    }

    public void useTeleport(Grid grid){
        if (ownedTeleports.size() > 0){
            var teleport = ownedTeleports.get(0);
            teleport.use(grid);
            ownedTeleports.remove(teleport);
        }
    }

    public void useTimeTurner(Grid grid){
        if (ownedTimeTurners.size() > 0){
            var timeTurner = ownedTimeTurners.get(0);
            timeTurner.use(grid);
            ownedTimeTurners.remove(timeTurner);
        }
    }

    public void setOwnedTeleports(List<Teleport> ownedTeleports) { this.ownedTeleports = ownedTeleports; }

    public void setOwnedTimeTurners(List<TimeTurner> ownedTimeTurners) { this.ownedTimeTurners = ownedTimeTurners; }

    public List<Teleport> getOwnedTeleports() { return ownedTeleports; }

    public List<TimeTurner> getOwnedTimeTurners() { return ownedTimeTurners; }

    public Doctor copy() {
        var newDoctor =  new Doctor(this.position.x(), this.position.y());
        newDoctor.setOwnedTeleports(this.ownedTeleports);
        newDoctor.setOwnedTimeTurners(this.ownedTimeTurners);
        return newDoctor;
    }
}
