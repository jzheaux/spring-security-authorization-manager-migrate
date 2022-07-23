package example.after;

import example.IndexController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(IndexController.class)
public class CustomAccessDecisionManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(CustomAccessDecisionManagerApplication.class, args);
    }

}
