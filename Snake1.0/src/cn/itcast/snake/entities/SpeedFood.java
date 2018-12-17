package cn.itcast.snake.entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.util.Random;

import cn.itcast.snake.util.Global;

/**
 * 
 * ʳ��, ��x , y ���� �� ��ɫ������<BR>
 * ������setColor() �ı�ʳ�����ɫ<BR>
 * Ҳ����ͨ������ drawFood(Graphics, int, int, int, int) ���� �ı�ʳ�����ʾ��ʽ<BR>
 * 
 * @version 1.0, 01/01/08
 * 
 * @author ������
 * 
 */
public class SpeedFood extends Point {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/* ʳ�����ɫ */
	private Color color = new Color(0x66FF00);

	private Random random2 = new Random();

	/**
	 * Ĭ�ϵĹ�����, ����(0,0)������
	 */
	public SpeedFood() {
		super();
	}

	public Point getNew() {
		Point p2 = new Point();
		p2.x = random2.nextInt(Global.WIDTH);
		p2.y = random2.nextInt(Global.HEIGHT);
		return p2;
	}

	/**
	 * ��ʼ�������ָ��������ͬ�Ĺ�����
	 * 
	 * @param x
	 * @param y
	 */
	public SpeedFood(Point p2) {
		super(p2);
	}

	/**
	 * ���Ƿ�Ե���ʳ��
	 * 
	 * @param p
	 * @return
	 */
	public boolean isSnakeEatSpeedFood(Snake snake) {
		return this.equals(snake.getHead());
	}

	/**
	 * ���Լ�, ������ drawFood(Graphics, int, int, int, int) ����
	 * 
	 * @param g
	 */

	public void drawMe(Graphics g) {
		g.setColor(color);
		drawFood(g, x * Global.CELL_WIDTH, y * Global.CELL_HEIGHT,
				Global.CELL_WIDTH, Global.CELL_HEIGHT);
	}

	/**
	 * ��ʳ��, ���Ը�����������ı�ʳ�����ʾ
	 * 
	 * @param g
	 * @param x
	 *            �������� x
	 * @param y
	 *            �������� y
	 * @param width
	 *            ���(��λ:����)
	 * @param height
	 *            �߶�(��λ:����)
	 */
	public void drawFood(Graphics g, int x, int y, int width, int height) {
		g.fill3DRect(x, y, width, height, true);
	}

	/**
	 * �õ�ʳ�����ɫ
	 * 
	 * @return
	 */
	public Color getColor() {
		return color;
	}

	/**
	 * ����ʳ�����ɫ
	 * 
	 * @param color
	 */
	public void setColor(Color color) {
		this.color = color;
	}

}
