package com.test.main;

import com.test.main.entities.GameObject;
import com.test.main.entities.Player;

import java.awt.*;
import java.util.LinkedList;

public class Handler {

    public LinkedList<GameObject> object = new LinkedList<>();
    public boolean enemiesExist;
    public boolean playerExists;

    public void tick(){
        enemiesExist = false;
        playerExists = false;
        for (GameObject tempObject : object) {
            tempObject.tick();

            if (tempObject.getId().equals(ID.Enemy)) {
                enemiesExist = true;
            } else if (tempObject.getId().equals(ID.Player)) {
                playerExists = true;
            }
        }
    }

    public void render(Graphics g){
        for (GameObject tempObject : object) {
            tempObject.render(g);
        }
    }

    public void clearEnemys(){
        for (GameObject tempObject : object) {
            if (tempObject.getId().equals(ID.Enemy)) {
                Tiles tile = tempObject.getCurrentTile();
                tile.contains = Tiles.OBJECT.None;
            }
        }
        object.clear();
    }

    public void addObject(GameObject object){
        this.object.add(object);
    }

    public void removeObject(GameObject object){
        this.object.remove(object);
    }
}
