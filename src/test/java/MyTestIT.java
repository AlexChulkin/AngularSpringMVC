import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.concurrent.TimeUnit;

public class MyTestIT {


    private static WebDriver webDriver;
    private static final String NEW_LABEL = "Vahe Alex Ratio";
    private static final String NEW_IN_COMMITEE = "STRONG";
    private static final String NEW_PRE_COMMITEE = "ADEQUATE";
    private static final String NEW_FINAL = "VERY_WEAK";


    @BeforeClass
    public static void init() {
        webDriver = new FirefoxDriver();
    }

    @AfterClass
    public static void finish(){
        webDriver.close();
    }


//    @Test
    public void test() {
        webDriver.get("http://localhost:8080/AngularSpringTest/home");

        System.out.println("Test 1 is running");



        String title = webDriver.getTitle();
        Assert.assertEquals("AngularSpring", title);
        try {
            TimeUnit.SECONDS.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("before closing !!!!!!!");
        webDriver.close();
    }

    @Test
    public void test2() throws InterruptedException {
        webDriver.get("http://localhost:8080/AngularSpringTest/home");

        System.out.println("Test 2 is running");

        WebElement labelInputElement = webDriver.findElement(By.cssSelector("#newLabel"));
        Assert.assertNotNull("New Label input is not found", labelInputElement);
        labelInputElement.sendKeys(NEW_LABEL);
        WebElement preInputElement = webDriver.findElement(By.cssSelector("#proCommiteeNewVal"));
        Assert.assertNotNull("New Pre Commitee input is not found",preInputElement);
        preInputElement.sendKeys(NEW_PRE_COMMITEE);
        WebElement inInputElement = webDriver.findElement(By.cssSelector("#inCommiteeNewVal"));
        Assert.assertNotNull("New In Commitee input is not found",inInputElement);
        inInputElement.sendKeys(NEW_IN_COMMITEE);
        WebElement finalInputElement = webDriver.findElement(By.cssSelector("#finalNewVal"));
        Assert.assertNotNull("New Final input is not found",finalInputElement);
        finalInputElement.sendKeys(NEW_FINAL);

        WebElement btnElement = webDriver.findElement(By.cssSelector("#addBtn"));
        Assert.assertNotNull("Add btn is not found",btnElement);
        btnElement.click();

        (new WebDriverWait(webDriver, 10000))
                .until(ExpectedConditions.presenceOfElementLocated(By.cssSelector("#lbl_6")));


        WebElement labelElement = webDriver.findElement(By.cssSelector("#lbl_6"));
        Assert.assertNotNull("New Label is not inserted",labelElement);
        Assert.assertEquals("New Label is inserted but is unproper", labelElement.getText(), NEW_LABEL);
        WebElement preElement = webDriver.findElement(By.cssSelector("#6_inp_0"));
        Assert.assertNotNull("New Pre Commitee is not inserted",preElement);
        Assert.assertEquals("New Pre Commitee is inserted but is unproper", preElement.getText(), NEW_PRE_COMMITEE);
        WebElement inElement = webDriver.findElement(By.cssSelector("#6_inp_1"));
        Assert.assertNotNull("New In Commitee is not inserted",inElement);
        Assert.assertEquals("New In Commitee is inserted but is unproper", inElement.getText(), NEW_IN_COMMITEE);
        WebElement finalElement = webDriver.findElement(By.cssSelector("#6_inp_2"));
        Assert.assertNotNull("New Final is not inserted",finalElement);
        Assert.assertEquals("New Final is inserted but is unproper", finalElement.getText(), NEW_FINAL);






    }

}
