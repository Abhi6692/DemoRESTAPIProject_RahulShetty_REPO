
import static io.restassured.RestAssured.*;
import java.io.File;
import org.testng.Assert;
import io.restassured.RestAssured;
import io.restassured.filter.session.SessionFilter;
import io.restassured.path.json.JsonPath;

public class JiraTest_5 {

	public static void main(String[] args) {

		RestAssured.baseURI="http://localhost:8080";
		//1. Login Scenario
		//Session Filter for Authentication 
		/*
		 * A session filter can be used record the session id returned from the server
		 * as well as automatically apply this session id in subsequent requests
		 */

		/*
		 *  relaxedHTTPSValidation() to handle HTTPS Certification.
		 *   This means thatyou'll trust all hosts regardless if the SSL certificate is invalid.
		 */		

		SessionFilter session=new SessionFilter();

		String response=given().relaxedHTTPSValidation().header("Content-Type","application/json").body("{\r\n" +
				"    \"username\": \"Abhi6692\",\r\n" +
				"    \"password\": \"Rony#6692\"\r\n" +
				"}").log().all().filter(session).when().post("/rest/auth/1/session").then().log().all().extract().response().asString();

		System.out.println("The String Response from Login Scenarios is ---> " + response);

		String comment ="Hi How are you?";

		//2. Add comment
		String addCommentResponse = given().pathParam("key", "10000").log().all().header("Content-Type","application/json").body("{\r\n" +
				"    \"body\": \""+comment+"\",\r\n" +
				"    \"visibility\": {\r\n" +
				"        \"type\": \"role\",\r\n" +
				"        \"value\": \"Administrators\"\r\n" +
				"    }\r\n" +
				"}").filter(session).when().post("/rest/api/2/issue/{key}/comment").then().log().all()
				.assertThat().statusCode(201).extract().response().asString();

		System.out.println("The String Response from Add Comment Scenarios is ---> " + addCommentResponse);

		JsonPath js=new JsonPath(addCommentResponse);
		String commentId= js.getString("id");


		//3. Add Attachment
		//Use of multipart method for sending attachments
		given().header("X-Atlassian-Token","no-check").filter(session).pathParam("key", "10000")
		.header("Content-Type","multipart/form-data")
		.multiPart("file",new File("JiraAttachmentFIle.txt")).when().
		post("rest/api/2/issue/{key}/attachments").then().log().all().assertThat().statusCode(200);

		//4. Get Issue
		String issueDetailsResponse=given().filter(session).pathParam("key", "10000")
				.queryParam("fields", "comment")
				.log().all().when().get("/rest/api/2/issue/{key}").then()
				.log().all().extract().response().asString();

		System.out.println("The String Response from Get Issue Scenarios is ---> "+issueDetailsResponse);


		JsonPath js1 =new JsonPath(issueDetailsResponse);
		int commentsCount=js1.getInt("fields.comment.comments.size()");

		for(int i=0;i<commentsCount;i++)
		{
			String commentIdIssue =js1.get("fields.comment.comments["+i+"].id").toString();
			if (commentIdIssue.equalsIgnoreCase(commentId))
			{
				String message= js1.get("fields.comment.comments["+i+"].body").toString();
				System.out.println(message);
				Assert.assertEquals(message, comment);
				System.out.println("If this method execution reaches this line ---> The test has passed");
			}
		}

	}

}
