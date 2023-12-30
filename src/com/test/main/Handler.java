package com.test.main;

import java.awt.*;
import java.util.LinkedList;

public class Handler {

    public LinkedList<GameObject> object = new LinkedList<>();
    public boolean enemiesExist;

    public void tick(){
        enemiesExist = false;
        for (GameObject tempObject : object) {
            tempObject.tick();

            if (tempObject.getId().equals(ID.Enemy)) {
                enemiesExist = true;
            }
        }
    }

    public void render(Graphics g){
        for (GameObject tempObject : object) {
            tempObject.render(g);
        }
    }

    public void clearEnemys(){
        for(int i = 0; i < object.size(); i++){
            GameObject tempObject = object.get(i);
            if(tempObject.getId() == ID.Player){
                object.clear();
                addObject(new Player((int) tempObject.getX(), (int) tempObject.getY(), ID.Player, this));
            }
        }
    }

    public void addObject(GameObject object){
        this.object.add(object);
    }

    public void removeObject(GameObject object){
        this.object.remove(object);
    }
}
