package example.before;

import example.CsrfController;
import example.IndexController;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;

@SpringBootApplication
@EnableWebSocketMessageBroker
@Import({ IndexController.class, CsrfController.class })
public class MessagingSecurityApplication {


    public static void main(String[] args) {
        SpringApplication.run(MessagingSecurityApplication.class, args);
    }

}
