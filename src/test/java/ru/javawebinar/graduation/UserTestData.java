package ru.javawebinar.graduation;

import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import ru.javawebinar.graduation.model.AbstractBaseEntity;
import ru.javawebinar.graduation.model.Role;
import ru.javawebinar.graduation.model.User;
import ru.javawebinar.graduation.web.json.JsonUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.javawebinar.graduation.model.AbstractBaseEntity.START_SEQ;

public class UserTestData {
    public static TestMatcher<User> USER_MATCHER = TestMatcher.usingFieldsWithIgnoringAssertions(User.class, "registered", "meals", "password");

    public static final int NOT_FOUND = 10;
    public static final int ADMIN1_ID = AbstractBaseEntity.START_SEQ;
    public static final int USER1_ID = AbstractBaseEntity.START_SEQ + 1;

    public static final User ADMIN1 = new User(ADMIN1_ID, "Admin1", "admin1@gmail.com", "admin", Role.ADMIN);
    public static final User USER1 = new User(USER1_ID, "User1", "user1@yandex.ru", "password", Role.USER);
    public static final User USER2 = new User(USER1_ID + 1, "User2", "user2@yandex.ru", "password", Role.USER);
    public static final User USER3 = new User(USER1_ID + 2, "User3", "user3@yandex.ru", "password", Role.USER);

    public static User getNew() {
        return new User(null, "UserNew", "usernew@yandex.ru", "password", true, new Date(), Collections.singleton(Role.USER));
    }

    public static User getUser1() {
        return new User(USER1_ID, USER1.getName(), USER1.getEmail(), USER1.getPassword(), USER1.isEnabled(), USER1.getRegistered(), Collections.singleton(Role.USER));
    }

    public static User getUpdated() {
        User updated = new User(USER1);
        updated.setName("UpdatedUser1");
        updated.setRoles(Collections.singletonList(Role.ADMIN));
        return updated;
    }

    public static String jsonWithPassword(User user, String passw) {
        return JsonUtil.writeAdditionProps(user, "password", passw);
    }

    public static RequestPostProcessor userHttpBasic(User user) {
        return SecurityMockMvcRequestPostProcessors.httpBasic(user.getEmail(), user.getPassword().replace("{noop}", ""));
    }
}
