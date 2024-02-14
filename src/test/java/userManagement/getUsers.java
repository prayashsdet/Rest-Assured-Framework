package userManagement;

import io.restassured.response.Response;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import static io.restassured.RestAssured.given;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.assertEquals;

public class getUsers {
    @Test
    public void getUserData() {
        given().when().get("https://reqres.in/api/users?page=2").then().assertThat().statusCode(200);
    }

    @Test
    public void validateGetResponseBody() {
        given().when().get("https://reqres.in/api/users?page=2")
                .then().assertThat().statusCode(200)
                .body(not(isEmptyString()))
                .body("page", equalTo(2))
                .body("total_pages", equalTo(2));
    }


    @Test
    public void validateGetResponseJson() {
        // Sending GET request and extracting the response
        Response response = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .extract()
                .response();

        // Asserting on the JSON content of the response
        assertThat(response.jsonPath().getList("data.email"), hasItems("michael.lawson@reqres.in"));
    }

    @Test
    public void validateResponseHasSize() {
        Response response = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .extract()
                .response();

        // Asserting on the JSON content of the response
        assertThat(response.jsonPath().getList("data"), hasSize(6));
    }

    @Test
    public void validateListContainOrder() {

        Response response = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .extract()
                .response();

        // Asserting on the JSON content of the response
        List<String> expectedEmails = Arrays.asList(
                "michael.lawson@reqres.in",
                "lindsay.ferguson@reqres.in",
                "tobias.funke@reqres.in",
                "byron.fields@reqres.in",
                "george.edwards@reqres.in",
                "rachel.howell@reqres.in"
        );
        assertThat(response.jsonPath().getList("data.email"), contains(expectedEmails.toArray(new String[0])));
        //response.then().assertThat().body("data.email", equalTo(expectedEmails));
        // response.then().assertThat().body("data.email", contains(expectedEmails.toArray()));
//        response.then().assertThat().body("data.email", hasSize(expectedEmails.size()))
//                .body("data.email", hasItems(expectedEmails.toArray()));
    }


    @Test
    public void validateallFields() {

        Response response = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .extract()
                .response();
//Assert that the first user in the list has correct value
        response.then().body("data[0].id", is(7));
        response.then().body("data[0].email", is("michael.lawson@reqres.in"));
        response.then().body("data[0].first_name", is("Michael"));
        response.then().body("data[0].last_name", is("Lawson"));
        response.then().body("data[0].avatar", is("https://reqres.in/img/faces/7-image.jpg"));


    }

    @Test
    public void queryParamValidation() {
        Response resp =
                given().queryParam("page", 2)
                        .when()
                        .get("https://reqres.in/api/users");


        int actualStatusCode = resp.statusCode();
        assertEquals(actualStatusCode, 200);
    }


    @Test
    public void multipleQueryParamValidation() {
        Response resp =
                given().queryParam("page", 2)
                        .queryParam("per_page", 3)
                        .when()
                        .get("https://reqres.in/api/users")
                        .then()
                        .statusCode(200)
                        .extract()
                        .response();


    }
}



