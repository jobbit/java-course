package cn.itcast.snake.listener;

/**
 *
 * �ߵļ�����
 * 
 */
public interface SnakeListener {
	/**
	 * ���ƶ��¼�
	 */
	void snakeMoved();

	/**
	 * �߳Ե�ʳ���¼�
	 * @return 
	 */
	void snakeEatFood();
	
	void snakeEatSpeedFood();
}
