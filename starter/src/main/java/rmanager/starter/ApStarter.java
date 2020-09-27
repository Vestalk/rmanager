package rmanager.starter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

@EnableScheduling
@SpringBootApplication
@EnableAspectJAutoProxy
@ComponentScan({"rmanager"})
@EntityScan("rmanager")
@EnableJpaRepositories("rmanager")
public class ApStarter {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(ApStarter.class, args);
    }

}

