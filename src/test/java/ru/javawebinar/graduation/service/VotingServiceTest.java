package ru.javawebinar.graduation.service;

import org.hibernate.Hibernate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import ru.javawebinar.graduation.RestaurantTestData;
import ru.javawebinar.graduation.UserTestData;
import ru.javawebinar.graduation.model.Restaurant;
import ru.javawebinar.graduation.model.Vote;
import ru.javawebinar.graduation.model.Voting;
import ru.javawebinar.graduation.util.exception.VoteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javawebinar.graduation.RestaurantTestData.*;

public class VotingServiceTest extends AbstractServiceTest {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private VotingService votingService;

    @Autowired
    private VoteService voteService;

    @Test
    void getByRestaurantIdAndDate() {
        RestaurantTestData.increaseVoteTime();
        voteService.vote(RestaurantTestData.RESTAURANT2_ID, UserTestData.USER1.getEmail());
        voteService.vote(RestaurantTestData.RESTAURANT2_ID, UserTestData.USER2.getEmail());
        voteService.vote(RestaurantTestData.RESTAURANT3_ID, UserTestData.USER3.getEmail());
        Voting votingResult = votingService.getBy(RestaurantTestData.RESTAURANT2.getId(), LocalDate.now());
        assertEquals(votingResult.getRestaurant().getId(), RestaurantTestData.RESTAURANT2_ID);
        assertEquals(votingResult.getVotesNumber(), 2);
    }

    @Test
    void getByDate() {
        RestaurantTestData.increaseVoteTime();
        voteService.vote(RestaurantTestData.RESTAURANT2_ID, UserTestData.USER1.getEmail());
        voteService.vote(RestaurantTestData.RESTAURANT2_ID, UserTestData.USER2.getEmail());
        voteService.vote(RestaurantTestData.RESTAURANT3_ID, UserTestData.USER3.getEmail());
        List<Voting> votingResult = votingService.getBy(LocalDate.now());
        assertEquals(votingResult.size(), 2);
        assertEquals(votingResult.get(0).getRestaurant().getId(), RestaurantTestData.RESTAURANT2_ID);
        assertEquals(votingResult.get(0).getVotesNumber(), 2);
        assertEquals(votingResult.get(1).getRestaurant().getId(), RestaurantTestData.RESTAURANT3_ID);
        assertEquals(votingResult.get(1).getVotesNumber(), 1);
    }
}
