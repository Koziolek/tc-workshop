package com.example.demo;

import com.example.demo.support.LocalEnvironmentInitializer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class TestDemoApplication {

    public static void main(String[] args) {
        SpringApplication springApplication = new SpringApplication(DemoApplication.class);

        springApplication.addInitializers(new LocalEnvironmentInitializer());
        springApplication.run(args);
    }
}
