/*
 * Copyright (c) 2016.  Alex Chulkin
 */

package com.selenium;

import com.somecode.domain.Role;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.springframework.test.context.ActiveProfiles;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

import static org.junit.Assert.*;

/**
 * The fairly self-explanatory selenium tests class.
 * @version 1.0
 */

@ActiveProfiles
public class SeleniumTests {
    /**
     * The time to wait after the button click.
     */
    private static final int TIME_TO_WAIT_AFTER_CLICK = 500;

    /**
     * The web driver
     */
    private static WebDriver webDriver;

    /**
     * Runs before each test. Reinitializes the web Driver.
     */
    @Before
    public void beforeEachTest() {
        System.setProperty("webdriver.chrome.driver", "E:\\Java\\ChromeDriver\\chromedriver.exe");
        webDriver = new ChromeDriver();
        webDriver.get("http://localhost:8084");
    }

    /**
     * Runs after each test. Closes the web Driver.
     */
    @After
    public void afterEachTest(){
        Optional.ofNullable(webDriver).ifPresent(
                WebDriver::close
        );
    }

    @Test
    public void testLoginPositiveAndLogout() throws InterruptedException {
        checkLoginPageBeforeLoggingIn();
        performLogin(Role.ADMIN);
        checkLogout();
    }

    @Test
    public void testLoginNegative() throws InterruptedException {
        WebElement usernameInput = webDriver.findElement(By.cssSelector("#username"));
        usernameInput.sendKeys("fake");

        WebElement passwdInput = webDriver.findElement(By.cssSelector("#password"));
        passwdInput.sendKeys("fake");

        WebElement loginBtn = webDriver.findElement(By.cssSelector("#loginBtn"));
        clickButton(loginBtn);

        assertEquals("Unsuccessful authentication URL is improper",
                "http://localhost:8084/" + "#/login",
                webDriver.getCurrentUrl()
        );
        WebElement authError = webDriver.findElement(By.cssSelector("#authError"));
        assertNotNull("Authentication error web element is not found", authError);
        assertTrue("Authentication error web element is not visible", authError.isDisplayed());
    }

    @Test
    public void testComptsPanel() throws InterruptedException {
        performLogin(Role.ADMIN);
        List<WebElement> pktBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a"));
        boolean atLeastOnePktIsLoaded = !pktBtns.isEmpty();

        if (atLeastOnePktIsLoaded){
            removeAllPackets();
        }

        WebElement saveAllBtn = webDriver.findElement(By.id("saveAllBtn"));
        clickButton(saveAllBtn);

        checkComptsPanel(true, true, true, atLeastOnePktIsLoaded);

        WebElement addPktBtn = webDriver.findElement(By.id("addPacketBtn"));
        clickButton(addPktBtn);

        checkComptsPanel(false, true, true, atLeastOnePktIsLoaded);
        WebElement selectBtn = webDriver.findElement(By.cssSelector("div.packet-buttons-div>a:nth-child(1)"));
        clickButton(selectBtn);

        checkComptsPanel(false, false, true, true);

        WebElement addComptBtnEl = webDriver.findElement(By.cssSelector("#addComptBtn"));

        assertFalse("Compt adding button is enabled", addComptBtnEl.isEnabled());

        WebElement newComptLabelEl = webDriver.findElement(By.cssSelector("#newComptLabel"));

        newComptLabelEl.sendKeys("");
        WebElement requiredDiv = webDriver.findElement(By.cssSelector("#required"));
        assertFalse("Compt adding button is enabled for empty label", addComptBtnEl.isEnabled());
        assertNotNull("Error 'You did not enter a label' is not shown", requiredDiv);
        newComptLabelEl.clear();

        int exceededTextLength = 71;
        StringBuilder sb = new StringBuilder();
        IntStream.rangeClosed(1, exceededTextLength).forEach(i -> sb.append("a"));
        newComptLabelEl.sendKeys(sb.toString());
        WebElement maxlengthDiv = webDriver.findElement(By.cssSelector("#maxlength"));
        assertFalse("Compt adding button is enabled for exceeded label length", addComptBtnEl.isEnabled());
        assertNotNull("Error 'Label is too long' is not shown", maxlengthDiv);
        newComptLabelEl.clear();


        String[] forbiddenSymbols = {"{", "-", "@", "я", "\""};
        for (String s : forbiddenSymbols) {
            newComptLabelEl.sendKeys(s);
            WebElement patternDiv = webDriver.findElement(By.cssSelector("#pattern"));
            assertFalse("Compt adding button is enabled for improper label pattern", addComptBtnEl.isEnabled());
            assertNotNull("Error 'Label should contain latin letters, digits, underscore and spaces only'" +
                    " is not shown", patternDiv);
            newComptLabelEl.clear();
        }

        String weirdButProperLabel = " 1_ E o 8 ";
        newComptLabelEl.sendKeys(weirdButProperLabel);
        assertTrue("Compt adding button is not enabled", addComptBtnEl.isEnabled());

        List<WebElement> newComptValsSelects = webDriver.findElements(By.cssSelector("div#newComptVals>select"));
        newComptValsSelects.stream().forEach(sel ->
                assertEquals("Default new compt val is improper", "VERY_WEAK",
                        (new Select(sel)).getFirstSelectedOption().getText()
                )
        );
        newComptLabelEl.sendKeys(weirdButProperLabel);
        clickButton(addComptBtnEl);

        WebElement blacklistDiv = webDriver.findElement(By.cssSelector("#blacklist"));
        assertFalse("Compt adding button is enabled for already used label", addComptBtnEl.isEnabled());
        assertNotNull("Error 'Label is not unique' is not shown", blacklistDiv);

        checkComptsPanel(false, false, false, true);

        List<WebElement> radioBtns = webDriver.findElements(By.cssSelector("input[type]"));
        int radioBtnsSize = radioBtns.size();
        assertEquals("Radio buttons quantity is improper", 3, radioBtnsSize);
        List<WebElement> comptsSpans = webDriver.findElements(By.cssSelector("td>span"));
        assertEquals("Improper number of compts and spans was added after clicking the 'addComptBtn'",
                radioBtnsSize * 2,
                comptsSpans.size()
        );

        for (int i = 0; i < radioBtnsSize; i++) {
            WebElement radioBtn = radioBtns.get(i);
            if (i != 0) {
                assertFalse("Improper radio button is selected", radioBtn.isSelected());
                clickButton(radioBtn, 100);
            } else {
                assertTrue("Proper radio button is not selected", radioBtn.isSelected());
            }
            for (int j = 0; j < radioBtnsSize; j++) {
                WebElement ngHideSpan = comptsSpans.get(j * 2);
                WebElement ngShowSpan = comptsSpans.get(j * 2 + 1);
                if (j != i) {
                    assertTrue("'ng-hide' span is not visible", ngHideSpan.isDisplayed());
                    assertFalse("'ng-show' span is visible", ngShowSpan.isDisplayed());
                } else {
                    assertNotNull("'ng-hide' span hasn't got the corresponding attr",
                            ngHideSpan.getAttribute("ng-hide")
                    );
                    assertFalse("'ng-hide' span is visible", ngHideSpan.isDisplayed());
                    assertNotNull("'ng-show' span hasn't got the corresponding attr",
                            ngShowSpan.getAttribute("ng-show")
                    );
                    assertTrue("'ng-show' span is not visible", ngShowSpan.isDisplayed());
                }
            }
        }
        List <WebElement> deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertFalse("delete Compt btn does not exist", deleteComptBtns.isEmpty());
        assertTrue("More than one 'delete Compt' buttons", deleteComptBtns.size() == 1);
        clickButton(deleteComptBtns.get(0));
        checkComptsPanel(false, false, true, true);
        List<WebElement> paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertTrue("pagination buttons do exist", paginationBtns.isEmpty());

        int numOfComptsToAdd = 11;
        for (int i = 1; i <= numOfComptsToAdd; i++) {
            newComptLabelEl.sendKeys(Integer.toString(i));
            assertTrue("Compt adding button is not enabled", addComptBtnEl.isEnabled());
            clickButton(addComptBtnEl, 100);
            paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
            if (i < numOfComptsToAdd) {
                assertTrue("More or less than one pagination button", paginationBtns.size() == 1);
            } else {
                assertTrue("More or less than two pagination buttons", paginationBtns.size() == 2);
            }
        }
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than ten compts on the first(full) page", deleteComptBtns.size() == 10);
        clickButton(paginationBtns.get(1));
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than one compt on the second page", deleteComptBtns.size() == 1);
        clickButton(deleteComptBtns.get(0));
        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertTrue("More or less than one pagination button", paginationBtns.size() == 1);
        List<WebElement> inputSelects = webDriver.findElements(By.cssSelector(".special"));
        Select inputSelect1 = new Select(inputSelects.get(0));
        String[] textForSelect = new String[3];
        textForSelect[0] = "STRONG";
        inputSelect1.selectByVisibleText(textForSelect[0]);
        Select inputSelect2 = new Select(inputSelects.get(1));
        textForSelect[1] = "MODERATE";
        textForSelect[2] = "VERY_WEAK";
        inputSelect2.selectByVisibleText(textForSelect[1]);
        newComptLabelEl.sendKeys("newInputSelectVals");
        clickButton(addComptBtnEl);
        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 2", 2, paginationBtns.size());

        clickButton(paginationBtns.get(1));

        List<WebElement> comptsSpans2 = webDriver.findElements(By.cssSelector("td>span"));
        assertEquals("Improper number of compts and spans was added after clicking the 'addComptBtn'",
                radioBtnsSize * 2,
                comptsSpans2.size()
        );

        for (int j = 0; j < radioBtnsSize; j++) {
            WebElement ngHideSpan = comptsSpans2.get(j * 2);
            WebElement ngShowSpan = comptsSpans2.get(j * 2 + 1);
            if (j != 2) {
                assertTrue("'ng-hide' span is not visible", ngHideSpan.isDisplayed());
                assertEquals("improper span text", textForSelect[j], ngHideSpan.getText());
                assertFalse("'ng-show' span is visible", ngShowSpan.isDisplayed());
            } else {
                assertNotNull("'ng-hide' span hasn't got the corresponding attr", ngHideSpan.getAttribute("ng-hide"));
                assertFalse("'ng-hide' span is visible", ngHideSpan.isDisplayed());
                assertNotNull("'ng-show' span hasn't got the corresponding attr", ngShowSpan.getAttribute("ng-show"));
                assertTrue("'ng-show' span is not visible", ngShowSpan.isDisplayed());
                Select spanSelect = new Select(ngShowSpan.findElement(By.cssSelector(".standard")));
                assertEquals("Default new compt val is improper",
                        textForSelect[j],
                        spanSelect.getFirstSelectedOption().getText()
                );
            }
        }

        clickButton(saveAllBtn);


        WebElement reloadAllBtn = webDriver.findElement(By.id("reloadAllBtn"));
        clickButton(reloadAllBtn);
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than ten compts on the first(full) page", deleteComptBtns.size() == 10);
        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 2", 2, paginationBtns.size());
        clickButton(paginationBtns.get(1));
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than one compt on the second page", deleteComptBtns.size() == 1);

        newComptLabelEl = webDriver.findElement(By.cssSelector("#newComptLabel"));
        newComptLabelEl.sendKeys("thisNewComptWontBeSaved");
        addComptBtnEl = webDriver.findElement(By.cssSelector("#addComptBtn"));
        clickButton(addComptBtnEl);
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than 2 compts on the second page", deleteComptBtns.size() == 2);

        reloadAllBtn = webDriver.findElement(By.id("reloadAllBtn"));
        clickButton(reloadAllBtn);

        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 2", 2, paginationBtns.size());
        clickButton(paginationBtns.get(1));

        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than one compt on the second page", deleteComptBtns.size() == 1);

        clickButton(deleteComptBtns.get(0));

        reloadAllBtn = webDriver.findElement(By.id("reloadAllBtn"));
        clickButton(reloadAllBtn);

        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 2", 2, paginationBtns.size());
        clickButton(paginationBtns.get(1));

        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than one compt on the second page", deleteComptBtns.size() == 1);

        clickButton(deleteComptBtns.get(0));

        saveAllBtn = webDriver.findElement(By.id("saveAllBtn"));
        clickButton(saveAllBtn);

        reloadAllBtn = webDriver.findElement(By.id("reloadAllBtn"));
        clickButton(reloadAllBtn);

        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 1", 1, paginationBtns.size());
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than ten compt on the first page", deleteComptBtns.size() == 10);

        newComptLabelEl = webDriver.findElement(By.cssSelector("#newComptLabel"));
        newComptLabelEl.sendKeys("thisNewComptWillBeSaved");
        addComptBtnEl = webDriver.findElement(By.cssSelector("#addComptBtn"));
        clickButton(addComptBtnEl);

        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 2", 2, paginationBtns.size());
        clickButton(paginationBtns.get(1));

        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than 1 compt in the second page", deleteComptBtns.size() == 1);
        List<WebElement> saveBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a:nth-child(2)"));
        assertEquals("save pkt buttons quantity is != 1", 1, saveBtns.size());
        clickButton(saveBtns.get(0));

        List<WebElement> reloadBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a:nth-child(3)"));
        assertEquals("reload pkt buttons quantity is != 1", 1, reloadBtns.size());
        clickButton(reloadBtns.get(0));

        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 2", 2, paginationBtns.size());
        clickButton(paginationBtns.get(1));

        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than 1 compt in the second page", deleteComptBtns.size() == 1);
        reloadAllBtn = webDriver.findElement(By.id("reloadAllBtn"));
        clickButton(reloadAllBtn);

        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 2", 2, paginationBtns.size());
        clickButton(paginationBtns.get(1));

        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than 1 compt in the second page", deleteComptBtns.size() == 1);
        clickButton(deleteComptBtns.get(0));

        saveBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a:nth-child(2)"));
        assertEquals("save pkt buttons quantity is != 1", 1, saveBtns.size());
        clickButton(saveBtns.get(0));

        reloadBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a:nth-child(3)"));
        assertEquals("reload pkt buttons quantity is != 1", 1, reloadBtns.size());
        clickButton(reloadBtns.get(0));

        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 1", 1, paginationBtns.size());
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than 10 compts in the first page", deleteComptBtns.size() == 10);
        reloadAllBtn = webDriver.findElement(By.id("reloadAllBtn"));
        clickButton(reloadAllBtn);

        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 1", 1, paginationBtns.size());
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than 10 compts in the first page", deleteComptBtns.size() == 10);

        newComptLabelEl = webDriver.findElement(By.cssSelector("#newComptLabel"));
        newComptLabelEl.sendKeys("thisNewComptWontBeSavedAgain");
        addComptBtnEl = webDriver.findElement(By.cssSelector("#addComptBtn"));
        clickButton(addComptBtnEl);

        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 2", 2, paginationBtns.size());
        clickButton(paginationBtns.get(1));
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than 1 compt in the second page", deleteComptBtns.size() == 1);

        reloadBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a:nth-child(3)"));
        assertEquals("reload pkt buttons quantity is != 1", 1, reloadBtns.size());
        clickButton(reloadBtns.get(0));

        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 1", 1, paginationBtns.size());
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than 10 compts in the first page", deleteComptBtns.size() == 10);
        reloadAllBtn = webDriver.findElement(By.id("reloadAllBtn"));
        clickButton(reloadAllBtn);

        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 1", 1, paginationBtns.size());
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than 10 compts in the first page", deleteComptBtns.size() == 10);

        clickButton(deleteComptBtns.get(9));

        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than 9 compts in the first page", deleteComptBtns.size() == 9);

        reloadBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a:nth-child(3)"));
        assertEquals("reload pkt buttons quantity is != 1", 1, reloadBtns.size());
        clickButton(reloadBtns.get(0));

        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 1", 1, paginationBtns.size());
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than 10 compts in the first page", deleteComptBtns.size() == 10);
        reloadAllBtn = webDriver.findElement(By.id("reloadAllBtn"));
        clickButton(reloadAllBtn);

        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 1", 1, paginationBtns.size());
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than 10 compts in the first page", deleteComptBtns.size() == 10);

        newComptLabelEl = webDriver.findElement(By.cssSelector("#newComptLabel"));
        newComptLabelEl.sendKeys("thisNewComptWontBeSavedAgain");
        addComptBtnEl = webDriver.findElement(By.cssSelector("#addComptBtn"));
        clickButton(addComptBtnEl);
        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 2", 2, paginationBtns.size());
        clickButton(paginationBtns.get(1));
        deleteComptBtns = webDriver.findElements(By.cssSelector("td>button"));
        assertTrue("More or less than 1 compt in the second page", deleteComptBtns.size() == 1);

        inputSelects = webDriver.findElements(By.cssSelector(".special"));

        inputSelect1 = new Select(inputSelects.get(0));
        textForSelect[0] = inputSelect1.getFirstSelectedOption().getText();
        inputSelect2 = new Select(inputSelects.get(1));
        textForSelect[1] = inputSelect2.getFirstSelectedOption().getText();
        Select inputSelect3 = new Select(inputSelects.get(2));
        textForSelect[2] = inputSelect3.getFirstSelectedOption().getText();
        assertEquals("The new compt select #1 has changed", "VERY_WEAK", textForSelect[0]);
        assertEquals("The new compt select #2 has changed", "VERY_WEAK", textForSelect[1]);
        assertEquals("The new compt select #3 has changed", "VERY_WEAK", textForSelect[2]);

        saveBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a:nth-child(2)"));
        assertEquals("save pkt buttons quantity is != 1", 1, saveBtns.size());
        clickButton(saveBtns.get(0));

        comptsSpans2 = webDriver.findElements(By.cssSelector("td>span"));
        WebElement ngShowSpan = comptsSpans2.get(5);
        Select spanSelect = new Select(ngShowSpan.findElement(By.cssSelector(".standard")));
        String newSelectVal = "VERY_STRONG";
        spanSelect.selectByVisibleText(newSelectVal);

        reloadBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a:nth-child(3)"));
        assertEquals("reload pkt buttons quantity is != 1", 1, reloadBtns.size());
        clickButton(reloadBtns.get(0));

        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 2", 2, paginationBtns.size());
        clickButton(paginationBtns.get(1));


        comptsSpans2 = webDriver.findElements(By.cssSelector("td>span"));

        for (int j = 0; j < radioBtnsSize; j++) {
            WebElement ngHideSpan = comptsSpans2.get(j * 2);
            ngShowSpan = comptsSpans2.get(j * 2 + 1);
            if (j != 2) {
                assertEquals("improper span text", textForSelect[j], ngHideSpan.getText());
            } else {
                spanSelect = new Select(ngShowSpan.findElement(By.cssSelector(".standard")));
                assertEquals("Default new compt val is improper",
                        textForSelect[j],
                        spanSelect.getFirstSelectedOption().getText()
                );
            }
        }

        comptsSpans2 = webDriver.findElements(By.cssSelector("td>span"));
        ngShowSpan = comptsSpans2.get(5);
        spanSelect = new Select(ngShowSpan.findElement(By.cssSelector(".standard")));
        spanSelect.selectByVisibleText(newSelectVal);

        reloadAllBtn = webDriver.findElement(By.id("reloadAllBtn"));
        clickButton(reloadAllBtn);


        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 2", 2, paginationBtns.size());
        clickButton(paginationBtns.get(1));


        comptsSpans2 = webDriver.findElements(By.cssSelector("td>span"));
        for (int j = 0; j < radioBtnsSize; j++) {
            if (j != 2) {
                WebElement ngHideSpan = comptsSpans2.get(j * 2);
                assertEquals("improper span text", textForSelect[j], ngHideSpan.getText());
            } else {
                ngShowSpan = comptsSpans2.get(j * 2 + 1);
                spanSelect = new Select(ngShowSpan.findElement(By.cssSelector(".standard")));
                assertEquals("Default new compt val is improper",
                        textForSelect[j],
                        spanSelect.getFirstSelectedOption().getText()
                );
            }
        }

        comptsSpans2 = webDriver.findElements(By.cssSelector("td>span"));
        ngShowSpan = comptsSpans2.get(5);
        spanSelect = new Select(ngShowSpan.findElement(By.cssSelector(".standard")));
        spanSelect.selectByVisibleText(newSelectVal);

        saveBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a:nth-child(2)"));
        assertEquals("save pkt buttons quantity is != 1", 1, saveBtns.size());
        clickButton(saveBtns.get(0));


        reloadBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a:nth-child(3)"));
        assertEquals("reload pkt buttons quantity is != 1", 1, reloadBtns.size());
        clickButton(reloadBtns.get(0));


        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 2", 2, paginationBtns.size());
        clickButton(paginationBtns.get(1));


        comptsSpans2 = webDriver.findElements(By.cssSelector("td>span"));

        for (int j = 0; j < radioBtnsSize - 1; j++) {
            WebElement ngHideSpan = comptsSpans2.get(j * 2);
            assertEquals("improper span text", textForSelect[j], ngHideSpan.getText());
        }

        ngShowSpan = comptsSpans2.get(5);
        spanSelect = new Select(ngShowSpan.findElement(By.cssSelector(".standard")));

        assertEquals("Default new compt val is improper", newSelectVal, spanSelect.getFirstSelectedOption().getText());

        comptsSpans2 = webDriver.findElements(By.cssSelector("td>span"));
        ngShowSpan = comptsSpans2.get(5);
        spanSelect = new Select(ngShowSpan.findElement(By.cssSelector(".standard")));
        String newSelectVal2 = "ADEQUATE";
        spanSelect.selectByVisibleText(newSelectVal2);

        saveAllBtn = webDriver.findElement(By.id("saveAllBtn"));
        clickButton(saveAllBtn);

        reloadAllBtn = webDriver.findElement(By.id("reloadAllBtn"));
        clickButton(reloadAllBtn);


        paginationBtns = webDriver.findElements(By.cssSelector("div#pagination>a"));
        assertEquals("pagination buttons quantity is != 2", 2, paginationBtns.size());
        clickButton(paginationBtns.get(1));

        comptsSpans2 = webDriver.findElements(By.cssSelector("td>span"));
        for (int j = 0; j < radioBtnsSize - 1; j++) {
            WebElement ngHideSpan = comptsSpans2.get(j * 2);
            assertEquals("improper span text", textForSelect[j], ngHideSpan.getText());
        }
        ngShowSpan = comptsSpans2.get(5);
        spanSelect = new Select(ngShowSpan.findElement(By.cssSelector(".standard")));
        assertEquals("Default new compt val is improper", newSelectVal2, spanSelect.getFirstSelectedOption().getText());
    }

    @Test
    public void testUsersLimitedRights() throws InterruptedException {
        performLogin(Role.USER);
        List<WebElement> pktBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a"));
        boolean atLeastOnePktIsLoaded = !pktBtns.isEmpty();

        if (atLeastOnePktIsLoaded){
            removeAllPackets();
        }

        WebElement saveAllBtn = webDriver.findElement(By.id("saveAllBtn"));
        assertEquals("true", saveAllBtn.getAttribute("disabled"));

        checkComptsPanel(true, true, true, atLeastOnePktIsLoaded);

        WebElement addPktBtn = webDriver.findElement(By.id("addPacketBtn"));
        clickButton(addPktBtn);
        WebElement saveBtn = webDriver.findElement(By.cssSelector("div.packet-buttons-div>a:nth-child(2)"));
        assertEquals("true", saveBtn.getAttribute("disabled"));
    }

    @Test
    public void testPacketsPanel() throws InterruptedException {
        performLogin(Role.ADMIN);
        removeAllPackets();
        WebElement addPktBtn = webDriver.findElement(By.id("addPacketBtn"));
        final int numOfPkts = 100;
        for (int i = 0; i < numOfPkts; i++) {
            addPktBtn.click();
        }
        TimeUnit.MILLISECONDS.sleep(TIME_TO_WAIT_AFTER_CLICK);

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
        clickButton(pktBtns.get(0));
        assertEquals("Number of new packet buttons is improper", pktBtnsSize, numOfPkts * 4);
        for (int i = 0; i < pktBtnsSize; i++) {
            int num = i % 4;
            WebElement btn = pktBtns.get(i);
            assertTrue(btn.isDisplayed());
            assertFalse(btn.isSelected());
            switch (num) {
                case 0:
                    String lbl = btn.getText();
                    String prefix = pktBtnsMap.get(num);
                    assertTrue(pktBtnsImproperLblsMessagesMap.get(num),
                            lbl.startsWith(prefix) && prefix.length() < lbl.length()
                    );
                    int pktId = getPktId(lbl);
                    if (pktId != -1) {
                        assertEquals(pktBtnCssClassesMap.get(num),
                                getPacketCssClass(i == 0, pktId),
                                btn.getAttribute("class")
                        );
                    }
                    break;
                case 2:
                    assertEquals("true",btn.getAttribute("disabled"));
                    clickButton(pktBtns.get(i - 1));
                    assertNull(btn.getAttribute("disabled"));
                    assertFalse(btn.isSelected());
                    assertEquals(pktBtnsImproperLblsMessagesMap.get(num), pktBtnsMap.get(num), btn.getText());
                    assertEquals(pktBtnsImproperCssClassesMessages.get(num),
                            pktBtnCssClassesMap.get(num),
                            btn.getAttribute("class")
                    );
                    break;
                case 1: case 3:
                    assertNull(btn.getAttribute("disabled"));
                    assertFalse(btn.isSelected());
                    assertEquals(pktBtnsImproperLblsMessagesMap.get(num), pktBtnsMap.get(num), btn.getText());
                    assertEquals(pktBtnsImproperCssClassesMessages.get(num),
                            pktBtnCssClassesMap.get(num),
                            btn.getAttribute("class")
                    );
                    break;
            }
        }
        removeAllPackets();
        WebElement saveAllBtn = webDriver.findElement(By.id("saveAllBtn"));
        clickButton(saveAllBtn);
    }

    private void clickButton(WebElement btn) throws InterruptedException {
        btn.click();
        TimeUnit.MILLISECONDS.sleep(TIME_TO_WAIT_AFTER_CLICK);
    }

    private void clickButton(WebElement btn, int time) throws InterruptedException {
        btn.click();
        TimeUnit.MILLISECONDS.sleep(time);
    }

    private void checkLoginPageBeforeLoggingIn() throws InterruptedException {
        String title = webDriver.getTitle();
        assertEquals("Title is improper", "Packet App Administration", title);
        assertEquals("Login URL is improper", "http://localhost:8084/" + "#/login", webDriver.getCurrentUrl());

        WebElement authError = webDriver.findElement(By.id("authError"));
        assertNotNull("Authentication error web element is not found", authError);
        assertFalse("Authentication error web element is visible", authError.isDisplayed());
        assertEquals("Authentication error web element has improper css class",
                "alert alert-danger ng-binding ng-hide",
                authError.getAttribute("class")
        );

        WebElement loginInfo = webDriver.findElement(By.id("loginInfo"));
        assertNotNull("Login info web element is not found", loginInfo);
        assertTrue("Login info web element is not visible", loginInfo.isDisplayed());
        assertEquals("Login info web element has improper css class",
                "alert alert-info",
                loginInfo.getAttribute("class")
        );

        WebElement timeout = webDriver.findElement(By.id("timeout"));
        assertNotNull("Timeout web element is not found", timeout);
        assertFalse("Timeout web element is visible", timeout.isDisplayed());
        assertEquals("Timeout web element has improper css class",
                "alert alert-warning ng-hide",
                timeout.getAttribute("class")
        );
    }

    private void removeAllPackets() throws InterruptedException {
        List<WebElement> pktBtns = webDriver.findElements(By.cssSelector("div.packet-buttons-div>a:nth-child(4n)"));
        for (WebElement btn : pktBtns) {
            btn.click();
        }
        TimeUnit.MILLISECONDS.sleep(TIME_TO_WAIT_AFTER_CLICK);
        checkComptsPanel(true, false, false, false);
    }

    private void checkLogout() throws InterruptedException {
        WebElement logoutBtn = webDriver.findElement(By.cssSelector("#logoutBtn"));
        clickButton(logoutBtn);
        checkLoginPageBeforeLoggingIn();
    }

    private void checkComptsPanel(boolean noPackets, boolean noPktSelected, boolean isSelectedPktEmpty, 
                                  boolean isPktAlreadySelectedAtLeastOnce) {
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
        if (!noPackets && noPktSelected && isPktAlreadySelectedAtLeastOnce) {
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

    private void performLogin(Role role) throws InterruptedException {
        WebElement usernameInput = webDriver.findElement(By.cssSelector("#username"));
        assertNotNull("Username input is not found", usernameInput);
        usernameInput.sendKeys(role == Role.ADMIN ? "ADMIN" : "USER");

        WebElement passwdInput = webDriver.findElement(By.cssSelector("#password"));
        assertNotNull("Password input is not found", passwdInput);
        passwdInput.sendKeys(role == Role.ADMIN ? "ADMIN" : "USER");

        WebElement loginBtn = webDriver.findElement(By.cssSelector("#loginBtn"));
        assertNotNull("Login button is not found", loginBtn);
        clickButton(loginBtn);

        assertEquals("Successful authentication URL is improper",
                "http://localhost:8084/" + "#/main",
                webDriver.getCurrentUrl()
        );

        String title = webDriver.getTitle();//check title
        assertEquals("Title is improper", "Packet App Administration", title);

        WebElement greetingsDiv = webDriver.findElement(By.cssSelector("#greetings"));
        String adminGreetings = role == Role.ADMIN ? "Hello, ADMIN" : "Hello, USER";
        assertEquals(adminGreetings, greetingsDiv.getText());
    }

    private String getPacketCssClass(boolean selected, int pktId) {
        String result = "ng-binding btn-md";

        if (pktId < 10) {
            result += " narrow-packet-caption";
        } else if (pktId < 100) {
            result += " wide-packet-caption";
        } else {
            result += " super-wide-packet-caption";
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
