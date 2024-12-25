# ☕ CoffeePlex

### Еще один проект по Javа, из-за которого я её окончательно возненавидел

> [![Java](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=java&logoColor=white)](https://www.java.com/ru/)
> [![Servlets](https://img.shields.io/badge/Servlets-000000?style=for-the-badge&logo=tomcat&logoColor=white)](https://tomcat.apache.org/)
> [![MySQL](https://img.shields.io/badge/MySQL-00000F?style=for-the-badge&logo=mysql&logoColor=white)](https://www.mysql.com/)  
> [![Maven](https://img.shields.io/badge/Maven-C71A36?style=for-the-badge&logo=apache-maven&logoColor=white)](https://maven.apache.org/)
> [![IntelliJ IDEA](https://img.shields.io/badge/IntelliJ%20IDEA-000000.svg?style=for-the-badge&logo=intellij-idea&logoColor=white)](https://www.jetbrains.com/idea/)

Эти бейджики автокомплитом вставились 👍

## Как запускать проект

1. сбилдить war файл как хотите, мне idea артефакты билдила
2. ```shell
    docker compose up -d
    docker compose exec db bash -c "mariadb -proot coffeeplex < /migrations/coffeeplex_schema.sql"
    docker compose restart
    cd frontend && pnpm i && pnpm dev
    ```

Если tomcat шалит и выплевывает ошибки, пните контейнер `docker compose restart app`

## А вот смешные скрины

| ![](.github/img/img1.png) | ![](.github/img/img3.png) |
|---------------------------|---------------------------|
| ![](.github/img/img2.png) | ![](.github/img/img4.png) |
