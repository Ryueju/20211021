package co.eju.prj;

import java.awt.Image;

import javax.swing.ImageIcon;

public class Mouse {
	Image image = new ImageIcon(getClass().getResource("/Image/mouse.png")).getImage();
	int x;
	int y;
	int width = image.getWidth(null);
	int height = image.getHeight(null);
	int hp = 5;
	
	public Mouse(int x, int y) { //위치정보를 매개변수로 받는 생성자를 만들어주고
		this.x = x;
		this.y = y;
	}
	
	public void move() { //쥐가 움직이게 하는 메소드
		this.x -= 6;
		
	}
}
