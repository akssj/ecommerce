package alledrogo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.scheduling.annotation.EnableScheduling;


/**
 * Main class for this application
 */
@SpringBootApplication
@ComponentScan
@EnableScheduling
public class Alledrogo {
    public static void main(String[] args) {SpringApplication.run(Alledrogo.class, args);}
}