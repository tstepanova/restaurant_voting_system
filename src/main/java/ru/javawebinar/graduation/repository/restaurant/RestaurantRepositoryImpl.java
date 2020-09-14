package ru.javawebinar.graduation.repository.restaurant;

import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.graduation.model.Restaurant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;
import java.util.List;

@Transactional(readOnly = true)
public class RestaurantRepositoryImpl implements RestaurantRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Restaurant getById(int id) {
        Query query = entityManager.createQuery(
                "SELECT r FROM Restaurant r " +
                        "LEFT JOIN FETCH r.menus m " +
                        "WHERE r.id = :id");
        query.setParameter("id", id);
        return (Restaurant) query.getSingleResult();
    }

    @Override
    public List<Restaurant> getByDate(LocalDate date) {
        Query query = entityManager.createQuery(
                "SELECT r FROM Restaurant r " +
                        "LEFT JOIN FETCH r.menus m " +
                        "WHERE m.date = :date");
        query.setParameter("date", date);
        return (List<Restaurant>) query.getResultList();
    }
}
