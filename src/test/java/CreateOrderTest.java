import io.restassured.response.Response;
import model.AuthenticationResponse;
import model.UserRequest;
import org.junit.Test;
import config.BaseTest;
import config.OrderData;

import static org.apache.http.HttpStatus.*;

import static org.junit.Assert.assertEquals;

public class CreateOrderTest extends BaseTest {

    @Test
    public void createOrderWithAuthUser() {
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        postNewUser(radyushin);
        AuthenticationResponse authResponse = authorizeUser(radyushin).as(AuthenticationResponse.class);

        Response response = createOrder(OrderData.getCorrectOrder(), authResponse.getAccessToken());

        assertEquals("Order should have been created!", SC_OK, response.getStatusCode());
    }

    @Test
    public void createOrderWithUnauthUser() {
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        postNewUser(radyushin);

        Response response = createOrder(OrderData.getCorrectOrder(), "");

        assertEquals("Order should have been created!", SC_OK, response.getStatusCode());
    }

    @Test
    public void createOrderWithoutIngredients() {
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        postNewUser(radyushin);
        AuthenticationResponse authResponse = authorizeUser(radyushin).as(AuthenticationResponse.class);

        Response response = createOrder(OrderData.getEmptyOrder(), authResponse.getAccessToken());

        assertEquals("Order without ingredients cannot be created!", SC_BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void createOrderWithWrongIngredients() {
        UserRequest radyushin = new UserRequest("m.radyushin" + getUniqueId() + "@yandex.ru", "PraktikumAPI", "radyushin");
        postNewUser(radyushin);
        AuthenticationResponse authResponse = authorizeUser(radyushin).as(AuthenticationResponse.class);

        Response response = createOrder(OrderData.getIncorrectOrder(), authResponse.getAccessToken());

        assertEquals("Order with wrong ingredients cannot be created!", SC_INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}