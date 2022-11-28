import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.AuthenticationResponse;
import model.PatchResponse;
import model.UserRequest;
import org.junit.Test;
import config.BaseTest;
import java.util.concurrent.TimeUnit;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class UpdateUserTest extends BaseTest {

    @Test
    @DisplayName("Изменение имени авторизованного пользователя")
    @Description("Ожидаем успешное изменение данных пользователя")
    public void updatingUserWithAuthNewName() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        postNewUser(radyushin);
        AuthenticationResponse authResponse = authorizeUser(radyushin).as(AuthenticationResponse.class);

        String newName = "Maksim Radyushin";
        radyushin.setName(newName);

        Response response1 = updateUser(radyushin, authResponse.getAccessToken());

        assertEquals("Code should be 200!", SC_OK, response1.getStatusCode());
        assertTrue("User update should have succeeded!", response1.as(PatchResponse.class).isSuccess());
        assertEquals("This field should have been updated!", newName, response1.as(PatchResponse.class).getUser().getName());

    }


    @Test
    @DisplayName("Изменение эмейла авторизованного пользователя")
    @Description("Ожидаем успешное изменение данных пользователя")
    public void updatingUserWithAuthNewEmail() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        postNewUser(radyushin);
        AuthenticationResponse authResponse = authorizeUser(radyushin).as(AuthenticationResponse.class);

        String newEmail = "newradyushin" + getUniqueId() + "@yandex.com";
        radyushin.setEmail(newEmail);

        Response response2 = updateUser(radyushin, authResponse.getAccessToken());

        assertEquals("Code should be 200!", SC_OK, response2.getStatusCode());
        assertTrue("User update should have succeeded!", response2.as(PatchResponse.class).isSuccess());
        assertEquals("This field should have been updated!", newEmail, response2.as(PatchResponse.class).getUser().getEmail());

    }

    @Test
    @DisplayName("Изменение пароля авторизованного пользователя")
    @Description("Ожидаем успешное изменение данных пользователя")
    public void updatingUserWithAuthNewPassword() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        postNewUser(radyushin);
        AuthenticationResponse authResponse = authorizeUser(radyushin).as(AuthenticationResponse.class);

        String newPassword = "newPraktikumAPI";
        radyushin.setPassword(newPassword);

        Response response3 = updateUser(radyushin, authResponse.getAccessToken());
        Response authWithNewPassword = authorizeUser(radyushin);

        assertEquals("Code should be 200!", SC_OK, response3.getStatusCode());
        assertTrue("User update should have succeeded!", response3.as(PatchResponse.class).isSuccess());
        assertEquals("Should have authorized with new password!",
                SC_OK,
                authWithNewPassword.getStatusCode());
    }

    @Test
    @DisplayName("Изменение имени не авторизованного пользователя")
    @Description("Ожидаем ошибку изменение данных пользователя")
    public void updatingUserWithoutAuthNewName() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        UserRequest updatedRadyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        postNewUser(radyushin);

        String newName = "Maksim Radyushin";
        updatedRadyushin.setName(newName);

        Response response1 = updateUser(updatedRadyushin, "");

        assertEquals("Code should be 401!", SC_UNAUTHORIZED, response1.getStatusCode());
    }

    @Test
    @DisplayName("Изменение эмейла не авторизованного пользователя")
    @Description("Ожидаем ошибку изменение данных пользователя")
    public void updatingUserWithoutAuthNewEmail() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        UserRequest updatedRadyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        postNewUser(radyushin);

        String newEmail = "newRadyushin" + getUniqueId() + "@yandex.com";
        updatedRadyushin.setEmail(newEmail);

        Response response2 = updateUser(updatedRadyushin, "");

        assertEquals("Code should be 401!", SC_UNAUTHORIZED, response2.getStatusCode());
    }


    @Test
    @DisplayName("Изменение пароля не авторизованного пользователя")
    @Description("Ожидаем ошибку изменение данных пользователя")
    public void updatingUserWithoutAuthNewPassword() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        UserRequest updatedRadyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        postNewUser(radyushin);

        String newPassword = "newPraktikumAPI";
        updatedRadyushin.setPassword(newPassword);

        Response response3 = updateUser(updatedRadyushin, "");

        assertEquals("Code should be 401!", SC_UNAUTHORIZED, response3.getStatusCode());
    }
}