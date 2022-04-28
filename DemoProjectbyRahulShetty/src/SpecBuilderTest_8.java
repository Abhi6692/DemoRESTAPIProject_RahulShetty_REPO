import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import pojo.AddPlaceBody_AddPlaceAPI;
import pojo.Location_AddPlaceAPI;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.util.ArrayList;
import java.util.List;

import org.testng.Assert;

import files.payload;
import io.restassured.RestAssured;

public class SpecBuilderTest_8 {

public static void main(String[] args) {
	
	RestAssured.baseURI= "https://rahulshettyacademy.com";
	
	AddPlaceBody_AddPlaceAPI ap = new AddPlaceBody_AddPlaceAPI ();
	ap.setAccuracy(50);
	ap.setName("Frontline house");
	ap.setPhone_number("(+91) 983 893 3937");
	ap.setAddress("29, side layout, cohen 09");
	ap.setWebsite("http://google.com");
	ap.setLanguage("French-IN");
	List<String> myList = new ArrayList<String>();
	myList.add("shoe park");
	myList.add("shop");
	ap.setTypes(myList);
	
	Location_AddPlaceAPI loc = new Location_AddPlaceAPI();
	loc.setLat(-38.383494);
	loc.setLng(33.427362);
	ap.setLocation(loc);
	
	
	
	RequestSpecification reqSpec = new RequestSpecBuilder().setBaseUri("https://rahulshettyacademy.com").
			addQueryParam("key", "qaclick123").setContentType(ContentType.JSON).build();
	
	ResponseSpecification resSpec = new ResponseSpecBuilder().expectStatusCode(200).build();
	
	String response=given().log().all().spec(reqSpec).body(ap).
	when()
		.post("maps/api/place/add/json")
	.then()
		.assertThat().spec(resSpec).extract().response().asString();
	
	System.out.println("The Response is --> " + response);
	
}

	
	
}
//{
//"location": {
//  "lat": -38.383494,
//  "lng": 33.427362
//},
//"accuracy": 50,
//"name": "Frontline house",
//"phone_number": "(+91) 983 893 3937",
//"address": "29, side layout, cohen 09",
//"types": [
//  "shoe park",
//  "shop"
//],
//"website": "http://google.com",
//"language": "French-IN"
//}