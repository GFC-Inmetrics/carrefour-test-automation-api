package runners;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions(
        features = "classpath:features",
        glue = {"steps"},
        plugin = {"pretty",
                "html:target/cucumber-report/html/cucumber-report.html",
                "json:target/cucumber-report.json",
                "junit:target/cucumber.xml"
        },
        monochrome = true
)
public class TestRunner {
}


