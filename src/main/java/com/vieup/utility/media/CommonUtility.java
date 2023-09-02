package com.vieup.utility.media;

import com.google.gson.Gson;
import freemarker.template.Configuration;
import freemarker.template.TemplateExceptionHandler;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;


public class CommonUtility {

    static Gson gson = new Gson();
    public static String velocityHtml(String templatePath, Object context) {
        StringWriter writer = new StringWriter();

        VelocityEngine velocityEngine = new VelocityEngine();
        velocityEngine.setProperty("resource.loader", "class");
        velocityEngine.setProperty("class.resource.loader.class", "org.apache.velocity.runtime.resource.loader.ClasspathResourceLoader");
        velocityEngine.init();

        Map<String, Object> contextMap = gson.fromJson(gson.toJson(context), Map.class);
        // 创建 Velocity 上下文，并设置模板变量的值
        VelocityContext velocityContext = new VelocityContext(contextMap);
        System.out.println(gson.toJson(context) + ", " + velocityContext.get("title"));
        // 加载模板
        org.apache.velocity.Template template = velocityEngine.getTemplate(templatePath);

        // 渲染模板
//        template.setData(context);
        template.merge(velocityContext, writer);

        // 输出渲染结果
//        System.out.println(writer.toString());
        return writer.toString();
    }

    public static String freeMarkerHtml(String templatePath, Object context) throws  Exception {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_22);

        System.out.println(CommonUtility.class.getClassLoader().getResource("templates/freemarker"));
        cfg.setDirectoryForTemplateLoading(new java.io.File(CommonUtility.class.getClassLoader().getResource("templates/freemarker").getFile()));
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        freemarker.template.Template template = cfg.getTemplate(templatePath);

        StringWriter writer = new StringWriter();
        template.process(context, writer);
        return writer.toString();
    }

    public static String resourcePath(String resource) {
        return CommonUtility.class.getClassLoader().getResource(resource).getFile();
    }

    private static ChromeDriver buildDriver(int width, int height) {
        System.setProperty("webdriver.chrome.driver", "/Users/xusage/Devapps/chromedriver/chromedriver");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*"); // 设置为无头模式
        options.addArguments("--headless");
        options.addArguments("--disable-gpu");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--start-maximized");
        options.addArguments("--window-size=" + width + "," + height);
        // 创建 ChromeDriver 对象
        ChromeDriver driver = new ChromeDriver(options);

        return driver;
    }

    public static void html2Image(String html, String file, Integer width, Integer height) throws IOException {
        ChromeDriver driver = buildDriver(width, height);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        driver.get("data:text/html," + html);

        // 加载 HTML 字符串

        // 等待页面加载完成
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        Long winWidth = (Long) driver.executeScript("return document.documentElement.scrollWidth");
        Long winHeight = (Long) driver.executeScript("return document.documentElement.scrollHeight");
        //设置浏览器弹窗页面的大小
        driver.manage().window().setSize(new Dimension(winWidth.intValue(), winHeight.intValue()));

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

    public static void html2Image(File htmlFile, String imagePath, Integer width, Integer height) throws IOException {

        ChromeDriver driver = buildDriver(width, height);
        try {
            driver.get("file://" + htmlFile.getAbsolutePath());

            // 等待页面加载完成
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Long winWidth = (Long) driver.executeScript("return document.documentElement.scrollWidth");
            Long winHeight = (Long) driver.executeScript("return document.documentElement.scrollHeight");
            //设置浏览器弹窗页面的大小
            driver.manage().window().setSize(new Dimension(winWidth.intValue(), winHeight.intValue()));

            // 截取屏幕并保存为图片
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            BufferedImage bufferedImage = ImageIO.read(screenshotFile);
            int subWidth = Math.min(bufferedImage.getWidth(), width);
            int subHeight = Math.min(bufferedImage.getHeight(), height);

            bufferedImage = bufferedImage.getSubimage(0, 0, subWidth, subHeight);

            File outputFile = new File(imagePath);
            ImageIO.write(bufferedImage, "png", outputFile);
            System.out.println("Image saved to: " + outputFile.getAbsolutePath());

        }
        catch (IOException e) {
            e.printStackTrace();
        }
        finally {
            driver.quit();
        }
    }
}
