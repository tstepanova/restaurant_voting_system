package ru.javawebinar.graduation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.graduation.model.Vote;
import ru.javawebinar.graduation.service.VoteService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping(value = VoteRestController.VOTES_REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VoteRestController {

    static final String VOTES_REST_URL = "/rest/votes";

    private VoteService voteService;

    @Autowired
    public VoteRestController(VoteService voteService) {
        this.voteService = voteService;
    }

    @PutMapping(value = "/for")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void vote(@RequestParam("restaurantId") Integer restaurantId, Authentication authentication) {
        voteService.vote(restaurantId, authentication.getName());
    }

    @GetMapping
    public List<Vote> getAll(Authentication authentication) {
        return voteService.getAllByUser_Email(authentication.getName());
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping(value = "/by")
    public List<Vote> getBy(@RequestParam(value = "restaurantId", required = false) Integer restaurantId,
                            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("date") LocalDate date) {
        if (restaurantId == null) return voteService.getBy(date);
        return voteService.getBy(restaurantId, date);
    }
}
