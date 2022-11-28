# user-service

### 1. Connect to H2 database
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
  # Without below config, the H2 still work well
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

### 2. Connect to MySQL database
1. Add dependency ``mysql-connector-java``
```xml
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
</dependency>
```
2. Add properties to configure mysql database
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/photo-app
    driver-class-name: com.mysql.cj.jdbc.Driver
    username: root
    password: root
  jpa:
    hibernate:
      ddl-auto: update
```

### 3. A note on hibernate dialect
I tested with H2, without the below properties, the H2 database still work well
```yaml
spring:
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
```

In case mysql, since Hibernate 6, we don't need explicitly declare hibernate dialect
```yaml
spring:
  jpa:
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
```
Read more: https://vladmihalcea.com/hibernate-dialect/

### 4. Spring cloud starter netflix hystrix
Netflix hystrix is in maintenance mode, not active anymore. We should use Resilience4j instead \
If you want to use hystrix, add the hystrix dependency with explicit version because it's removed from spring cloud netflix
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-netflix-hystrix</artifactId>
    <version>2.2.10.RELEASE</version>
</dependency>
```
The ```@EnableCircuitBreaker``` is deprecated because it has only one implementation which is hystrix

### 5. Resilience4j problem
When I follow the setup from Resilience4j [homepage](https://resilience4j.readme.io/docs/getting-started-3#setup), I got the same error with this [question](https://stackoverflow.com/questions/60587334/getting-compatible-version-issue-when-running-spring-boot-application-however-in) \
I resolved it by using spring cloud starter version instead native resilience4j
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-circuitbreaker-resilience4j</artifactId>
</dependency>
```

### 6. Sleuth & Zipkin
#### 6.1. Adding sleuth & zipkin to your project
Spring sleuth provides spring boot auto-configuration for distributed tracking. And Zipkin is
a tool visualize the tracing \
To use sleuth & zipkin in your project, you have to add these 2 dependencies
```xml
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-starter-sleuth</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.cloud</groupId>
    <artifactId>spring-cloud-sleuth-zipkin</artifactId>
</dependency>
```

And the following config
```yaml
spring:
  zipkin:
    base-url: http://localhost:9411 # default setting
  sleuth:
    sampler:
      probability: 1
```

**Note:** In some code example, they use the dependency ``spring-cloud-starter-zipkin``, but this dependency is deprecated in
later spring cloud bom. You should use the 2 above dependencies

#### 6.1. Start Zipkin server
You can check out the [Zipkin homepage](https://zipkin.io/). I prefer using docker
```shell
docker run -d -p 9411:9411 openzipkin/zipkin
```

### 7. Bean validation
Starting with Boot 2.3, we also need to explicitly add the ``spring-boot-starter-validation`` dependency to have bean validation ``@NotNull``, ``@NotBlank``, etc
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### 8. Configuration properties
From Spring 2.2+ ``@ConfigurationProperties`` scanning Classes annotated with ``@ConfigurationProperties`` can now be found via 
classpath scanning as an alternative to using ``@EnableConfigurationProperties`` or ``@Component`` \
Add ``@ConfigurationPropertiesScan`` to your application to enable scanning \
```java
// Old way
@EnableConfigurationProperties // Add this annotation for every @ConfigurationProperties
@ConfigurationProperties(prefix = "token")
class TokenProperties {
    //
}

// New way
@SpringBootApplication
@ConfigurationPropertiesScan // It will automatically scan all properties bean
class Application {
    //
}

@ConfigurationProperties(prefix = "token")
class TokenProperties {
    //
}
```