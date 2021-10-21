package co.eju.prj;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Bonus {
	Image image = new ImageIcon(getClass().getResource("/Image/bonus.png")).getImage();
	int x;
	int y;
	int width = image.getWidth(null);
	int height = image.getHeight(null);
	int score = 10;
	
	public Bonus(int x, int y) {
		this.x = x;
		this.y = x;
	}
	public void shoot() {
		this.x -=5;
	}
	
}
