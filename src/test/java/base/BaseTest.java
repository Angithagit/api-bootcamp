package base;

import org.testng.annotations.BeforeClass;

import config.ConfigReader;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;

public class BaseTest {

	protected RequestSpecification authrequest;
	
	ConfigReader config=new ConfigReader();
	@BeforeClass
	public void setup()
	{
	   RestAssured.baseURI=config.getProperty("baseURI");
	   authrequest=RestAssured.given().auth()
			   .basic(
					   config.getProperty("username"),
					   config.getProperty("password")
					  )
			   .header("Content-Type",
					   config.getProperty("content-type")
					  );
	}
	
}
