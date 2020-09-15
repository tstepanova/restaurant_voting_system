# Topjava graduation project
Design and implement a REST API using Hibernate/Spring/SpringMVC **without frontend**.

## The task is:
Build a voting system for deciding where to have lunch.

 * 2 types of users: admin and regular users
 * Admin can input a restaurant and it's lunch menu of the day (2-5 items usually, just a dish name and price)
 * Menu changes each day (admins do the updates)
 * Users can vote on which restaurant they want to have lunch at
 * Only one vote counted per user
 * If user votes again the same day:
    - If it is before 11:00 we asume that he changed his mind.
    - If it is after 11:00 then it is too late, vote can't be changed

Each restaurant provides new menu each day.

## API documentation and couple curl commands
Application deployed at application context `restaurant_voting_system`
Test login and password:
 * admin1@gmail.com:admin   
 * user1@yandex.ru:password
 * user2@yandex.ru:password
 * user3@yandex.ru:password

The curl commands were tested from Windows PC.
Use "Lucida Console", "chcp 65001" (in cmd.exe) and unicode converter (convert unicode text to UTF-16).

### Example of work

Step 1. Admin create restaurant

`curl -s -X POST -d "{\"name\":\"\u0415\u0432\u0440\u0430\u0437\u0438\u044f\",\"address\":\"\u0421\u0430\u043d\u043a\u0442\u002d\u041f\u0435\u0442\u0435\u0440\u0431\u0443\u0440\u0433\u002c \u041d\u0435\u0432\u0441\u043a\u0438\u0439 \u043f\u0440\u002e\u002c \u0031\u0037\u0035\"}" -H "Content-Type:application/json;charset=UTF-8" --user admin1@gmail.com:admin http://localhost:8080/restaurant_voting_system/rest/restaurants`

    {"id":100024,"name":"Евразия","address":"Санкт-Петербург, Невский пр., 175","menus":[]}

Step 2. Admin create menu

`curl -s -X POST -d "{\"restaurantId\": 100024,\"menuItems\": [{\"name\": \"\u041c\u0438\u0441\u043e \u0441\u0443\u043f\",\"price\": 90},{\"name\": \"\u0420\u043e\u043b\u043b \u041a\u0430\u043b\u0438\u0444\u043e\u0440\u043d\u0438\u044f\",\"price\": 160}]}" -H "Content-Type:application/json;charset=UTF-8" --user admin1@gmail.com:admin http://localhost:8080/restaurant_voting_system/rest/menus`

    {"id":100025,"date":"2020-09-15","menuItems":[{"id":100026,"name":"Мисо суп","price":90},{"id":100027,"name":"Ролл Калифорния","price":160}]}
    
Step 3. Admin update menu

`curl -s -X PUT -d "{\"restaurantId\": 100005, \"date\": \"2020-09-15\", \"menuItems\":[{\"name\": \"\u0421\u0443\u043f\",\"price\": 90},{\"name\": \"\u0427\u0430\u0439\",\"price\": 35},{\"name\": \"\u0421\u0430\u043b\u0430\u0442\",\"price\": 159}]}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/restaurant_voting_system/rest/menus/100010 --user admin1@gmail.com:admin`

    {"id":100010,"date":"2020-09-15","menuItems":[{"id":100028,"name":"Суп","price":90},{"id":100029,"name":"Чай","price":35},{"id":100030,"name":"Салат","price":159}]}
    
`curl -s -X PUT -d "{\"restaurantId\": 100006, \"date\": \"2020-09-15\", \"menuItems\":[{\"name\": \"\u041f\u0438\u0440\u043e\u0433\",\"price\": 150},{\"name\": \"\u041a\u043e\u0444\u0435\",\"price\": 60}]}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/restaurant_voting_system/rest/menus/100011 --user admin1@gmail.com:admin`

    {"id":100011,"date":"2020-09-15","menuItems":[{"id":100031,"name":"Пирог","price":150},{"id":100032,"name":"Кофе","price":60}]}

Step 4. User registers

`curl -s -i -X POST -d "{\"name\":\"Tamara\",\"email\":\"tamara@mail.ru\",\"password\":\"password\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/restaurant_voting_system/rest/profile/register`

    {"id":100033,"name":"Tamara","email":"tamara@mail.ru","enabled":true,"registered":"2020-09-14T21:30:27.895+00:00","roles":["USER"]}

Step 5. All restaurants with a menu for the current date

`curl -s GET http://localhost:8080/restaurant_voting_system/rest/restaurants/by?date=2020-09-15 --user admin1@gmail.com:admin`
 
    [{"id":100005,"name":"Pizza Hut","address":"Санкт-Петербург, Обводного канала наб., 120","menus":[{"id":100010,"date":"2020-09-15","menuItems":[{"id":100028,"name":"Суп","price":90.00},{"id":100029,"name":"Чай","price":35.00},{"id":100030,"name":"Салат","price":159.00}]}]},{"id":100006,"name":"Две палочки","address":"Санкт-Петербург, Просвещения пр., 19","menus":[{"id":100011,"date":"2020-09-15","menuItems":[{"id":100031,"name":"Пирог","price":150.00},{"id":100032,"name":"Кофе","price":60.00}]}]},{"id":100007,"name":"Лимончелло","address":"Санкт-Петербург, пр. Литейный, 40","menus":[{"id":100012,"date":"2020-09-15","menuItems":[{"id":100019,"name":"Картофельный крем-суп с беконом","price":180.00},{"id":100020,"name":"Паста Феттучине Болонезе","price":110.00},{"id":100021,"name":"Мини Фокаччо","price":35.00},{"id":100022,"name":"Американо","price":70.00}]}]},{"id":100024,"name":"Евразия","address":"Санкт-Петербург, Невский пр., 175","menus":[{"id":100025,"date":"2020-09-15","menuItems":[{"id":100026,"name":"Мисо суп","price":90.00},{"id":100027,"name":"Ролл Калифорния","price":160.00}]}]}]

Step 6. Users vote

 * `curl -s -X PUT --user user1@yandex.ru:password http://localhost:8080/restaurant_voting_system/rest/votes/for?restaurantId=100024`
 * `curl -s -X PUT --user user2@yandex.ru:password http://localhost:8080/restaurant_voting_system/rest/votes/for?restaurantId=100006`
 * `curl -s -X PUT --user user3@yandex.ru:password http://localhost:8080/restaurant_voting_system/rest/votes/for?restaurantId=100005`
 * `curl -s -X PUT --user tamara@mail.ru:password http://localhost:8080/restaurant_voting_system/rest/votes/for?restaurantId=100024`

Step 7. Voting results

`curl -s GET "http://localhost:8080/restaurant_voting_system/rest/votes/by?date=2020-09-15" --user admin1@gmail.com:admin`

    [{"id":100034,"date":"2020-09-15","userEmail":"user1@yandex.ru","restaurant":{"id":100024,"name":"Евразия","address":"Санкт-Петербург, Невский пр., 175","menus":null}},{"id":100035,"date":"2020-09-15","userEmail":"user2@yandex.ru","restaurant":{"id":100006,"name":"Две палочки","address":"Санкт-Петербург, Просвещения пр., 19","menus":null}},{"id":100036,"date":"2020-09-15","userEmail":"user3@yandex.ru","restaurant":{"id":100005,"name":"Pizza Hut","address":"Санкт-Петербург, Обводного канала наб., 120","menus":null}},{"id":100037,"date":"2020-09-15","userEmail":"tamara@mail.ru","restaurant":{"id":100024,"name":"Евразия","address":"Санкт-Петербург, Невский пр., 175","menus":null}}]

### User
#### Get all users
`curl -s http://localhost:8080/restaurant_voting_system/rest/users --user admin1@gmail.com:admin`

 Response:
 
    [
          {
          "id": 100000,
          "name": "Admin1",
          "email": "admin1@gmail.com",
          "enabled": true,
          "registered": "2020-09-08T15:18:59.324+00:00",
          "roles": ["ADMIN"]
       },
          {
          "id": 100001,
          "name": "User1",
          "email": "user1@yandex.ru",
          "enabled": true,
          "registered": "2020-09-08T15:18:59.324+00:00",
          "roles": ["USER"]
       },
          {
          "id": 100002,
          "name": "User2",
          "email": "user2@yandex.ru",
          "enabled": true,
          "registered": "2020-09-08T15:18:59.324+00:00",
          "roles": ["USER"]
       },
          {
          "id": 100003,
          "name": "User3",
          "email": "user3@yandex.ru",
          "enabled": true,
          "registered": "2020-09-08T15:18:59.324+00:00",
          "roles": ["USER"]
       }
    ]

#### Get user
`curl -s http://localhost:8080/restaurant_voting_system/rest/users/100001 --user admin1@gmail.com:admin`

Response:
    
    {
       "id": 100001,
       "name": "User1",
       "email": "user1@yandex.ru",
       "enabled": true,
       "registered": "2020-09-08T15:18:59.324+00:00",
       "roles": ["USER"]
    }

#### User HTTP status codes:

| Code | Status | Description |
| --- | --- | --- |
| 200 | OK |
| 201 | Created |
| 204 | No Content |
| 401 | Unauthorized |
| 403 | Forbidden |
| 422 | Unprocessable Entity |
| 422 | Unprocessable Entity | User with this email already exists


### Profile
#### Register user
`curl -s -i -X POST -d '{"name":"New User","email":"test@mail.ru","password":"test-password"}" -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant_voting_system/rest/profile/register`

Win:
`curl -s -i -X POST -d "{\"name\":\"New User\",\"email\":\"test@mail.ru\",\"password\":\"test-password\"}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/restaurant_voting_system/rest/profile/register`

Response:

    {
       "id": 100024,
       "name": "New User",
       "email": "test@mail.ru",
       "enabled": true,
       "registered": "2020-09-08T16:16:54.125+00:00",
       "roles": ["USER"]
    }

#### Get profile
`curl -s http://localhost:8080/restaurant_voting_system/rest/profile --user user1@yandex.ru:password`

Response:

    {
       "id": 100001,
       "name": "User1",
       "email": "user1@yandex.ru",
       "enabled": true,
       "registered": "2020-09-08T15:18:59.324+00:00",
       "roles": ["USER"]
    }

#### Profile HTTP status codes:

| Code | Status | Description |
| --- | --- | --- |
| 200 | OK |
| 201 | Created |
| 204 | No Content |
| 401 | Unauthorized |
| 422 | Unprocessable Entity | Validation error
| 422 | Unprocessable Entity | User with this email already exists


### Restaurant
#### Get all restaurants
`curl -s http://localhost:8080/restaurant_voting_system/rest/restaurants --user user1@yandex.ru:password`

Response:

    [
          {
          "id": 100004,
          "name": "Аль-Шарк",
          "address": "Санкт-Петербург, Литейный пр., 43",
          "menus": null
       },
          {
          "id": 100005,
          "name": "Pizza Hut",
          "address": "Санкт-Петербург, Обводного канала наб., 120",
          "menus": null
       },
          {
          "id": 100006,
          "name": "Две палочки",
          "address": "Санкт-Петербург, Просвещения пр., 19",
          "menus": null
       },
          {
          "id": 100007,
          "name": "Лимончелло",
          "address": "Санкт-Петербург, пр. Литейный, 40",
          "menus": null
       },
          {
          "id": 100008,
          "name": "Orange-Club",
          "address": "Санкт-Петербург, Сенная площадь, 2",
          "menus": null
       },
          {
          "id": 100009,
          "name": "Барашки",
          "address": "Санкт-Петербург, Сенная площадь, 2",
          "menus": null
       }
    ]

#### Get restaurant
`curl -s http://localhost:8080/restaurant_voting_system/rest/restaurants/100005 --user user1@yandex.ru:password`

Response:

    {
       "id": 100005,
       "name": "Pizza Hut",
       "address": "Санкт-Петербург, Обводного канала наб., 120",
       "menus": [   {
          "id": 100010,
          "date": "2020-08-31",
          "menuItems":       [
                      {
                "id": 100014,
                "name": "Запеченные картофельные дольки",
                "price": 149.5
             },
                      {
                "id": 100015,
                "name": "Пепси",
                "price": 99
             },
                      {
                "id": 100013,
                "name": "Хат ролл с курицей",
                "price": 159
             }
          ]
       }]
    }
    
#### Get all restaurants with menus on a date
`curl -s GET http://localhost:8080/restaurant_voting_system/rest/restaurants/by?date=2020-09-15 --user user1@yandex.ru:password`

Response:

    [
          {
          "id": 100005,
          "name": "Pizza Hut",
          "address": "Санкт-Петербург, Обводного канала наб., 120",
          "menus": [      {
             "id": 100010,
             "date": "2020-09-15",
             "menuItems":          [
                            {
                   "id": 100028,
                   "name": "Суп",
                   "price": 90
                },
                            {
                   "id": 100029,
                   "name": "Чай",
                   "price": 35
                },
                            {
                   "id": 100030,
                   "name": "Салат",
                   "price": 159
                }
             ]
          }]
       },
          {
          "id": 100006,
          "name": "Две палочки",
          "address": "Санкт-Петербург, Просвещения пр., 19",
          "menus": [      {
             "id": 100011,
             "date": "2020-09-15",
             "menuItems":          [
                            {
                   "id": 100031,
                   "name": "Пирог",
                   "price": 150
                },
                            {
                   "id": 100032,
                   "name": "Кофе",
                   "price": 60
                }
             ]
          }]
       },
          {
          "id": 100007,
          "name": "Лимончелло",
          "address": "Санкт-Петербург, пр. Литейный, 40",
          "menus": [      {
             "id": 100012,
             "date": "2020-09-15",
             "menuItems":          [
                            {
                   "id": 100019,
                   "name": "Картофельный крем-суп с беконом",
                   "price": 180
                },
                            {
                   "id": 100020,
                   "name": "Паста Феттучине Болонезе",
                   "price": 110
                },
                            {
                   "id": 100021,
                   "name": "Мини Фокаччо",
                   "price": 35
                },
                            {
                   "id": 100022,
                   "name": "Американо",
                   "price": 70
                }
             ]
          }]
       },
          {
          "id": 100024,
          "name": "Евразия",
          "address": "Санкт-Петербург, Невский пр., 175",
          "menus": [      {
             "id": 100025,
             "date": "2020-09-15",
             "menuItems":          [
                            {
                   "id": 100026,
                   "name": "Мисо суп",
                   "price": 90
                },
                            {
                   "id": 100027,
                   "name": "Ролл Калифорния",
                   "price": 160
                }
             ]
          }]
       }
    ]

#### Create restaurant
`curl -s -X POST -d '{"name":"Евразия","address":"Санкт-Петербург, Невский пр., 175"}' -H 'Content-Type:application/json;charset=UTF-8' --user admin1@gmail.com:admin http://localhost:8080/restaurant_voting_system/rest/restaurants`

Win:
`curl -s -X POST -d "{\"name\":\"\u0415\u0432\u0440\u0430\u0437\u0438\u044f\",\"address\":\"\u0421\u0430\u043d\u043a\u0442\u002d\u041f\u0435\u0442\u0435\u0440\u0431\u0443\u0440\u0433\u002c \u041d\u0435\u0432\u0441\u043a\u0438\u0439 \u043f\u0440\u002e\u002c \u0031\u0037\u0035\"}" -H "Content-Type:application/json;charset=UTF-8" --user admin1@gmail.com:admin http://localhost:8080/restaurant_voting_system/rest/restaurants`

Response:

    {
       "id": 100024,
       "name": "Евразия",
       "address": "Санкт-Петербург, Невский пр., 175",
       "menus": []
    }
    
#### Update restaurant
`curl -s -X PUT -d '{"name": "Аль-Шарк", "address": "Новый адрес"}' -H 'Content-Type:application/json;charset=UTF-8' --user admin1@gmail.com:admin http://localhost:8080/restaurant_voting_system/rest/restaurants/100004`

Win:
`curl -s -X PUT -d "{\"name\": \"\u0410\u043b\u044c\u002d\u0428\u0430\u0440\u043a\", \"address\": \"\u041d\u043e\u0432\u044b\u0439 \u0430\u0434\u0440\u0435\u0441\"}" -H "Content-Type:application/json;charset=UTF-8" --user admin1@gmail.com:admin http://localhost:8080/restaurant_voting_system/rest/restaurants/100004`

#### Delete restaurant
`curl -s -X DELETE --user admin1@gmail.com:admin http://localhost:8080/restaurant_voting_system/rest/restaurants/100004`

#### Restaurant HTTP status codes:

| Code | Status |
| --- | --- |
| 200 | OK |
| 204 | No Content |
| 401 | Unauthorized |
| 403 | Forbidden |
| 404 | Not Found |
| 409 | Conflict |
| 422 | Unprocessable Entity |


### Menu
#### Create menu
`curl -s -X POST -d '{"restaurantId": 100008,"menuItems": [{"name": "Мисо суп","price": 90},{"name": "Ролл Калифорния","price": 160}]}' -H 'Content-Type:application/json;charset=UTF-8' --user admin1@gmail.com:admin http://localhost:8080/restaurant_voting_system/rest/menus`
    
Win:
`curl -s -X POST -d "{\"restaurantId\": 100008,\"menuItems\": [{\"name\": \"\u041c\u0438\u0441\u043e \u0441\u0443\u043f\",\"price\": 90},{\"name\": \"\u0420\u043e\u043b\u043b \u041a\u0430\u043b\u0438\u0444\u043e\u0440\u043d\u0438\u044f\",\"price\": 160}]}" -H "Content-Type:application/json;charset=UTF-8" --user admin1@gmail.com:admin http://localhost:8080/restaurant_voting_system/rest/menus`

Response:

    {
       "id": 100024,
       "date": "2020-09-09",
       "menuItems":    [
                {
             "id": 100025,
             "name": "Мисо суп",
             "price": 90
          },
                {
             "id": 100026,
             "name": "Ролл Калифорния",
             "price": 160
          }
       ]
    }

#### Get all menus
`curl -s --user admin1@gmail.com:admin   http://localhost:8080/restaurant_voting_system/rest/menus`

Response:

    [
          {
          "id": 100010,
          "restaurantId": 100005,
          "menuItems":       [
                      {
                "id": 100013,
                "name": "Хат ролл с курицей",
                "price": 159
             },
                      {
                "id": 100014,
                "name": "Запеченные картофельные дольки",
                "price": 149.5
             },
                      {
                "id": 100015,
                "name": "Пепси",
                "price": 99
             }
          ],
          "date": "2020-08-31"
       },
          {
          "id": 100011,
          "restaurantId": 100006,
          "menuItems":       [
                      {
                "id": 100016,
                "name": "Крем-суп с лососем",
                "price": 135
             },
                      {
                "id": 100017,
                "name": "Салат Цезарь",
                "price": 120
             },
                      {
                "id": 100018,
                "name": "Морс ягодный",
                "price": 65
             }
          ],
          "date": "2020-08-31"
       },
          {
          "id": 100012,
          "restaurantId": 100007,
          "menuItems":       [
                      {
                "id": 100019,
                "name": "Картофельный крем-суп с беконом",
                "price": 180
             },
                      {
                "id": 100020,
                "name": "Паста Феттучине Болонезе",
                "price": 110
             },
                      {
                "id": 100021,
                "name": "Мини Фокаччо",
                "price": 35
             },
                      {
                "id": 100022,
                "name": "Американо",
                "price": 70
             }
          ],
          "date": "2020-09-09"
       }
    ]

#### Get menu
`curl -s http://localhost:8080/restaurant_voting_system/rest/menus/100010 --user user1@yandex.ru:password`

Response:

    {
       "id": 100010,
       "date": "2020-08-31",
       "menuItems":    [
                {
             "id": 100014,
             "name": "Запеченные картофельные дольки",
             "price": 149.5
          },
                {
             "id": 100015,
             "name": "Пепси",
             "price": 99
          },
                {
             "id": 100013,
             "name": "Хат ролл с курицей",
             "price": 159
          }
       ]
    }
    
#### Get all menus by restaurant id
`curl -s http://localhost:8080/restaurant_voting_system/rest/menus/by?restaurantId=100005 --user user1@yandex.ru:password`

Response:

    [{
       "id": 100010,
       "date": "2020-08-31",
       "menuItems":    [
                {
             "id": 100014,
             "name": "Запеченные картофельные дольки",
             "price": 149.5
          },
                {
             "id": 100015,
             "name": "Пепси",
             "price": 99
          },
                {
             "id": 100013,
             "name": "Хат ролл с курицей",
             "price": 159
          }
       ]
    }]

#### Get menu by restaurant id and date
`curl -s GET 'http://localhost:8080/restaurant_voting_system/rest/menus/by?restaurantId=100005&date=2020-08-31' --user admin1@gmail.com:admin`

Win:
`curl -s GET "http://localhost:8080/restaurant_voting_system/rest/menus/by?restaurantId=100005&date=2020-08-31" --user admin1@gmail.com:admin`

Response:

    [{
       "id": 100010,
       "date": "2020-08-31",
       "menuItems":    [
                {
             "id": 100014,
             "name": "Запеченные картофельные дольки",
             "price": 149.5
          },
                {
             "id": 100015,
             "name": "Пепси",
             "price": 99
          },
                {
             "id": 100013,
             "name": "Хат ролл с курицей",
             "price": 159
          }
       ]
    }]
    
#### Get all menus by date
`curl -s GET 'http://localhost:8080/restaurant_voting_system/rest/menus/by?date=2020-08-31' --user admin1@gmail.com:admin`

Response:

    [
          {
          "id": 100010,
          "date": "2020-08-31",
          "menuItems":       [
                      {
                "id": 100013,
                "name": "Хат ролл с курицей",
                "price": 159
             },
                      {
                "id": 100014,
                "name": "Запеченные картофельные дольки",
                "price": 149.5
             },
                      {
                "id": 100015,
                "name": "Пепси",
                "price": 99
             }
          ]
       },
          {
          "id": 100011,
          "date": "2020-08-31",
          "menuItems":       [
                      {
                "id": 100016,
                "name": "Крем-суп с лососем",
                "price": 135
             },
                      {
                "id": 100017,
                "name": "Салат Цезарь",
                "price": 120
             },
                      {
                "id": 100018,
                "name": "Морс ягодный",
                "price": 65
             }
          ]
       }
    ]


#### Update menu
`curl -s -X PUT -d '{"id": "100010", "restaurantId": 100005, "date": "2020-09-09", "menuItems":[{"name": "Суп","price": 90},{"name": "Чай","price": 35},{"name": "Салат","price": 159}]}' -H 'Content-Type:application/json;charset=UTF-8' http://localhost:8080/restaurant_voting_system/rest/menus/100010'--user admin1@gmail.com:admin`

Win:
`curl -s -X PUT -d "{\"id\": \"100010\", \"restaurantId\": 100005, \"date\": \"2020-09-09\", \"menuItems\":[{\"name\": \"\u0421\u0443\u043f\",\"price\": 90},{\"name\": \"\u0427\u0430\u0439\",\"price\": 35},{\"name\": \"\u0421\u0430\u043b\u0430\u0442\",\"price\": 159}]}" -H "Content-Type:application/json;charset=UTF-8" http://localhost:8080/restaurant_voting_system/rest/menus/100010 --user admin1@gmail.com:admin`

Response:

    {
       "id": 100010,
       "date": "2020-09-09",
       "menuItems":    [
                {
             "id": 100026,
             "name": "Суп",
             "price": 90
          },
                {
             "id": 100027,
             "name": "Чай",
             "price": 35
          },
                {
             "id": 100028,
             "name": "Салат",
             "price": 120
          }
       ]
    }

#### Delete menu
`curl -s -X DELETE --user admin1@gmail.com:admin http://localhost:8080/restaurant_voting_system/rest/menus/100010'`

#### Menu HTTP status codes:

| Code | Status |
| --- | --- |
| 200 | OK |
| 201 | Created |
| 204 | No Content |
| 401 | Unauthorized |
| 403 | Forbidden |
| 422 | Unprocessable Entity |


### Vote
#### Vote for restaurant
`curl -s -X PUT --user user2@yandex.ru:password http://localhost:8080/restaurant_voting_system/rest/votes/for?restaurantId=100006`

#### Get user votes
`curl -s --user user2@yandex.ru:password http://localhost:8080/restaurant_voting_system/rest/votes`

Response:

    [{
       "id": 100024,
       "date": "2020-09-09",
       "userEmail": "user2@yandex.ru",
       "restaurant":    {
          "id": 100006,
          "name": "Две палочки",
          "address": "Санкт-Петербург, Просвещения пр., 19",
          "menus": null
       }
    }]

#### Get all votes by date
`curl -s GET http://localhost:8080/restaurant_voting_system/rest/votes/by?date=2020-09-15 --user admin1@gmail.com:admin`

Response:

    [
          {
          "id": 100034,
          "date": "2020-09-15",
          "userEmail": "user1@yandex.ru",
          "restaurant":       {
             "id": 100024,
             "name": "Евразия",
             "address": "Санкт-Петербург, Невский пр., 175",
             "menus": null
          }
       },
          {
          "id": 100035,
          "date": "2020-09-15",
          "userEmail": "user2@yandex.ru",
          "restaurant":       {
             "id": 100006,
             "name": "Две палочки",
             "address": "Санкт-Петербург, Просвещения пр., 19",
             "menus": null
          }
       },
          {
          "id": 100036,
          "date": "2020-09-15",
          "userEmail": "user3@yandex.ru",
          "restaurant":       {
             "id": 100005,
             "name": "Pizza Hut",
             "address": "Санкт-Петербург, Обводного канала наб., 120",
             "menus": null
          }
       },
          {
          "id": 100037,
          "date": "2020-09-15",
          "userEmail": "tamara@mail.ru",
          "restaurant":       {
             "id": 100024,
             "name": "Евразия",
             "address": "Санкт-Петербург, Невский пр., 175",
             "menus": null
          }
       }
    ]

#### Get votes by restaurant id and date
`curl -s GET 'http://localhost:8080/restaurant_voting_system/rest/votes/by?restaurantId=100006&date=2020-09-09' --user admin1@gmail.com:admin`

Win:
`curl -s GET "http://localhost:8080/restaurant_voting_system/rest/votes/by?restaurantId=100006&date=2020-09-09" --user admin1@gmail.com:admin`

Response:

    [{
       "id": 100030,
       "date": "2020-09-09",
       "userEmail": "user2@yandex.ru",
       "restaurant":    {
          "id": 100006,
          "name": "Две палочки",
          "address": "Санкт-Петербург, Просвещения пр., 19",
          "menus": null
       }
    }]

#### Vote HTTP status codes:

| Code | Status |
| --- | --- |
| 200 | OK |
| 204 | No Content |
| 401 | Unauthorized |
| 409 | Conflict |
