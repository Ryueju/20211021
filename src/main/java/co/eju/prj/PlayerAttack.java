package co.eju.prj;

import java.awt.Image;

import javax.swing.ImageIcon;

public class PlayerAttack {
	Image image = new ImageIcon(getClass().getResource("/Image/fishbone.png")).getImage();
	int x;
	int y;
	int width = image.getWidth(null); // 공격의 충돌판정을 위해 이미지의 너비와 높이도 필요함
	int height = image.getHeight(null);
	int attack = 5;

	public PlayerAttack(int x, int y ) { //x,y를 매개변수로 하는 생성자를 만들어줌
		this.x = x;
		this.y = y;
	
		
	}
	//생선뼈다귀 발사 메소드
	//공격 방향 >>> 오른쪽이므로 x값을 증가시켜야 함
	public void shoot() {
		this.x += 20;
		
	}

}
