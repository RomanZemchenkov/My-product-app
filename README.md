# МоиТовары - REST API

Этот проект представляет собой REST API для интернет-магазина "МоиТовары".

## Версия 0.0.1-SNAPSHOT
1. Реализован функционал добавления, удаления и обновления товарова, а также поиск по id и получение всех товаров.

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
4. Запустите приложение 
    ```bash
    java -jar target/MyProducts-0.0.1-SNAPSHOT.jar
    ```
   
### Функциональные возможности
1. ```http://localhost:8080/api/products``` - GET запрос на получение всех товаров
2. ```http://localhost:8080/api/products``` - POST запрос на добавление нового товара
   * Тело запроса должно выгдяеть подобным образом:
     {
         "title": "Название товара",
         "description": "Описание товара",
         "cost": 100,
         "state": true
     }
3. ```http://localhost:8080/api/products/{n}``` - PATCH запрос на обновление товара; n - id товара
   * Тело запроса должно выгдяеть подобным образом:
     {
         "title": "Название товара",
         "description": "Описание товара",
         "cost": 100,
         "state": true
     }
4. ```http://localhost:8080/api/products/{n}``` - GET запрос на получение товара по ID; n - id товара
5. ```http://localhost:8080/api/products/{n}``` - DELETE запрос на удаление товара по ID; n - id товара