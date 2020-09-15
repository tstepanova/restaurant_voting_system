package ru.javawebinar.graduation.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.graduation.model.Restaurant;
import ru.javawebinar.graduation.model.Voting;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class VotingRepository {

    @PersistenceContext
    EntityManager entityManager;

    public Voting getByRestaurant_IdAndDate(int restaurantId, LocalDate date) {
        javax.persistence.Query query = entityManager.createQuery("SELECT r, count(v.userEmail) FROM Vote v " +
                "LEFT JOIN v.restaurant r " +
                "WHERE r.id = : restaurantId AND v.date = :date " +
                "GROUP BY r.id");
        query.setParameter("restaurantId", restaurantId);
        query.setParameter("date", date);
        List<Object[]> queryResult = query.getResultList();
        if (queryResult != null && queryResult.size() > 0) {
            return new Voting((Restaurant) queryResult.get(0)[0], (Long) queryResult.get(0)[1]);
        }
        return null;
    }

    public List<Voting> getByDate(LocalDate date) {
        List<Voting> resultList = new ArrayList<>();
        javax.persistence.Query query = entityManager.createQuery(
                "SELECT r, count(v.userEmail) FROM Vote v " +
                        "LEFT JOIN v.restaurant r " +
                        "WHERE v.date = :date " +
                        "GROUP BY r.id " +
                        "ORDER BY COUNT(v.userEmail) DESC");
        query.setParameter("date", date);
        List<Object[]> queryResult = query.getResultList();
        for (Object[] obj : queryResult) {
            resultList.add(new Voting((Restaurant) obj[0], (Long) obj[1]));
        }
        return resultList;
    }
}
