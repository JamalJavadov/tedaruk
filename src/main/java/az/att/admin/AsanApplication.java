package az.att.admin;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@Slf4j
@RequiredArgsConstructor
@EnableScheduling
@SpringBootApplication
public class AsanApplication implements CommandLineRunner {


    public static void main(String[] args) {
        SpringApplication.run(AsanApplication.class, args);
    }

    @Override
    @SneakyThrows
    public void run(String... args) {

    }
}




