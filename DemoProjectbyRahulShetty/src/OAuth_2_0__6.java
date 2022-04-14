
import static io.restassured.RestAssured.*;
import io.restassured.parsing.Parser;
import io.restassured.path.json.JsonPath;



public class OAuth_2_0__6 {

	public static void main(String[] args) {
		
		//The Catch is - everytime you have to give this URL fresh from API Contract Document / From Postman
		String url ="https://rahulshettyacademy.com/getCourse.php?code=4%2F0AX4XfWiMdmOKkPnREUrXZqXRZ8GLfHc7K_zDIjVDcuOiZTMNMJtHv6v8LQMJjYqgKzKhwQ&scope=email+openid+https%3A%2F%2Fwww.googleapis.com%2Fauth%2Fuserinfo.email&authuser=0&prompt=none";
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
		
	}

}
