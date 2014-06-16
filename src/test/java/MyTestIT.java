import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.concurrent.TimeUnit;

public class MyTestIT {


    @Test
    public void test() {

        System.out.println("Test 1 is running");


        WebDriver webDriver = new FirefoxDriver();
        webDriver.get("http://localhost:8080/AngularSpringTest/home");
        String title = webDriver.getTitle();
        Assert.assertEquals("AngularSpring", title);
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("before colsing !!!!!!!");
        webDriver.close();
    }

    @Test
    public void test2() {
        System.out.println("Test 2 is running");

    }
}
