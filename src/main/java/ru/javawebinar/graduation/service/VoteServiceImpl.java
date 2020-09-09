package ru.javawebinar.graduation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.graduation.model.Menu;
import ru.javawebinar.graduation.model.Restaurant;
import ru.javawebinar.graduation.model.Vote;
import ru.javawebinar.graduation.repository.JpaVoteRepository;
import ru.javawebinar.graduation.repository.restaurant.JpaRestaurantRepository;
import ru.javawebinar.graduation.util.VoteTime;
import ru.javawebinar.graduation.util.exception.VoteException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class VoteServiceImpl implements VoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoteServiceImpl.class);

    private final JpaVoteRepository jpaVoteRepository;

    private final JpaRestaurantRepository jpaRestaurantRepository;

    private final VoteTime voteTime;

    @Autowired
    public VoteServiceImpl(JpaVoteRepository jpaVoteRepository, JpaRestaurantRepository jpaRestaurantRepository, VoteTime voteTime) {
        this.jpaVoteRepository = jpaVoteRepository;
        this.jpaRestaurantRepository = jpaRestaurantRepository;
        this.voteTime = voteTime;
    }

    @Override
    public void vote(int restaurantId, String email) {
        LOGGER.info("{} vote for restaurant with id {}", email, restaurantId);
        Vote vote = jpaVoteRepository.findByUserEmailAndDate(email, LocalDate.now());

        if (vote != null && restaurantId != vote.getRestaurant().getId() && LocalTime.now().isAfter(VoteTime.getTime())) {
            throw new VoteException("Time for change vote expired at " + voteTime + " by server time");
        }

        Restaurant restaurant = jpaRestaurantRepository.getOne(restaurantId);
        if (vote == null) {
            vote = new Vote();
            vote.setUserEmail(email);
        }
        vote.setRestaurant(restaurant);
        jpaVoteRepository.save(vote);
    }

    @Override
    public List<Vote> getAllByUser_Email(String email) {
        return jpaVoteRepository.getAllByUserEmail(email);
    }

    @Override
    public List<Vote> getBy(int restaurantId, LocalDate date) {
        return jpaVoteRepository.getByRestaurant_IdAndDate(restaurantId, date);
    }

    @Override
    public List<Vote> getBy(LocalDate date) {
        return jpaVoteRepository.getByDate(date);
    }

    @Override
    public List<Vote> getAllByDate(LocalDate date) {
        return jpaVoteRepository.getByDate(date);
    }

    @Override
    public Vote findByUser_EmailAndDate(String email, LocalDate date) {
        return jpaVoteRepository.findByUserEmailAndDate(email, date);
    }
}