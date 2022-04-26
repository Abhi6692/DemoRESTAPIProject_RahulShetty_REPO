
import static io.restassured.RestAssured.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.testng.Assert;

import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;
import pojo.Api;
import pojo.GetCourse;
import pojo.WebAutomation;



public class OAuth_2_0__6 {

	public static void main(String[] args) {

		//The Catch is - everytime you have to give this URL fresh from API Contract Document / From Postman
		String url ="https://rahulshettyacademy.com/getCourse.php?code=4%2F0AX4XfWh723JnG7ILsfb_KwwC34C3j7GzJecNKh96q7qg2dgvnHpDWtys_ljsnywwWzLCAA&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none";
		String partialcode=url.split("code=")[1];
		String code=partialcode.split("&scope")[0];
		System.out.println("The Code Extracted from the above URL is : " +code);


		String response =given().
				queryParams("code",code).
				urlEncodingEnabled(false).
				queryParams("client_id", "692183103107-p0m7ent2hk7suguv4vq22hjcfhcr43pj.apps.googleusercontent.com").
				queryParams("client_secret", "erZOWM9g3UtwNRj340YYaK_W").
				queryParams("grant_type", "authorization_code").
				queryParams("scope", "https://www.googleapis.com/auth/userinfo.email").
				queryParams("redirect_uri", "https://rahulshettyacademy.com/getCourse.php").
				when().log().all().
				post("https://www.googleapis.com/oauth2/v4/token").asString();

		System.out.println("The Response is :" + response);

		JsonPath jsonPath = new JsonPath(response);
		String accessToken = jsonPath.getString("access_token"); 
		System.out.println("The Access Token Fetched is : "+accessToken);

		String response_2=given().
				contentType("application/json").
				queryParams("access_token", accessToken).
				when().
				get("https://rahulshettyacademy.com/getCourse.php").asString();

		System.out.println("The Course response JSON is : "+response_2);


		//		--------------------------------------------------------------------------


		//Serialization and Deserialization Concept Begins	

		GetCourse gc = given().
				contentType("application/json").
				queryParams("access_token", accessToken).expect().defaultParser(Parser.JSON).
				when().
				get("https://rahulshettyacademy.com/getCourse.php").as(GetCourse.class);

		System.out.println("LinkedIn URL Fetched from Pojo Response --> " + gc.getLinkedIn());

		//Issue with this approach is the index may change / we need to hardcode the index
		System.out.println("The Price of the course is "+ gc.getCourses().getMobile().get(0).getPrice());

		//Dynamic Approach
		List<Api> apiCourses =  gc.getCourses().getApi();
		for (int i =0 ; i< apiCourses.size(); i++) {

			if (apiCourses.get(i).getCourseTitle().equalsIgnoreCase("Rest Assured Automation using Java")) {

				System.out.println("The Price of the Rest Assured Automation using Java is -->  "+apiCourses.get(i).getPrice());
			}

		}

		//Get all the course title of Web Automation 

		List<WebAutomation> WebAutomationCourses =  gc.getCourses().getWebAutomation();
		for (int i =0 ; i< WebAutomationCourses.size(); i++) {

			System.out.println("The Courses titles  under WebAutomation is "+WebAutomationCourses.get(i).getCourseTitle());



		}

		String expectedArray[] = {"Selenium Webdriver Java", "Cypress","Protractor"};
		ArrayList<String> actualTitlesFetched = new ArrayList<String>();

		List<WebAutomation> WebAutomationCourses_1 =  gc.getCourses().getWebAutomation();

		for (int i =0 ; i< WebAutomationCourses_1.size(); i++) {

			actualTitlesFetched.add(WebAutomationCourses_1.get(i).getCourseTitle());
		}

		Assert.assertTrue(actualTitlesFetched.equals(Arrays.asList(expectedArray)))	;

	}}
