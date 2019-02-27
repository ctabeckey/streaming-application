package org.nanocontext.streamingapplication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.io.File;

@SpringBootApplication
public class Application {
    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Value( "${db.root}" )
    private String rootDirectory;

    @Bean
    public DataSource dataSource() {
        return new FileDataSource(new File(rootDirectory));
    }

}
