package com.test.main;

import com.test.main.entities.GameObject;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Random;

public class Tiles extends MouseAdapter implements MouseMotionListener{

    Random r = new Random();
    private final Game game;
    private final Handler handler;
    private GameObject tempObject;
    public int tx;
    public int ty;
    public int xUnit;
    public int yUnit;
    public static int size = 94;
    private boolean hover = false;
    private boolean selectable = false;

    public enum OBJECT {
        Player,
        Enemy,
        None
    }

    public OBJECT contains = OBJECT.None;

    public Tiles(Game game, Handler handler, int tx, int ty, int xUnit, int yUnit){
        this.game = game;
        this.handler = handler;
        this.tx = tx;
        this.ty = ty;
        this.xUnit = xUnit;
        this.yUnit = yUnit;
//        for(int i = 0; i < handler.object.size(); i++) {
//            tempObject = handler.object.get(i);
//            if(tempObject.getX() == (tx-31) && tempObject.getY() == (ty-31)) {
//                if(tempObject.getId().equals(ID.Player)) {
//                    contains = OBJECT.Player;
//                } else {
//                    contains = OBJECT.Enemy;
//                }
//                break;
//            }
//        }
    }

//    public void scanTile() {
//        for(int i = 0; i < handler.object.size(); i++) {
//            this.tempObject = handler.object.get(i);
//            if(tempObject.getX() == (tx-31) && tempObject.getY() == (ty-31)) {
//                if(tempObject.getId().equals(ID.Player)) {
//                    this.contains = OBJECT.Player;
//                } else {
//                    this.contains = OBJECT.Enemy;
//                }
//                break;
//            }
//            this.tempObject = null;
//        }
//    }

    @Override
    public void mousePressed(MouseEvent e) {
        int mx = e.getX();
        int my = e.getY();

        int xmax = this.tx + size;
        int ymax = this.ty + size;

        if(mouseOver(mx, my, this.tx, this.ty, 94, 94) && selectable){
            if(game.topBar.getCurrentAction().equals(TopBar.ACTION.MOVE) && contains.equals(OBJECT.None)) {
                game.movePlayer(this);
            } else if(game.topBar.getCurrentAction().equals(TopBar.ACTION.FIGHT) && contains.equals(OBJECT.Enemy)){
                game.destroyEnemy(this);
            }
        }

    }

    @Override
    public void mouseMoved(MouseEvent e){
        int mx = e.getX();
        int my = e.getY();

        int xmax = this.tx + size;
        int ymax = this.ty + size;

        if (mx > tx && mx < xmax) {
            hover = my > ty && my < ymax;
        }
        else {
            hover = false;
        }
    }

    private boolean mouseOver(int mx, int my, int x, int y, int width, int height) {
        if(game.gameState == Game.STATE.Game) {
            if (mx > x && mx < x + width) {
                return my > y && my < y + height;
            }
        }
        return false;
    }

    public void tick() {}

    public void render(Graphics g) {

        if(game.gameState == Game.STATE.Game) {
//            g.setColor(Color.WHITE);
//            g.drawRect(tx,ty,size,size);

           if(selectable) {
               g.setColor(Color.DARK_GRAY);
               g.fillRect(tx,ty,size,size);
           }
           if(hover) {
                g.setColor(Color.lightGray);
                g.fillRect(tx,ty,size,size);
           }
           if (game.topBar.getCurrentAction().equals(TopBar.ACTION.FIGHT)) {
               game.makeSelectable(2,true);
           } else {
               game.makeSelectable(2,game.topBar.getCurrentAction().equals(TopBar.ACTION.MOVE));
           }

            g.setColor(Color.WHITE);
            g.drawRect(tx,ty,size,size);
        }
    }

    public boolean isHover() {
        return hover;
    }

    public void setHover(boolean hover) {
        this.hover = hover;
    }

    public boolean isSelectable() {
        return selectable;
    }

    public void setSelectable(boolean selectable) {
        this.selectable = selectable;
    }

    public GameObject getTempObject() {
        return tempObject;
    }

    public void setTempObject(GameObject tempObject) {
        this.tempObject = tempObject;
        if(tempObject == null){
            contains = OBJECT.None;
        } else {
            tempObject.setX(tx+32);
            tempObject.setY(ty+32);
            if (tempObject.getId().equals(ID.Player)) {
                contains = OBJECT.Player;
            } else if (tempObject.getId().equals(ID.Enemy)) {
                contains = OBJECT.Enemy;
            }
            tempObject.setCurrentTile(this);
        }
    }
}
