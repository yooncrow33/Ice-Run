package system;

import view.IPlayerCoordinate;

import javax.swing.*;
import java.awt.*;

public class GraphicsManager {
    public final int WORLD_WIDTH = 19200;
    public final int WORLD_HEIGHT = 10800;

    final int VIRTUAL_X_SCREEN_CENTER = 960;
    final int VIRTUAL_Y_SCREEN_CENTER = 540;

    final double WORLD_HALF_WIDTH = WORLD_WIDTH / 2.0;
    final double MIN_X = -WORLD_HALF_WIDTH;
    final double MAN_X = WORLD_HALF_WIDTH;

    final double WORLD_HALF_HEIGHT = WORLD_HEIGHT / 2.0;
    final double MIN_Y = -WORLD_HALF_HEIGHT;
    final double MAX_Y = WORLD_HALF_HEIGHT;

    IPlayerCoordinate iPlayerCoordinate;

    public GraphicsManager(IPlayerCoordinate iPlayerCoordinate) {
        this.iPlayerCoordinate = iPlayerCoordinate;
    }

    public void renderMap(Graphics g) {
        final int MAP_SCALE = 80;

        final int MINIMAP_X_START = 10;
        final int MINIMAP_Y_START = 10;
        final int MINIMAP_WIDTH = WORLD_WIDTH / MAP_SCALE;
        final int MINIMAP_HEIGHT = WORLD_HEIGHT / MAP_SCALE;

        // 미니맵 배경
        g.setColor(Color.black);
        g.fillRect(MINIMAP_X_START - 5, MINIMAP_Y_START - 5, MINIMAP_WIDTH + 10, MINIMAP_HEIGHT + 10);
        g.setColor(Color.white);
        g.fillRect(MINIMAP_X_START, MINIMAP_Y_START, MINIMAP_WIDTH, MINIMAP_HEIGHT);

        // 플레이어 좌표 계산
        // 1. 월드 좌표를 미니맵 크기로 변환
        double scaledX = iPlayerCoordinate.getPlayerX() / MAP_SCALE;
        double scaledY = iPlayerCoordinate.getPlayerY() / MAP_SCALE;

        // 2. 미니맵의 중앙을 기준으로 현재 위치를 계산 (중앙 오프셋)
        int mapCenterX = MINIMAP_WIDTH / 2;
        int mapCenterY = MINIMAP_HEIGHT / 2;
        int playerMapX = MINIMAP_X_START + mapCenterX + (int)scaledX;
        int playerMapY = MINIMAP_Y_START + mapCenterY + (int)scaledY;

        // 플레이어 표시
        g.setColor(Color.red);
        g.fillRect(playerMapX - 3, playerMapY - 3, 6, 6);
    }
}
