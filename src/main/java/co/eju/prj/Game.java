package co.eju.prj;

import java.awt.*;
import java.util.ArrayList;

import javax.swing.ImageIcon;

public class Game extends Thread { // thread형태로 구현하기위해 thread상속받아줌
	private int delay = 20; // 게임의 딜레이, 딜레이마다 증가할 cnt선언
	private long pretime;
	private int cnt;
	private int score;

	private Image player = new ImageIcon(getClass().getResource("/Image/minicat.png")).getImage();
	private int playerX;
	private int playerY;
	private int playerWidth = player.getWidth(null);
	private int playerHeight = player.getHeight(null);
	private int playerSpeed = 10; // 키 입력이 한 번 인식됐을때, 플레이어가 이동할 거리
	private int playerHp = 30;

	// player의 움직임을 제어할 up,down,left,down변수선언 //shooting 변소는 true일 경우 공격발사
	private boolean up, down, left, right, shooting;
	private boolean isOver;

	private ArrayList<PlayerAttack> playerAttackList = new ArrayList<PlayerAttack>();
	// ArrayList안의 내용에 쉽게 접근할 수 있게
	// playerAttack변수를 선언
	private ArrayList<Mouse> mouseList = new ArrayList<Mouse>();
	private ArrayList<MouseAttack> mouseAttackList = new ArrayList<MouseAttack>();

	private ArrayList<Bonus> bonusList = new ArrayList<Bonus>();

	private PlayerAttack playerAttack;
	private MouseAttack mouseAttack;
	private Mouse mouse;
	private Bonus bonus;



	@Override
	// 이 thread를 시작할 때 실행할 메소드
	public void run() {

	
	
		cnt = 0;
		playerX = 10;
		playerY = (Main.SCREEN_HEIGHT - playerHeight) / 2;

		reset();
		while (true) {
			while (!isOver) {
				pretime = System.currentTimeMillis();
				if (System.currentTimeMillis() - pretime < delay) {
					try {
						Thread.sleep(delay - System.currentTimeMillis() + pretime);
						keyProcess();
						playerAttackProcess(); // 만든 메소드도 while루프에 추가해줘야함
						mouseAppearProcess();
						mouseMovingProcess();
						mouseAttackProcess();
						bonusGetProcess();
						cnt++;
					} catch (Exception e) {
						e.printStackTrace();
						// TODO: handle exception
					}
				}
			}
			try {
				// cnt를 앞에서 설정한 delay밀리초가 지날때마다 증가시켜줌
				Thread.sleep(200); // 2초
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}
	}

	public void reset() {
		isOver = false;
		cnt = 0;
		score = 0;
		playerX = 10;
		playerY = (Main.SCREEN_HEIGHT - playerHeight) / 2;
		playerHp = 30;

	
		playerAttackList.clear();
		mouseList.clear();
		mouseAttackList.clear();

	}

	// key입력을 처리할 메소드
	private void keyProcess() {
		if (up && playerY - playerSpeed > 0)
			playerY -= playerSpeed;
		if (down && playerY + playerHeight + playerSpeed < Main.SCREEN_HEIGHT)
			playerY += playerSpeed;
		if (left && playerX - playerSpeed > 0)
			playerX -= playerSpeed;
		if (right && playerX + playerWidth + playerSpeed < Main.SCREEN_WIDTH)
			playerX += playerSpeed;
		if (shooting && cnt % 8 == 0) {
			playerAttack = new PlayerAttack(playerX + 65, playerY + 15); // 플레이어와 적당히 떨어진 위치에 공격만들어줌
			playerAttackList.add(playerAttack);
		}
		// cnt가 0.02초 마다 올라가므로 0.3초마다 미사일이 발사되도록 설정
	}

	// 고양이 공격처리 메소드
	private void playerAttackProcess() {
		for (int i = 0; i < playerAttackList.size(); i++) {
			// Array의 get메소드를 이용해 담긴 객체 하나하나에 접근해 shoot메소드를 실행해줌
			playerAttack = playerAttackList.get(i);
			playerAttack.shoot();

			for (int j = 0; j < mouseList.size(); j++) {
				mouse = mouseList.get(j);
				if (playerAttack.x > mouse.x && playerAttack.x < mouse.x + mouse.width && playerAttack.y > mouse.y
						&& playerAttack.y < mouse.y + mouse.height) {
					mouse.hp -= playerAttack.attack;
					playerAttackList.remove(playerAttack);

				}
				if (mouse.hp <= 0) {
					
					mouseList.remove(mouse);
					score += 100;
				}
			}
		}
	}

	// 쥐가 계속해서 나오는 처리 메소드
	private void mouseAppearProcess() {
		if (cnt % 50 == 0) {
			mouse = new Mouse(500, (int) (Math.random() * 265));// y축범위에서 랜덤으로 나오도록 설정
			// arraylist에 추가
			mouseList.add(mouse);
		}
	}

	private void mouseMovingProcess() {
		for (int i = 0; i < mouseList.size(); i++) {
			mouse = mouseList.get(i);// arraylist에 접근해서 move메소드 호출해줘야함
			mouse.move();
		}
	}

	// 치즈 날아옴(공격)
	private void mouseAttackProcess() {
		if (cnt % 50 == 0) {
			mouseAttack = new MouseAttack(mouse.x - 200, mouse.y + 35);
			mouseAttackList.add(mouseAttack);
		}
		for (int i = 0; i < mouseAttackList.size(); i++) {
			mouseAttack = mouseAttackList.get(i);
			mouseAttack.shoot();

			if (mouseAttack.x > playerX & mouseAttack.x < playerX + playerWidth && mouseAttack.y > playerY
					&& mouseAttack.y < playerY + playerHeight) {
			   
				playerHp -= mouseAttack.attack;
				mouseAttackList.remove(mouseAttack);
				if (playerHp <= 0)
					isOver = true;
			}
		}
	}

	// 보너스 메소드
	private void bonusGetProcess() {
		if (cnt % 40 == 0) {
			bonus = new Bonus(mouse.x - 100, mouse.y + 55);
			bonusList.add(bonus);
		}
		for (int i = 0; i < bonusList.size(); i++) {
			bonus = bonusList.get(i);
			bonus.shoot();
			if (bonus.x > playerX & bonus.x < playerX + playerWidth && bonus.y > playerY
					&& bonus.y < playerY + playerHeight) {
				score += bonus.score;
				bonusList.remove(bonus);
				// if(playerHp <=0)
				// isOver = true;
			}
		}
	}

	// 게임화면 요소들 구현

	public void gameDraw(Graphics g) {
		playerDraw(g);
		mouseDraw(g);
		infoDraw(g);

	}

	public void infoDraw(Graphics g) {
		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 11));
		g.drawString("[ SCORE  >  " + score + " ] ", 390, 50);

		g.setColor(Color.BLACK);
		g.setFont(new Font("Arial", Font.BOLD, 11));
		g.drawString("['Esc' to exit ]", 400, 270);

		if (isOver) {
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 15));
			g.drawString("Your SCORE is ... " + score + " ! ", 170, 120);
			g.setColor(Color.BLACK);
			g.setFont(new Font("Arial", Font.BOLD, 20));
			g.drawString("Press 'R' to restart ...", 160, 150);
		}
	}

	// private 변수의 경우 객체를 통한 직접적인 접근이 안되어서 setter필요
	public void playerDraw(Graphics g) {
		// TODO Auto-generated method stub
		g.drawImage(player, playerX, playerY, null);
		g.setColor(Color.ORANGE);
		g.fillRect(playerX - 1, playerY - 9, playerHp * 3, 9);

		for (int i = 0; i < playerAttackList.size(); i++) {
			// Array의 get메소드를 이용해 담긴 객체 하나하나에 접근해 shoot메소드를 실행해줌
			playerAttack = playerAttackList.get(i);
			g.drawImage(playerAttack.image, playerAttack.x, playerAttack.y, null);
		}
	}

	// 적과 적의 공격을 그려줄 메소드
	public void mouseDraw(Graphics g) {
		for (int i = 0; i < mouseList.size(); i++) {
			mouse = mouseList.get(i);
			g.drawImage(mouse.image, mouse.x, mouse.y, null);
			g.setColor(Color.GREEN);
			g.fillRect(mouse.x + 1, mouse.y - 8, mouse.hp * 3, 9);
		}
		for (int i = 0; i < mouseAttackList.size(); i++) {
			mouseAttack = mouseAttackList.get(i);
			g.drawImage(mouseAttack.image, mouseAttack.x, mouseAttack.y, null);

		}
		for (int i = 0; i < bonusList.size(); i++) {
			bonus = bonusList.get(i);
			g.drawImage(bonus.image, bonus.x, bonus.y, null);
		}
	}

	public boolean isOver() {
		return isOver;
	}

	public void setUp(boolean up) {
		this.up = up;
	}

	public void setDown(boolean down) {
		this.down = down;
	}

	public void setLeft(boolean left) {
		this.left = left;
	}

	public void setRight(boolean right) {
		this.right = right;
	}

	public void setShooting(boolean shooting) {
		this.shooting = shooting;
	}

}
