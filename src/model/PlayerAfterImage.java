package model;

import java.awt.*;

public class PlayerAfterImage {
    double x;
    double y;
    int playerWidth;
    int playerHeight;
    int alpha = 128;
    int alphaDegree = 8;

    final int VIRTUAL_X_SCREEN_CENTER = 960;
    final int VIRTUAL_Y_SCREEN_CENTER = 540;

    public PlayerAfterImage(double playerX, double playerY, int playerWidth, int playerHeight) {
        this.x = playerX;
        this.y = playerY;

        this.playerWidth = playerWidth;
        this.playerHeight = playerHeight;
    }

    public void update() {
        alpha -= alphaDegree;
    }

    public void draw(Graphics g, double playerX, double playerY) {
        double relativeX = x - playerX + VIRTUAL_X_SCREEN_CENTER - (double) playerWidth /2;
        double relativeY = y - playerY + VIRTUAL_Y_SCREEN_CENTER - (double) playerHeight /2;

        g.setColor(new Color(0, 0, 0, alpha));
        g.fillOval((int)relativeX, (int)relativeY, playerWidth, playerHeight);
    }

    public boolean isExpired() {
        return alpha <= 0;
    }
}
