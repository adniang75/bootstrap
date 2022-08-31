package rsb.bootstrap.bootiful;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import rsb.bootstrap.CustomerService;
import rsb.bootstrap.Demo;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {

        System.setProperty("spring.profiles.active", "prod");
        SpringApplication.run(Application.class, args);
    }

}

@RequiredArgsConstructor
@Profile("dev")
@Component
class DemoListener {

    private final CustomerService customerService;

    @EventListener(ApplicationReadyEvent.class)
    public void exercise() {
        Demo.workWithCustomerService(getClass(), this.customerService);
    }

}