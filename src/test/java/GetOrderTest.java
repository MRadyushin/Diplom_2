import io.restassured.response.Response;
import model.AuthenticationResponse;
import model.UserRequest;
import org.junit.Test;
import config.BaseTest;

import java.util.concurrent.TimeUnit;

import static org.apache.http.HttpStatus.*;

import static org.junit.Assert.assertEquals;

public class GetOrderTest extends BaseTest {

    @Test
    public void getOrdersForAuthorizedUserTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        postNewUser(radyushin);
        AuthenticationResponse authResponseModel = authorizeUser(radyushin).as(AuthenticationResponse.class);

        Response orders = getOrders(authResponseModel.getAccessToken());

        assertEquals("Request should be successful!", SC_OK, orders.statusCode());
    }

    @Test
    public void getOrdersForUnauthorizedUserTest() throws InterruptedException {
        TimeUnit.SECONDS.sleep(3);
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        postNewUser(radyushin);

        Response orders = getOrders("");

        assertEquals("Request should have failed!", SC_UNAUTHORIZED, orders.statusCode());
    }
}