package ru.javawebinar.graduation.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.graduation.model.Menu;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public interface JpaMenuRepository extends JpaRepository<Menu, Integer> {

    Menu getById(int id);

    @EntityGraph(attributePaths = {"menuItems"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Menu> getByRestaurant_Id(int restaurantId);

    @EntityGraph(attributePaths = {"menuItems"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Menu> getByRestaurant_IdAndDate(int restaurantId, LocalDate date);

    @EntityGraph(attributePaths = {"menuItems"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Menu> getByDate(LocalDate date);

    @EntityGraph(attributePaths = {"menuItems"}, type = EntityGraph.EntityGraphType.LOAD)
    List<Menu> getAllByOrderByIdAsc();

    @Modifying
    @Transactional
    @Query("DELETE FROM Menu WHERE id=?1")
    int delete(int id);
}
