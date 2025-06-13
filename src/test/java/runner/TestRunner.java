package runner;
import io.cucumber.testng.CucumberOptions;

import org.testng.annotations.BeforeSuite;

import io.cucumber.testng.AbstractTestNGCucumberTests;

@CucumberOptions(
    features = {"src/test/resources/features"},
    plugin = {"pretty", 
        "html:target/cucumber-reports.html",
        "json:target/cucumber.json",
        "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
		"com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:"},
    monochrome=true,
    		 dryRun = false,
    		
    		glue= {"stepdefinitions"}) 
 

public class TestRunner extends AbstractTestNGCucumberTests {
	  @BeforeSuite
	    public void setAllureResultDirectory() {
	        System.setProperty("allure.results.directory", "target/allure-results");
}
}