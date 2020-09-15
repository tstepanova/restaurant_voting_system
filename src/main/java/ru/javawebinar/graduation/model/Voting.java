package ru.javawebinar.graduation.model;

public class Voting {

    private Restaurant restaurant;
    private int votesNumber;

    public Voting() {
    }

    public Voting(Restaurant restaurant, Long votesNumber) {
        this(restaurant, votesNumber.intValue());
    }

    public Voting(Restaurant restaurant, int votesNumber) {
        this.restaurant = restaurant;
        this.votesNumber = votesNumber;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public int getVotesNumber() {
        return votesNumber;
    }

    public void setVotesNumber(int votesNumber) {
        this.votesNumber = votesNumber;
    }

    @Override
    public String toString() {
        return "Voting{" +
                "restaurant=" + restaurant +
                ", votesNumber=" + votesNumber +
                '}';
    }
}
