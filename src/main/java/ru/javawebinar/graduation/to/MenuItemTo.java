package ru.javawebinar.graduation.to;

import ru.javawebinar.graduation.HasId;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class MenuItemTo implements HasId {

    protected Integer id;

    @NotNull
    private String name;

    @NotNull
    private BigDecimal price;

    @NotNull
    private Integer menuId;


    public MenuItemTo(Integer id, String name, BigDecimal price, Integer menuId) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuId = menuId;
    }

    public MenuItemTo() {
    }

    @Override
    public Integer getId() {
        return this.id;
    }

    @Override
    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }
}
