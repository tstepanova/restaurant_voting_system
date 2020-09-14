package ru.javawebinar.graduation.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.graduation.model.Restaurant;
import ru.javawebinar.graduation.model.Vote;
import ru.javawebinar.graduation.repository.JpaVoteRepository;
import ru.javawebinar.graduation.repository.restaurant.JpaRestaurantRepository;
import ru.javawebinar.graduation.util.EndVotingTime;
import ru.javawebinar.graduation.util.exception.VoteException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Service
public class VoteService {

    private static final Logger LOGGER = LoggerFactory.getLogger(VoteService.class);

    private final JpaVoteRepository jpaVoteRepository;

    private final JpaRestaurantRepository jpaRestaurantRepository;

    private final EndVotingTime endVotingTime;

    @Autowired
    public VoteService(JpaVoteRepository jpaVoteRepository, JpaRestaurantRepository jpaRestaurantRepository, EndVotingTime endVotingTime) {
        this.jpaVoteRepository = jpaVoteRepository;
        this.jpaRestaurantRepository = jpaRestaurantRepository;
        this.endVotingTime = endVotingTime;
    }

    public void vote(int restaurantId, String email) {
        LOGGER.info("{} vote for restaurant with id {}", email, restaurantId);
        Vote vote = jpaVoteRepository.findByUserEmailAndDate(email, LocalDate.now());

        if (vote != null && restaurantId != vote.getRestaurant().getId() && LocalTime.now().isAfter(EndVotingTime.getTime())) {
            throw new VoteException("Time for change vote expired at " + endVotingTime + " by server time");
        }

        Restaurant restaurant = jpaRestaurantRepository.getOne(restaurantId);
        if (vote == null) {
            vote = new Vote();
            vote.setUserEmail(email);
        }
        vote.setRestaurant(restaurant);
        jpaVoteRepository.save(vote);
    }

    public List<Vote> getAllByUser_Email(String email) {
        return jpaVoteRepository.getAllByUserEmail(email);
    }

    public List<Vote> getBy(int restaurantId, LocalDate date) {
        return jpaVoteRepository.getByRestaurant_IdAndDate(restaurantId, date);
    }

    public List<Vote> getBy(LocalDate date) {
        return jpaVoteRepository.getByDate(date);
    }

    public List<Vote> getAllByDate(LocalDate date) {
        return jpaVoteRepository.getByDate(date);
    }

    public Vote findByUser_EmailAndDate(String email, LocalDate date) {
        return jpaVoteRepository.findByUserEmailAndDate(email, date);
    }
}