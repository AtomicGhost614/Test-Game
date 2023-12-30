package com.test.main;

import java.awt.*;

public class BasicEnemy extends GameObject{

    private Handler handler;

    public BasicEnemy(ID id, Handler handler) {
        super(0, 0, id);
        this.handler = handler;
        this.setMoveRange(2);

        //velX = 5;
        //velY = 5;
    }

    public BasicEnemy(int x, int y, ID id, Handler handler) {
        super(x, y, id);
        this.handler = handler;

        //velX = 5;
        //velY = 5;
    }

    public Rectangle getBounds(){
        return new Rectangle((int) x, (int) y, 32, 32);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

        //Graphics2D g2d = (Graphics2D) g;

        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y, 32, 32);


    }
}
