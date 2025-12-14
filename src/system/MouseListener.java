package system;

import view.IMove;
import view.IShoot;
import view.IWeapon;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class MouseListener extends MouseAdapter {

    IMove move;
    IWeapon iWeapon;
    IShoot iShoot;

    public MouseListener(IMove move, IWeapon iWeapon, IShoot iShoot) {
        this.move = move;
        this.iWeapon = iWeapon;
        this.iShoot = iShoot;
    }

    @Override
    public void mousePressed(MouseEvent e) {
        iShoot.shootBullet();
    }
}
