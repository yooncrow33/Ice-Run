package model;

import java.awt.*;

public class Bomb {
    double x;
    double y;
    public int size = 60;
    int alpha = 255;
    int alphaDegree = 8;

    boolean damage = false;

    final int VIRTUAL_X_SCREEN_CENTER = 960;
    final int VIRTUAL_Y_SCREEN_CENTER = 540;

    public Bomb(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public void update() {
        alpha -= alphaDegree;
        if (alpha < 255) {
            damage = true;
        }
    }

    public void render(Graphics g, double playerX, double playerY) {
        double relativeX = x - playerX + VIRTUAL_X_SCREEN_CENTER - (double) size /2;
        double relativeY = y - playerY + VIRTUAL_Y_SCREEN_CENTER - (double) size /2;

        g.setColor(new Color(255, 0, 0, alpha));
        g.fillRect((int)relativeX, (int)relativeY, size, size);
    }

    public boolean isExpired() {
        return alpha <= 0;
    }

    public boolean isDamage() { return damage; }
    public double getX() {return x;}
    public double getY() {return y;}
    public double getSize() {return size;}
}
