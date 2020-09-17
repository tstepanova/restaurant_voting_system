package ru.javawebinar.graduation.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.javawebinar.graduation.View;
import ru.javawebinar.graduation.model.MenuItem;
import ru.javawebinar.graduation.repository.JpaMenuItemRepository;
import ru.javawebinar.graduation.repository.JpaMenuRepository;
import ru.javawebinar.graduation.to.MenuItemTo;
import ru.javawebinar.graduation.util.MenuItemUtil;
import ru.javawebinar.graduation.util.exception.NotFoundException;

import java.net.URI;
import java.util.List;

import static ru.javawebinar.graduation.util.ValidationUtil.assureIdConsistent;
import static ru.javawebinar.graduation.util.ValidationUtil.checkNew;

@RestController
@RequestMapping(value = MenuItemRestController.REST_URL, produces = MediaType.APPLICATION_JSON_VALUE)
public class MenuItemRestController {
    static final String REST_URL = "/rest/menu_items";

    @Autowired
    private JpaMenuItemRepository jpaMenuItemRepository;

    @Autowired
    private JpaMenuRepository jpaMenuRepository;

    @GetMapping
    public List<MenuItem> getAll() {
        return jpaMenuItemRepository.findAll();
    }

    @GetMapping("/{id}")
    public MenuItem getById(@PathVariable("id") int id) {
        return jpaMenuItemRepository.findById(id).orElseThrow(() -> new NotFoundException("Not found MenuItem with id=" + id));
    }

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<MenuItem> create(@RequestBody @Validated(View.Web.class) MenuItemTo menuItemTo) {
        checkNew(menuItemTo);
        MenuItem menuItem = MenuItemUtil.createFromTo(menuItemTo);
        menuItem.setMenu(jpaMenuRepository.getOne(menuItemTo.getMenuId()));
        MenuItem created = jpaMenuItemRepository.save(menuItem);
        URI uriOfNewResource = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(REST_URL + "/{id}")
                .buildAndExpand(created.getId()).toUri();

        return ResponseEntity.created(uriOfNewResource).body(created);
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void update(@PathVariable("id") Integer id, @Validated(View.Web.class) @RequestBody MenuItem menuItem) {
        assureIdConsistent(menuItem, id);
        MenuItem found = jpaMenuItemRepository.findById(id).orElseThrow(() -> new NotFoundException("MenuItem not found for ID " + id));
        found.setName(menuItem.getName());
        found.setPrice(menuItem.getPrice());
        jpaMenuItemRepository.save(found);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable("id") Integer id) {
        jpaMenuItemRepository.deleteById(id);
    }

}
