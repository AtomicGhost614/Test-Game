package com.test.main.entities;

import com.test.main.Handler;
import com.test.main.ID;
import com.test.main.entities.GameObject;

import java.awt.*;

public class DeadlyEnemy extends GameObject {

    public DeadlyEnemy(ID id, Handler handler) {
        super(0, 0, id);
        this.setMoveRange(1);
    }

    public Rectangle getBounds(){
        return new Rectangle((int) x, (int) y, 32, 32);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect((int) x, (int) y, 32, 32);
    }
}
