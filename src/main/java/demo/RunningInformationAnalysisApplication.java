package demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan
public class RunningInformationAnalysisApplication {
    public static void main(String[] args) {

        SpringApplication.run(RunningInformationAnalysisApplication.class, args);
    }
}

