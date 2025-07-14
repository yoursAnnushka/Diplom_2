import io.qameta.allure.junit4.DisplayName;
import models.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import steps.UserSteps;

import static errormessages.UserErrorMessages.USER_LOGIN_REQUEST_WITHOUT_CORRECT_LOGIN_OR_PASSWORD;
import static models.User.getRandomUser;

public class UserLoginTest extends BaseTest {

    private User user;
    private UserSteps userSteps;

    @Before
    public void startUpSecond() {
        userSteps = new UserSteps();
        user = getRandomUser();
        userSteps.createUser(user);
    }

    @Test
    @DisplayName("Проверка авторизации пользователя")
    public void userLoginWithValidCredentialsResponse200Test() {
        userSteps.userLogin(user.getEmail(), user.getPassword())
                .statusCode(200)
                .and()
                .body("success", Matchers.is(true))
                .body("accessToken", Matchers.startsWith("Bearer "))
                .body("refreshToken", Matchers.notNullValue())
                .body("user.email", Matchers.equalTo(user.getEmail()))
                .body("user.name", Matchers.equalTo(user.getName()));
    }

    @Test
    @DisplayName("Проверка авторизации пользователя без логина")
    public void userLoginWithoutLoginResponse401Test() {
        userSteps.userLogin("", user.getPassword())
                .statusCode(401)
                .and()
                .body("success", Matchers.is(false))
                .body("message", Matchers.is(USER_LOGIN_REQUEST_WITHOUT_CORRECT_LOGIN_OR_PASSWORD));
    }

    @Test
    @DisplayName("Проверка авторизации пользователя без пароля")
    public void userLoginWithoutPasswordResponse401Test() {
        userSteps.userLogin(user.getEmail(), "")
                .statusCode(401)
                .and()
                .body("success", Matchers.is(false))
                .body("message", Matchers.is(USER_LOGIN_REQUEST_WITHOUT_CORRECT_LOGIN_OR_PASSWORD));
    }

    @Test
    @DisplayName("Проверка авторизации с невалидным логином")
    public void userLoginWithInvalidLoginResponse401Test() {
        String email = RandomStringUtils.randomAlphabetic(6).toLowerCase() + "@ya.ru";
        userSteps.userLogin(email, user.getPassword())
                .statusCode(401)
                .and()
                .body("success", Matchers.is(false))
                .body("message", Matchers.is(USER_LOGIN_REQUEST_WITHOUT_CORRECT_LOGIN_OR_PASSWORD));
    }

    @Test
    @DisplayName("Проверка авторизации с невалидным паролем")
    public void userLoginWithInvalidPasswordResponse401Test() {
        String password = RandomStringUtils.randomAlphabetic(6);
        userSteps.userLogin(user.getEmail(), password)
                .statusCode(401)
                .and()
                .body("success", Matchers.is(false))
                .body("message", Matchers.is(USER_LOGIN_REQUEST_WITHOUT_CORRECT_LOGIN_OR_PASSWORD));
    }

    @Test
    @DisplayName("Проверка авторизации несуществующего пользователя")
    public void userLoginOfNonExistentUserResponse401Test() {
        String email = RandomStringUtils.randomAlphabetic(9).toLowerCase() + "@ya.ru";
        String password = RandomStringUtils.randomAlphabetic(6);
        userSteps.userLogin(email, password)
                .statusCode(401)
                .and()
                .body("success", Matchers.is(false))
                .body("message", Matchers.is(USER_LOGIN_REQUEST_WITHOUT_CORRECT_LOGIN_OR_PASSWORD));
    }

    @After
    public void tearDown() {
        try {
            String accessToken = userSteps.userLogin(user.getEmail(), user.getPassword())
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
