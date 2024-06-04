package spider.Handler;

import spider.UI.GridManager;

import javax.swing.*;
import java.awt.*;

public class p1 extends JFrame {
    GridManager GM = new GridManager(500, 200);
    Font textFont = new Font("微软雅黑",Font.PLAIN,16);
    JPanel jPanel1 = new JPanel();
    JPanel jPanel2 = new JPanel();
    JPanel jPanel3 = new JPanel();
    JButton startButton = new JButton("启动！");
    JLabel titleLabel = new JLabel("请输入待爬取网页信息：", JLabel.CENTER);
    JLabel settingLabel = new JLabel("最大爬取数量：");
    JLabel urlLabel = new JLabel("爬取目标网址：");
    JTextField urlTextField = new JTextField(80);
    JTextField settingTextField = new JTextField(80);


    public p1() {
        super();
    }

    public void init() {
        this.setTitle("网络文本爬取及敏感词分析");
        this.setSize(GM.WIDTH, GM.HEIGHT);
        this.setLayout(new BorderLayout());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        int windowWidth = GM.WIDTH;
        int windowHeight = GM.HEIGHT;
        int x = (screenWidth - windowWidth) / 2;
        int y = (screenHeight - windowHeight) / 2;
        this.setBounds(x, y, GM.WIDTH, GM.HEIGHT);

        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        try { // 使用Windows的界面风格
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        jPanel1.setLayout(new BorderLayout());
        settingLabel.setFont(textFont);
        jPanel1.add(settingLabel, BorderLayout.WEST);
        settingTextField.setFont(textFont);
        jPanel1.add(settingTextField, BorderLayout.CENTER);

        jPanel2.setLayout(new BorderLayout());
        urlLabel.setFont(textFont);
        jPanel2.add(urlLabel, BorderLayout.WEST);
        urlTextField.setFont(textFont);
        jPanel2.add(urlTextField, BorderLayout.CENTER);

        jPanel3.setLayout(new GridLayout(3, 1));
        jPanel3.add(jPanel1);
        jPanel3.add(jPanel2);
        startButton.setFont(textFont);
        jPanel3.add(startButton);

        titleLabel.setFont(textFont);
        this.add(titleLabel, BorderLayout.CENTER);
        this.add(jPanel3, BorderLayout.SOUTH);
        this.setVisible(true);
        revalidate();
        startButton.addActionListener((k) -> {
            if (urlTextField.getText().trim().equals("") || settingTextField.getText().trim().equals("")) {
                JOptionPane.showMessageDialog(null, "输入不得为空！", "error", JOptionPane.ERROR_MESSAGE);
                return ;
            }
            try {
                new p2(Integer.parseInt(settingTextField.getText())
                        , urlTextField.getText());
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "非法输入！\n" + e.getMessage(), "error", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
                return ;
            }
            this.dispose();
        });
    }
}
