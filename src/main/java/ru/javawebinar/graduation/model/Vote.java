package ru.javawebinar.graduation.model;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Entity
@Table(name = "vote", uniqueConstraints = {@UniqueConstraint(columnNames = {"date_added", "user_email"}, name = "vote_unique_date_user_idx")})
public class Vote extends AbstractBaseEntity {

    @NotNull
    @Column(name = "date_added")
    private LocalDate date = LocalDate.now();

    @NotNull
    @Column(name = "user_email")
    private String userEmail;

    @ManyToOne
    @JoinColumn(name = "restaurant_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public Vote() {
    }

    public Vote(Integer id, String userEmail, Restaurant restaurant) {
        super(id);
        this.userEmail = userEmail;
        this.restaurant = restaurant;
    }

    public Vote(Integer id, LocalDate date, String userEmail, Restaurant restaurant) {
        this(id, userEmail, restaurant);
        this.date = date;
    }

    public Vote(String userEmail, Restaurant restaurant) {
        this.userEmail = userEmail;
        this.restaurant = restaurant;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Vote{" +
                "id=" + id +
                ", date=" + date +
                ", user=" + userEmail +
                ", restaurant=" + restaurant +
                '}';
    }
}
