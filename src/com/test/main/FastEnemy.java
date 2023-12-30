package com.test.main;

import java.awt.*;

public class FastEnemy extends GameObject{

    private Handler handler;

    public FastEnemy(ID id, Handler handler) {
        super(0, 0, id);
        this.handler = handler;
        this.setMoveRange(4);
    }

    public Rectangle getBounds(){
        return new Rectangle((int) x, (int) y, 32, 32);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect((int) x, (int) y, 32, 32);
    }
}
