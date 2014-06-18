import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.util.List;
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
        webDriver.get("localhost:8080/AngularSpringTest/");
    }

//    @AfterClass
    public static void finish(){
        webDriver.close();
    }


    @Test
    public void test() {
        System.out.println("Test 1 is running");
        String title = webDriver.getTitle();
        Assert.assertEquals("AngularSpring", title);
    }

    @Test
    public void test2() throws InterruptedException {
        System.out.println("Test 2 is running");

        TimeUnit.SECONDS.sleep(2);

        WebElement element = webDriver.findElement(By.cssSelector("input[value=IN_COMMITEE]"));
        Assert.assertNotNull(element);
        element.click();
        TimeUnit.SECONDS.sleep(2);


//        List<WebElement> combos = webDriver.findElements(By.cssSelector("select[class^='standard']"));




        List<WebElement> spanElements = webDriver.findElements(By.cssSelector("span[ng-show='labels.defaultLabel===labels[state.id]']"));


        for(WebElement we : spanElements) {
            try {

                we.click();
                System.out.println(we.getText());
            } catch (Exception e) {
//                e.printStackTrace();
            }
            System.out.println(we.getText());
        }

        TimeUnit.SECONDS.sleep(2);

    }


    @Test
    public void test3() throws InterruptedException {

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


        TimeUnit.SECONDS.sleep(1);



        WebElement lastTr = webDriver.findElement(By.cssSelector("tr.ng-scope:last-child"));

        System.out.println(lastTr.getText());
        List<WebElement> spanList = lastTr.findElements(By.cssSelector("td>span"));
        Assert.assertEquals(NEW_LABEL, spanList.get(0).getText());

    }


}
