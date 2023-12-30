package com.test.main.entities;

import com.test.main.Handler;
import com.test.main.ID;
import com.test.main.entities.GameObject;

import java.awt.*;

public class BasicEnemy extends GameObject {

    public BasicEnemy(ID id, Handler handler) {
        super(0, 0, id);
        this.setMoveRange(2);
    }

    public Rectangle getBounds(){
        return new Rectangle((int) x, (int) y, 32, 32);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.ORANGE);
        g.fillRect((int) x, (int) y, 32, 32);
    }
}
