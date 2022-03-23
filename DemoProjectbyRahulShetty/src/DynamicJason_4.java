
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import files.ReUsableMethods;
import files.payload;


public class DynamicJason_4 {



	@Test(dataProvider="BooksData")
	public void addBook(String isbn,String aisle) {

		RestAssured.baseURI="http://216.10.245.166";
		String resp=given().log().all().

				header("Content-Type","application/json").

				body(payload.Addbook(isbn,aisle)).

				when().

				post("/Library/Addbook.php").

				then().assertThat().statusCode(200).

				extract().response().asString();

		JsonPath js= ReUsableMethods.rawToJson(resp);

		String id=js.get("ID");

		System.out.println(id);
		
		//Delete The Book 
		System.out.println("Deleting the added book with the ID --> "+id );
		
		given().log().all().


		body("{\r\n"
				+ " \r\n"
				+ "\"ID\" : \""+id+"\"\r\n"
				+ " \r\n"
				+ "} ").

		when().

		delete("/Library/DeleteBook.php").

		then().assertThat().statusCode(200).body("msg", equalTo("book is successfully deleted"));

		
		

	}

	@DataProvider(name="BooksData")
	public  Object[][] getData(){

	//array=collection of elements

	//multidimensional array= collection of arrays
		
		return new Object[][] {{"awsa","1234"},{"wapo","4321"}, {"rony","6675"}};
	}

	
}




