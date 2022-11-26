package config;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import model.*;
import org.junit.After;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;

public class BaseTest {
    static {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site/api";
    }

    static final String USER_CREATION_ENDPOINT = "/auth/register";
    static final String USER_AUTH_ENDPOINT = "/auth/login";
    static final String USER_LOGOUT_ENDPOINT = "/auth/logout";
    static final String USER_ENDPOINT = "/auth/user";
    static final String ORDER_ENDPOINT = "/orders";

    protected List<UserRequest> usersToDelete = new ArrayList<>();

    @After
    public void tearDown() {
        if (!usersToDelete.isEmpty()) {
            for (UserRequest user : usersToDelete) {
                Response response = authorizeUser(user);

                deleteUser(response.as(AuthenticationResponse.class).getAccessToken());
            }
            usersToDelete = new ArrayList<>();
        }
    }

    @Step("Create a user")
    protected static Response createUser(UserRequest user) {
        return given()
                .header("Content-type", "application/json")
                .body(user)
                .post(USER_CREATION_ENDPOINT);
    }

    @Step("Authorize a user")
    protected static Response authorizeUser(UserRequest user) {
        return given()
                .header("Content-type", "application/json")
                .body(new Login(user.getEmail(), user.getPassword()))
                .post(USER_AUTH_ENDPOINT);
    }

    @Step("Delete a user")
    protected static void deleteUser(String token) {
        given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .delete(USER_ENDPOINT)
                .then()
                .statusCode(SC_ACCEPTED);
    }

    @Step("Successfully create a user")
    protected void postNewUser(UserRequest radyushin) {
        Response response = createUser(radyushin);

        assertEquals("User should have been created!",
                SC_OK,
                response.getStatusCode());
        usersToDelete.add(radyushin);
    }

    @Step("Successfully update a user")
    protected Response updateUser(UserRequest updatedUser, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(updatedUser)
                .patch(USER_ENDPOINT);
    }

    @Step("Get authorized users orders")
    protected Response getOrders(String token) {
        return given()
                .header("Authorization", token)
                .get(ORDER_ENDPOINT);
    }

    @Step("Create an order")
    protected Response createOrder(OrderRequest order, String token) {
        return given()
                .header("Content-type", "application/json")
                .header("Authorization", token)
                .body(order)
                .post(ORDER_ENDPOINT);
    }

    protected String getUniqueId() {
        return UUID.randomUUID().toString();
    }
}