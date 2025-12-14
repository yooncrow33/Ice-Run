package model;// model.Orbit.java 수정
import view.IMouse;

import java.awt.*;
import java.awt.geom.AffineTransform;

public class Orbit {

    private final IMouse iMouse;

    final int VIRTUAL_X_SCREEN_CENTER = 960;
    final int VIRTUAL_Y_SCREEN_CENTER = 540;

    // 1. 조준선 오브젝트 자체의 길이와 폭 (선처럼 보이기 위한 직사각형)
    final int AIM_HEIGHT = 10; // 선의 두께

    public Orbit(IMouse iMouse) {
        this.iMouse = iMouse;
    }

    public double getOrbitAngle() {
        // Y 변화량 / X 변화량
        return Math.atan2(iMouse.getVirtualMouseY() - VIRTUAL_Y_SCREEN_CENTER,
                iMouse.getVirtualMouseX() - VIRTUAL_X_SCREEN_CENTER);
    }

    public double getDistanceToMouse() {
        double mouseX = iMouse.getVirtualMouseX();
        double mouseY = iMouse.getVirtualMouseY();

        // 피타고라스 정리 (a² + b² = c²)를 사용
        // 거리 = sqrt( (Δx)² + (Δy)² )
        double deltaX = mouseX - VIRTUAL_X_SCREEN_CENTER;
        double deltaY = mouseY - VIRTUAL_Y_SCREEN_CENTER;

        return Math.sqrt(deltaX * deltaX + deltaY * deltaY);
    }

    public void draw(Graphics g, int playerWidth, double COLOR_CHANGE_DISTANCE) {
        double distance = getDistanceToMouse(); // 마우스와의 실제 거리
        double angle = getOrbitAngle();

        Graphics2D d2 = (Graphics2D) g;
        AffineTransform oldTransform = d2.getTransform();

        // 1. 각도 위치 설정.
        d2.translate(VIRTUAL_X_SCREEN_CENTER, VIRTUAL_Y_SCREEN_CENTER);
        d2.rotate(angle);

        // 2. 조준선 그리기
        final int START_OFFSET = playerWidth / 2; // 플레이어 중심에서 조준선이 시작되는 위치
        double normalLength = Math.min(distance, COLOR_CHANGE_DISTANCE) - START_OFFSET;

        d2.setColor(new Color(128,128,128,128));

        if (normalLength > 0) {
            // X 시작점: 플레이어 몸체 끝 (START_OFFSET)
            // Y 오프셋: 중앙 정렬 (-AIM_HEIGHT / 2)
            d2.fillRect(START_OFFSET, -AIM_HEIGHT / 2, (int)normalLength, AIM_HEIGHT);
        }

        if (distance > COLOR_CHANGE_DISTANCE) {
            // B. 빨간색 선의 길이 계산
            double redLength = distance - COLOR_CHANGE_DISTANCE;

            // C. 빨간색 선의 시작 위치 계산
            // X 시작점: (COLOR_CHANGE_DISTANCE) 지점
            int redStartX = (int)COLOR_CHANGE_DISTANCE;

            // 빨간색 설정
            d2.setColor(new Color(255,0,0,128));
            d2.fillRect(redStartX, -AIM_HEIGHT / 2, (int)redLength, AIM_HEIGHT);
        }

        d2.setTransform(oldTransform);
    }
}