import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

public class MyTestIT {

    private static WebDriver webDriver;

    @BeforeClass
    public static void init() {
        System.setProperty("webdriver.chrome.driver", "E:\\Java\\ChromeDriver\\chromedriver.exe");
        webDriver = new ChromeDriver();
        webDriver.get("http://localhost:8084");
    }

    @AfterClass
    public static void finish(){
        webDriver.close();
    }

    @Test
    public void testLoginPositive() throws InterruptedException {
        String title = webDriver.getTitle();
        assertEquals("Title is improper", "Packet App Administration", title);
        assertEquals("Login URL is improper", "http://localhost:8084/" + "#/login", webDriver.getCurrentUrl());

        WebElement authError = webDriver.findElement(By.id("authError"));
        assertNotNull("Authentication error web element is not found", authError);
        assertFalse("Authentication error web element is visible", authError.isDisplayed());
        assertEquals("Authentication error web element has improper css class", "alert alert-danger ng-binding ng-hide", authError.getAttribute("class"));

        WebElement loginInfo = webDriver.findElement(By.id("loginInfo"));
        assertNotNull("Login info web element is not found", loginInfo);
        assertTrue("Login info web element is not visible", loginInfo.isDisplayed());
        assertEquals("Login info web element has improper css class", "alert alert-info", loginInfo.getAttribute("class"));

        WebElement timeout = webDriver.findElement(By.id("timeout"));
        assertNotNull("Timeout web element is not found", timeout);
        assertFalse("Timeout web element is visible", timeout.isDisplayed());
        assertEquals("Timeout web element has improper css class", "alert alert-warning ng-hide", timeout.getAttribute("class"));

        performLogin();
    }

    @Test
    public void testLoginNegative() throws InterruptedException {
        WebElement usernameInput = webDriver.findElement(By.cssSelector("#username"));
        usernameInput.sendKeys("fake");

        WebElement passwdInput = webDriver.findElement(By.cssSelector("#password"));
        passwdInput.sendKeys("fake");

        WebElement loginBtn = webDriver.findElement(By.cssSelector("#loginBtn"));
        loginBtn.click();
        TimeUnit.MILLISECONDS.sleep(500);

        assertEquals("Unsuccessful authentication URL is improper", "http://localhost:8084/" + "#/login", webDriver.getCurrentUrl());
        WebElement authError = webDriver.findElement(By.cssSelector("#authError"));
        assertNotNull("Authentication error web element is not found", authError);
        assertTrue("Authentication error web element is not visible", authError.isDisplayed());
    }

    @Test
    public void testAddCompts() throws InterruptedException {
        performLogin();

        removeAllPackets();
        checkRightColumn(true, true, true);

        WebElement addPktBtn = webDriver.findElement(By.id("addPacketBtn"));
        addPktBtn.click();
        checkRightColumn(false, true, true);
        WebElement selectBtn = webDriver.findElement(By.cssSelector("div.packet-buttons-div>a:nth-child(1)"));
        selectBtn.click();
        checkRightColumn(false, false, true);

        WebElement addComptBtnEl = webDriver.findElement(By.cssSelector("#addComptBtn"));

        assertFalse("Compt adding button is enabled", addComptBtnEl.isEnabled());

        WebElement newComptLabelEl = webDriver.findElement(By.cssSelector("#newComptLabel"));

        newComptLabelEl.sendKeys("1");
        assertTrue("Compt adding button is not enabled", addComptBtnEl.isEnabled());

        List<WebElement> newComptValsSelects = webDriver.findElements(By.cssSelector("div#newComptVals>select"));
        newComptValsSelects.stream().forEach(sel -> {
            assertEquals("Default new compt val is improper", "VERY_WEAK", (new Select(sel)).getFirstSelectedOption().getText());
        });
        addComptBtnEl.click();

        checkRightColumn(false, false, false);

        List<WebElement> radioBtns = webDriver.findElements(By.cssSelector("input[type]"));
        int radioBtnsSize = radioBtns.size();
        assertEquals("Radio buttons quantity is improper", 3, radioBtnsSize);
        List<WebElement> comptsSpans = webDriver.findElements(By.cssSelector("td>span"));
        assertEquals("Improper number of compts and spans was added after clicking the 'addComptBtn'", radioBtnsSize * 2, comptsSpans.size());

        IntStream.range(0, radioBtnsSize).boxed().forEach(i -> {
            WebElement radioBtn = radioBtns.get(i);
            if (i != 0) {
                assertFalse("Radio button is  selected", radioBtn.isSelected());
                radioBtn.click();
            } else {
                assertTrue("Radio button is not selected", radioBtn.isSelected());
            }
            for (int j = 0; j < radioBtnsSize; j++) {
                WebElement ngHideSpan = comptsSpans.get(j * 2);
                WebElement ngShowSpan = comptsSpans.get(j * 2 + 1);
                if (j != i) {
                    assertTrue("'ng-hide' span is not visible", ngHideSpan.isDisplayed());
                    assertFalse("'ng-show' span is visible", ngShowSpan.isDisplayed());
                } else {
                    assertNotNull("'ng-hide' span hasn't got the corresponding attr", ngHideSpan.getAttribute("ng-hide"));
                    assertFalse("'ng-hide' span is visible", ngHideSpan.isDisplayed());
                    assertNotNull("'ng-show' span hasn't got the corresponding attr", ngShowSpan.getAttribute("ng-show"));
                    assertTrue("'ng-show' span is not visible", ngShowSpan.isDisplayed());
                }
            }
        });
        List<WebElement> deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertFalse("delete Compt btn does not exist", deleteComptBtns.isEmpty());
        assertTrue("More than one 'delete Compt' buttons", deleteComptBtns.size() == 1);
        deleteComptBtns.get(0).click();
        checkRightColumn(false, false, true);
        List<WebElement> paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertTrue("pagination buttons do exist", paginationBtns.isEmpty());


        int numOfComptsToAdd = 11;
        for (int i = 1; i <= numOfComptsToAdd; i++) {
            newComptLabelEl.sendKeys(Integer.toString(i));
            assertTrue("Compt adding button is not enabled", addComptBtnEl.isEnabled());
            addComptBtnEl.click();
            paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
            if (i < 11) {
                assertTrue("More or less than one pagination button", paginationBtns.size() == 1);
            } else {
                assertTrue("More or less than two pagination buttons", paginationBtns.size() == 2);
            }
        }
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than ten compts on the first(full) page", deleteComptBtns.size() == 10);
        paginationBtns.get(1).click();
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than one compt on the second page", deleteComptBtns.size() == 1);
        deleteComptBtns.get(0).click();
        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertTrue("More or less than one pagination button", paginationBtns.size() == 1);


    }

    @Test
    public void testPacketsPanel() throws InterruptedException {
        performLogin();
        removeAllPackets();
        WebElement addPktBtn = webDriver.findElement(By.id("addPacketBtn"));
        final int numOfPkts = 20;
        for (int i = 0; i < numOfPkts; i++) {
            addPktBtn.click();
        }

        Map<Integer, String> pktBtnsMap = new HashMap<>();
        pktBtnsMap.put(0, "Packet#");
        pktBtnsMap.put(1, "Save");
        pktBtnsMap.put(2, "Reload");
        pktBtnsMap.put(3, "Del");

        Map<Integer, String> pktBtnsImproperLblsMessagesMap = new HashMap<>();
        pktBtnsImproperLblsMessagesMap.put(0, "Packet selection btn lbl format is improper");
        pktBtnsImproperLblsMessagesMap.put(1, "Packet save btn label is improper");
        pktBtnsImproperLblsMessagesMap.put(2, "Packet reload btn label is improper");
        pktBtnsImproperLblsMessagesMap.put(3, "Packet delete btn label is improper");

        Map<Integer, String> pktBtnCssClassesMap = new HashMap<>();
        pktBtnCssClassesMap.put(1, "btn btn-md btn-success packet-button");
        pktBtnCssClassesMap.put(2, "btn btn-md btn-warning packet-button");
        pktBtnCssClassesMap.put(3, "btn btn-md btn-danger packet-button");

        Map<Integer, String> pktBtnsImproperCssClassesMessages = new HashMap<>();
        pktBtnsImproperCssClassesMessages.put(0, "Packet selection btn css class is improper");
        pktBtnsImproperCssClassesMessages.put(1, "Packet save btn css class is improper");
        pktBtnsImproperCssClassesMessages.put(2, "Packet reload btn css class is improper");
        pktBtnsImproperCssClassesMessages.put(3, "Packet delete btn css class is improper");

        List<WebElement> pktBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a"));
        int pktBtnsSize = pktBtns.size();
        pktBtns.get(0).click();
        assertEquals("Number of new packet buttons is improper", pktBtnsSize, numOfPkts * 4);
        IntStream.range(0, pktBtnsSize).boxed().forEach(i -> {
            int num = i % 4;
            WebElement btn = pktBtns.get(i);
            switch (num) {
                case 0:
                    String lbl = btn.getText();
                    String prefix = pktBtnsMap.get(num);
                    assertTrue(pktBtnsImproperLblsMessagesMap.get(num), lbl.startsWith(prefix) && prefix.length() < lbl.length());
                    int pktId = getPktId(lbl);
                    if (pktId != -1) {
                        assertEquals(pktBtnCssClassesMap.get(num), getPacketCssClass(i == 0, pktId), btn.getAttribute("class"));
                    }
                    break;
                default:
                    assertEquals(pktBtnsImproperLblsMessagesMap.get(num), pktBtnsMap.get(num), btn.getText());
                    assertEquals(pktBtnsImproperCssClassesMessages.get(num), pktBtnCssClassesMap.get(num), btn.getAttribute("class"));
                    break;
            }
        });
    }

    private void removeAllPackets() throws InterruptedException {
        List<WebElement> pktBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a:nth-child(4n)"));
        for (WebElement btn : pktBtns) {
            btn.click();
        }
        checkRightColumn(true, false, false);
    }

    private void checkRightColumn(boolean noPackets, boolean noPktSelected, boolean isSelectedPktEmpty) {
        WebElement noPacketsEl = webDriver.findElement(By.cssSelector("#noPackets"));
        assertNotNull("'Not loaded packets info' div is not existing", noPacketsEl);
        if (noPackets) {
            assertTrue("'Not loaded packets info' div is not visible", noPacketsEl.isDisplayed());
        } else {
            assertFalse("'Not loaded packets info' div is visible", noPacketsEl.isDisplayed());
        }

        WebElement rightColumnDiv = webDriver.findElement(By.cssSelector("#rightColumn"));
        assertNotNull("Right column (compts) div is not existing", rightColumnDiv);
        if (noPackets || noPktSelected) {
            assertFalse("Right column (compts) div is visible", rightColumnDiv.isDisplayed());
        } else {
            assertTrue("Right column (compts) div is not visible", rightColumnDiv.isDisplayed());
        }

        WebElement noPktSelectedDiv = webDriver.findElement(By.cssSelector("#noPktSelected"));
        assertNotNull("'No packet selected' div is not existing", noPktSelectedDiv);
        if (!noPackets && noPktSelected) {
            assertTrue("'No packet selected' div is not visible", noPktSelectedDiv.isDisplayed());
        } else {
            assertFalse("'No packet selected' div is visible", noPktSelectedDiv.isDisplayed());
        }

        WebElement emptyPacketSelectedDiv = webDriver.findElement(By.cssSelector("#emptyPkt"));
        assertNotNull("'Empty packet selected' div is not existing", emptyPacketSelectedDiv);
        if (!noPackets && !noPktSelected && isSelectedPktEmpty) {
            assertTrue("'Empty packet selected' div is not visible", emptyPacketSelectedDiv.isDisplayed());
        } else {
            assertFalse("'Empty packet selected' div is visible", emptyPacketSelectedDiv.isDisplayed());
        }

        List<WebElement> comptsSpans = webDriver.findElements(By.cssSelector("td>span"));
        if (!noPackets && !noPktSelected && !isSelectedPktEmpty) {
            assertFalse("No compts found ", comptsSpans.isEmpty());
        } else {
            assertTrue("Compts found ", comptsSpans.isEmpty());
        }

    }

    private void performLogin() throws InterruptedException {
        WebElement usernameInput = webDriver.findElement(By.cssSelector("#username"));
        assertNotNull("Username input is not found", usernameInput);
        usernameInput.sendKeys("ADMIN");

        WebElement passwdInput = webDriver.findElement(By.cssSelector("#password"));
        assertNotNull("Password input is not found", passwdInput);
        passwdInput.sendKeys("ADMIN");

        WebElement loginBtn = webDriver.findElement(By.cssSelector("#loginBtn"));
        assertNotNull("Login button is not found", loginBtn);
        loginBtn.click();
        TimeUnit.MILLISECONDS.sleep(500);

        assertEquals("Successful authentication URL is improper", "http://localhost:8084/" + "#/main", webDriver.getCurrentUrl());

        String title = webDriver.getTitle();//check title
        assertEquals("Title is improper", "Packet App Administration", title);
    }

    private String getPacketCssClass(boolean selected, int pktId) {
        String result = "ng-binding btn-md";

        if (pktId < 10) {
            result += " narrow-packet-selection";
        } else {
            result += " wide-packet-selection";
        }

        if (selected) {
            result += " btn-primary";
        }

        return result;
    }

    private int getPktId(String lbl) {
        int result = -1;
        String num = lbl.substring(lbl.indexOf("#") + 1);
        try {
            result = Integer.parseInt(num);
        } catch (NumberFormatException e) {
            assertTrue("Packet selection btn lbl format is improper", false);
        }
        return result;
    }
}
