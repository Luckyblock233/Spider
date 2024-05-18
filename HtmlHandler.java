package spider;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.regex.Pattern;

public class HtmlHandler {
    public static String getHtml(String website) {
        String buf;
        StringBuilder text = new StringBuilder();
        try {
            var url = new URL(website);
            var connection = url.openConnection();
            connection.connect();
            var contentType = connection.getContentType();
            if (!contentType.startsWith("text/html")) {
                throw new Exception(url + " Not a text/html file!");
            }
            System.out.println(contentType);

            var br = new BufferedReader(new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8));

            while (true) {
                buf = br.readLine();
                if (buf == null) break;
                text.append(buf).append("\n");
            }
            br.close();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, website + "爬取源代码失败\r\n" + e.getLocalizedMessage());
        }
//        System.out.println(text);
        return text.toString();
    }

    static String regExHtml = "<[^>]+>";        //匹配标签
    static String regExScript = "<script[^>]*?>[\\s\\S]*?<\\/script>";        //匹配script标签
    static String regExStyle = "<style[^>]*?>[\\s\\S]*?<\\/style>";        //匹配style标签
    static String regExA = "<a[^>]*?>[\\s\\S]*?<\\/a>";        //匹配style标签
    static String regExSpace = "[\\s]{2,}";    //匹配连续空格或回车等
    static String regExImg = "&[\\S]*?;+";    //匹配转义符
    static String regExHref = "href=\"[^\"]*\"";
    static String regExlink = "\"[\\s\\S]*\"";

    //定义正则表达式
    static Pattern pattern3 = Pattern.compile(regExHtml, Pattern.CASE_INSENSITIVE);
    static Pattern pattern1 = Pattern.compile(regExScript, Pattern.CASE_INSENSITIVE);
    static Pattern pattern2 = Pattern.compile(regExStyle, Pattern.CASE_INSENSITIVE);
    static Pattern pattern4 = Pattern.compile(regExSpace, Pattern.CASE_INSENSITIVE);
    static Pattern pattern5 = Pattern.compile(regExImg, Pattern.CASE_INSENSITIVE);
    static Pattern patternA = Pattern.compile(regExA, Pattern.CASE_INSENSITIVE);
    static Pattern patternHref = Pattern.compile(regExHref, Pattern.CASE_INSENSITIVE);
    static Pattern patternLink = Pattern.compile(regExlink, Pattern.CASE_INSENSITIVE);

    public static String getText(String str) {
        var matcher = pattern1.matcher(str);
        str = matcher.replaceAll("");        //去掉普通标签
        matcher = pattern2.matcher(str);
        str = matcher.replaceAll("");        //去掉script标签
        matcher = pattern3.matcher(str);
        str = matcher.replaceAll("");        //去掉style标签
        matcher = pattern4.matcher(str);
        str = matcher.replaceAll("\n");    //连续回车或空格变一个
        matcher = pattern5.matcher(str);
        str = matcher.replaceAll("");        //去掉转义符
        return str;        //返回文本
    }

    public static ArrayList<String> getNextUrl(String str) {
        ArrayList<String> urls = new ArrayList<>();

        var matcherA = patternA.matcher(str);
        while (matcherA.find()) {
            var matcherHref = patternHref.matcher(matcherA.group());
            while (matcherHref.find()) {
                var matcherLink = patternLink.matcher(matcherHref.group());
                while (matcherLink.find()) {
                    var url = matcherLink.group().split("\"")[1];
                    System.out.println("Found: " + url);
                    urls.add(url);
                }
            }
        }
        return urls;
    }
}