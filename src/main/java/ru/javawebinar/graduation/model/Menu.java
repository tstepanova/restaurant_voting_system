package ru.javawebinar.graduation.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.FilterDef;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.ParamDef;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "menu", uniqueConstraints = {@UniqueConstraint(columnNames = {"date_added", "restaurant_id"}, name = "menu_unique_date_added_restaurant_idx")})
public class Menu extends AbstractBaseEntity {

    @NotNull
    @Column(name = "date_added")
    private LocalDate date;

    @JsonManagedReference
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("id ASC")
    private List<MenuItem> menuItems;

    @JsonBackReference
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "restaurant_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Restaurant restaurant;

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public void setMenuItems(List<MenuItem> menuItems) {
        this.menuItems = menuItems;
    }

    public Restaurant getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(Restaurant restaurant) {
        this.restaurant = restaurant;
    }

    public void addMenuItem(MenuItem menuItem) {
        menuItems.add(menuItem);
        menuItem.setMenu(this);
    }

    public void removeMenuItem(MenuItem menuItem) {
        menuItems.remove(menuItem);
        menuItem.setMenu(null);
    }

    public Menu() {
    }

    public Menu(LocalDate date, Restaurant restaurant) {
        this.date = date;
        this.restaurant = restaurant;
    }

    public Menu(Integer id, LocalDate date, List<MenuItem> menuItems, Restaurant restaurant) {
        super(id);
        this.date = date;
        this.menuItems = menuItems;
        this.restaurant = restaurant;
    }

    @Override
    public String toString() {
        return "Menu{" +
                "date=" + date +
                ", id=" + id +
                '}';
    }
}
