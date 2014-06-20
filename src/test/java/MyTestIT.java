import org.junit.AfterClass;
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
    private static final int EXPECTED_SPAN_LIST_SIZE = 7;
    private static final int EXPECTED_NUMBER_OF_ELEMENTS_IN_COMBO = 6;
    private static final String NEW_LABEL = "Vahe Alex Ratio";
    private static final String NEW_PRE_COMMITEE = "ADEQUATE";
    private static final String NEW_IN_COMMITEE = "STRONG";
    private static final String NEW_FINAL = "VERY_WEAK";
    private static final String EXPECTED_COMBO_VALUE = "WEAK";


    @BeforeClass
    public static void init() {
        webDriver = new FirefoxDriver();
        webDriver.get("localhost:8080/AngularSpringTest/");
    }

    @AfterClass
    public static void finish(){
//        webDriver.close();
    }

//    @Test
    public void test2() throws InterruptedException {

        String title = webDriver.getTitle();//check title
        Assert.assertEquals("Title is unproper","AngularSpring", title);


        TimeUnit.SECONDS.sleep(5);

        //check and click the radio btn(In Commitee)
        WebElement inElement = webDriver.findElement(By.cssSelector("input[value=IN_COMMITEE]"));
        Assert.assertNotNull("In commitee radio btn is not found",inElement);
        inElement.click();

        //check and input in the input elements
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

        //check and click the add btn
        WebElement btnElement = webDriver.findElement(By.cssSelector("#addBtn"));
        Assert.assertNotNull("Add btn is not found",btnElement);
        btnElement.click();


        TimeUnit.SECONDS.sleep(1);

        //get the new row(list of the spans)
        WebElement lastTr = webDriver.findElement(By.cssSelector("tr.ng-scope:last-child"));
        List<WebElement> spanList = lastTr.findElements(By.cssSelector("td>span"));

        //check the list's size
        Assert.assertEquals("The new row spanlist's size is unproper", EXPECTED_SPAN_LIST_SIZE, spanList.size());

        //check all the labels except for In Commitee - it should be replaced by the combobox
        Assert.assertEquals("New Label is unproper", NEW_LABEL, spanList.get(0).getText());
        Assert.assertEquals("New Pre Commitee label is unproper",NEW_PRE_COMMITEE, spanList.get(1).getText());
        Assert.assertEquals("New Final label is unproper",NEW_FINAL, spanList.get(5).getText());

        //check the In Commitee's label invisibility
        Assert.assertEquals("New In Commitee label shouldn't be visible",false, spanList.get(3).isDisplayed());

        //check the In Commitee's combobox
        Assert.assertEquals("The number of elements in In Commitee Combobox is unproper",spanList.get(4).findElements(By.cssSelector("option")).size(), EXPECTED_NUMBER_OF_ELEMENTS_IN_COMBO);
        Assert.assertEquals("The second el in In Commitee Combobox is unproper",spanList.get(4).findElements(By.cssSelector("option")).get(1).getText(), EXPECTED_COMBO_VALUE);

        //check and click the radio btn(Final)
        WebElement finalElement = webDriver.findElement(By.cssSelector("input[value=FINAL]"));
        Assert.assertNotNull("Final radio btn is not found",finalElement);
        finalElement.click();

        // The In Commitee label now should be visible
        Assert.assertEquals("New In Commitee label shouldn't be visible",true, spanList.get(3).isDisplayed());

    }

}
