
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
    private static final int MIN_CNT=20;//��С��������Ϊ20
	private static final int MAX_CNT=50;//����������Ϊ50
	private static final int MAX_TAKE=3;//����û�������Ϊ3
	private static final int DELAY=1000;//�ӳ�ʱ��Ϊ1000����
	
	//����������ѡ��ť
    JButton jb1 = new JButton();
	JButton jb2 = new JButton(); 
	
	//�˻�ģʽ�������
	private JFrame mainFrame;//�˻�����
	private MyPaintPanel[] paints;//��������
	private JButton userBtn;//�û���ť
	private JComboBox userNum;//�û�������
	private JButton compBtn;//�������ð�ť
    private JLabel compTxt;//���Ա�ǩ
    private Timer timer;//��ʱ��
	
    //����ģʽ�������
	private JFrame mainFrame2;
	private JButton user1Btn;//�û�1��take��ť
	private JButton user2Btn;//�û�2��take��ť
	private JComboBox user1Num;//�û�1������
	private JComboBox user2Num;//�û�2������

	
    public Swing7() {
        this.setTitle("���С��Ϸ");
    	setLayout(new FlowLayout());//���ж��룬ʹ�÷���add�����˳�����ӵ�������
    	
    	JLabel txt=new JLabel();
    	txt.setText("<html><body>��Ϸ����<br>"
    				+"1.������ȡ���˭�õ����һ��˭��ʤ<br>"
    				+"2.ÿ��ÿ��������1-3��<br>"
    				+"3.����ѡ���˻���ս�����˶�ս����ģʽ<br>"
    				+"&nbsp;&nbsp;�������԰�~~~<br><br><br></body></html>");
    	this.add(txt);
    	
        jb1.setText("�˻�ģʽ");
        this.add(jb1);
        this.setBounds(100, 300, 250, 300);//���øñ�ǩ��ʾλ��Ϊ���꣨100, 300��,��Ϊ250����300
        this.setVisible(true);
        
        jb2.setText("����ģʽ");
        this.add(jb2);
        this.setBounds(120, 500, 250, 300);
        this.setVisible(true);

    	mainFrame=new JFrame();
    	mainFrame2=new JFrame();
        
        jb1.addActionListener(new ActionListener()//Ϊjb1�����Ϊ�¼�������
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
        
        jb2.addActionListener(new ActionListener()//Ϊjb2�����Ϊ�¼�������
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

	
	private void ha()//�˻���Ϸ������
	{
		mainFrame=new JFrame();
		mainFrame.setResizable(false);//���ô����С���ɵ���
		
		//mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);//����Ĭ�ϵĹرղ���Ϊ����dispose��ǰ����
		mainFrame.addWindowListener( new WindowAdapter() {//�����˻�ģʽ���ڵĹر��¼�������
            @Override
            public void windowClosing(WindowEvent we) {
            	jb2.setEnabled(true);
    			jb1.setEnabled(true);
            }
        });
		
		JPanel mainPanel=new JPanel();//�����
		mainPanel.setLayout(null);//û���κβ���
		mainPanel.setPreferredSize(new Dimension(600,400));//���������ѡ��С����Ϊ����ֵ
		mainFrame.add(mainPanel);
		
		JLabel lab11=new JLabel("����");//��ǩ��ť������Ϊ����
		lab11.setHorizontalAlignment(JLabel.CENTER);//��������ˮƽ����
		lab11.setBounds(0,10,200,20);    
		mainPanel.add(lab11);
		
		JButton btn11=new JButton("Reset");
		btn11.addActionListener(new ActionListener()//Ϊ��Reset�������Ϊ�¼������������������ڲ���ķ�ʽ
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				init();//���ó�ʼ������
			}
		});
		btn11.setBounds(250,10,100,20);
		mainPanel.add(btn11);
		
		JLabel lab12=new JLabel("�û�");//�û���ǩ
		lab12.setHorizontalAlignment(JLabel.CENTER);
		lab12.setBounds(400,10,200,20);
		mainPanel.add(lab12);
		
		compBtn=new JButton("Computer First");//�������ð�ť
		compBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				compTake();//ֱ�ӵ��õ����÷���
			}
		});
		compBtn.setBounds(10,40,180,20);
		mainPanel.add(compBtn);
		
		compTxt=new JLabel();//�ձ�ǩ������ʾ�������˼������
		compTxt.setBounds(10,70,400,20);
		mainPanel.add(compTxt);
		
		userBtn=new JButton("Take");//�û��ð�ť
		userBtn.addActionListener(new ActionListener()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				userTake();//ֱ�ӵ����û��÷���
			}
		});
		userBtn.setBounds(400,40,90,20);
		mainPanel.add(userBtn);

		userNum=new JComboBox();//�û���������ѡ���ü���
		for(int i=0;i<MAX_TAKE;i++)
		{
			userNum.addItem(i+1); //ͨ��ѭ�����б��������ѡ��
		}
		userNum.setBounds(495,40,90,20);
		mainPanel.add(userNum);

		paints=new MyPaintPanel[3];//����Ż������
		for(int i=0;i<3;i++)//�����
		{
			paints[i]=new MyPaintPanel();
			paints[i].setBounds(5+200*i,110,190,285); //������������λ�ú����꣬���Ϊ5
			mainPanel.add(paints[i]);
		}
		
		mainFrame.pack(); //�����˴��ڵĴ�С�����ʺ������������ѡ��С�Ͳ���
		mainFrame.setVisible(true); //���õ�ǰ�����ڵĿɼ��ԣ�����
		init(); //���ó�ʼ������
	}

		private void init()//�˻���Ϸ���ݳ�ʼ��
		{
			if(timer!=null)
			{
				timer.cancel();//�����ʱ����Ϊ�յĻ���������ֹ��ʱ��������ִ���̣߳�ȫ���������
			}
			timer=new Timer();//�µļ�ʱ������
			compTxt.setText("");
			compBtn.setEnabled(true);
			userBtn.setEnabled(true);
			paints[0].reset();//���¶������
			paints[1].reset();
			paints[2].reset();
			int num=MIN_CNT+(int)((MAX_CNT+1-MIN_CNT)*Math.random());//�������һ������20С��50����
			
			for(int i=0;i<num;i++)
			{
				paints[1].add(i+1); //�ֱ𽫲������������ӵ��ڶ�����壡��
			}
		}

		private void userTake()//�û��ú���
		{
			compBtn.setEnabled(false);//�������ð�ť������
			int takeCnt=userNum.getSelectedIndex()+1;//�û�������
			int rel =paints[1].getCnt();//ʣ��������
			if(takeCnt>rel) //�ö��˸�����ʾ
			{
				JOptionPane.showMessageDialog(mainFrame,"There is only"+rel+"matches.");
				return;
			}
			
			for(int i=0;i<takeCnt;i++)//�ö����ƶ����
			{
				int index=paints[1].remove();//�����м����index
				paints[2].add(index);
			}
			
			if(takeCnt==rel)//�������꣬��Ϸ������Ӯ
			{
				JOptionPane.showMessageDialog(mainFrame,"You are the winner.");
				init();//�����³�ʼ��������
			}
			else
			{
				compTake();//�õ����û��
			}
		}

		private void compTake()//�����ú���
		{
			compBtn.setEnabled(false); 
			userBtn.setEnabled(false); 
			MyTask task =new MyTask(); //MyTask����
			timer.schedule(task,DELAY); //ִ���̣߳�������ָ���ӳ�1���ִ��ָ��������	
		}

		private class MyTask extends TimerTask{//����һ���̳���ʵ��Runnable�ӿڵ���ʹ�Լ�Ҳ�����̹߳���
			@Override
			public void run()//��ΪTimerTaskʵ����Runnable�ӿڣ���Ҫ��д�����run����
			{
				int rel = paints[1].getCnt();//ʣ��������
				int takeCnt = 0;//��ǰ�û����Ϊ0
				if(rel <= MAX_TAKE)
				{
					takeCnt = rel;//ֱ�������Ӯ��
				}
				else if(rel % (MAX_TAKE + 1) > 0)
				{
					takeCnt = rel % (MAX_TAKE + 1);//����ʣ��������4�ı������Լ���Ӯ
				}
				else
				{
					takeCnt = 1 + (int) (MAX_TAKE * Math.random());//Ŀǰû��Ӯ��ϣ������1-3�����
				}
			
				for(int i=0;i<takeCnt;i++)
				{
					int index=paints[1].remove();
					paints[0].add(index);
				}
			
				compTxt.setText("Computer takes "+takeCnt+"matches this time.");//��ʾ�����û����
			
				if(takeCnt == rel)
				{
					JOptionPane.showMessageDialog(mainFrame,"Computer is the winner.");//����Ӯ����Ϸ����
					init();//��ʼ��������
				}
				userBtn.setEnabled(true);//������û���ť���¿��ã����û�������
			}
		}


private class MyPaintPanel extends Component //����һ���Զ��������ಢ�̳��������
{
	private BufferedImage bimg;       //����һ��BufferedImage�ı���bimg
	private Stack<Integer> data;      //����һ��˽�е�ջ���͵ı���data
	
	private MyPaintPanel()//�����ҵĳ�ʼ���ķ���
	{
		bimg = new BufferedImage(190,285,BufferedImage.TYPE_3BYTE_BGR);
		//��ʾһ������8λRGB��ɫ������ͼ��Graphics2D
		Graphics2D g2=bimg.createGraphics();//����һ��Graphics2D�����������Ƶ���BufferedImage��
		g2.setColor(Color.WHITE);//����Ҫ������ɫ����������ɫ
		g2.fillRect(0,0,190,285);//�����꣨0,0������һ����СΪ��190����Ϊ285�İ�ɫ��������
		g2.dispose();//�ͷŴ�ͼ�ε��������Լ���ʹ�õ�����ϵͳ��Դ������dispose֮�󣬾Ͳ�����ʹ��Graphics����
		data=new Stack<Integer>();//�ڴ������ʵ����һ�������ջ�Ķ���
	}
	
	public void paint(Graphics g)//���廭ͼ�ε�Ĭ�Ϸ���
	{	
		g.drawImage(bimg,0,0,null);//�����꣨0,0������ʼ��ͼƬ
		g.dispose();//�ͷŴ�ͼ�ε��������Լ���ʹ�õ�����ϵͳ��Դ������dispose֮�󣬾Ͳ�����ʹ��Graphics����
	}
	
	private	void add(int num)//��ӻ���
	{
		Graphics2D g2=bimg.createGraphics();//����һ��Graphics2D�����������Ƶ���BufferedImage��
		
		int loc=data.size();//����ǰջ�Ĵ�С��������
		//System.out.println("��ǰջΪ"+data.size());
		int offX=loc%3*65;//��̬������
		int offY=loc/3*15;//��̬������
		g2.setColor(Color.YELLOW);//���û�������ɫΪ��ɫ 
		g2.fillRect(offX+8,offY+5,50,6);//����������ɫ����
		g2.setColor(Color.GREEN);//���û���ͷ����ɫΪ��ɫ
		g2.fillArc(offX+50,offY+3,10,10,0,360);//���û���ͷ�����꣬��СΪ��͸�Ϊ10��360�ȵĻ�
		g2.setColor(Color.BLACK);//���õ�ǰ�����ĸ�������ɫ
		g2.drawString(String.valueOf(num),offX,offY+g2.getFont().getSize());//��������
		data.push(num);//������numѹ���ջ����
		repaint();//�ػ�
		g2.dispose();//�ͷŴ�ͼ�ε��������Լ���ʹ�õ�����ϵͳ��Դ������dispose֮�󣬾Ͳ�����ʹ��Graphics����
	}
	
	private int remove()//�Ƴ�����
	{
		Graphics2D g2=bimg.createGraphics();//����һ��Graphics2D�����������Ƶ���BufferedImage��
		int loc=data.size()- 1;//����ǰջ�Ĵ�С��������������1
		//System.out.println("��ǰջΪ"+data.size()); 
		int offX=loc%3*65;//��̬�Ļ�ȡ����	 
		int offY=loc/3*15;//��̬�Ļ�ȡ����
		g2.setColor(Color.WHITE);//�����Ƴ������ɫΪ��ɫ	 
		g2.fillRect(offX,offY,60,15);//�����Ƴ�������λ��
		g2.dispose();//�ͷŴ�ͼ�ε��������Լ���ʹ�õ�����ϵͳ��Դ������dispose֮�󣬾Ͳ�����ʹ��Graphics����
		repaint();//�������»��ķ���
		return data.pop();//�Ƴ���ջ�����Ķ��󣬲���Ϊ�˺�����ֵ���ظö���
	}
	
	private void reset()//�������
	{
		data.clear();//��������ջ�е�����
		Graphics2D g2=bimg.createGraphics();//����һ�� Graphics2D�����������Ƶ���BufferedImage��
		g2.setColor(Color.WHITE);//������ɫΪ��ɫ
		g2.fillRect(0,0,190,285);//������Ϊ��0,0��������СΪ190,285�ľ������� 
		g2.dispose();//�ͷŴ�ͼ�ε��������Լ���ʹ�õ�����ϵͳ��Դ������dispose֮�󣬾Ͳ�����ʹ��Graphics����
		repaint();//�ػ�������
	}
		
	private int getCnt()//��ȡ�ڶ�������л�������
	{
		return data.size();//���ص�ǰջ�еĻ�����������
	}
}

	private void hb()
    {
		JFrame mainFrame2 = new JFrame();
   		mainFrame2.setResizable(false);//���ô����С���ɵ���
   		
   		mainFrame2.setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);//����Ĭ�ϵĹرղ���Ϊ����dispose��ǰ����
   		mainFrame2.addWindowListener( new WindowAdapter() {//��������ģʽ���ڵĹر��¼�������
            @Override
            public void windowClosing(WindowEvent we) {
            	jb2.setEnabled(true);
    			jb1.setEnabled(true);
            }
        });
   		
   		JPanel mainPanel2=new JPanel();//��������
   		mainPanel2.setLayout(null);//û���κβ���
   		mainPanel2.setPreferredSize(new Dimension(600,400));//���������ѡ��С����Ϊ����ֵ
   		mainFrame2.add(mainPanel2);
   		
   		JLabel lab11=new JLabel("�û�1");//��ǩ��ť������Ϊ�û�1
   		lab11.setHorizontalAlignment(JLabel.CENTER);//���ֵľ���
   		lab11.setBounds(0,10,200,20);
   		mainPanel2.add(lab11);
   		
   		JButton btn11=new JButton("Reset");
   		btn11.addActionListener(new ActionListener()
   		{
   			@Override
   			public void actionPerformed(ActionEvent e)
   			{
   				initpp();//���ó�ʼ������
   			}
   		});
   		btn11.setBounds(250,10,100,20);
   		mainPanel2.add(btn11);
   		
   		JLabel lab12=new JLabel("�û�2");
   		lab12.setHorizontalAlignment(JLabel.CENTER);
   		lab12.setBounds(400,10,200,20);
   		mainPanel2.add(lab12);
   	
   		user1Btn = new JButton("Take");//�û�1��ť
   		user1Btn.addActionListener(new ActionListener()
   		{
   			@Override
   			public void actionPerformed(ActionEvent e)
   			{
   				user1Take();//�����û�1�÷�������
   			}
   		});
   		user1Btn.setBounds(10,40,90,20);
   		mainPanel2.add(user1Btn);

   	    user1Num = new JComboBox();//�û�1������
   		for(int i=0;i<MAX_TAKE;i++)
   		{
   			user1Num.addItem(i+1);
   		}
   		user1Num.setBounds(105,40,90,20);
   		mainPanel2.add(user1Num);
   		
   		user2Btn = new JButton("Take");//�û�2��ť
   		user2Btn.addActionListener(new ActionListener()
   		{
   			@Override
   			public void actionPerformed(ActionEvent e)
   			{
   				user2Take();//�����û�2�÷�������
   			}
   		});
   		user2Btn.setBounds(400,40,90,20);
   		mainPanel2.add(user2Btn);

   		user2Num = new JComboBox();//�û�2������
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
   			paints[i].setBounds(5+200*i,110,190,285); //������������λ�ú����꣬���Ϊ5
   			mainPanel2.add(paints[i]);
   		}
   		
   		mainFrame2.pack(); //�����˴��ڵĴ�С�����ʺ������������ѡ��С�Ͳ���
   		mainFrame2.setVisible(true); //���õ�ǰ�����ڵĿɼ���
   		initpp(); //���ó�ʼ������
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
			initpp();//�����³�ʼ��
		}
//		else    //�������ȵĻ���ִ����������  ������Ϸ��Ҫ��������ϵͳ���ùؼ���ť�Ŀ�����񣬵ȴ��û��ĵ����������ֱ�ӵ��ú������л�����ȡ 
//		{       //ֻ���û�ѡ���û����֮��������take��ť���ܵ��������������漰��ť�ļ����¼�
//		//	user2Take();//�õ��Խ����û���
//		}
		user2Btn.setEnabled(true);//���ð�ť������񣡣���
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
			initpp();//��ʼ��������
		}
//		else    //�������ȵĻ���ִ����������
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

	public static void main(String args[]) {//��Ϸ���
        Swing7 s = new Swing7();
    }
}