# МоиТовары - REST API

Этот проект представляет собой REST API для интернет-магазина "МоиТовары".

## Версия 0.0.4-SNAPSHOT
1. Добавлена возможность поиск продуктов с использованием фильтров и сортировки. Описание добавлено в раздел "Функциональные возможности".

### Настройка и запуск
1. Склонируйте репозиторий
    ```bash
     git clone https://github.com/RomanZemchenkov/My-product-app.git
    ```
2. Перейдите в директорию проекта
    ```bash
     cd MyProducts
    ```
3. Соберите проект:
   ```bash
    mvn clean package -DskipTests
   ```
4. Создание Docker образа
    ```bash
     docker build -t myproducts:0.0.4 .
    ```

#### Установка конфигурации
Вы можете настроить подключение к базе данных тремя способами:
##### Способ 1: Указание переменных окружения в командной строке
Запустите приложение, передав параметры подключения через флаги : 
```bash
 cd docker
 DB_NAME=your_database_name DB_USERNAME=username DB_PASSWORD=password docker-compose up --build
```
   * DB_NAME - название вашей базы данных.
   * DB_USERNAME - имя пользователя базы данных.
   * DB_PASSWORD - пароль пользователя базы данных.

##### Способ 2: Использование .env файла
1. Создайте файл .env :
    ```bash
     touch .env
    ```
2. Откройте данный файл и добавьте три строки с парамтерами, после '=' в каждой укажите нужные данные
    * DB_NAME=название_вашей_базы
    * DB_USERNAME=ваш_логин
    * DB_PASSWORD=ваш пароль
3. Запустите приложение
    ```bash
     cd docker
     docker-compose --env-file ../.env up --build 
    ```

##### Способ 3: Использование базовой конфигурации
При данной конфигурации приложение будет запущено с базовыми параметрами:
* DB_NAME - base_name
* DB_USERNAME - postgres
* DB_PASSWORD - postgres
    ```bash
     cd docker
     docker-compose up --build 
    ```
   
### Функциональные возможности
1. ```http://localhost:8080/api/products``` - GET запрос на получение всех товаров
2. ```http://localhost:8080/api/products``` - POST запрос на добавление нового товара
   * Тело запроса должно выгдяеть подобным образом:
     {
         "title": "Название товара",
         "description": "Описание товара",
         "cost": 100,
         "inStock": EXIST
     }
3. ```http://localhost:8080/api/products/{n}``` - PATCH запрос на обновление товара; n - id товара
   * Тело запроса должно выгдяеть подобным образом:
     {
         "title": "Название товара",
         "description": "Описание товара",
         "cost": 100,
         "inStock": EXIST
     }
4. ```http://localhost:8080/api/products/{n}``` - GET запрос на получение товара по ID; n - id товара
5. ```http://localhost:8080/api/products/{n}``` - DELETE запрос на удаление товара по ID; n - id товара
6. ```http://localhost:8080/api/products/byFilter?key1=value1&keyN=valueN``` - GET запрос на поиск товаро с фильтрами и сортировкой
   1. Список параметров запроса: 
      1. Фильтры:
         * title: название товара
         * cost: точная цена товара
         * costMin: минимальная цена товара
         * costMax: максимальная цена товара
         * inStock: наличие товара (EXIST,NOT_EXIST)
      2. Сортировка:
         * sortBy: параметр сортировки(title, cost и тд)
         * orderBy: направление сортировки(ASC или DESC)
      3. Пангинация:
         * page(по умолчанию 0): номер страницы
         * size(по умолчанию 12): количество товаров на странице