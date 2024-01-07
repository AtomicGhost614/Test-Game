package com.test.main.entities;

import com.test.main.Handler;
import com.test.main.ID;

import java.awt.*;

public class PowerUp extends GameObject{

    public PowerUp(ID id, Handler handler) {
        super(0, 0, id);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillOval((int) x, (int) y, 32,32);
    }

    @Override
    public Rectangle getBounds() {
        return null;
    }
}
