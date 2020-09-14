DELETE
FROM vote;
DELETE
FROM menu_item;
DELETE
FROM menu;
DELETE
FROM restaurant;
DELETE
FROM users;
DELETE
FROM user_role;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password)
VALUES ('Admin1', 'admin1@gmail.com', '{noop}admin'),
       ('User1', 'user1@yandex.ru', '{noop}password'),
       ('User2', 'user2@yandex.ru', '{noop}password'),
       ('User3', 'user3@yandex.ru', '{noop}password');

INSERT INTO user_role (role, user_id)
VALUES ('ADMIN', 100000),
       ('USER', 100001),
       ('USER', 100002),
       ('USER', 100003);

INSERT INTO restaurant (name, address)
VALUES ('Аль-Шарк', 'Санкт-Петербург, Литейный пр., 43'),
       ('Pizza Hut', 'Санкт-Петербург, Обводного канала наб., 120'),
       ('Две палочки', 'Санкт-Петербург, Просвещения пр., 19'),
       ('Лимончелло', 'Санкт-Петербург, пр. Литейный, 40'),
       ('Orange-Club', 'Санкт-Петербург, Сенная площадь, 2'),
       ('Барашки', 'Санкт-Петербург, Сенная площадь, 2');

INSERT INTO menu (date_added, restaurant_id)
VALUES ('2020-08-31', 100005),
       ('2020-08-31', 100006),
        (now(), 100007);

INSERT INTO menu_item (name, price, menu_id)
VALUES ('Хат ролл с курицей', 159, 100010),
       ('Запеченные картофельные дольки', 149.50, 100010),
       ('Пепси', 99, 100010),

       ('Крем-суп с лососем', 135, 100011),
       ('Салат Цезарь', 120, 100011),
       ('Морс ягодный', 65, 100011),

       ('Картофельный крем-суп с беконом', 180, 100012),
       ('Паста Феттучине Болонезе', 110, 100012),
       ('Мини Фокаччо', 35, 100012),
       ('Американо', 70, 100012);

INSERT INTO vote (date_added, user_email, restaurant_id)
VALUES ('2020-08-31', 'user2@yandex.ru', 100006)