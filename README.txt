It's the restful mvc web app with:
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <spring-version>4.3.1.RELEASE</spring-version> - AOP, Spring Data JPA, Servlet 3 Code-Based Configuration
                                                         (and in general zero xml config), Spring transactions support
        <hibernate-version>5.1.0.Final</hibernate-version> - with named queries;
        Angular v1.5, JSP, HTML;
        H2 database, H2SQL;
        JUnit + Mockito, Selenium, Jasmine testing,
        Lombok log4j;
        javadoc;
        design patterns: MVC, DAO, defensive copies and immutable objects(at least collections), etc...

0) Install Java 8(at least), Maven, Tomcat 7 or 8 (VM options: '-Dspring.profiles.active=dev'), IDE (DEA is preferable).
1)Build and Test
  1.1) move to the root folder;
  1.2) in order to build project run this in IDE (not maven cause the Servlet 3 Code-Based Configuration is used, hence
       maven can't find the web.xml and becomes angry);
  1.3) in order to run java unit(JUnit) and integration(JUnit) tests type "mvn verify";
  1.4) in order to run integration(Selenium) tests run com.selenium.SeleniumTests;
  1.5) in order to run JS(Jasmine) tests go to src/main/webapp/resources and run the following
       "karma start karma.config.js"
