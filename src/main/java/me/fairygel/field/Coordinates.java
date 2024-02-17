package me.fairygel.field;

public class Coordinates {
    private int x;
    private int y;
    private int direction;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Coordinates(int x, int y, char direction) {
        this.x = x - 'a';
        this.y = y;
        this.direction = direction == 'v' ? 1 : 0;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getDirection() {
        return direction;
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }
}
