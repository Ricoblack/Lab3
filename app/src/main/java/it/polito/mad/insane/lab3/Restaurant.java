package it.polito.mad.insane.lab3;

import java.util.List;

/**
 * Created by carlocaramia on 28/04/16.
 */
public class Restaurant {

    private RestaurateurProfile profile;
    private List<Review> reviews;
    private List<Dish> dishes;
    private String restaurantID;


    public Restaurant(){}

    public Restaurant(RestaurateurProfile profile, List<Review> reviews, List<Dish> dishes) {
        this.profile = profile;
        this.reviews = reviews;
        this.dishes = dishes;
    }

    public RestaurateurProfile getProfile() {
        return profile;
    }

    public void setProfile(RestaurateurProfile profile) {
        this.profile = profile;
    }

    public List<Review> getReviews() {
        return reviews;
    }

    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    public String getRestaurantID() {
        return restaurantID;
    }

    public void setRestaurantID(String restaurantID) {
        this.restaurantID = restaurantID;
    }
}
