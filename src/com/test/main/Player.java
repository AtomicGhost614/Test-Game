package com.test.main;

import java.awt.*;
import java.util.Random;

public class Player extends GameObject{

    Random r = new Random();
//    private Tiles currentTile;
    private Handler handler;

    public Player() {}

    public Player(ID id, Handler handler) {
        super(0, 0, id);
        this.handler = handler;
        this.setMoveRange(2);
    }

    public Player(int x, int y, ID id, Handler handler) {
        super(x, y, id);
        this.handler = handler;
    }

    public Rectangle getBounds(){
        return new Rectangle((int) x, (int) y, 32, 32);
    }

    @Override
    public void tick() {
        x += velX;
        y += velY;
    }

    @Override
    public void render(Graphics g) {
        if (this.selected) {
            g.setColor(Color.CYAN);
        } else {
            g.setColor(Color.BLUE);
        }
        g.fillRect((int) x, (int) y, 32, 32);
    }

    public Handler getHandler() {
        return handler;
    }

    public void setHandler(Handler handler) {
        this.handler = handler;
    }
}
