package com.photoapp.user;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
// From Spring 2.2+ @ConfigurationProperties scanning Classes annotated with @ConfigurationProperties
// can now be found via classpath scanning as an alternative to using @EnableConfigurationProperties or @Component.
// Add @ConfigurationPropertiesScan to your application to enable scanning.
@ConfigurationPropertiesScan
public class UserApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserApplication.class, args);
    }

}
