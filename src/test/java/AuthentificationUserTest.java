import model.AuthenticationResponse;
import model.UserRequest;

import java.util.concurrent.TimeUnit;
import org.junit.Test;

import config.BaseTest;

import io.restassured.response.Response;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;

import static org.apache.http.HttpStatus.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class AuthentificationUserTest extends BaseTest {

    @Test
    @DisplayName("Авторизация пользователя с позитивными данными")
    @Description("Ожидаем авторизацию пользователя \"success\": true, код 200")
    public void authExistingUserTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(5);
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        postNewUser(radyushin);

        Response response = authorizeUser(radyushin);

        assertEquals("Code should be 200!", SC_OK, response.getStatusCode());
        assertTrue("User auth should have succeeded!", response.as(AuthenticationResponse.class).isSuccess());
    }

 @Test
 @DisplayName("Авторизация пользователя с неверным логином")
 @Description("Ожидаем ошибку авторизации  \"message\": \"email or password are incorrect")
public void authExistingUserWrongTest() throws InterruptedException {
     TimeUnit.SECONDS.sleep(3);
    UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@mail.ru", "PraktikumAPI", "radyushin");
    postNewUser(radyushin);

    Response response = authorizeUser(new UserRequest("FailedRadyushin@yandex.ru", "FailedPassword", "radyushin"));

    assertEquals("User auth should have failed because of wrong credentials!",
            SC_UNAUTHORIZED,
            response.getStatusCode());
}
}