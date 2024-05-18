package spider;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.JTextArea;
import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Queue;

public class p2 extends JFrame {
    public GridManager GM2 = new GridManager(1440, 960);
    JPanel jPanel = new JPanel();
    JPanel htmlPanel = new JPanel();
    JPanel textPanel = new JPanel();
    JPanel buttonPanel = new JPanel();
    JPanel wordPanel = new JPanel();
    JPanel listPanel = new JPanel();
    JPanel graphPanel = new JPanel();


    JLabel graphLabel = new JLabel();
    JLabel listLabel = new JLabel("已爬取URL列表");

    JButton buttonStart = new JButton("开始爬取");
    JButton buttonImport = new JButton("导入敏感词");
    JButton buttonHighlight = new JButton("提取敏感词");

    JTabbedPane tabPane = new JTabbedPane();
    JTextArea htmlArea = new JTextArea(50, 80);
    JScrollPane htmlSPane = new JScrollPane(htmlArea);
    JTextArea textArea = new JTextArea(50, 80);
    JScrollPane textSPane = new JScrollPane(textArea);
    JTextArea wordArea = new JTextArea(50, 80);
    JScrollPane wordSPane = new JScrollPane(wordArea);

    ArrayList<String> wordList = new ArrayList<>();
    ArrayList<Integer> wordNum = new ArrayList<>();
    ArrayList<String> urlList = new ArrayList<>();
    ArrayList<String> htmlList = new ArrayList<>();
    ArrayList<String> textList = new ArrayList<>();
    HashMap<String, ArrayList<String>> nextList = new HashMap<String, ArrayList<String> >();
    HashMap<String, Boolean> visitedHashMap = new HashMap<String, Boolean>();
    HashMap<String, Boolean> succeedHashMap = new HashMap<>();

    int remainUrlCount;

    public p2(int maxUrlCount, String url) {
        try { // 使用Windows的界面风格
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        remainUrlCount = maxUrlCount;
        this.setTitle("爬取网址: " + url);
        this.setSize(GM2.WIDTH, GM2.HEIGHT);
        this.setLocation(100,50);
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);

        jPanel.setLayout(new BorderLayout());
        htmlPanel.setLayout(new BorderLayout());
        htmlArea.setLineWrap(true);
        htmlPanel.add(htmlSPane, BorderLayout.CENTER);
        textPanel.setLayout(new BorderLayout());
        textArea.setLineWrap(true);
        textPanel.add(textSPane, BorderLayout.CENTER);
        wordArea.setLineWrap(true);
        wordPanel.setLayout(new BorderLayout());
        wordPanel.add(wordSPane, BorderLayout.CENTER);
        tabPane.add("源代码", htmlPanel);
        tabPane.add("文本", textPanel);
        tabPane.add("敏感词", wordPanel);
//        wordPane.setPreferredSize(new Dimension(6, 50));
        buttonPanel.add(buttonStart, BorderLayout.WEST);
        buttonPanel.add(buttonImport, BorderLayout.NORTH);
        buttonPanel.add(buttonHighlight, BorderLayout.EAST);

        listPanel.setLayout(new BorderLayout());
        listPanel.add(listLabel, BorderLayout.NORTH);

        ImageIcon graphImage = new ImageIcon(
                "C:\\Users\\Lenovo\\Desktop\\work_station\\sth\\image\\Suzuran\\92093214_p0.jpg");
        graphImage.setImage(graphImage.getImage().getScaledInstance(400,
                800,Image.SCALE_DEFAULT));
        graphLabel.setIcon(graphImage);
        graphPanel.add(graphLabel);

        jPanel.add(listPanel, BorderLayout.WEST);
        jPanel.add(tabPane, BorderLayout.CENTER);
        jPanel.add(graphPanel, BorderLayout.EAST);
        jPanel.add(buttonPanel, BorderLayout.SOUTH);
        this.add(jPanel);
        this.setVisible(true);
        buttonStart.addActionListener((k) -> {
            new Spider(this, url).start();
        });
        buttonImport.addActionListener((k) -> {
            getLib();
        });
        buttonHighlight.addActionListener((k) -> {
            showSensword();
        });
    }

    //从文件中读取敏感词
    public void getLib() {
        String relativePath = "lib";
        String absolutePath = System.getProperty("user.dir") + "\\" + relativePath;
        System.out.println(absolutePath);
        var fChooser = new JFileChooser(absolutePath);    //文件选择框

        var ok = fChooser.showOpenDialog(this);
        if (ok != JFileChooser.APPROVE_OPTION) return;    //判断是否正常选择
        wordList.clear();    //清空之前的记录
//        sensWord.setText("");
        var choosenLib = fChooser.getSelectedFile();    //获取选择的文件

        try {    //读取选中文件中的记录
//                var br = new BufferedReader(new FileReader(choosenLib));
            var br = new BufferedReader(new InputStreamReader(new FileInputStream(choosenLib), StandardCharsets.UTF_8));

            while (true) {
                var str = br.readLine();
                if (str == null) break;
                wordArea.append(str + "\n");
            }
            br.close();    //关闭文件流
        } catch (FileNotFoundException e1) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, "文件不存在");
            e1.printStackTrace();
        } catch (IOException e1) {
            // TODO Auto-generated catch block
            JOptionPane.showMessageDialog(null, "文件读取失败");
            e1.printStackTrace();
        }
    }

    //高亮显示
    public void showSensword() {
        String wordtext = wordArea.getText();
        String[] splitLines = wordtext.split("\\n");
        wordList.clear();
        wordNum.clear();
        for (String line : splitLines) {
            wordList.add(line);    //添加到记录中
            wordNum.add(0);        //设置对应的初始值
        }

        var hg = textArea.getHighlighter();    //设置文本框的高亮显示
        hg.removeAllHighlights();    //清除之前的高亮显示记录
        var text = textArea.getText();    //得到文本框的文本
        var painter = new DefaultHighlighter.DefaultHighlightPainter(Color.PINK);
//            if(wordList.isEmpty())return ;
        for (var str : wordList) {    //匹配其中的每一个敏感词
            var index = 0;
            while ((index = text.indexOf(str, index)) >= 0) {
                try {
                    hg.addHighlight(index, index + str.length(), painter);    //高亮显示匹配到的词语
                    index += str.length();    //更新匹配条件继续匹配
                } catch (BadLocationException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

     class Spider extends Thread {
        String url;    //网页链接
        MyProgressBar mpb;    //进度条
        public Spider(JFrame fa, String str) {
            mpb = new MyProgressBar(fa, "Running");
            url = str;
//            url = url.substring(url.indexOf("//") + 2);
//            url = url.replaceAll("\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}", "");
        }
        private void crawl(String nowUrl) {
            if (nowUrl.isEmpty()) {    //判断网址是否正常
                return;
            }
            mpb.setText("爬取" + nowUrl + "中...");    //设置进度条界面标题
            mpb.setVisible(true);        //显示进度条
            var html = HtmlHandler.getHtml(nowUrl);    //开始爬取
            mpb.dispose();    //关闭进度条
            if (!html.isEmpty()) {    //若爬取正常
                remainUrlCount --;

                JOptionPane.showMessageDialog(null, url + "已爬取完毕，剩余 " +
                        remainUrlCount + " 个网页待爬取");    //提示完成

                htmlArea.append(html);    //显示html源代码
                var text = HtmlHandler.getText(html);    //匹配网页文本
                textArea.append(text);//显示网页文本

                urlList.add(nowUrl);
                htmlList.add(html);
                textList.add(text);
                succeedHashMap.put(nowUrl, true);

                ArrayList<String> nextUrls = HtmlHandler.getNextUrl(html);
                nextList.put(nowUrl, nextUrls);
            }
        }
        public void run() {
            LinkedList<String> queue = new LinkedList<String>();
            queue.add(url);
            while (!queue.isEmpty() && remainUrlCount > 0) {
                String front = queue.removeFirst();
                if (visitedHashMap.containsKey(front)) continue;
                crawl(front);
                visitedHashMap.put(front, true);
                if (!nextList.containsKey(front)) continue;
                for (var next: nextList.get(front)) {
                    if (!visitedHashMap.containsKey(next)) {
                        queue.add(next);
                    }
                }
            }

            for (var s: urlList) {
                System.out.println(s + ":");
                for (var next: nextList.get(s)) {
                    if (succeedHashMap.containsKey(next)) {
                        System.out.println("    " + next);
                    }
                }
            }
        }
    }
}
