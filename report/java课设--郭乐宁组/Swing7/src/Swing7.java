
import javax.swing.*;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.Stack;
import java.util.Timer;
import java.util.TimerTask;

public class Swing7 extends JFrame { 
    private static final int MIN_CNT=20;//最小火柴根数量为20
	private static final int MAX_CNT=50;//最大火柴根数量为50
	private static final int MAX_TAKE=3;//最大拿火柴根数量为3
	private static final int DELAY=1000;//延迟时间为1000毫秒
	
	//主界面两个选择按钮
    JButton jb1 = new JButton();
	JButton jb2 = new JButton(); 
	
	//人机模式界面设计
	private JFrame mainFrame;//人机窗口
	private MyPaintPanel[] paints;//画板数组
	private JButton userBtn;//用户按钮
	private JComboBox userNum;//用户下拉框
	private JButton compBtn;//电脑先拿按钮
    private JLabel compTxt;//电脑标签
    private Timer timer;//计时器
	
    //人人模式界面设计
	private JFrame mainFrame2;
	private JButton user1Btn;//用户1的take按钮
	private JButton user2Btn;//用户2的take按钮
	private JComboBox user1Num;//用户1下拉框
	private JComboBox user2Num;//用户2下拉框

	
    public Swing7() {
        this.setTitle("火柴小游戏");
    	setLayout(new FlowLayout());//居中对齐，使用方法add将组件顺序地添加到容器中
    	
    	JLabel txt=new JLabel();
    	txt.setText("<html><body>游戏规则：<br>"
    				+"1.轮流拿取火柴，谁拿到最后一根谁获胜<br>"
    				+"2.每次每方必须拿1-3根<br>"
    				+"3.可以选择人机对战和人人对战两种模式<br>"
    				+"&nbsp;&nbsp;快来试试吧~~~<br><br><br></body></html>");
    	this.add(txt);
    	
        jb1.setText("人机模式");
        this.add(jb1);
        this.setBounds(100, 300, 250, 300);//设置该标签显示位置为坐标（100, 300）,宽为250，高300
        this.setVisible(true);
        
        jb2.setText("人人模式");
        this.add(jb2);
        this.setBounds(120, 500, 250, 300);
        this.setVisible(true);

    	mainFrame=new JFrame();
    	mainFrame2=new JFrame();
        
        jb1.addActionListener(new ActionListener()//为jb1添加行为事件监听器
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(!mainFrame.isVisible() && !mainFrame2.isVisible()) {
					jb1.setEnabled(false);
					jb2.setEnabled(false);
		        	ha();
		        }
			}
		});
        
        jb2.addActionListener(new ActionListener()//为jb2添加行为事件监听器
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				if(!mainFrame.isVisible() && !mainFrame2.isVisible()) {
					jb2.setEnabled(false);
					jb1.setEnabled(false);
		        	hb();
		        	
		        }
			}
		});
    }

	
	private void ha()//人机游戏总流程
	{
		mainFrame=new JFrame();
		mainFrame.setResizable(false);//设置窗体大小不可调整
		
		//mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);//设置默认的关闭操作为隐藏dispose当前窗口
		mainFrame.addWindowListener( new WindowAdapter() {//设置人机模式窗口的关闭事件监听器
            @Override
            public void windowClosing(WindowEvent we) {
            	jb2.setEnabled(true);
    			jb1.setEnabled(true);
            }
        });
		
		JPanel mainPanel=new JPanel();//主面板
		mainPanel.setLayout(null);//没有任何布局
		mainPanel.setPreferredSize(new Dimension(600,400));//将组件的首选大小设置为常量值
		mainFrame.add(mainPanel);
		
		JLabel lab11=new JLabel("电脑");//标签按钮的内容为电脑
		lab11.setHorizontalAlignment(JLabel.CENTER);//设置文字水平居中
		lab11.setBounds(0,10,200,20);    
		mainPanel.add(lab11);
		
		JButton btn11=new JButton("Reset");
		btn11.addActionListener(new ActionListener()//为“Reset”添加行为事件监听器，采用匿名内部类的方式
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				init();//调用初始化方法
			}
		});
		btn11.setBounds(250,10,100,20);
		mainPanel.add(btn11);
		
		JLabel lab12=new JLabel("用户");//用户标签
		lab12.setHorizontalAlignment(JLabel.CENTER);
		lab12.setBounds(400,10,200,20);
		mainPanel.add(lab12);
		
		compBtn=new JButton("Computer First");//电脑先拿按钮
		compBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				compTake();//直接调用电脑拿方法
			}
		});
		compBtn.setBounds(10,40,180,20);
		mainPanel.add(compBtn);
		
		compTxt=new JLabel();//空标签用来显示电脑拿了几根火柴
		compTxt.setBounds(10,70,400,20);
		mainPanel.add(compTxt);
		
		userBtn=new JButton("Take");//用户拿按钮
		userBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				userTake();//直接调用用户拿方法
			}
		});
		userBtn.setBounds(400,40,90,20);
		mainPanel.add(userBtn);

		userNum=new JComboBox();//用户的下拉框选择拿几根
		for(int i=0;i<MAX_TAKE;i++)
		{
			userNum.addItem(i+1); //通过循环给列表框逐个添加选项
		}
		userNum.setBounds(495,40,90,20);
		mainPanel.add(userNum);

		paints=new MyPaintPanel[3];//定义放火柴的面板
		for(int i=0;i<3;i++)//画面板
		{
			paints[i]=new MyPaintPanel();
			paints[i].setBounds(5+200*i,110,190,285); //设置三个面板的位置和坐标，间距为5
			mainPanel.add(paints[i]);
		}
		
		mainFrame.pack(); //调整此窗口的大小，以适合其子组件的首选大小和布局
		mainFrame.setVisible(true); //设置当前主窗口的可见性！！！
		init(); //调用初始化方法
	}

		private void init()//人机游戏内容初始化
		{
			if(timer!=null)
			{
				timer.cancel();//如果计时器不为空的话，快速终止计时器的任务执行线程，全部任务都清空
			}
			timer=new Timer();//新的计时器对象
			compTxt.setText("");
			compBtn.setEnabled(true);
			userBtn.setEnabled(true);
			paints[0].reset();//重新定义面板
			paints[1].reset();
			paints[2].reset();
			int num=MIN_CNT+(int)((MAX_CNT+1-MIN_CNT)*Math.random());//随机产生一个大于20小于50的数
			
			for(int i=0;i<num;i++)
			{
				paints[1].add(i+1); //分别将产生的随机数添加到第二块面板！！
			}
		}

		private void userTake()//用户拿函数
		{
			compBtn.setEnabled(false);//电脑先拿按钮不可用
			int takeCnt=userNum.getSelectedIndex()+1;//用户拿数量
			int rel =paints[1].getCnt();//剩余火柴数量
			if(takeCnt>rel) //拿多了给出提示
			{
				JOptionPane.showMessageDialog(mainFrame,"There is only"+rel+"matches.");
				return;
			}
			
			for(int i=0;i<takeCnt;i++)//拿对了移动火柴
			{
				int index=paints[1].remove();//借助中间变量index
				paints[2].add(index);
			}
			
			if(takeCnt==rel)//正好拿完，游戏结束，赢
			{
				JOptionPane.showMessageDialog(mainFrame,"You are the winner.");
				init();//并重新初始化！！！
			}
			else
			{
				compTake();//让电脑拿火柴
			}
		}

		private void compTake()//电脑拿函数
		{
			compBtn.setEnabled(false); 
			userBtn.setEnabled(false); 
			MyTask task =new MyTask(); //MyTask对象！
			timer.schedule(task,DELAY); //执行线程，安排在指定延迟1秒后执行指定的任务！	
		}

		private class MyTask extends TimerTask{//定义一个继承了实现Runnable接口的类使自己也具有线程功能
			@Override
			public void run()//因为TimerTask实现了Runnable接口，需要重写父类的run方法
			{
				int rel = paints[1].getCnt();//剩余火柴数量
				int takeCnt = 0;//当前拿火柴数为0
				if(rel <= MAX_TAKE)
				{
					takeCnt = rel;//直接拿完就赢了
				}
				else if(rel % (MAX_TAKE + 1) > 0)
				{
					takeCnt = rel % (MAX_TAKE + 1);//保持剩余火柴数是4的倍数则自己能赢
				}
				else
				{
					takeCnt = 1 + (int) (MAX_TAKE * Math.random());//目前没有赢的希望，则1-3随机拿
				}
			
				for(int i=0;i<takeCnt;i++)
				{
					int index=paints[1].remove();
					paints[0].add(index);
				}
			
				compTxt.setText("Computer takes "+takeCnt+"matches this time.");//显示电脑拿火柴数
			
				if(takeCnt == rel)
				{
					JOptionPane.showMessageDialog(mainFrame,"Computer is the winner.");//电脑赢，游戏结束
					init();//初始化！！！
				}
				userBtn.setEnabled(true);//否则就用户按钮重新可用，让用户可以拿
			}
		}


private class MyPaintPanel extends Component //定义一个自定义的面板类并继承了组件类
{
	private BufferedImage bimg;       //创建一个BufferedImage的变量bimg
	private Stack<Integer> data;      //定义一个私有的栈类型的变量data
	
	private MyPaintPanel()//构造我的初始面板的方法
	{
		bimg = new BufferedImage(190,285,BufferedImage.TYPE_3BYTE_BGR);
		//表示一个具有8位RGB颜色分量的图像Graphics2D
		Graphics2D g2=bimg.createGraphics();//创建一个Graphics2D，并将它绘制到此BufferedImage中
		g2.setColor(Color.WHITE);//设置要画的颜色，即面板的颜色
		g2.fillRect(0,0,190,285);//在坐标（0,0）处画一个大小为宽190，高为285的白色矩形区域
		g2.dispose();//释放此图形的上下文以及它使用的所有系统资源。调用dispose之后，就不能再使用Graphics对象。
		data=new Stack<Integer>();//在此面板中实例化一个具体的栈的对象
	}
	
	public void paint(Graphics g)//定义画图形的默认方法
	{	
		g.drawImage(bimg,0,0,null);//从坐标（0,0）处开始画图片
		g.dispose();//释放此图形的上下文以及它使用的所有系统资源。调用dispose之后，就不能再使用Graphics对象。
	}
	
	private	void add(int num)//添加火柴棍
	{
		Graphics2D g2=bimg.createGraphics();//创建一个Graphics2D，并将它绘制到此BufferedImage中
		
		int loc=data.size();//将当前栈的大小赋给变量
		//System.out.println("当前栈为"+data.size());
		int offX=loc%3*65;//动态的坐标
		int offY=loc/3*15;//动态的坐标
		g2.setColor(Color.YELLOW);//设置火柴棒的颜色为黄色 
		g2.fillRect(offX+8,offY+5,50,6);//填充火柴棒的颜色区域
		g2.setColor(Color.GREEN);//设置火柴棒头的颜色为绿色
		g2.fillArc(offX+50,offY+3,10,10,0,360);//设置火柴棒头的坐标，大小为宽和高为10的360度的弧
		g2.setColor(Color.BLACK);//设置当前火柴棒的根数的颜色
		g2.drawString(String.valueOf(num),offX,offY+g2.getFont().getSize());//画出数字
		data.push(num);//把数字num压入堆栈顶部
		repaint();//重画
		g2.dispose();//释放此图形的上下文以及它使用的所有系统资源。调用dispose之后，就不能再使用Graphics对象
	}
	
	private int remove()//移除火柴棍
	{
		Graphics2D g2=bimg.createGraphics();//创建一个Graphics2D，并将它绘制到此BufferedImage中
		int loc=data.size()- 1;//将当前栈的大小即火柴棍的数量减1
		//System.out.println("当前栈为"+data.size()); 
		int offX=loc%3*65;//动态的获取坐标	 
		int offY=loc/3*15;//动态的获取坐标
		g2.setColor(Color.WHITE);//设置移除填充颜色为白色	 
		g2.fillRect(offX,offY,60,15);//画出移除火柴棍的位置
		g2.dispose();//释放此图形的上下文以及它使用的所有系统资源。调用dispose之后，就不能再使用Graphics对象
		repaint();//调用重新画的方法
		return data.pop();//移除堆栈顶部的对象，并作为此函数的值返回该对象。
	}
	
	private void reset()//重置面板
	{
		data.clear();//清楚该面板栈中的内容
		Graphics2D g2=bimg.createGraphics();//创建一个 Graphics2D，并将它绘制到此BufferedImage中
		g2.setColor(Color.WHITE);//设置颜色为白色
		g2.fillRect(0,0,190,285);//在坐标为（0,0）处填充大小为190,285的矩形区域 
		g2.dispose();//释放此图形的上下文以及它使用的所有系统资源。调用dispose之后，就不能再使用Graphics对象。
		repaint();//重绘此组件。
	}
		
	private int getCnt()//获取第二个面板中火柴棍数量
	{
		return data.size();//返回当前栈中的火柴棍数量！！
	}
}

	private void hb()
    {
		JFrame mainFrame2 = new JFrame();
   		mainFrame2.setResizable(false);//设置窗体大小不可调整
   		
   		mainFrame2.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);//设置默认的关闭操作为隐藏dispose当前窗口
   		mainFrame2.addWindowListener( new WindowAdapter() {//设置人人模式窗口的关闭事件监听器
            @Override
            public void windowClosing(WindowEvent we) {
            	jb2.setEnabled(true);
    			jb1.setEnabled(true);
            }
        });
   		
   		JPanel mainPanel2=new JPanel();//主面板对象
   		mainPanel2.setLayout(null);//没有任何布局
   		mainPanel2.setPreferredSize(new Dimension(600,400));//将组件的首选大小设置为常量值
   		mainFrame2.add(mainPanel2);
   		
   		JLabel lab11=new JLabel("用户1");//标签按钮的内容为用户1
   		lab11.setHorizontalAlignment(JLabel.CENTER);//文字的居中
   		lab11.setBounds(0,10,200,20);
   		mainPanel2.add(lab11);
   		
   		JButton btn11=new JButton("Reset");
   		btn11.addActionListener(new ActionListener()
   		{
   			@Override
   			public void actionPerformed(ActionEvent e)
   			{
   				initpp();//调用初始化函数
   			}
   		});
   		btn11.setBounds(250,10,100,20);
   		mainPanel2.add(btn11);
   		
   		JLabel lab12=new JLabel("用户2");
   		lab12.setHorizontalAlignment(JLabel.CENTER);
   		lab12.setBounds(400,10,200,20);
   		mainPanel2.add(lab12);
   	
   		user1Btn = new JButton("Take");//用户1按钮
   		user1Btn.addActionListener(new ActionListener()
   		{
   			@Override
   			public void actionPerformed(ActionEvent e)
   			{
   				user1Take();//调用用户1拿方法函数
   			}
   		});
   		user1Btn.setBounds(10,40,90,20);
   		mainPanel2.add(user1Btn);

   	    user1Num = new JComboBox();//用户1下拉框
   		for(int i=0;i<MAX_TAKE;i++)
   		{
   			user1Num.addItem(i+1);
   		}
   		user1Num.setBounds(105,40,90,20);
   		mainPanel2.add(user1Num);
   		
   		user2Btn = new JButton("Take");//用户2按钮
   		user2Btn.addActionListener(new ActionListener()
   		{
   			@Override
   			public void actionPerformed(ActionEvent e)
   			{
   				user2Take();//调用用户2拿方法函数
   			}
   		});
   		user2Btn.setBounds(400,40,90,20);
   		mainPanel2.add(user2Btn);

   		user2Num = new JComboBox();//用户2下拉框
   		for(int i=0;i<MAX_TAKE;i++)
   		{
   			user2Num.addItem(i+1);
   		}
   		user2Num.setBounds(495,40,90,20);
   		mainPanel2.add(user2Num);
   		
   		paints=new MyPaintPanel[3];
   		for(int i=0;i<3;i++)
   		{
   			paints[i]=new MyPaintPanel();
   			paints[i].setBounds(5+200*i,110,190,285); //设置三个面板的位置和坐标，间距为5
   			mainPanel2.add(paints[i]);
   		}
   		
   		mainFrame2.pack(); //调整此窗口的大小，以适合其子组件的首选大小和布局
   		mainFrame2.setVisible(true); //设置当前主窗口的可见性
   		initpp(); //调用初始化方法
     }

	private void user1Take() {
		user2Btn.setEnabled(false);
		int takeCnt1=user1Num.getSelectedIndex()+1;
		//System.out.println(takeCnt1);
		int rel =paints[1].getCnt();
		//System.out.println(rel);
		if(takeCnt1>rel)
		{
			JOptionPane.showMessageDialog(mainFrame2,"There is only"+rel+"matches.");
			return;
		}
		
		for(int i=0;i<takeCnt1;i++)
		{
			int index=paints[1].remove();
			paints[0].add(index);
		}
		
		if(takeCnt1==rel)
		{
			JOptionPane.showMessageDialog(mainFrame2,"User1 is the winner.");
			initpp();//并重新初始化
		}
//		else    //如果不相等的话，执行下面的语句  就是游戏需要继续，则系统设置关键按钮的可用与否，等待用户的点击，而不是直接调用函数进行火柴的拿取 
//		{       //只有用户选择拿火柴了之后点击两个take按钮才能调用这俩函数，涉及按钮的监听事件
//		//	user2Take();//让电脑接着拿火柴棍
//		}
		user2Btn.setEnabled(true);//设置按钮可用与否！！！
		user1Btn.setEnabled(false);
	}
	
	private void user2Take() {
		user1Btn.setEnabled(false);
		int takeCnt2=user2Num.getSelectedIndex()+1;
		int rel =paints[1].getCnt();
		if(takeCnt2>rel)
		{
			JOptionPane.showMessageDialog(mainFrame2,"There is only"+rel+"matches.");
			return;
		}
		
		for(int i=0;i<takeCnt2;i++) 
		{
			int index=paints[1].remove();
			paints[2].add(index); 
		}
		
		if(takeCnt2==rel)
		{
			JOptionPane.showMessageDialog(mainFrame2,"User2 is the winner.");
			initpp();//初始化！！！
		}
//		else    //如果不相等的话，执行下面的语句
//		{
//		
//		}
		user1Btn.setEnabled(true);
		user2Btn.setEnabled(false);
	}

	private void initpp() {
		user1Btn.setEnabled(true);
		user2Btn.setEnabled(true);
		
		paints[0].reset();
		paints[1].reset();
		paints[2].reset();
		
		int num=MIN_CNT+(int)((MAX_CNT+1-MIN_CNT)*Math.random());
		
		for(int i=0;i<num;i++)
		{
			paints[1].add(i+1);
		}
	}

	public static void main(String args[]) {//游戏入口
        Swing7 s = new Swing7();
    }
}