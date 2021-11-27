package model;

import java.util.*;

public class Grid {

    public HashMap<Vector2d, ArrayList<Entity>> entities = new HashMap<>();
    public ArrayList<Entity> movableEntities = new ArrayList<>();
    private final int width;
    private final int height;

    public Grid(int width, int height){
        this.width=width;
        this.height=height;
    }

    public void placeFirstEntities() {
        //changes will be made here according to further instructions about the game
        //temporary
        Animal animal1 = new Animal(2,2);
        Animal animal2 = new Animal(2,2);
        Rock rock1 = new Rock(3,3);
        Rock rock2 = new Rock(5,5);
        place(animal1);
        place(animal2);
        place(rock1);
        place(rock2);
    }

    public void place (Entity entity){
        Vector2d v = entity.getPosition();
        ArrayList<Entity> list = entities.get(v);
        if (list==null){
            ArrayList<Entity> newList = new ArrayList<>();
            newList.add(entity);
            entities.put(v, newList);
        }
        else {
            list.add(entity);
        }

        if (entity instanceof Movable){
           movableEntities.add(entity);
        }
    }

    public void remove(Entity entity){
        Vector2d v = entity.getPosition();
        ArrayList<Entity> list = entities.get(v);
        if (list!=null && !list.isEmpty()){
            entities.remove(v);
            list.remove(entity);
            entities.put(v, list);
        }

        if (entity instanceof Movable){
            movableEntities.remove(entity);
        }
    }

    public void moveMovables(){
        ArrayList<Entity> tempList = movableEntities;
        for (Entity entity: tempList){
            //changes will be made here according to further instructions about the game
            Vector2d v = new Vector2d(1, 1);
            remove(entity);
            ((Movable)entity).move(v);
            place(entity);
        }
    }

    public int getWidth(){
        return this.width;
    }

    public int getHeight(){
        return this.height;
    }

}
