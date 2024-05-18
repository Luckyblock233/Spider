package spider;

import javax.swing.*;
import java.awt.*;

public class MyProgressBar extends JDialog {
    static final long serialVersionUID = 1L;
    JPanel jPanel = new JPanel();
    JProgressBar jpb = new JProgressBar();    //进度条
    JLabel curSpiding = new JLabel();    //显示当前网址

    //构造函数初始化,设置父窗口以及标题
    public MyProgressBar(JFrame f, String title) {
        super(f, title);
        this.setLocation(f.getWidth() / 2 + (int) f.getLocation().getX() / 2 - 80, f.getHeight() / 2 + (int) f.getLocation().getY() / 2 - 30);
        this.setSize(400, 100);
        this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        try { // 使用Windows的界面风格
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        jpb.setString("加载中，请稍等...");
        jpb.setIndeterminate(true);        //设置进度条为不确定模式
        jpb.setStringPainted(true);
        jpb.setBorderPainted(false);
        jpb.setForeground(Color.GREEN);    //设置进度条颜色
        jpb.setBackground(Color.WHITE);    //设置背景
        curSpiding.setPreferredSize(new Dimension(400, 30));

        //界面布局
        jPanel.setLayout(new BorderLayout());
        jPanel.add(curSpiding, BorderLayout.NORTH);
        jPanel.add(jpb, BorderLayout.CENTER);
        this.add(jPanel);
    }

    public void setText(String text) {
        curSpiding.setText(text);
    }
}
