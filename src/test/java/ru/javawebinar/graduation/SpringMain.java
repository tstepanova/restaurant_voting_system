package ru.javawebinar.graduation;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import ru.javawebinar.graduation.model.Role;
import ru.javawebinar.graduation.model.User;
import ru.javawebinar.graduation.web.RestaurantRestController;
import ru.javawebinar.graduation.web.user.AdminRestController;

import java.util.Arrays;

import static ru.javawebinar.graduation.TestUtil.mockAuthorize;
import static ru.javawebinar.graduation.UserTestData.USER1;

public class SpringMain {
    public static void main(String[] args) {
        try (ConfigurableApplicationContext appCtx = new ClassPathXmlApplicationContext("spring/spring-app.xml")) {

            System.out.println("Bean definition names: " + Arrays.toString(appCtx.getBeanDefinitionNames()));

            AdminRestController adminUserController = appCtx.getBean(AdminRestController.class);
            adminUserController.create(new User(null, "userName", "email@mail.ru", "password", Role.ADMIN));

            mockAuthorize(USER1);
            RestaurantRestController restaurantController = appCtx.getBean(RestaurantRestController.class);
            System.out.println("All restaurants: " + restaurantController.getAll());
        }
    }
}
