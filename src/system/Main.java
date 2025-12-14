package system;

import model.*;
import view.*;
import ygk.Base;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class Main extends Base implements IMove, IMouse, IWeapon, IShoot, IPlayerCoordinate, IStop {

    static final String version = "1.0.1";

    //System
    MouseListener mouseListener = new MouseListener(this,this,this);
    GraphicsManager gm = new GraphicsManager(this);

    ArrayList<BackGroundIce> backGroundIces = new ArrayList<>();
    ArrayList<PlayerAfterImage> playerAfterImages = new ArrayList<>();
    ArrayList<Bullet> bullets = new ArrayList<>();
    ArrayList<Bomb> bombs = new ArrayList<>();

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

        this.addMouseListener(mouseListener);
        setupKeyBindings();
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

        for (Bomb p : bombs) {
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
        g.fillRect(165, 910, 110, 110);
        g.fillRect(295, 910, 110, 110);
        g.fillRect(425, 910, 110, 110);

        g.setColor(Color.black);
        g.drawString(Double.toString(orbit.getOrbitAngle()), 20, 800);

        g.setColor(Color.black);
        gm.renderMap(g);
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
                bombs.add(new Bomb(p.getX(), p.getY()));
                bullets.remove(i);
            }
        }


        for (int i = bombs.size() - 1; i >= 0; i--) {
            Bomb p = bombs.get(i);

            p.update();

            if (p.isExpired()) {
                bombs.remove(i);
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
        new Main("Ice & Run (alpha " + version + ")");
    }

    private void setupKeyBindings() {
        // 1. InputMap과 ActionMap을 가져옵니다.
        // WHEN_IN_FOCUSED_WINDOW: 창이 활성화되어 있는 동안 입력이 작동하도록 설정
        InputMap im = this.getInputMap(JPanel.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = this.getActionMap();

        // --- W 키 (위로 이동) ---
        // Key Pressed (눌렀을 때)
        // Key Released (떼었을 때)
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, false), "W_PRESS");
        am.put("W_PRESS", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { setTrueMoveUp(); }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_W, 0, true), "W_RELEASE");
        am.put("W_RELEASE", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { setFalseMoveUp(); }
        });

        // --- S 키 (아래로 이동) ---
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, false), "S_PRESS");
        am.put("S_PRESS", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { setTrueMoveDown(); }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_S, 0, true), "S_RELEASE");
        am.put("S_RELEASE", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { setFalseMoveDown(); }
        });

        // --- A 키 (왼쪽 이동) ---
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, false), "A_PRESS");
        am.put("A_PRESS", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { setTrueMoveLeft(); }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_A, 0, true), "A_RELEASE");
        am.put("A_RELEASE", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { setFalseMoveLeft(); }
        });

        // --- D 키 (오른쪽 이동) ---
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, false), "D_PRESS");
        am.put("D_PRESS", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { setTrueMoveRight(); }
        });
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_D, 0, true), "D_RELEASE");
        am.put("D_RELEASE", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { setFalseMoveRight(); }
        });

        // --- SHIFT 키 (무기 변경) ---
        // SHIFT는 누르는 순간에만 작동하고 뗄 때는 동작이 필요 없으므로 PRESS만 설정
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_F, 0, false), "F_PRESS");
        am.put("F_PRESS", new AbstractAction() {
            @Override public void actionPerformed(ActionEvent e) { setWeapon(); }
        });
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
