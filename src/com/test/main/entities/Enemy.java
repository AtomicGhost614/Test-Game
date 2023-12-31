package com.test.main.entities;

import com.test.main.*;

import java.awt.*;
import java.util.Random;
import java.util.Set;

public class Enemy extends GameObject {

    public Enemy(ID id) {
        super(0, 0, id);
    }

    @Override
    public void tick() {

    }

    @Override
    public void render(Graphics g) {

    }

    @Override
    public Rectangle getBounds() {
        return null;
    }
}
