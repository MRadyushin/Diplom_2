package config;

import model.OrderRequest;

import java.util.List;

public class OrderData {
    private static final String INGREDIENT_1 = "61c0c5a71d1f82001bdaaa6d";
    private static final String INGREDIENT_2 = "61c0c5a71d1f82001bdaaa6f";
    private static final String INCORRECT_INGREDIENT_3 = "test_hash";

    public static OrderRequest getCorrectOrder() {
        return new OrderRequest(List.of(INGREDIENT_1, INGREDIENT_2));
    }

    public static OrderRequest getEmptyOrder() {
        return new OrderRequest(List.of());
    }

    public static OrderRequest getIncorrectOrder() {
        return new OrderRequest(List.of(INGREDIENT_1, INCORRECT_INGREDIENT_3));
    }
}