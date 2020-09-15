package ru.javawebinar.graduation.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.graduation.model.Voting;
import ru.javawebinar.graduation.repository.VotingRepository;

import java.time.LocalDate;
import java.util.List;

@Service
public class VotingService {

    private final VotingRepository repository;

    @Autowired
    public VotingService(VotingRepository votingRepository) {
        this.repository = votingRepository;
    }

    public Voting getBy(int restaurantId, LocalDate date) {
        return repository.getByRestaurant_IdAndDate(restaurantId, date);
    }

    public List<Voting> getBy(LocalDate date) {
        return repository.getByDate(date);
    }
}