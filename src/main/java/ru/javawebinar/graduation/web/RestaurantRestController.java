package ru.javawebinar.graduation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.graduation.View;
import ru.javawebinar.graduation.model.Restaurant;
import ru.javawebinar.graduation.repository.restaurant.JpaRestaurantRepository;
import ru.javawebinar.graduation.util.exception.NotFoundException;

import java.net.URI;
import java.time.LocalDate;
import java.util.List;

import static ru.javawebinar.graduation.util.ValidationUtil.*;

@RestController
@RequestMapping(value = RestaurantRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class RestaurantRestController {
    static final String REST_URL = "/rest/restaurants";

    private final JpaRestaurantRepository jpaRestaurantRepository;

    @Autowired
    public RestaurantRestController(JpaRestaurantRepository jpaRestaurantRepository) {
        this.jpaRestaurantRepository = jpaRestaurantRepository;

    }

    @GetMapping
    public List<Restaurant> getAll() {
        return jpaRestaurantRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity getById(@PathVariable("id") Integer id) {
        Restaurant restaurant = jpaRestaurantRepository.getById(id);
        checkNotFound(restaurant, "No Restaurant found for ID " + id);
        return ResponseEntity.ok(restaurant);
    }

    @GetMapping(value = "/by")
    public ResponseEntity getBy(@DateTimeFormat(iso = DateTimeFormat.ISO.DATE) @RequestParam("date") LocalDate date) {
        return ResponseEntity.ok().body(jpaRestaurantRepository.getByDate(date));
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity create(@Validated(View.Web.class) @RequestBody Restaurant restaurant) {
        checkNew(restaurant);
        Restaurant created = jpaRestaurantRepository.save(restaurant);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Integer id, @Validated(View.Web.class) @RequestBody Restaurant restaurant) {
        assureIdConsistent(restaurant, id);
        Restaurant found = jpaRestaurantRepository.findById(id).orElseThrow(() -> new NotFoundException("Restaurant not found for ID " + id));
        found.setName(restaurant.getName());
        found.setAddress(restaurant.getAddress());
        jpaRestaurantRepository.save(found);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer id) {
        jpaRestaurantRepository.deleteById(id);
    }

}
