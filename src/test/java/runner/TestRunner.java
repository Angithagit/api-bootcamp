package runner;
import io.cucumber.testng.CucumberOptions;
import io.cucumber.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
    features = {"src/test/resources/features"},
    plugin = {"pretty", 
        "html:target/cucumber-reports.html",
        "json:target/cucumber.json"},
    monochrome=true,
    		 dryRun = false,
    		
    		glue= {"stepdefinitions"}) 
 
public class TestRunner extends AbstractTestNGCucumberTests {
}