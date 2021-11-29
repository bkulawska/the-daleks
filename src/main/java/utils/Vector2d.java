package utils;

public class Vector2d {

    public int x;
    public int y;

    public Vector2d(int x, int y){
        this.x = x;
        this.y = y;
    }

    @Override
    public int hashCode() {
        int hash = 13;
        hash += this.x * 31;
        hash += this.y * 17;
        return hash;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Vector2d obj)){
            return false;
        }
        return (obj.x==this.x && obj.y == this.y);
    }

    public boolean precedes(Vector2d other){
        if (this.x<=other.x){
            return this.y <= other.y;
        }
        return false;
    }

    public boolean follows(Vector2d other){
        if (this.x>=other.x){
            return this.y >= other.y;
        }
        return false;
    }

    public void add(Vector2d other){
        this.x=this.x+other.x;
        this.y=this.y+other.y;
    }

}
