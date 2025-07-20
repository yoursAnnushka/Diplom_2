import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import org.junit.Before;

public class BaseTest {

    @Before
    public void startUpFirst() {
        RestAssured.requestSpecification = new RequestSpecBuilder()
                .setBaseUri("https://stellarburgers.nomoreparties.site/")
                .setContentType(ContentType.JSON)
                .build();
    }
}
