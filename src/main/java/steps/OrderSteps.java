package steps;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import models.Order;

import static io.restassured.RestAssured.given;

public class OrderSteps {

    public static final String ENDPOINT_CREATE_ORDER = "api/orders";

    @Step("Создание заказа c авторизацией")
    public ValidatableResponse createOrderWithAuthorization(Order order, String accessToken) {
        return given()
                .body(order)
                .auth().oauth2(accessToken)
                .when()
                .post(ENDPOINT_CREATE_ORDER)
                .then();
    }

    @Step("Создание заказа без авторизации")
    public ValidatableResponse createOrderWithoutAuthorization(Order order) {
        return given()
                .body(order) // массив ингредиентов
                .when()
                .post(ENDPOINT_CREATE_ORDER)
                .then();
    }
}
