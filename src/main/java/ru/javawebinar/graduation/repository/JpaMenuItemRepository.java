package ru.javawebinar.graduation.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.javawebinar.graduation.model.MenuItem;

public interface JpaMenuItemRepository extends JpaRepository<MenuItem, Integer> {

}
