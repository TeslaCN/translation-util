package ltd.scau.util.translation.impl;

import ltd.scau.util.translation.Phonetic;
import ltd.scau.util.translation.TranslationResult;
import ltd.scau.util.translation.TranslationTool;
import ltd.scau.util.translation.WordMean;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

/**
 * @author Weijie Wu
 */
public class BaiduTranslationToolImpl implements TranslationTool {

    private static ThreadLocal<ChromeDriver> driverThreadLocal = new ThreadLocal<>();

    private static final String CHROME_DRIVER_PATH = "webdriver.chrome.driver";

    private static final Pattern URL_PATTERN = Pattern.compile(".*(https?://.*\\.\\w+).*");
    private static final Pattern PHONETIC_PATTERN = Pattern.compile("(.*)\\s+\\[(.*)\\]");
    private static final String SOURCE_XPATH = "//*[@id=\"main-outer\"]/div/div/div[1]/div[1]/div[1]/a[1]/span/span";
    private static final String TARGET_XPATH = "//*[@id=\"main-outer\"]/div/div/div[1]/div[1]/div[1]/a[3]/span/span";
    private static final String TARGET_URL = "https://fanyi.baidu.com/";

    @Override
    public TranslationResult translate(String query) {

        /*
        注意！此处复用了Driver以提高性能，尚未处理资源释放，注意Chrome进程
         */
        ChromeDriver driver;
        if ((driver = driverThreadLocal.get()) == null) {
            String chromeDriverPath;
            if ((chromeDriverPath = System.getProperty(CHROME_DRIVER_PATH)) == null || chromeDriverPath.isEmpty()) {
                System.setProperty(CHROME_DRIVER_PATH, "chromedriver");
            }
            ChromeOptions options = new ChromeOptions();
            options.addArguments("headless");
            driver = new ChromeDriver(options);
//            driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
            driver.manage().timeouts().pageLoadTimeout(3, TimeUnit.SECONDS);
            driverThreadLocal.set(driver);
        }
        for (; ; ) {
            try {

                TranslationResult translationResult = new TranslationResult();

                for (; ; ) {
                    try {
                        driver.get(TARGET_URL);
                        break;
                    } catch (Exception e) {
                    }
                }
                for (; ; ) {
                    try {
                        driver.findElement(By.id("baidu_translate_input")).clear();
                        driver.findElement(By.id("baidu_translate_input")).sendKeys(query);
                        driver.findElement(By.id("translate-button")).click();
                        WebDriverWait wait = new WebDriverWait(driver, 1);
                        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("target-output")));
                        translationResult.setTranslated(driver.findElement(By.className("target-output")).getText());
                        break;
                    } catch (Exception e) {
                    }
                }

                translationResult.setQuery(query);

                translationResult.setSource(
                        driver.findElement(By.xpath(SOURCE_XPATH))
                                .getText()
                                .replace("检测到", ""));

                translationResult.setTarget(
                        driver.findElement(
                                By.xpath(TARGET_XPATH)).getText());

                String imageUrl;
                try {
                    imageUrl = driver.findElement(By.className("dictionary-baike-img")).getCssValue("background-image");
                    if (imageUrl != null && !imageUrl.isEmpty()) {
                        Matcher m = URL_PATTERN.matcher(imageUrl);
                        if (m.find()) {
                            translationResult.setImageUrl(m.group(1));
                        }
                    }
                } catch (Exception e) {
                }

                translationResult.setWordMeans(
                        driver.findElements(By.xpath("//*[@id=\"left-result-container\"]/div/div/div/div/div[2]/p"))
                                .stream()
                                .map(WebElement::getText)
                                .map(s -> {
                                    WordMean m = new WordMean();
                                    String[] splited = s.split("\n");
                                    if (splited.length == 2) {
                                        m.setPart(splited[0]);
                                        m.setMeans(Arrays.asList(splited[1].split(";")));
                                    } else if (splited.length == 1) {
                                        m.setMeans(Arrays.asList(splited[0].split(";")));
                                    }
                                    return m;
                                })
                                .collect(Collectors.toList()));

                translationResult.setPhonetics(driver.findElements(By.className("phonetic-transcription"))
                        .stream()
                        .map(WebElement::getText)
                        .map(s -> {
                            Phonetic phonetic = new Phonetic();
                            Matcher m = PHONETIC_PATTERN.matcher(s);
                            if (m.find()) {
                                phonetic.setName(m.group(1));
                                phonetic.setValue(m.group(2));
                                return phonetic;
                            }
                            return null;
                        })
                        .collect(Collectors.toList()));

                return translationResult;
            } catch (Exception e) {

            }
        }
    }
}
