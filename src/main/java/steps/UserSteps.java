package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.User;

import static io.restassured.RestAssured.given;

public class UserSteps {

    public static final String ENDPOINT_CREATE_USER = "api/auth/register";
    public static final String ENDPOINT_USER_LOGIN = "api/auth/login";
    public static final String ENDPOINT_DELETE_USER = "api/auth/user";

    @Step("Создание пользователя")
    public ValidatableResponse createUser(User user) {
        return given()
                .body(user)
                .when()
                .post(ENDPOINT_CREATE_USER)
                .then();
    }

    @Step("Авторизация пользователя")
    public ValidatableResponse userLogin(User user) {
        return given()
                .body(user)
                .when()
                .post(ENDPOINT_USER_LOGIN)
                .then();
    }

    @Step("Удаление пользователя")
    public void deleteUser(String accessToken) {
        given()
                .body(accessToken)
                .when()
                .delete(ENDPOINT_DELETE_USER)
                .then();
    }
}