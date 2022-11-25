# users-service

### Connect to H2 database
1. Add H2 dependency
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>runtime</scope>
</dependency>
```

2. Add properties to configure H2 database
```yaml
spring:
  h2:
    console:
      enabled: true
  datasource:
    url: jdbc:h2:mem:testdb
    driver-class-name: org.h2.Driver
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
```

3. Need to add ``spring-boot-starter-data-jpa`` to auto configure the H2 database (``H2ConsoleAutoConfiguration``)
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
```
Read more: https://stackoverflow.com/questions/63587966/spring-boot-h2-database-not-found-not-created-by-liquibase