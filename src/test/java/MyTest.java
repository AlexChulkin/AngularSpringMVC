import com.luxoft.snp.test.IntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 * Created by Vharutyunyan on 12.06.2014.
 */
@Category(IntegrationTest.class)
public class MyTest {

    @Test
    public void test(){

        System.out.println("Testet 456456465");

        WebDriver webDriver = new FirefoxDriver();
        webDriver.get("http://www.google.com");
        WebElement element = webDriver.findElement(By.cssSelector("#gbqfq"));
        element.sendKeys("Alex Vahe");


        WebElement goElement = webDriver.findElement(By.cssSelector("button#gbqfb"));

        Assert.assertNotNull(goElement);
//        goElement.click();

//        Assert.assertTrue("5".equals("54"));
    }
}
