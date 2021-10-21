package co.eju.prj;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Timer;
import java.util.TimerTask;
import java.awt.*;

public class ShootingGame extends JFrame { // GUI형태로 구현하기 위해서 JFrame상속해줌

	private Image bufferImage; // 이미지가 깜빡거리는 것을 방지하기 위해 더블버퍼링을사용해줌.그것을위해 미리 변수설정해놓기.
	private Graphics screenGraphic;

	private Image mainScreen = new ImageIcon(getClass().getResource("/Image/main.gif")).getImage();
	private Image loadingScreen = new ImageIcon(getClass().getResource("/Image/loading2.gif")).getImage();
	private Image gameScreen = new ImageIcon(getClass().getResource("/Image/pixel22.gif")).getImage();

	// boolean변수들로 화면 컨트롤
	private boolean isMainScreen, isLoadingScreen, isGameScreen;

	// game클래스의 객체 추가
	public static Game game = new Game();


	public ShootingGame() {
		setTitle("🐾 HUNTING CAT 🐾");
		setUndecorated(false);
		setSize(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);
		setResizable(false);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		setLayout(null);

		init();

	}

	// 초기화를 해줄 init메소드로 isMainScreen만 true로 해줌
	private void init() {
		isMainScreen = true;
		isLoadingScreen = false;
		isGameScreen = false;


		addKeyListener(new KeyListener()); // keyListener 추가

	}

	// 로딩, 게임화면으로 넘어가기 위한 gameStart메소드
	public void gameStart() {
		isMainScreen = false;
		isLoadingScreen = true;
		Timer loadingTimer = new Timer();
		TimerTask loadingTask = new TimerTask() {

			@Override
			public void run() {
				// TODO Auto-generated method stub
				// 실행할 내용
				isLoadingScreen = false;
				isGameScreen = true;
				game.start();
			}
		};
		loadingTimer.schedule(loadingTask, 5000);// 5초 후에 게임화면으로 넘어가도록

		// game클래스의 thread를 시작하기 위해서
	}

	public void paint(Graphics g) {
		/*
		 * 더블 버퍼링 : 컴퓨터가 그래픽을 표시하기 위해 일정시간마다 화면을 뿌려주는데, 지속적으로 갱신하다보면 깜빡임 현상이 나타남 이것을
		 * 방지하기 위해 사용됨
		 */
		bufferImage = createImage(Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT);

		screenGraphic = bufferImage.getGraphics();
		screenDraw(screenGraphic);
		g.drawImage(bufferImage, 0, 0, null);
	}

	public void screenDraw(Graphics g) {
		if (isMainScreen) {
			g.drawImage(mainScreen, 0, 0, null);
		}
		if (isLoadingScreen) {
			g.drawImage(loadingScreen, 0, 0, null);
		}
		if (isGameScreen) {
			g.drawImage(gameScreen, 0, 0, null);
			game.gameDraw(g);// 게임화면인때 gamedraw메소드를 실행하도록
		}
		this.repaint();
	}

	// esc 키를 누르면 종료되는 기능 구현
	class KeyListener extends KeyAdapter {

		// @Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				game.setUp(true);
				break;
			case KeyEvent.VK_DOWN:
				game.setDown(true);
				break;
			case KeyEvent.VK_LEFT:
				game.setLeft(true);
				break;
			case KeyEvent.VK_RIGHT:
				game.setRight(true);
				break;
			case KeyEvent.VK_SPACE:
				game.setShooting(true);
				break;
			case KeyEvent.VK_ENTER: // enter누르면
				if (isMainScreen) {
					gameStart(); // enter누르면 게임시작
				}
				break;
			case KeyEvent.VK_ESCAPE:
				System.exit(0); // esc 눌렀을 때, system.exit(0) 호출하기
				break;
			case KeyEvent.VK_R:
				if (game.isOver()) {
					game.reset();
					break;

				}

			}
		}

		public void keyReleased(KeyEvent e) { // 키 뗄 때
			switch (e.getKeyCode()) {
			case KeyEvent.VK_UP:
				game.setUp(false);
				break;
			case KeyEvent.VK_DOWN:
				game.setDown(false);
				break;
			case KeyEvent.VK_LEFT:
				game.setLeft(false);
				break;
			case KeyEvent.VK_RIGHT:
				game.setRight(false);
				break;
			case KeyEvent.VK_SPACE:
				game.setShooting(false);
				break;

			}
		}

	}
}
