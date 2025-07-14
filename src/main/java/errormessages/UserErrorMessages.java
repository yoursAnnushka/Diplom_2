package errormessages;

public class UserErrorMessages {
    public static final String CREATE_USER_REQUEST_WITH_DUPLICATE_LOGIN = "User already exists";
    public static final String CREATE_USER_REQUEST_WITHOUT_EMAIL_OR_PASSWORD_OR_NAME = "Email, password and name are required fields";
    public static final String USER_LOGIN_REQUEST_WITHOUT_CORRECT_LOGIN_OR_PASSWORD = "email or password are incorrect";
}
