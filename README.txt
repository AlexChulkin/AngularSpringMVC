0) Install Java 8(at least), Maven, Tomcat 7 or 8 (VM options: '-Dspring.profiles.active=dev'), IDE (DEA is preferable).
1)Build and Test
  1.1) move to the root folder;
  1.2) in order to build project run this in IDE (not maven cause the Servlet 3 Code-Based Configuration is used, hence
       maven can't find the web.xml and becomes angry);
  1.3) in order to run java unit(JUnit) and integration(JUnit) tests type "mvn verify";
  1.4) in order to run integration(Selenium) tests run com.selenium.SeleniumTests;
  1.5) in order to run JS(Jasmine) tests go to src/main/webapp/resources and run the following
       "karma start karma.config.js"
