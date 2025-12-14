package model;

import java.awt.*;

public class Bullet {
    // 상태 변수
    private double x;
    private double y;
    private final double angle;
    private double traveledDistance = 0; // 총 이동 거리
    private final double distance; // 사정거리 (Orbit에서 받은 COLOR_CHANGE_DISTANCE)
    private final double speed = 15; // 픽셀/프레임 (예시)

    // 렌더링 상수
    final int SIZE = 20;
    final int VIRTUAL_X_SCREEN_CENTER = 960;
    final int VIRTUAL_Y_SCREEN_CENTER = 540;


    public Bullet(double startX, double startY, double angle, double distance) {
        this.x = startX;
        this.y = startY;
        this.angle = angle;
        this.distance = distance;
    }

    // 1. 상태 업데이트 로직 (핵심)
    public void update(double dt) {
        // 시간에 따른 이동 거리 계산
        double distanceThisFrame = speed * dt; // dt는 Main.update의 시간 스케일 팩터

        // 삼각함수를 이용한 X, Y 이동량 계산
        double deltaX = Math.cos(angle) * distanceThisFrame;
        double deltaY = Math.sin(angle) * distanceThisFrame;

        x += deltaX;
        y += deltaY;

        traveledDistance += distanceThisFrame; // 총 이동 거리 갱신
    }

    // 2. 렌더링 로직 (Ice와 동일)
    public void render(Graphics g, double playerX, double playerY) {
        // 카메라 변환 (상대 좌표계 계산)
        double relativeX = x - playerX + VIRTUAL_X_SCREEN_CENTER;
        double relativeY = y - playerY + VIRTUAL_Y_SCREEN_CENTER;

        g.setColor(Color.blue); // 총알은 빨간색
        g.fillOval((int)relativeX - SIZE / 2, (int)relativeY - SIZE / 2, SIZE, SIZE);
    }

    // 3. 터지는 조건 (한계 거리에 도달했는지 확인)
    public boolean isExpired() {
        return traveledDistance >= distance;
    }

    public double getX() {return x;}
    public double getY() {return y;}

    // 터지는 효과 (추후 구현)
    public void explode() {
        // Explosion 애니메이션 또는 사운드 출력
        System.out.println("Projectile Exploded at max distance!");
    }

    // 마우스 위치 도달 시 터지는 조건은 2단계에서 논의
}