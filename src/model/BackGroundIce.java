package model;

import java.awt.*;
import java.util.Random;

public class BackGroundIce {

    final double x;
    final double y;

    final double cullingDistanceWidth = 2000;
    final double cullingDistanceHeight = 1000;

    final int VIRTUAL_X_SCREEN_CENTER = 960;
    final int VIRTUAL_Y_SCREEN_CENTER = 540;

    final int iceWidth = 900;
    final int iceHeight = 900;

    Random random = new Random();

    public BackGroundIce() {
        x = random.nextDouble(19201) - (double)19200 /2;
        y = random.nextDouble(10801) - (double) 10800/2;
    }

    public void renderIce(Graphics g, double playerX, double playerY) {
        double relativeX = x - playerX + VIRTUAL_X_SCREEN_CENTER;
        double relativeY = y - playerY + VIRTUAL_Y_SCREEN_CENTER;

        //g.setColor(new Color(135, 206, 255));
        g.setColor(new Color(245,245,245));

        g.fillRect((int)relativeX - iceWidth/2, (int)relativeY - iceHeight/2, iceWidth,iceHeight );
    }

    public boolean isVisibleToPlayer(double playerX, double playerY) {
        double relativeX = x - playerX;
        double relativeY = y - playerY;

        if (cullingDistanceWidth > relativeX) {
            if (-1.0 * cullingDistanceWidth < relativeX) {
                if (cullingDistanceHeight > relativeY) {
                    if (-1.0 * cullingDistanceHeight < relativeY) {
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    return false;
                }
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
