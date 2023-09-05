package com.vieup.utility.media;

import lombok.Data;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.FontMetrics;
import java.awt.image.BufferedImage;
import java.io.*;
import javax.imageio.ImageIO;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;


import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

public class TextToImageUtil {

    public void buildImage(String text, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2d = image.createGraphics();
        g2d.setColor(Color.WHITE); // 设置背景色
        g2d.fillRect(0, 0, width, height); // 填充背景色

        Font font = new Font("Arial", Font.BOLD, 20); // 字体样式
        g2d.setFont(font);
        g2d.setColor(Color.BLACK); // 字体颜色

        FontMetrics fontMetrics = g2d.getFontMetrics();
        int lineHeight = fontMetrics.getHeight();

        String[] lines = wrapText(text, fontMetrics, width);
        int textHeight = lines.length * lineHeight;
        int textWidth = fontMetrics.stringWidth(text);
        if (textWidth > width) {

        }
        int x = 50; // 文本的横坐标
        int y = 100; // 文本的纵坐标
        g2d.drawString(text, x, y);

        g2d.dispose();

        String outputPath = "output.png"; // 图片保存路径
        File outputFile = new File(outputPath);
//        ImageIO.write(image, "png", outputFile);

    }

    public void html2Image(String html, String file, ImageOptions imageOptions) throws IOException {
        System.setProperty("webdriver.chrome.driver", "/Users/xusage/Devapps/chromedriver/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.setHeadless(true)
                .addArguments("--remote-allow-origins=*"); // 设置为无头模式
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--start-maximized");

        // 创建 ChromeDriver 对象
        WebDriver driver = new ChromeDriver(options);

        // 设置窗口大小，以适应页面内容
        Dimension dimension = new Dimension(imageOptions.getWidth(), imageOptions.getHeight());
        driver.manage().window().setSize(dimension);

        driver.get("data:text/html," + html);

        // 加载 HTML 字符串

        // 等待页面加载完成
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 截取屏幕并保存为图片
        File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        BufferedImage image;
        try {
            image = ImageIO.read(screenshotFile);
            // 可以根据需要对图片进行进一步的处理，如裁剪、调整大小等

            // 保存图片
            File outputFile = new File(file);
            ImageIO.write(image, "png", outputFile);
            System.out.println("Image saved to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        // 关闭浏览器
        driver.quit();
    }

    /**
     * build image from template and options
     * @param templateName
     * @param imageOptions
     * @throws IOException
     */
    private static void buildImage(String templateName, ImageOptions imageOptions) throws IOException {
        String html = buildHtml(templateName, imageOptions);
    }

    private static String buildHtml(String templateName, ImageOptions options) throws IOException {
        StringWriter writer = new StringWriter();

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.init();

        // 创建 Velocity 上下文，并设置模板变量的值
        VelocityContext context = new VelocityContext();
        context.put("width", options.getWidth());
        context.put("height", options.getHeight());
        context.put("content", options.getContent());
        context.put("fontSize", options.getFontSize());

        // 加载模板
        Template template = velocityEngine.getTemplate(templateName);

        // 渲染模板
        template.merge(context, writer);

        // 输出渲染结果
//        System.out.println(writer.toString());
        return writer.toString();
    }

    /**
     * wrap the text by font and width
     * @param text
     * @param fontMetrics
     * @param width
     * @return
     */
    private static String[] wrapText(String text, FontMetrics fontMetrics, int width) {
        StringBuilder sb = new StringBuilder();
        String[] words = text.split(" ");
        String[] lines = new String[words.length];
        int lineIndex = 0;

        for (String word : words) {
            if (fontMetrics.stringWidth(sb.toString() + word) > width) {
                lines[lineIndex] = sb.toString();
                sb.setLength(0);
                lineIndex++;
            }
            sb.append(word).append(" ");
        }
        lines[lineIndex] = sb.toString().trim();

        return lines;
    }

    private static final String HTML_TEMPLATE = "<div style='width:100%; height:100%; padding: 1em; overflow-y: scroll; '></div>";
    @Data
    public static class ImageOptions {
        int width;
        int height;

        String fontSize;

        Object content;
    }

    public void buildNumber99() throws IOException {
        TextToImageUtil textToImage = new TextToImageUtil();
        ImageOptions options = new ImageOptions();
        options.setWidth(1280);
        options.setHeight(720);
        options.setFontSize("8em");
        for (int i = 0; i < 100; i ++) {
            textToImage.html2Image("" + i, "number_" + i + ".png", options);
        }
    }
    public static void main(String... args) throws IOException {


    }
}
