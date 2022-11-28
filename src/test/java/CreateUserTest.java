import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import model.UserRequest;
import org.junit.Test;
import config.BaseTest;
import java.util.concurrent.TimeUnit;

import static org.apache.http.HttpStatus.*;
import static org.junit.Assert.assertEquals;

public class CreateUserTest extends BaseTest {

    @Test
    @DisplayName("Создание пользователя")
    @Description("Ожидаем успешное создание пользователя")
    public void creatingUserTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");

        postNewUser(radyushin);
    }

    @Test
    @DisplayName("Создать пользователя, который уже зарегистрирован")
    @Description("Ожидаем ошибку создания пользователя ")
    public void duplicateCreatingUserTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        String uniqueId = getUniqueId();
        UserRequest radyushin = new UserRequest("m.radyushin" + uniqueId + "@yandex.ru", "PraktikumAPI", "radyushin");
        usersToDelete.add(radyushin);

        Response response = createUser(radyushin);

        assertEquals("User should have been created!",
                SC_OK,
                response.getStatusCode());

        Response failedResponse = createUser(radyushin);

        assertEquals("Should have received status code 403 because user already exists!",
                SC_FORBIDDEN,
                failedResponse.getStatusCode());
    }

    @Test
    @DisplayName("создать пользователя и не заполнить одно из обязательных полей")
    @Description("Ожидаем ошибку создания пользователя ")
    public void creatingUserWithoutRequiredFieldTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        UserRequest user1 = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", null);
        UserRequest user2 = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", null, "radyushin");
        UserRequest user3 = new UserRequest(null, "PraktikumAPI", "radyushin");

        Response failedResponse2 = createUser(user1);
        assertEquals("Should have received status code 403 because there are missing required fields!",
                SC_FORBIDDEN,
                failedResponse2.getStatusCode());
        TimeUnit.SECONDS.sleep(3);
        Response failedResponse1 = createUser(user2);
        assertEquals("Should have received status code 403 because there are missing required fields!",
                SC_FORBIDDEN,
                failedResponse1.getStatusCode());
        TimeUnit.SECONDS.sleep(3);
        Response failedResponse = createUser(user3);
        assertEquals("Should have received status code 403 because there are missing required fields!",
                SC_FORBIDDEN,
                failedResponse.getStatusCode());
    }
}