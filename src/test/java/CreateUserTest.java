import io.qameta.allure.junit4.DisplayName;
import models.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static errormessages.UserErrorMessages.*;
import static models.User.getRandomUser;

public class CreateUserTest extends BaseTest {

    private User user;
    private UserSteps userSteps;
    private String email = RandomStringUtils.randomAlphabetic(8).toLowerCase() + "@ya.ru";
    private String password = RandomStringUtils.randomAlphabetic(8);
    private String name = RandomStringUtils.randomAlphabetic(8);

    @Before
    public void startUpSecond() {
        userSteps = new UserSteps();
    }

    @Test
    @DisplayName("Проверка создания уникального пользователя")
    public void createUserResponse200Test() {
        user = getRandomUser();
        userSteps.createUser(user)
                .statusCode(200)
                .and()
                .body("success", Matchers.is(true))
                .body("user.email", Matchers.equalTo(user.getEmail()))
                .body("user.name", Matchers.equalTo(user.getName()))
                .body("accessToken", Matchers.startsWith("Bearer "))
                .body("refreshToken", Matchers.notNullValue());
    }

    @Test
    @DisplayName("Проверка создания пользователя, который уже зарегистрирован")
    public void createTwoIdenticalUsersResponse403Test() {
        user = getRandomUser();
        userSteps.createUser(user);
        userSteps.createUser(user)
                .statusCode(403)
                .and()
                .body("success", Matchers.is(false))
                .and()
                .body("message", Matchers.is(CREATE_USER_REQUEST_WITH_DUPLICATE_LOGIN));
    }

    @Test
    @DisplayName("Проверка создания пользователя без email")
    public void createUserWithoutEmailResponse403Test() {
        user = new User(null, password, name);
        userSteps.createUser(user)
                .statusCode(403)
                .and()
                .body("success", Matchers.is(false))
                .body("message", Matchers.is(CREATE_USER_REQUEST_WITHOUT_EMAIL_OR_PASSWORD_OR_NAME));
    }

    @Test
    @DisplayName("Проверка создания пользователя без пароля")
    public void createUserWithoutPasswordResponse403Test() {
        user = new User(email, null, name);
        userSteps.createUser(user)
                .statusCode(403)
                .and()
                .body("success", Matchers.is(false))
                .body("message", Matchers.is(CREATE_USER_REQUEST_WITHOUT_EMAIL_OR_PASSWORD_OR_NAME));
    }

    @Test
    @DisplayName("Проверка создания пользователя без имени")
    public void createUserWithoutNameResponse403Test() {
        user = new User(email, password, null);
        userSteps.createUser(user)
                .statusCode(403)
                .and()
                .body("success", Matchers.is(false))
                .body("message", Matchers.is(CREATE_USER_REQUEST_WITHOUT_EMAIL_OR_PASSWORD_OR_NAME));
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
