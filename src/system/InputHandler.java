package system;

import view.IMove;
import view.IShoot;
import view.IWeapon;

import java.awt.event.*;

public class InputHandler extends MouseAdapter implements KeyListener {

    IMove move;
    IWeapon iWeapon;
    IShoot iShoot;

    public InputHandler(IMove move, IWeapon iWeapon, IShoot iShoot) {
        this.move = move;
        this.iWeapon = iWeapon;
        this.iShoot = iShoot;
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) { move.setTrueMoveUp(); }
        if (key == KeyEvent.VK_A) { move.setTrueMoveLeft(); }
        if (key == KeyEvent.VK_S) { move.setTrueMoveDown(); }
        if (key == KeyEvent.VK_D) { move.setTrueMoveRight(); }

        if (key == KeyEvent.VK_SHIFT) { iWeapon.setWeapon(); }
    }
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_W) { move.setFalseMoveUp(); }
        if (key == KeyEvent.VK_A) { move.setFalseMoveLeft(); }
        if (key == KeyEvent.VK_S) { move.setFalseMoveDown(); }
        if (key == KeyEvent.VK_D) { move.setFalseMoveRight(); }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        iShoot.shootBullet();
    }




    @Override
    public void keyTyped(KeyEvent e) {

    }
}
