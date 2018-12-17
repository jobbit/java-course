package cn.itcast.snake.listener;

/**
 *
 * 蛇的监听器
 * 
 */
public interface SnakeListener {
	/**
	 * 蛇移动事件
	 */
	void snakeMoved();

	/**
	 * 蛇吃到食物事件
	 * @return 
	 */
	void snakeEatFood();
	
	void snakeEatSpeedFood();
}
