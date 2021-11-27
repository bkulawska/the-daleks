package model;

public class Animal extends Entity implements Movable {

    public Animal(int x, int y) {
        super(x, y);
    }

    public void move(Vector2d direction){
        this.position.add(direction);
    }

}
