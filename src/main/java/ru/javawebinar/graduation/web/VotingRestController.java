package ru.javawebinar.graduation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.javawebinar.graduation.model.Vote;
import ru.javawebinar.graduation.model.Voting;
import ru.javawebinar.graduation.service.VotingService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping(value = VotingRestController.VOTING_REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class VotingRestController {

    static final String VOTING_REST_URL = "/rest/voting";

    private VotingService votingService;

    @Autowired
    public VotingRestController(VotingService votingService) {
        this.votingService = votingService;
    }

    @GetMapping(value = "/by")
    public List<Voting> getBy(@RequestParam(value = "restaurantId", required = false) Integer restaurantId,
                              @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("date") LocalDate date) {
        if(restaurantId == null) return votingService.getBy(date);
        return new ArrayList<>(List.of(votingService.getBy(restaurantId, date)));
    }
}
