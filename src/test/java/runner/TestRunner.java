package runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;

@CucumberOptions(
    features = "src/test/resources/FeatureFiles",// path to your .feature files
    		 //tags = "@putscenario", // tags from feature file
    glue = "stepDefinition",                    // package containing step definitions
    plugin = {
        "pretty",
        "html:target/cucumber-reports.html","io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
        "json:target/cucumber.json"
    },
    monochrome = true
)
public class TestRunner extends AbstractTestNGCucumberTests {
}