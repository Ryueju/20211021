package co.eju.prj;

import java.awt.Image;

import javax.swing.ImageIcon;

public class MouseAttack {
//playerAttack처럼
	Image image = new ImageIcon(getClass().getResource("/Image/cheeze.png")).getImage();
	int x;
	int y;
	int width  = image.getWidth(null);
	int height = image.getHeight(null);
	int attack = 3;
	
	public MouseAttack(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public void shoot() {
		this.x -= 8;
	}
	
}
