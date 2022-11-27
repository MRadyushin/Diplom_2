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
    public void updatingUserWithAuth() throws InterruptedException {
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


        String newEmail = "newradyushin" + getUniqueId() + "@yandex.com";
        radyushin.setEmail(newEmail);

        Response response2 = updateUser(radyushin, authResponse.getAccessToken());

        assertEquals("Code should be 200!", SC_OK, response2.getStatusCode());
        assertTrue("User update should have succeeded!", response2.as(PatchResponse.class).isSuccess());
        assertEquals("This field should have been updated!", newEmail, response2.as(PatchResponse.class).getUser().getEmail());


        String newPassword = "newPraktikumAPI";
        radyushin.setPassword(newPassword);

        Response response3 = updateUser(radyushin, authResponse.getAccessToken());

        assertEquals("Code should be 200!", SC_OK, response3.getStatusCode());
        assertTrue("User update should have succeeded!", response3.as(PatchResponse.class).isSuccess());

        Response authWithNewPassword = authorizeUser(radyushin);

        assertEquals("Should have authorized with new password!",
                SC_OK,
                authWithNewPassword.getStatusCode());
    }

    @Test
    public void updatingUserWithoutAuth() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        UserRequest updatedRadyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        postNewUser(radyushin);

        String newName = "Maksim Radyushin";
        updatedRadyushin.setName(newName);

        Response response1 = updateUser(updatedRadyushin, "");

        assertEquals("Code should be 401!", SC_UNAUTHORIZED, response1.getStatusCode());


        String newEmail = "newRadyushin" + getUniqueId() + "@yandex.com";
        updatedRadyushin.setEmail(newEmail);

        Response response2 = updateUser(updatedRadyushin, "");

        assertEquals("Code should be 401!", SC_UNAUTHORIZED, response2.getStatusCode());


        String newPassword = "newPraktikumAPI";
        updatedRadyushin.setPassword(newPassword);

        Response response3 = updateUser(updatedRadyushin, "");

        assertEquals("Code should be 401!", SC_UNAUTHORIZED, response3.getStatusCode());
    }
}