package model;

public abstract class Entity {

    public Vector2d position;

    public Entity(int x, int y){
        this.position = new Vector2d(x, y);
    }

    public Vector2d getPosition(){
        return position;
    }

}
