<a href="https://www.liquibase/com/"><img src="https://github.com/purnima-jain/miscellaneous-public/blob/master/icons/Liquibase_001.png" width=150 height=150 align=right /></a>

# Liquibase Integration with Spring Boot & PostgreSQL

<img src="https://github.com/purnima-jain/miscellaneous-public/blob/master/icons/Java_001.svg" width=50 height=50 /> <img src="https://github.com/purnima-jain/miscellaneous-public/blob/master/icons/Maven_001.svg" width=50 height=50 /> <img src="https://github.com/purnima-jain/miscellaneous-public/blob/master/icons/Docker_001.svg" width=50 height=50 /> <img src="https://github.com/purnima-jain/miscellaneous-public/blob/master/icons/PostgreSQL_002.svg" width=50 height=50 /> <img src="https://github.com/purnima-jain/miscellaneous-public/blob/master/icons/Spring_001.svg" width=50 height=50 /> <img src="https://github.com/purnima-jain/miscellaneous-public/blob/master/icons/pgAdmin_001.svg" width=50 height=50 /> <img src="https://github.com/purnima-jain/miscellaneous-public/blob/master/icons/Liquibase_002.png" width=50 height=50 />

This repository contains code for the demo of Liquibase integration with a Spring Boot application. The codebase has been intentionally kept as plain vanilla as possible.

## 📖 Liquibase Introduction
- Liquibase is a **version-control tool** for our **database schema changes**.
- Liquibase integrates with **Java & Spring Boot** to simplify database migrations.
- Liquibase acts like **Git for database**, tracking all schema changes via changelogs.
- Liquibase supports multiple change log formats:
    - XML
    - YAML
    - JSON
    - SQL
- Works with almost all relational databases, including:
    - PostgreSQL
    - MySQL
    - Oracle
    - SQL Server
    - MariaDB
    - H2 (for local development & testing)

## 🛠 Prerequisites
- Java
- Apache Maven
- Docker Desktop (to run PostgreSQL & pgAdmin in containers)

## ⏳ Environment Setup

### <img src="https://github.com/purnima-jain/miscellaneous-public/blob/master/icons/Postgresql_001.svg" width=30 height=30 /> PostgreSQL
We will use `docker-compose.yaml` to bring up our PostgreSQL database and pgAdmin instances in Docker containers. 

To execute `docker-compose.yaml`, go to the root folder of your repository and open a command-line there.

To bring up both PostgreSQL database and pgAdmin instances:
- `docker-compose up`

To bring down both PostgreSQL database and pgAdmin instances:
- `docker-compose down`

This **retains the database state**, i.e. the tables, indexes, data etc will  be retained and can be brought up again after a restart.

To clean up the entire state of the database:
- `docker-compose down -v`

This **erases all the volumes**, leaving the database in a clean state i.e. the tables, indexes, data etc will **NOT** be retained.

### <img src="https://github.com/purnima-jain/miscellaneous-public/blob/master/icons/pgAdmin_001.svg" width=30 height=30 /> pgAdmin
To connect pgAdmin to PostgreSQL running in the container, open your browser and go to http://localhost:8888/browser/

Login using:
- Email Address/Username: pgadmin4@pgadmin.org
- Password: pgadmin_password

Add a Server using:
- General Tab:
    - Name: postgres-docker
- Connection Tab:
    - Hostname/Address: postgres
    - Port: 5432
    - Maintenance database: postgres_db
    - Username: postgres_user
    - Password: postgres_password

You should be able to establish a connection and browse the database.

## <img src="https://github.com/purnima-jain/miscellaneous-public/blob/master/icons/Spring-Boot_001.png" width=30 height=30 /> Liquibase Integration with Spring Boot
As a Spring Boot application, its a bare-bones Spring Boot application with only `spring-boot-starter-web` as a dependency needed in `pom.xml`.

Github Commit [here](https://github.com/purnima-jain/liquibase-spring-boot-postgres-app/commit/52d199c38e831fd5885c842e1f9dd31bf2f5cc8b)

## <img src="https://github.com/purnima-jain/miscellaneous-public/blob/master/icons/Liquibase_002.png" width=30 height=30 /> Liquibase Integration with Spring Boot
1. The three dependencies required for Liquibase integration have been added in `pom.xml`.
```xml
		<!-- Dependencies for Liquibase - Start -->
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-jdbc</artifactId>
		</dependency>

		<dependency>
			<groupId>org.liquibase</groupId>
			<artifactId>liquibase-core</artifactId>
		</dependency>

		<!-- Postgres - Start -->
		<dependency>
			<groupId>org.postgresql</groupId>
			<artifactId>postgresql</artifactId>
		</dependency>
		<!-- Postgres - End -->

		<!-- Dependencies for Liquibase - End -->
```
2. The database changelog master file and its associated child files are [here](https://github.com/purnima-jain/liquibase-spring-boot-postgres-app/tree/master/src/main/resources/db/changelog)

- Script `db-changelog-master.xml` is the master change log.
- Script `_0.1.0_release.xml` & `_0.1.1_release.xml` use the XML syntax for change sets, as an example
- Script `_0.1.2_release.xml` use the inline-SQL syntax.
- Script `_0.1.3_release.xml` provide an example of externalized SQL files.

3. Add the database connection parameters & the path to the database change log master file in `application.properties`.
```properties
# Database Connection Parameters
spring.datasource.jdbcurl=jdbc:postgresql://localhost:15432/postgres_db
spring.datasource.username=postgres_user
spring.datasource.password=postgres_password
spring.datasource.driver-class-name=org.postgresql.Driver

# Liquibase Properties
spring.liquibase.change-log=classpath:db/changelog/db-changelog-master.xml
```

4. Initialize a Postgres Data Source in `PostgresConfig.java`.

## <img src="https://github.com/purnima-jain/miscellaneous-public/blob/master/icons/Rollback_001.svg" width=30 height=30 /> Rollback
1. For every release, we tag the database. This becomes the tag we rollback to in situations where we have to do a rollback.
```xml
	<!-- Tag so that we can rollback the change -->
	<changeSet id="tag_create-_0.1.0_release" author="Purnima Jain">
		<tagDatabase tag="_0.1.0_release" />
	</changeSet>
```

2. The individual change sets have their own corresponding rollback sections.
```xml
		<rollback>
			<dropTable tableName="CATEGORIES" />
		</rollback>
```

3. We perform a rollback via a REST API: http://localhost:8080/rollback/tag/{tagToRollbackTo}
The parameter `tagToRollbackTo` corresponds to the database tag we applied in step #1 above e.g. `_0.1.0_release`.

4. In case of an incorrect tag, a MissingTagException is thrown and a 400 Bad Request and an error message is displayed to the user.

## <img src="https://github.com/purnima-jain/miscellaneous-public/blob/master/icons/Cleanup_001.png" width=30 height=30 /> Clean Up
1. Kill the Spring Boot application.
2. Kill both the Docker containers and erase the database data by executing `docker-compose down -v`