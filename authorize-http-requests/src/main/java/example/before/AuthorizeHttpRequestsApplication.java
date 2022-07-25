package example.before;

import example.IndexController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;

@SpringBootApplication
@Import(IndexController.class)
public class AuthorizeHttpRequestsApplication {

    public static void main(String[] args) {
        SpringApplication.run(AuthorizeHttpRequestsApplication.class, args);
    }

}
