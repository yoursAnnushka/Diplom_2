import io.qameta.allure.junit4.DisplayName;
import java.lang.Exception;
import models.Order;
import models.User;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.OrderSteps;
import steps.UserSteps;

import java.util.ArrayList;

import static models.User.getRandomUser;

public class CreateOrderTest extends BaseTest {

    public static final String CREATE_ORDER_REQUEST_WITHOUT_INGREDIENTS = "Ingredient ids must be provided";

    private OrderSteps orderSteps;
    private User user;
    private UserSteps userSteps;

    @Before
    public void startUpSecond() {
        orderSteps = new OrderSteps();
        userSteps = new UserSteps();
        user = getRandomUser();
        userSteps.createUser(user);
    }

    @Test
    @DisplayName("Проверка создания заказа с ингредиентами с авторизацией пользователя")
    public void createOrderWithIngredientsWithAuthorizationResponse200Test() {
        String accessToken = userSteps.userLogin(user)
                .extract().body().path("accessToken");
        String accessTokenWithoutBearer = accessToken.replace("Bearer ", "");
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("61c0c5a71d1f82001bdaaa78");
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        Order order = new Order(ingredients);
        orderSteps.createOrderWithAuthorization(order, accessTokenWithoutBearer)
                .statusCode(200)
                .and()
                .body("name", Matchers.notNullValue())
                .body("order.number", Matchers.notNullValue())
                .body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Проверка создания заказа с ингредиентами без авторизации пользователя")
    public void createOrderWithIngredientsWithoutAuthorizationResponse200Test() {
         ArrayList<String> ingredients = new ArrayList<>();
         ingredients.add("61c0c5a71d1f82001bdaaa78");
         ingredients.add("61c0c5a71d1f82001bdaaa6d");
         Order order = new Order(ingredients);
         orderSteps.createOrderWithoutAuthorization(order)
                 .statusCode(200)
                 .and()
                 .body("name", Matchers.notNullValue())
                 .body("order.number", Matchers.notNullValue())
                 .body("success", Matchers.is(true));
    }

    @Test
    @DisplayName("Проверка создания заказа без ингредиентов с авторизацией пользователя")
    public void createOrderWithoutIngredientsWithAuthorizationResponse400Test() {
        String accessToken = userSteps.userLogin(user)
                .extract().body().path("accessToken");
        String accessTokenWithoutBearer = accessToken.replace("Bearer ", "");
        Order order = new Order(null);
        orderSteps.createOrderWithAuthorization(order, accessTokenWithoutBearer)
                .statusCode(400)
                .and()
                .body("success", Matchers.is(false))
                .body("message", Matchers.is(CREATE_ORDER_REQUEST_WITHOUT_INGREDIENTS));
    }

    @Test
    @DisplayName("Проверка создания заказа без ингредиентов без авторизации пользователя")
    public void createOrderWithoutIngredientsWithoutAuthorizationResponse400Test() {
        Order order = new Order(null);
        orderSteps.createOrderWithoutAuthorization(order)
                .statusCode(400)
                .and()
                .body("success", Matchers.is(false))
                .body("message", Matchers.is(CREATE_ORDER_REQUEST_WITHOUT_INGREDIENTS));
    }

    @Test
    @DisplayName("Проверка создания заказа с неверным хешем ингредиентов с авторизацией пользователя")
    public void createOrderWithInvalidIngredientsHashWithAuthorizationResponse500Test() {
        String accessToken = userSteps.userLogin(user)
                .extract().body().path("accessToken");
        String accessTokenWithoutBearer = accessToken.replace("Bearer ", "");
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("черстваябулка");
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        Order order = new Order(ingredients);
        orderSteps.createOrderWithAuthorization(order, accessTokenWithoutBearer)
                .statusCode(500);
    }

    @Test
    @DisplayName("Проверка создания заказа с неверным хешем ингредиентов без авторизации пользователя")
    public void createOrderWithInvalidIngredientsHashWithoutAuthorizationResponse500Test() {
        ArrayList<String> ingredients = new ArrayList<>();
        ingredients.add("черстваябулка");
        ingredients.add("61c0c5a71d1f82001bdaaa6d");
        Order order = new Order(ingredients);
        orderSteps.createOrderWithoutAuthorization(order)
                .statusCode(500);
    }

    @After
    public void tearDown() {
        try {
            String accessToken = userSteps.userLogin(user)
                    .extract().body().path("accessToken");

            if (accessToken != null) {
                String accessTokenWithoutBearer = accessToken.replace("Bearer ", "");
                userSteps.deleteUser(accessTokenWithoutBearer);
            }
        } catch (Exception exception) {
            System.out.println("Пользователь не был удалён");
        }
    }
}
