package ru.javawebinar.graduation.repository.restaurant;

import org.hibernate.Session;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.graduation.model.Restaurant;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDate;

@Transactional(readOnly = true)
public class RestaurantRepositoryImpl implements RestaurantRepository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    public Restaurant getByIdWithMenuByDate(int id, LocalDate date) {
        Session session = entityManager.unwrap(Session.class);
        session.enableFilter("date").setParameter("date", date);
        Query query = entityManager.createQuery(
                "SELECT r FROM Restaurant r " +
                        "LEFT JOIN FETCH r.menus m " +
                        "WHERE r.id = (:id)");
        query.setParameter("id", id);
        return (Restaurant) query.getSingleResult();
    }
}
