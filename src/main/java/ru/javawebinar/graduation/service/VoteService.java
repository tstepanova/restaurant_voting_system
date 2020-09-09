package ru.javawebinar.graduation.service;

import ru.javawebinar.graduation.model.Menu;
import ru.javawebinar.graduation.model.Vote;

import java.time.LocalDate;
import java.util.List;

public interface VoteService {

    void vote(int restaurantId, String username);

    List<Vote> getAllByUser_Email(String email);

    List<Vote> getBy(int restaurantId, LocalDate date);

    List<Vote> getBy(LocalDate date);

    List<Vote> getAllByDate(LocalDate date);

    Vote findByUser_EmailAndDate( String email, LocalDate date);
}
