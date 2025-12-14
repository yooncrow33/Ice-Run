package system;

import model.BackGroundIce;
import model.Orbit;
import model.PlayerAfterImage;
import model.Bullet;
import view.*;
import ygk.Base;

import java.awt.*;
import java.util.ArrayList;

public class Main extends Base implements IMove, IMouse, IWeapon, IShoot, IPlayerCoordinate, IStop {

    //System
    InputHandler inputHandler = new InputHandler(this, this, this);

    ArrayList<BackGroundIce> backGroundIces = new ArrayList<>();
    ArrayList<PlayerAfterImage> playerAfterImages = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();

    double dt;

    //class
    Orbit orbit = new Orbit(this);

    //개같은 final변수
    public boolean moveUp = false;
    public boolean moveLeft = false;
    public boolean moveDown = false;
    public boolean moveRight = false;

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

    final int PLAYER_WIDTH = 30;
    final int PLAYER_HEIGHT = 30;

    final int PLAYER_MOVE_SPEED = 10;

    final int weaponDistance[] = {120,200,400,600};
    int weapon = 0;

    boolean stop = false;

    double x;
    double y;

    public Main(String title) {
        super(title);
    }

    @Override
    protected void initGame() {
        for (int i = 0; 200 > i; i++) {
            backGroundIces.add(new BackGroundIce());
        }

        this.addKeyListener(inputHandler);
        this.addMouseListener(inputHandler);
    }

    @Override
    protected void render(Graphics g) {
        setBackground(Color.lightGray);

        g.setColor(Color.white);
        g.fillRect((int) (-((double) WORLD_WIDTH /2) - x) + VIRTUAL_X_SCREEN_CENTER, (int) (-((double) WORLD_HEIGHT/2 ) - y) + VIRTUAL_Y_SCREEN_CENTER, 19200,10800);


        for (BackGroundIce backGroundIce : backGroundIces) {
            if (backGroundIce.isVisibleToPlayer(x,y)) {
                backGroundIce.renderIce(g,x,y);
            }
        }

        g.setColor(Color.black);
        g.fillOval(VIRTUAL_X_SCREEN_CENTER - PLAYER_WIDTH / 2,
                VIRTUAL_Y_SCREEN_CENTER - PLAYER_HEIGHT / 2, PLAYER_WIDTH, PLAYER_HEIGHT);

        for (PlayerAfterImage ai : playerAfterImages) {
            ai.draw(g,x,y);
        }

        orbit.draw(g,PLAYER_WIDTH, weaponDistance[weapon]);

        for (Bullet p : bullets) {
            p.render(g, x, y); // x, y는 플레이어 월드 좌표
        }

        g.setColor(Color.black);
        g.fillRect(20, 900, 530,130);

        g.setColor(Color.green);
        if (weapon == 0) {
            g.fillRect(30, 905, 120, 120);
        } else if (weapon == 1) {
            g.fillRect(30 + 120 + 10, 905, 120, 120);
        } else if (weapon == 2) {
            g.fillRect(30 + 2 * (120 + 10), 905, 120, 120);
        } else if (weapon == 3) {
            g.fillRect(30 + 3 * (120 + 10), 905, 120, 120);

        }

        g.setColor(Color.white);
        g.fillRect(35, 910, 110, 110);
        g.fillRect(35 + 110 + 20, 910, 110, 110);
        g.fillRect(35 + 2 * (110 + 20), 910, 110, 110);
        g.fillRect(35 + 3 * (110 + 20), 910, 110, 110);

        g.setColor(Color.black);
        g.drawString(Double.toString(orbit.getOrbitAngle()), 20, 800);

        g.setColor(Color.black);
        renderMap(g);
    }

    @Override
    protected void update(double deltaTime) {
        dt = deltaTime / (16.0 / 1000.0);
        if (moveUp) { y -= PLAYER_MOVE_SPEED * dt; }
        if (moveDown) { y += PLAYER_MOVE_SPEED * dt; }
        if (moveLeft) { x -= PLAYER_MOVE_SPEED * dt; }
        if (moveRight) { x += PLAYER_MOVE_SPEED * dt; }

        for (int i = bullets.size() - 1; i >= 0; i--) {
            Bullet p = bullets.get(i);
            p.update(dt);

            if (p.isExpired()) {
                bullets.remove(i);
            }
        }

        playerAfterImages.add(new PlayerAfterImage(x,y,PLAYER_WIDTH,PLAYER_HEIGHT));

        for (int i = playerAfterImages.size() - 1; i >= 0; i--) {
            PlayerAfterImage p = playerAfterImages.get(i);

            if (p.isExpired()) { playerAfterImages.remove(i); }

            p.update();
        }

        checkBound();
    }

    public void shoot() {// Orbit에서 각도 정보를 가져옴
        double deltaX = getMouseX() - VIRTUAL_X_SCREEN_CENTER;
        double deltaY = getMouseY() - VIRTUAL_Y_SCREEN_CENTER;

        double distance = Math.sqrt(deltaX * deltaX + deltaY * deltaY);
        double length = Math.min(distance, weaponDistance[weapon]);

        double angle = orbit.getOrbitAngle();

        bullets.add(new Bullet(x,y,angle,length));
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
        double scaledX = x / MAP_SCALE;
        double scaledY = y / MAP_SCALE;

        // 2. 미니맵의 중앙을 기준으로 현재 위치를 계산 (중앙 오프셋)
        int mapCenterX = MINIMAP_WIDTH / 2;
        int mapCenterY = MINIMAP_HEIGHT / 2;
        int playerMapX = MINIMAP_X_START + mapCenterX + (int)scaledX;
        int playerMapY = MINIMAP_Y_START + mapCenterY + (int)scaledY;

        // 플레이어 표시
        g.setColor(Color.red);
        g.fillRect(playerMapX - 3, playerMapY - 3, 6, 6);
    }

    public void checkBound() {
        if (x > MAN_X) {
            x = MAN_X;
        } else if (x < MIN_X) {
            x = MIN_X;
        }

        if (y > MAX_Y) {
            y = MAX_Y;
        } else if (y < MIN_Y) {
            y = MIN_Y;
        }
    }

    public static void main(String[] args) {
        new Main("Ice & Run");
    }

    @Override public void setTrueMoveUp() { moveUp = true; }
    @Override public void setTrueMoveLeft() { moveLeft = true;  }
    @Override public void setTrueMoveDown() { moveDown = true;  }
    @Override public void setTrueMoveRight() { moveRight = true;  }

    @Override public void setFalseMoveUp() { moveUp = false;  }
    @Override public void setFalseMoveLeft() { moveLeft = false;  }
    @Override public void setFalseMoveDown() { moveDown = false;  }
    @Override public void setFalseMoveRight() { moveRight = false;  }

    @Override public int getVirtualMouseX() { return getMouseX(); }
    @Override public int getVirtualMouseY() { return getMouseY(); }

    @Override public void setWeapon() { if (weapon < 3) {
        weapon ++;
        System.out.println("+");
    } else {
        weapon = 0;
        System.out.println("-");
    }
    }

    @Override public void shootBullet() { shoot(); }

    @Override public double getPlayerX() { return x; }
    @Override public double getPlayerY() { return y; };

    @Override public void setGameStart() { stop = false; }
    @Override public void setGameStop() { stop = true; }
}
