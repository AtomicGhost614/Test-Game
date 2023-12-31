package com.test.main.entities;

import com.test.main.ID;
import com.test.main.Tiles;

import java.awt.*;

public abstract class GameObject {

    protected float x, y;
    protected ID id;
    protected float velX, velY;
    protected boolean selected = false;
    private Tiles currentTile;
    private int moveRange;

    public GameObject() {}

    public GameObject(float x, float y, ID id){
        this.x = x;
        this.y = y;
        this.id = id;
    }

    public abstract void tick();
    public abstract void render(Graphics g);
    public abstract Rectangle getBounds();

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setId(ID id) {
        this.id = id;
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public ID getId() {
        return id;
    }

    public float getVelX() {
        return velX;
    }

    public float getVelY() {
        return velY;
    }

    public Tiles getCurrentTile() {
        return currentTile;
    }

    public void setCurrentTile(Tiles currentTile) {
        this.currentTile = currentTile;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public int getMoveRange() {
        return moveRange;
    }

    public void setMoveRange(int moveRange) {
        this.moveRange = moveRange;
    }
}
